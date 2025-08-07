package com.wsiz.gameshub.decorator;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.dto.steam.SteamCategoryDto;
import com.wsiz.gameshub.dto.steam.SteamGameDetailsDto;
import com.wsiz.gameshub.dto.steam.SteamImageDto;
import com.wsiz.gameshub.model.entity.Category;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.entity.GameImage;
import com.wsiz.gameshub.model.repository.CategoryRepository;
import com.wsiz.gameshub.model.repository.GameImageRepository;
import com.wsiz.gameshub.service.SteamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SteamDecorator extends GameDecorator{

    private final SteamService steamService;
    private final CategoryRepository categoryRepository;
    private final GameImageRepository gameImageRepository;

    @Override
    @Transactional
    public void decorate(Game game) {
        SteamGameDetailsDto detailsDto = steamService.getGameDetails(game.getExternalAppId());

        game.setCurrency(detailsDto.getCurrency());
        game.setDiscountPercent(detailsDto.getDiscountPercent());
        game.setPriceInitial(detailsDto.getPriceInitial());
        game.setPriceFinal(detailsDto.getPriceFinal());
        game.setLoadedDetailsFromExternalApi(Boolean.TRUE);

        game.setIsReleased(detailsDto.getIsReleased());
        game.setIsGame("game".equals(detailsDto.getType()));
        game.setCategories(detailsDto.getGenres() != null ? getSteamCategories(detailsDto.getGenres()) : null);
        game.setShortDescription(detailsDto.getShortDescription());
        game.setDescription(detailsDto.getDescription());
        game.setMainImageUrl(detailsDto.getMainImageUrl());
        game.setDeveloper(detailsDto.getDevelopers() != null ? String.join(",", detailsDto.getDevelopers()) : null);
        game.setPublisher(detailsDto.getPublishers() != null ? String.join(",", detailsDto.getPublishers()) : null);
        game.setGameImages(detailsDto.getScreenshots() != null ? getSteamGameImagesForGame(detailsDto.getScreenshots(), game) : null);
        game.setUpdatedAt(LocalDateTime.now());

    }

    @Override
    protected String getMarketplaceName() {
        return MarketPlaceConstants.MARKETPLACE_NAME_STEAM;
    }

    private List<Category> getSteamCategories(List<SteamCategoryDto> categoryDtos){
        return categoryDtos.stream().map(this::getCategory).collect(Collectors.toList());
    }

    private Category getCategory(SteamCategoryDto categoryDto){
        Category category = categoryRepository.findByNameAndMarketplaceName(categoryDto.getDescription(), MarketPlaceConstants.MARKETPLACE_NAME_STEAM);

        if(category == null){
            category = Category.builder()
                    .name(categoryDto.getDescription())
                    .externalId(categoryDto.getId())
                    .marketplaceName(MarketPlaceConstants.MARKETPLACE_NAME_STEAM)
                    .build();
            categoryRepository.save(category);
        }

        return category;
    }

    private List<GameImage> getSteamGameImagesForGame(List<SteamImageDto> steamImageDtos, Game game){
        List<GameImage> images = steamImageDtos.stream().map(dto -> GameImage.builder()
                .imageUrl(dto.getImageFull())
                .imageThumbnailUrl(dto.getImageThumbnail())
                .game(game).build())
                .collect(Collectors.toList());
        gameImageRepository.saveAll(images);
        return images;
    }
}
