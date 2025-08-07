package com.wsiz.gameshub.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.wsiz.gameshub.dto.origin.OriginGameDetailsDto;
import com.wsiz.gameshub.dto.origin.OriginGamesListResponseDto;
import com.wsiz.gameshub.mapper.OriginMapper;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.repository.CategoryRepository;
import com.wsiz.gameshub.model.repository.GameImageRepository;
import com.wsiz.gameshub.model.repository.GamesRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OriginService implements GameProviderService<OriginGameDetailsDto> {

    @Value("${external-api.origin.api.url}")
    private String apiUrl;

    @Value("${external-api.origin.store.url}")
    private String storeUrl;

    private final RestTemplate restTemplate;
    private final GamesRepository gamesRepository;
    private final CategoryRepository categoryRepository;
    private final GameImageRepository gameImageRepository;
    private final OriginMapper mapper;
    private final WebClient webClient;

    private final static int ORIGIN_MAX_GAMES = 978;
    private final static int ORIGIN_PAGE_STEP = 30;

    @Autowired
    public OriginService(GamesRepository gamesRepository, OriginMapper steamMapper, CategoryRepository categoryRepository, GameImageRepository gameImageRepository){
        final BrowserVersion myChrome = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.CHROME)
                .setUserAgent("Googlebot/2.1 (+http://www.googlebot.com/bot.html)")
                .build();

        this.restTemplate = new RestTemplate();
        this.webClient = new WebClient(myChrome);
        this.webClient.getOptions().setThrowExceptionOnScriptError(false);
        this.webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        this.gamesRepository = gamesRepository;
        this.mapper = steamMapper;
        this.categoryRepository = categoryRepository;
        this.gameImageRepository = gameImageRepository;
    }

    @Override
    public void loadData(){

        int start = 0;

        while (start <= ORIGIN_MAX_GAMES) {
            ResponseEntity<OriginGamesListResponseDto> response = restTemplate.getForEntity(apiUrl + "/xsearch/store/en_us/pol/products?rows=30&isGDP=true&start=" + start, OriginGamesListResponseDto.class);

            List<Game> games = response.getBody().getGamesDtoList().stream().map(mapper::map).collect(Collectors.toList());

            gamesRepository.saveAll(games);

            start+=ORIGIN_PAGE_STEP;
        }
    }

    @Override
    public OriginGameDetailsDto getGameDetails(String externalGameId) {
        OriginGameDetailsDto originGameDetailsDto = new OriginGameDetailsDto();

        try {
            final HtmlPage gameDetailsHtml = webClient.getPage(storeUrl + "/usa/en-us/store/" + externalGameId);

            DomNodeList<DomNode> divsDescription = gameDetailsHtml.querySelectorAll(".origin-store-paragraph-contentcolumn");
            DomNodeList<DomNode> divPublisher = gameDetailsHtml.querySelectorAll("a[ng-href^='https://www.origin.com/usa/en-us/store/browse?fq=publisher']");
            DomNodeList<DomNode> divDeveloper = gameDetailsHtml.querySelectorAll("a[ng-href^='https://www.origin.com/usa/en-us/store/browse?fq=developer']");
            DomNodeList<DomNode> imagesHtml = gameDetailsHtml.querySelectorAll("img[ng-src^='https://data3.origin.com/content/dam/originx/web/app/games/']");
            DomNodeList<DomNode> divGenres = gameDetailsHtml.querySelectorAll("span[ng-repeat='link in ::genreLinks track by $index']");


            List<String> categpries = new ArrayList<>();

            divGenres.forEach(genre -> {
                if(genre.getFirstChild() != null && genre.getFirstChild().getFirstChild() != null){
                    categpries.add(genre.getFirstChild().getFirstChild().getNodeValue());
                }
            });

            List<String> images = new ArrayList<>();

            String gameName = StringUtils.substringBetween(externalGameId, "/", "/");

            imagesHtml.forEach(imageHtml -> {
                String imageUrl = imageHtml.getAttributes().getNamedItem("ng-src").getNodeValue();
                if(imageUrl.contains(gameName)){
                    images.add(imageUrl);
                }
            });

            originGameDetailsDto.setPublisher(getStringFromDiv(divPublisher));
            originGameDetailsDto.setDeveloper(getStringFromDiv(divDeveloper));
            originGameDetailsDto.setCategories(categpries);
            originGameDetailsDto.setImages(images);
            originGameDetailsDto.setDescription(getLongStringFromDiv(divsDescription));
            originGameDetailsDto.setPrice(extractPrice(gameDetailsHtml.getWebResponse().getContentAsString()));

        } catch (IOException e) {
            log.error("Cannot load origin game details {}", externalGameId);
        }

        return originGameDetailsDto;

    }

    @Override
    public List<Game> getSpecialOffers() {
        return new ArrayList<>();
    }

    private BigDecimal extractPrice(String htmlPage){
        String price = StringUtils.substringBetween(htmlPage, "price\":", ",");

        try {
            return new BigDecimal(price);
        } catch (NumberFormatException | NullPointerException e){
            return null;
        }
    }

    private String getStringFromDiv(DomNodeList<DomNode> div){
        if(!div.isEmpty()){
            DomNode node = div.get(0);
            if(node.getFirstChild() != null){
                return node.getFirstChild().getNodeValue();
            }
        }

        return null;
    }

    private String getLongStringFromDiv(DomNodeList<DomNode> div){
        if(!div.isEmpty()){
            DomNode node = div.get(0);
            return node.getTextContent();
        }

        return null;
    }

}
