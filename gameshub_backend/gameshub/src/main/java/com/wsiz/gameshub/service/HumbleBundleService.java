package com.wsiz.gameshub.service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.wsiz.gameshub.dto.HumbleBundleGameDetailsDto;
import com.wsiz.gameshub.dto.HumbleBundleGamesListResponseDto;
import com.wsiz.gameshub.mapper.HumbleBundleMapper;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.repository.CategoryRepository;
import com.wsiz.gameshub.model.repository.GameImageRepository;
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
    private final CategoryRepository categoryRepository;
    private final GameImageRepository gameImageRepository;
    private final HumbleBundleMapper mapper;
    private final WebClient webClient;

    private final static int HUMBLE_PAGE_MAX = 433;

    @Autowired
    public HumbleBundleService(GamesRepository gamesRepository, HumbleBundleMapper steamMapper, CategoryRepository categoryRepository, GameImageRepository gameImageRepository){

        final BrowserVersion myChrome = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.CHROME)
                .setUserAgent("Googlebot/2.1 (+http://www.googlebot.com/bot.html)")
                .build();

        this.restTemplate = new RestTemplate();
        this.webClient = new WebClient(myChrome);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        this.gamesRepository = gamesRepository;
        this.mapper = steamMapper;
        this.categoryRepository = categoryRepository;
        this.gameImageRepository = gameImageRepository;
    }

    @Override
    public void loadData(){

        int page = 301;

        while (page <= HUMBLE_PAGE_MAX) {
            ResponseEntity<HumbleBundleGamesListResponseDto> response = restTemplate.getForEntity(apiUrl + "/store/api/search?sort=discount&filter=all&request=1&page=" + page, HumbleBundleGamesListResponseDto.class);

            List<Game> games = response.getBody().getResults().stream().map(mapper::map).collect(Collectors.toList());

            gamesRepository.saveAll(games);
            page++;

            System.out.println("page");
            System.out.println(page);
        }
    }

    public HumbleBundleGameDetailsDto getGameDetails(String externalId){

        HumbleBundleGameDetailsDto humbleBundleGameDto = new HumbleBundleGameDetailsDto();

        try {
            final HtmlPage gameDetailsHtml = webClient.getPage(apiUrl + "/store/" + externalId);

            DomNodeList<DomNode> divsDescription = gameDetailsHtml.querySelectorAll(".js-property-content, .property-content");

            DomNodeList<DomNode> categories = gameDetailsHtml.querySelectorAll("[data-anchor-name=\"#genres_anchor\"][data-entity-kind=\"display_item\"]");

            System.out.println(divsDescription.get(0).getFirstChild().getNodeValue());
            List<DomNode> categoryDomNodes = categories.get(0).querySelectorAll("span");

            List<String> categoryNames = new ArrayList<>();

            categoryDomNodes.forEach(node -> {
                categoryNames.add(node.getFirstChild().getNodeValue());
            });

            //TODO: save categories, figure out how to get images
            categoryNames.forEach(System.out::println);

//            humbleBundleGameDto.setDescription(categories.get(0).getFirstChild().getNodeValue().trim());

        } catch (IOException e) {
            log.error("Cannot load humblebundle game details {}", externalId);
        }

        return humbleBundleGameDto;
    }

}
