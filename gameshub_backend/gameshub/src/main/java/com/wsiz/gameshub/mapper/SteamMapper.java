package com.wsiz.gameshub.mapper;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.dto.steam.SteamGameDto;
import com.wsiz.gameshub.model.entity.Game;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SteamMapper implements GameMapper<SteamGameDto, Game> {


    @Override
    public Game map(SteamGameDto source) {
        return Game.builder()
                .externalAppId(source.getAppId())
                .name(source.getName())
                .marketplaceName(getMarketplaceName())
                .addedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public String getMarketplaceName() {
        return MarketPlaceConstants.MARKETPLACE_NAME_STEAM;
    }


}
