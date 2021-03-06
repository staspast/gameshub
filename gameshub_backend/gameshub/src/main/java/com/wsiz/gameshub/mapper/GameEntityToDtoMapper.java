package com.wsiz.gameshub.mapper;

import com.wsiz.gameshub.dto.game.GameDto;
import com.wsiz.gameshub.model.entity.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GameEntityToDtoMapper implements Mapper<Game, GameDto> {

    private final GameImageEntityToDtoMapper gameImageMapper;
    private final CategoryEntityToDtoMapper categoryMapper;

    @Override
    public GameDto map(Game source) {
        return GameDto.builder()
                .id(source.getId())
                .name(source.getName())
                .externalAppId(source.getExternalAppId())
                .marketplaceName(source.getMarketplaceName())
                .priceInitial(source.getPriceInitial())
                .priceFinal(source.getPriceFinal())
                .discountPercent(source.getDiscountPercent())
                .loadedDetailsFromExternalApi(source.getLoadedDetailsFromExternalApi())
                .addedAt(source.getAddedAt())
                .updatedAt(source.getUpdatedAt())
                .currency(source.getCurrency())
                .isReleased(source.getIsReleased())
                .isGame(source.getIsGame())
                .mainImageUrl(source.getMainImageUrl())
                .description(source.getDescription())
                .shortDescription(source.getShortDescription())
                .developer(source.getDeveloper())
                .publisher(source.getPublisher())
                .categories(categoryMapper.mapList(source.getCategories()))
                .gameImages(gameImageMapper.mapList(source.getGameImages()))
                .build();
    }

}
