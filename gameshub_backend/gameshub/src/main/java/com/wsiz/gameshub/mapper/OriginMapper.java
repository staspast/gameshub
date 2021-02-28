package com.wsiz.gameshub.mapper;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.dto.origin.OriginGameDto;
import com.wsiz.gameshub.model.entity.Game;
import org.springframework.stereotype.Component;

@Component
public class OriginMapper implements GameMapper<OriginGameDto, Game> {

    @Override
    public Game map(OriginGameDto source) {
        return Game.builder()
                .externalAppId(source.getSlug())
                .name(source.getName())
                .marketplaceName(getMarketplaceName())
                .mainImageUrl(source.getMainImageIrl())
                .build();
    }

    @Override
    public String getMarketplaceName() {
        return MarketPlaceConstants.MARKETPLACE_NAME_EPIC_ORIGIN;
    }
}
