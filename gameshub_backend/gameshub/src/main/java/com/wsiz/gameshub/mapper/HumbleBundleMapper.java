package com.wsiz.gameshub.mapper;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.dto.humblebundle.HumbleBundleGameDto;
import com.wsiz.gameshub.model.entity.Game;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class HumbleBundleMapper implements GameMapper<HumbleBundleGameDto, Game> {

    @Override
    public Game map(HumbleBundleGameDto source) {
        return Game.builder()
                .externalAppId(source.getAppId())
                .name(source.getName())
                .priceInitial(source.getPriceInitial())
                .priceFinal(source.getPriceFinal())
                .currency(source.getCurrency())
                .marketplaceName(getMarketplaceName())
                .addedAt(LocalDateTime.now())
                .mainImageUrl(source.getImage())
                .build();
    }

    @Override
    public String getMarketplaceName() {
        return MarketPlaceConstants.MARKETPLACE_NAME_HUMBLE_BUNDLE;
    }
}
