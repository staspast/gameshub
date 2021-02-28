package com.wsiz.gameshub.mapper;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.dto.gog.GogGameDto;
import com.wsiz.gameshub.model.entity.Game;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GogMapper implements GameMapper<GogGameDto, Game> {

    @Override
    public Game map(GogGameDto source) {
        return Game.builder()
                .externalAppId(source.getAppId())
                .name(source.getName())
                .priceInitial(source.getPriceInitial())
                .priceFinal(source.getPriceFinal())
                .currency(source.getCurrency())
                .discountPercent(source.getDiscountPercent())
                .marketplaceName(getMarketplaceName())
                .addedAt(LocalDateTime.now())
                .isGame(source.getIsGame())
                .isReleased(!Boolean.TRUE.equals(source.getIsTBA()))
                .developer(source.getDeveloper())
                .publisher(source.getPublisher())
                .mainImageUrl(source.getImage() != null ? source.getImage().replace("//", "") + ".jpg" : null)
                .build();
    }

    @Override
    public String getMarketplaceName() {
        return MarketPlaceConstants.MARKETPLACE_NAME_GOG;
    }
}
