package com.wsiz.gameshub.factory;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.decorator.GameDecorator;
import com.wsiz.gameshub.decorator.GogDecorator;
import com.wsiz.gameshub.decorator.SteamDecorator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameDecoratorFactory {

    private final SteamDecorator steamDecorator;
    private final GogDecorator gogDecorator;

    public GameDecorator getDecoratorForMarketplace(String marketplaceName){
        switch (marketplaceName){
            case MarketPlaceConstants.MARKETPLACE_NAME_STEAM:
                return steamDecorator;
            case MarketPlaceConstants.MARKETPLACE_NAME_GOG:
                return gogDecorator;
            default:
                throw new IllegalArgumentException("Incorrect marketplace value");
        }
    }
}
