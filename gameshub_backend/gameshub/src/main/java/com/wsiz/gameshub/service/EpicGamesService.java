package com.wsiz.gameshub.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.dto.epic.EpicGameDetailsDto;
import com.wsiz.gameshub.dto.epic.EpicGamesListResponseDto;
import com.wsiz.gameshub.mapper.EpicGamesMapper;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.entity.GameImage;
import com.wsiz.gameshub.model.repository.GameImageRepository;
import com.wsiz.gameshub.model.repository.GamesRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EpicGamesService implements GameProviderService<EpicGameDetailsDto> {

    @Value("${external-api.epic-games.api.url}")
    private String apiUrl;

    private static final Map<String, String> GRAPHQL_QUERY = new HashMap<>();
    private static final Map<String, String> GRAPHQL_QUERY_DISCOUNT = new HashMap<>();
    private final RestTemplate restTemplate;
    private final GamesRepository gamesRepository;
    private final GameImageRepository gameImageRepository;
    private final EpicGamesMapper mapper;
    private final WebClient webClient;


    public EpicGamesService(GamesRepository gamesRepository, EpicGamesMapper steamMapper, GameImageRepository gameImageRepository){

        final BrowserVersion myChrome = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.CHROME)
                .setUserAgent("Googlebot/2.1 (+http://www.googlebot.com/bot.html)")
                .build();

        this.restTemplate = new RestTemplate();
        this.gamesRepository = gamesRepository;
        this.mapper = steamMapper;
        this.gameImageRepository = gameImageRepository;
        this.webClient = new WebClient(myChrome);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        GRAPHQL_QUERY.put("query", "query searchStoreQuery($allowCountries: String, $category: String, $count: Int, $country: String!, $keywords: String, $locale: String, $namespace: String, $itemNs: String, $sortBy: String, $sortDir: String, $start: Int, $tag: String, $releaseDate: String, $withPrice: Boolean = false, $withPromotions: Boolean = false, $priceRange: String, $freeGame: Boolean, $onSale: Boolean, $effectiveDate: String) {  Catalog {    searchStore(      allowCountries: $allowCountries      category: $category      count: $count      country: $country      keywords: $keywords      locale: $locale      namespace: $namespace      itemNs: $itemNs      sortBy: $sortBy      sortDir: $sortDir      releaseDate: $releaseDate      start: $start      tag: $tag      priceRange: $priceRange      freeGame: $freeGame      onSale: $onSale      effectiveDate: $effectiveDate    ) {      elements {        title        id        namespace        description        effectiveDate        keyImages {          type          url        }        currentPrice        seller {          id          name        }        productSlug        urlSlug        url        tags {          id        }        items {          id          namespace        }        customAttributes {          key          value        }        categories {          path        }        price(country: $country) @include(if: $withPrice) {          totalPrice {            discountPrice            originalPrice            voucherDiscount            discount            currencyCode            currencyInfo {              decimals            }            fmtPrice(locale: $locale) {              originalPrice              discountPrice              intermediatePrice            }          }          lineOffers {            appliedRules {              id              endDate              discountSetting {                discountType              }            }          }        }        promotions(category: $category) @include(if: $withPromotions) {          promotionalOffers {            promotionalOffers {              startDate              endDate              discountSetting {                discountType                discountPercentage              }            }          }          upcomingPromotionalOffers {            promotionalOffers {              startDate              endDate              discountSetting {                discountType                discountPercentage              }            }          }        }      }      paging {        count        total      }    }  }}");
        GRAPHQL_QUERY.put("variables", "{\"category\":\"games/edition/base|bundles/games|editors|software/edition/base\",\"count\":1000,\"country\":\"PL\",\"keywords\":\"\",\"locale\":\"en-US\",\"sortBy\":\"releaseDate\",\"sortDir\":\"DESC\",\"allowCountries\":\"PL\",\"start\":0,\"tag\":\"\",\"releaseDate\":\"[,2021-02-27T12:21:26.125Z]\",\"withPrice\":true}");

        GRAPHQL_QUERY_DISCOUNT.put("query", "query searchStoreQuery($allowCountries: String, $category: String, $count: Int, $country: String!, $keywords: String, $locale: String, $namespace: String, $itemNs: String, $sortBy: String, $sortDir: String, $start: Int, $tag: String, $releaseDate: String, $withPrice: Boolean = false, $withPromotions: Boolean = false, $priceRange: String, $freeGame: Boolean, $onSale: Boolean, $effectiveDate: String) {  Catalog {    searchStore(      allowCountries: $allowCountries      category: $category      count: $count      country: $country      keywords: $keywords      locale: $locale      namespace: $namespace      itemNs: $itemNs      sortBy: $sortBy      sortDir: $sortDir      releaseDate: $releaseDate      start: $start      tag: $tag      priceRange: $priceRange      freeGame: $freeGame      onSale: $onSale      effectiveDate: $effectiveDate    ) {      elements {        title        id        namespace        description        effectiveDate        keyImages {          type          url        }        currentPrice        seller {          id          name        }        productSlug        urlSlug        url        tags {          id        }        items {          id          namespace        }        customAttributes {          key          value        }        categories {          path        }        price(country: $country) @include(if: $withPrice) {          totalPrice {            discountPrice            originalPrice            voucherDiscount            discount            currencyCode            currencyInfo {              decimals            }            fmtPrice(locale: $locale) {              originalPrice              discountPrice              intermediatePrice            }          }          lineOffers {            appliedRules {              id              endDate              discountSetting {                discountType              }            }          }        }        promotions(category: $category) @include(if: $withPromotions) {          promotionalOffers {            promotionalOffers {              startDate              endDate              discountSetting {                discountType                discountPercentage              }            }          }          upcomingPromotionalOffers {            promotionalOffers {              startDate              endDate              discountSetting {                discountType                discountPercentage              }            }          }        }      }      paging {        count        total      }    }  }}");
        GRAPHQL_QUERY_DISCOUNT.put("variables", "{\"category\":\"games/edition/base|bundles/games|editors|software/edition/base\",\"count\":30,\"country\":\"PL\",\"keywords\":\"\",\"locale\":\"en-US\",\"sortBy\":\"releaseDate\",\"sortDir\":\"DESC\",\"allowCountries\":\"PL\",\"start\":0,\"tag\":\"\",\"releaseDate\":\"[,2021-02-27T12:21:26.125Z]\",\"withPrice\":true, \"onSale\":true}");
    }

    @Override
    public void loadData() {

        ResponseEntity<EpicGamesListResponseDto> response = restTemplate.postForEntity(apiUrl + "/graphql", new HttpEntity<>(GRAPHQL_QUERY), EpicGamesListResponseDto.class);

        response.getBody().getGamesList().forEach(epicGameDto -> {
            Game game = mapper.map(epicGameDto);
            gamesRepository.save(game);
            game.setGameImages(getGameImagesForGame(epicGameDto.getImages(), game));
        });
    }

    private List<GameImage> getGameImagesForGame(List<String> externalImages, Game game){
        List<GameImage> images = externalImages.stream().map(dto -> GameImage.builder()
                .imageUrl(dto)
                .game(game).build())
                .collect(Collectors.toList());
        gameImageRepository.saveAll(images);
        return images;
    }

    @Override
    public EpicGameDetailsDto getGameDetails(String externalGameId) {

        EpicGameDetailsDto detailsDto = new EpicGameDetailsDto();
        try {
            final HtmlPage gameDetailsHtml = webClient.getPage(apiUrl + "/store/en-US/p/" + externalGameId.replace("/home", "").replace("/", "--"));

            DomNodeList<DomNode> divsDescription = gameDetailsHtml.querySelectorAll(".css-cvywoi-Description-styles__imageContainerSimple");

            DomNode divDescription = divsDescription.get(0);

            String description = divDescription.getNextSibling().getFirstChild().getFirstChild().getNodeValue();

            List<String> categories = new ArrayList<>();

            log.info("Updating epic game {}", externalGameId.replace("/home", "").replace("/", "--"));

            DomNodeList<DomNode> divsCategories = gameDetailsHtml.querySelectorAll(".css-8lm9zv-GameMeta-styles__metaList");
            divsCategories.get(2).getFirstChild().getFirstChild().getChildNodes().forEach(divCategory -> {
                String category = divCategory.getFirstChild().getFirstChild().getNodeValue();
                categories.add(category);
            });

            detailsDto.setDescription(description);
            detailsDto.setCategories(categories);

        } catch (IOException e) {
            log.error("Cannot load epic store game details {}", externalGameId);
        } catch (IndexOutOfBoundsException e){
            log.error("Cannot load details of game {}", externalGameId.replace("/home", "").replace("/", "--"));
            detailsDto.setDescription("");
            detailsDto.setCategories(new ArrayList<>());
        }

        return detailsDto;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public List<Game> getSpecialOffers() {
        ResponseEntity<EpicGamesListResponseDto> response = restTemplate.postForEntity(apiUrl + "/graphql", new HttpEntity<>(GRAPHQL_QUERY_DISCOUNT), EpicGamesListResponseDto.class);

        List<Game> games = new ArrayList<>();

        response.getBody().getGamesList().forEach(epicGameDto -> {
            Game game = gamesRepository.findByNameAndMarketplaceName(epicGameDto.getTitle(), MarketPlaceConstants.MARKETPLACE_NAME_EPIC_GAMES);
            if(game != null){
                game.setDiscountPercent(epicGameDto.getDiscountPercent());
                game.setPriceFinal(epicGameDto.getCurrentPrice());
                game.setPriceInitial(epicGameDto.getPrice());
                games.add(game);
            }
        });

        return games;
    }

}
