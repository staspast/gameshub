package com.wsiz.gameshub.service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.wsiz.gameshub.dto.humblebundle.HumbleBundleGameDetailsDto;
import com.wsiz.gameshub.dto.humblebundle.HumbleBundleGamesListResponseDto;
import com.wsiz.gameshub.mapper.HumbleBundleMapper;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.repository.GamesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HumbleBundleService implements GameProviderService<HumbleBundleGameDetailsDto> {

    @Value("${external-api.humblebundle.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final GamesRepository gamesRepository;
    private final HumbleBundleMapper mapper;
    private final WebClient webClient;

    private final static int HUMBLE_PAGE_MAX = 433;

    @Autowired
    public HumbleBundleService(GamesRepository gamesRepository, HumbleBundleMapper steamMapper){

        final BrowserVersion myChrome = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.CHROME)
                .setUserAgent("Googlebot/2.1 (+http://www.googlebot.com/bot.html)")
                .build();

        this.restTemplate = new RestTemplate();
        this.webClient = new WebClient(myChrome);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        this.gamesRepository = gamesRepository;
        this.mapper = steamMapper;
    }

    @Override
    public void loadData(){

        int page = 1;

        while (page <= HUMBLE_PAGE_MAX) {
            ResponseEntity<HumbleBundleGamesListResponseDto> response = restTemplate.getForEntity(apiUrl + "/store/api/search?sort=discount&filter=all&request=1&page=" + page, HumbleBundleGamesListResponseDto.class);
            List<Game> games = response.getBody().getResults().stream().map(mapper::map).collect(Collectors.toList());

            gamesRepository.saveAll(games);
            page++;
        }
    }

    public HumbleBundleGameDetailsDto getGameDetails(String externalId){

        HumbleBundleGameDetailsDto humbleBundleGameDto = new HumbleBundleGameDetailsDto();

        try {
            final HtmlPage gameDetailsHtml = webClient.getPage(apiUrl + "/store/" + externalId);

            DomNodeList<DomNode> divsDescription = gameDetailsHtml.querySelectorAll(".js-property-content, .property-content");
            DomNodeList<DomNode> divPublisher = gameDetailsHtml.querySelectorAll("a[href^='/store/search?publisher=']");
            DomNodeList<DomNode> divDeveloper = gameDetailsHtml.querySelectorAll("a[href^='/store/search?developer=']");
            DomNodeList<DomNode> images = gameDetailsHtml.querySelectorAll(".carousel-image-container");

            humbleBundleGameDto.setDescription(getStringFromDiv(divsDescription));
            humbleBundleGameDto.setPublisher(getStringFromDiv(divPublisher));
            humbleBundleGameDto.setDeveloper(getStringFromDiv(divDeveloper));
            humbleBundleGameDto.setImages(images.stream().map(this::getImageUrlFromDivContainer).collect(Collectors.toList()));
            humbleBundleGameDto.setCategories(extractCategoriesFromDiv(gameDetailsHtml));

        } catch (IOException e) {
            log.error("Cannot load humblebundle game details {}", externalId);
        }

        return humbleBundleGameDto;
    }

    private List<String> extractCategoriesFromDiv(HtmlPage gameDetailsHtml){
        DomNodeList<DomNode> categories = gameDetailsHtml.querySelectorAll("[data-anchor-name=\"#genres_anchor\"][data-entity-kind=\"display_item\"]");

        try {
            List<DomNode> categoryDomNodes = categories.get(0).querySelectorAll("span");
            List<String> categoryNames = new ArrayList<>();
            categoryDomNodes.forEach(node -> {
                categoryNames.add(node.getFirstChild().getNodeValue());
            });
            return categoryNames;
        } catch (Exception e){
            return new ArrayList<>();
        }
    }

    private String getImageUrlFromDivContainer(DomNode image){
        try {
            return image.getFirstChild().getNextSibling().getAttributes().getNamedItem("data-lazy").getNodeValue();
        } catch (NullPointerException e){
            log.warn("Couldnt parse image from html");
            return null;
        }
    }

    private String getStringFromDiv(DomNodeList<DomNode> div){
        try {
            return div.get(0).getFirstChild().getNodeValue().trim();
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public List<Game> getSpecialOffers() {
        return new ArrayList<>();
    }

}
