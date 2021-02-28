package com.wsiz.gameshub.service;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.dto.GogGameDetailsDto;
import com.wsiz.gameshub.dto.GogGamesListResponseDto;
import com.wsiz.gameshub.dto.OriginGamesListResponseDto;
import com.wsiz.gameshub.mapper.GogMapper;
import com.wsiz.gameshub.mapper.OriginMapper;
import com.wsiz.gameshub.model.entity.Category;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.entity.GameImage;
import com.wsiz.gameshub.model.repository.CategoryRepository;
import com.wsiz.gameshub.model.repository.GameImageRepository;
import com.wsiz.gameshub.model.repository.GamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OriginService implements GameProviderService<GogGameDetailsDto> {

    @Value("${external-api.origin.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final GamesRepository gamesRepository;
    private final CategoryRepository categoryRepository;
    private final GameImageRepository gameImageRepository;
    private final OriginMapper mapper;

    private final static int ORIGIN_MAX_GAMES = 978;
    private final static int ORIGIN_PAGE_STEP = 30;

    @Autowired
    public OriginService(GamesRepository gamesRepository, OriginMapper steamMapper, CategoryRepository categoryRepository, GameImageRepository gameImageRepository){
        this.restTemplate = new RestTemplate();
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

    private List<Category> getCategories(List<String> categories){
        return categories.stream().map(this::getCategory).collect(Collectors.toList());
    }

    private Category getCategory(String categoryName){
        Category category = categoryRepository.findByNameAndMarketplaceName(categoryName, MarketPlaceConstants.MARKETPLACE_NAME_GOG);

        if(category == null){
            category = Category.builder()
                    .name(categoryName)
                    .externalId(null)
                    .marketplaceName(MarketPlaceConstants.MARKETPLACE_NAME_GOG)
                    .build();
            categoryRepository.save(category);
        }

        return category;
    }

    private List<GameImage> getGameImagesForGame(List<String> externalImages, Game game){
        List<GameImage> images = externalImages.stream().map(dto -> GameImage.builder()
                .imageUrl(dto.replace("//", "") + ".jpg")
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
