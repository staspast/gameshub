package com.wsiz.gameshub.service;

import com.wsiz.gameshub.dto.EpicGamesListResponseDto;
import com.wsiz.gameshub.dto.GogGameDetailsDto;
import com.wsiz.gameshub.mapper.EpicGamesMapper;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.entity.GameImage;
import com.wsiz.gameshub.model.repository.GameImageRepository;
import com.wsiz.gameshub.model.repository.GamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EpicGamesService implements GameProviderService<GogGameDetailsDto> {

    @Value("${external-api.epic-games.api.url}")
    private String apiUrl;

    private static final Map<String, String> GRAPHQL_QUERY = new HashMap<>();
    private final RestTemplate restTemplate;
    private final GamesRepository gamesRepository;
    private final GameImageRepository gameImageRepository;
    private final EpicGamesMapper mapper;


    @Autowired
    public EpicGamesService(GamesRepository gamesRepository, EpicGamesMapper steamMapper, GameImageRepository gameImageRepository){
        this.restTemplate = new RestTemplate();
        this.gamesRepository = gamesRepository;
        this.mapper = steamMapper;
        this.gameImageRepository = gameImageRepository;

        GRAPHQL_QUERY.put("query", "query searchStoreQuery($allowCountries: String, $category: String, $count: Int, $country: String!, $keywords: String, $locale: String, $namespace: String, $itemNs: String, $sortBy: String, $sortDir: String, $start: Int, $tag: String, $releaseDate: String, $withPrice: Boolean = false, $withPromotions: Boolean = false, $priceRange: String, $freeGame: Boolean, $onSale: Boolean, $effectiveDate: String) {  Catalog {    searchStore(      allowCountries: $allowCountries      category: $category      count: $count      country: $country      keywords: $keywords      locale: $locale      namespace: $namespace      itemNs: $itemNs      sortBy: $sortBy      sortDir: $sortDir      releaseDate: $releaseDate      start: $start      tag: $tag      priceRange: $priceRange      freeGame: $freeGame      onSale: $onSale      effectiveDate: $effectiveDate    ) {      elements {        title        id        namespace        description        effectiveDate        keyImages {          type          url        }        currentPrice        seller {          id          name        }        productSlug        urlSlug        url        tags {          id        }        items {          id          namespace        }        customAttributes {          key          value        }        categories {          path        }        price(country: $country) @include(if: $withPrice) {          totalPrice {            discountPrice            originalPrice            voucherDiscount            discount            currencyCode            currencyInfo {              decimals            }            fmtPrice(locale: $locale) {              originalPrice              discountPrice              intermediatePrice            }          }          lineOffers {            appliedRules {              id              endDate              discountSetting {                discountType              }            }          }        }        promotions(category: $category) @include(if: $withPromotions) {          promotionalOffers {            promotionalOffers {              startDate              endDate              discountSetting {                discountType                discountPercentage              }            }          }          upcomingPromotionalOffers {            promotionalOffers {              startDate              endDate              discountSetting {                discountType                discountPercentage              }            }          }        }      }      paging {        count        total      }    }  }}");
        GRAPHQL_QUERY.put("variables", "{\"category\":\"games/edition/base|bundles/games|editors|software/edition/base\",\"count\":1000,\"country\":\"PL\",\"keywords\":\"\",\"locale\":\"en-US\",\"sortBy\":\"releaseDate\",\"sortDir\":\"DESC\",\"allowCountries\":\"PL\",\"start\":0,\"tag\":\"\",\"releaseDate\":\"[,2021-02-27T12:21:26.125Z]\",\"withPrice\":true}");
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
    public GogGameDetailsDto getGameDetails(String externalGameId) {
        return null;
    }

}
