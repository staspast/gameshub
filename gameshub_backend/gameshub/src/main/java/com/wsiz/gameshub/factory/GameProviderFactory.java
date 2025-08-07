package com.wsiz.gameshub.factory;

import org.springframework.stereotype.Component;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.service.EpicGamesService;
import com.wsiz.gameshub.service.GameProviderService;
import com.wsiz.gameshub.service.GogService;
import com.wsiz.gameshub.service.HumbleBundleService;
import com.wsiz.gameshub.service.OriginService;
import com.wsiz.gameshub.service.SteamService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GameProviderFactory {

    private final SteamService steamService;
    private final GogService gogService;
    private final HumbleBundleService humbleBundleService;
    private final EpicGamesService epicGamesService;
    private final OriginService originService;

    public GameProviderService<?> getProviderForMarketplace(String marketplaceName){
        switch (marketplaceName){
            case MarketPlaceConstants.MARKETPLACE_NAME_STEAM:
                return steamService;
            case MarketPlaceConstants.MARKETPLACE_NAME_GOG:
                return gogService;
            case MarketPlaceConstants.MARKETPLACE_NAME_HUMBLE_BUNDLE:
                return humbleBundleService;
            case MarketPlaceConstants.MARKETPLACE_NAME_EPIC_GAMES:
                return epicGamesService;
            case MarketPlaceConstants.MARKETPLACE_NAME_EPIC_ORIGIN:
                return originService;
            default:
                throw new IllegalArgumentException("Incorrect marketplace value");
        }
    }
}
