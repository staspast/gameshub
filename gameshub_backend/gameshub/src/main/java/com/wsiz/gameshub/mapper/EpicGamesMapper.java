package com.wsiz.gameshub.mapper;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.dto.epic.EpicGameDto;
import com.wsiz.gameshub.model.entity.Game;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EpicGamesMapper implements GameMapper<EpicGameDto, Game> {

    @Override
    public Game map(EpicGameDto source) {
        return Game.builder()
                .externalAppId(source.getSlug())
                .name(source.getTitle())
                .priceInitial(source.getPrice())
                .priceFinal(source.getCurrentPrice())
                .currency("PLN")
                .discountPercent(source.getDiscountPercent())
                .marketplaceName(getMarketplaceName())
                .addedAt(LocalDateTime.now())
                .isGame(true)
                .isReleased(true)
                .developer(source.getDeveloper())
                .publisher(source.getPublisher())
                .mainImageUrl(source.getMainImage())
                .build();
    }

    @Override
    public String getMarketplaceName() {
        return MarketPlaceConstants.MARKETPLACE_NAME_EPIC_GAMES;
    }
}
