package com.wsiz.gameshub.decorator;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.dto.HumbleBundleGameDetailsDto;
import com.wsiz.gameshub.dto.SteamImageDto;
import com.wsiz.gameshub.model.entity.Category;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.entity.GameImage;
import com.wsiz.gameshub.model.repository.CategoryRepository;
import com.wsiz.gameshub.model.repository.GameImageRepository;
import com.wsiz.gameshub.service.HumbleBundleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HumbleBundleDecorator implements GameDecorator{

    private final HumbleBundleService humbleBundleService;
    private final CategoryRepository categoryRepository;
    private final GameImageRepository gameImageRepository;

    @Override
    @Transactional
    public void decorate(Game game) {

        if(!Boolean.TRUE.equals(game.getLoadedDetailsFromExternalApi())) {
            HumbleBundleGameDetailsDto detailsDto = humbleBundleService.getGameDetails(game.getExternalAppId());

            game.setDescription(detailsDto.getDescription());
            game.setCategories(getCategories(detailsDto.getCategories()));
            game.setPublisher(detailsDto.getPublisher());
            game.setDeveloper(detailsDto.getDeveloper());
            game.setGameImages(getGameImagesForGame(detailsDto.getImages(), game));
            game.setLoadedDetailsFromExternalApi(true);
        }

    }

    private List<Category> getCategories(List<String> categories){
        return categories.stream().map(this::getCategory).collect(Collectors.toList());
    }

    private Category getCategory(String categoryName){
        Category category = categoryRepository.findByNameAndMarketplaceName(categoryName, MarketPlaceConstants.MARKETPLACE_NAME_HUMBLE_BUNDLE);

        if(category == null){
            category = Category.builder()
                    .name(categoryName)
                    .externalId(null)
                    .marketplaceName(MarketPlaceConstants.MARKETPLACE_NAME_HUMBLE_BUNDLE)
                    .build();
            categoryRepository.save(category);
        }

        return category;
    }

    private List<GameImage> getGameImagesForGame(List<String> imagesStrings, Game game){
        List<GameImage> images = imagesStrings.stream().filter(Objects::nonNull).map(image -> GameImage.builder()
                .imageUrl(image)
                .imageThumbnailUrl(null)
                .game(game).build())
                .collect(Collectors.toList());
        gameImageRepository.saveAll(images);
        return images;
    }
}
