package com.wsiz.gameshub.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.dto.steam.SteamGameDetailsDto;
import com.wsiz.gameshub.dto.steam.SteamGamesListResponseDto;
import com.wsiz.gameshub.mapper.SteamMapper;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.repository.GamesRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SteamService implements GameProviderService<SteamGameDetailsDto> {

    @Value("${external-api.steam.url}")
    private String apiUrl;

    @Value("${external-api.steam.store.url}")
    private String storeUrl;

    private final RestTemplate restTemplate;
    private final GamesRepository gamesRepository;
    private final SteamMapper mapper;
    private final WebClient webClient;

    public SteamService(GamesRepository gamesRepository, SteamMapper steamMapper){
        final BrowserVersion myChrome = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.CHROME)
                .setUserAgent("Googlebot/2.1 (+http://www.googlebot.com/bot.html)")
                .build();

        this.restTemplate = new RestTemplate();
        this.webClient = new WebClient(myChrome);
        this.webClient.getOptions().setJavaScriptEnabled(false);
        this.gamesRepository = gamesRepository;
        this.mapper = steamMapper;
    }

    @Override
    public void loadData(){

        ResponseEntity<SteamGamesListResponseDto> response = restTemplate.getForEntity(apiUrl + "/ISteamApps/GetAppList/v2", SteamGamesListResponseDto.class);

        List<Game> games = mapper.mapList(response.getBody().getGamesList());

        gamesRepository.saveAll(games);
    }

    @Override
    public SteamGameDetailsDto getGameDetails(String externalGameId) {

        System.out.println(externalGameId);

        SteamGameDetailsDto detailsDto = new SteamGameDetailsDto();

        try {
            ResponseEntity<String> respose = restTemplate.getForEntity(storeUrl + "/api/appdetails?appids=" + externalGameId, String.class);
            JSONObject jo = new JSONObject(respose.getBody());
            JSONObject statusObject = new JSONObject(jo.getString(String.valueOf(externalGameId)));

            if (statusObject.getBoolean("success")) {
                ObjectMapper mapper = new ObjectMapper();
                detailsDto = mapper.readValue(statusObject.getString("data"), SteamGameDetailsDto.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return detailsDto;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public List<Game> getSpecialOffers() {
        List<Game> games = new ArrayList<>();
        try {
            final HtmlPage gameOffersHtml = webClient.getPage(storeUrl + "/search/?filter=topsellers&specials=1");
            DomNodeList<DomNode> discountedGames = gameOffersHtml.querySelectorAll(".responsive_search_name_combined");

            discountedGames.forEach(discountedGameDiv -> {
                DomNode title = discountedGameDiv.querySelector(".title");
                DomNode discount = discountedGameDiv.querySelector(".search_discount");
                DomNode price = discountedGameDiv.querySelector(".search_price");

                log.info("GAME {}", title.getTextContent());
                Game game = gamesRepository.findByNameAndMarketplaceName(title.getTextContent(), MarketPlaceConstants.MARKETPLACE_NAME_STEAM);
                if(game != null){
                    BigDecimal discountBigDecimal = getDiscountFromString(discount.getTextContent());

                    if(discountBigDecimal != null) {
                        game.setDiscountPercent(discountBigDecimal);
                    }

                    BigDecimal currentPriceBigDecimal = getPriceFromDiv(price);

                    if(currentPriceBigDecimal != null){
                        game.setPriceFinal(currentPriceBigDecimal);
                    }

                    games.add(game);
                }
            });
        }catch (IOException e) {
            log.error("Cannot load special offers for steam");
        }

        return games;
    }

    private BigDecimal getDiscountFromString(String discount){
        String discountFiltered = discount.replace("-", "").replace("%", "").trim();
        try {
            return new BigDecimal(discountFiltered);
        } catch (NumberFormatException e){
            return null;
        }
    }

    private BigDecimal getPriceFromDiv(DomNode price){
        String priceString = price.getLastChild().getTextContent().trim();
        priceString = priceString.replaceAll("[^\\d,]", "").replace(",", ".");
        try {
            return new BigDecimal(priceString);
        } catch (NumberFormatException e){
            return null;
        }
    }
}
