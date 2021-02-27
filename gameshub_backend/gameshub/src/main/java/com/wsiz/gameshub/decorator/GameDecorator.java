package com.wsiz.gameshub.decorator;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.model.entity.Category;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.entity.GameImage;
import com.wsiz.gameshub.model.repository.CategoryRepository;
import com.wsiz.gameshub.model.repository.GameImageRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class GameDecorator {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private GameImageRepository gameImageRepository;

    public abstract void decorate(Game game);

    protected abstract String getMarketplaceName();

    protected List<Category> getCategories(List<String> categories){
        return categories.stream().map(this::getCategory).collect(Collectors.toList());
    }

    protected Category getCategory(String categoryName){
        Category category = categoryRepository.findByNameAndMarketplaceName(categoryName, getMarketplaceName());

        if(category == null){
            category = Category.builder()
                    .name(categoryName)
                    .externalId(null)
                    .marketplaceName(getMarketplaceName())
                    .build();
            categoryRepository.save(category);
        }

        return category;
    }

    protected List<GameImage> getGameImagesForGame(List<String> imagesStrings, Game game){
        List<GameImage> images = imagesStrings.stream().filter(Objects::nonNull).map(image -> GameImage.builder()
                .imageUrl(image)
                .imageThumbnailUrl(null)
                .game(game).build())
                .collect(Collectors.toList());
        gameImageRepository.saveAll(images);
        return images;
    }
}
