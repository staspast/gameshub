package com.wsiz.gameshub.service;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.dto.gog.GogGameDetailsDto;
import com.wsiz.gameshub.dto.gog.GogGamesListResponseDto;
import com.wsiz.gameshub.mapper.GogMapper;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GogService implements GameProviderService<GogGameDetailsDto> {

    @Value("${external-api.gog.embed.url}")
    private String apiEmbedUrl;

    @Value("${external-api.gog.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final GamesRepository gamesRepository;
    private final CategoryRepository categoryRepository;
    private final GameImageRepository gameImageRepository;
    private final GogMapper mapper;

    private final static int GOG_PAGE_MAX = 98;

    @Autowired
    public GogService(GamesRepository gamesRepository, GogMapper steamMapper, CategoryRepository categoryRepository, GameImageRepository gameImageRepository){
        this.restTemplate = new RestTemplate();
        this.gamesRepository = gamesRepository;
        this.mapper = steamMapper;
        this.categoryRepository = categoryRepository;
        this.gameImageRepository = gameImageRepository;
    }

    @Override
    public void loadData(){

        int page = 1;

        while (page <= GOG_PAGE_MAX) {
            ResponseEntity<GogGamesListResponseDto> response = restTemplate.getForEntity(apiEmbedUrl + "/games/ajax/filtered?mediaType=game&page=" + page, GogGamesListResponseDto.class);
            response.getBody().getProducts().forEach(gogGameDto -> {
                Game game = mapper.map(gogGameDto);
                gamesRepository.save(game);
                game.setCategories(getCategories(gogGameDto.getGenres()));
                game.setGameImages(getGameImagesForGame(gogGameDto.getGallery(), game));
            });
            page++;
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
        ResponseEntity<GogGameDetailsDto> respose = restTemplate.getForEntity(apiUrl + "/products/" + externalGameId + "?expand=description", GogGameDetailsDto.class);
        return respose.getBody() != null ? respose.getBody() : new GogGameDetailsDto();
    }

    @Override
    public List<Game> getSpecialOffers() {
        return new ArrayList<>();
    }

}
