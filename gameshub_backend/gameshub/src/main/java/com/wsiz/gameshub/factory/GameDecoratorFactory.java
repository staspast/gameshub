package com.wsiz.gameshub.factory;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.decorator.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameDecoratorFactory {

    private final SteamDecorator steamDecorator;
    private final GogDecorator gogDecorator;
    private final HumbleBundleDecorator humbleBundleDecorator;
    private final EpicGamesDecorator epicGamesDecorator;

    public GameDecorator getDecoratorForMarketplace(String marketplaceName){
        switch (marketplaceName){
            case MarketPlaceConstants.MARKETPLACE_NAME_STEAM:
                return steamDecorator;
            case MarketPlaceConstants.MARKETPLACE_NAME_GOG:
                return gogDecorator;
            case MarketPlaceConstants.MARKETPLACE_NAME_HUMBLE_BUNDLE:
                return humbleBundleDecorator;
            case MarketPlaceConstants.MARKETPLACE_NAME_EPIC_GAMES:
                return epicGamesDecorator;
            default:
                throw new IllegalArgumentException("Incorrect marketplace value");
        }
    }
}
