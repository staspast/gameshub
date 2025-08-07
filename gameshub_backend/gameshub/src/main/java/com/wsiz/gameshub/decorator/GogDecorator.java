package com.wsiz.gameshub.decorator;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.dto.gog.GogGameDetailsDto;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.service.GogService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class GogDecorator extends GameDecorator{

    private final GogService gogService;

    @Override
    @Transactional
    public void decorate(Game game) {
        GogGameDetailsDto gogGameDetailsDto = gogService.getGameDetails(game.getExternalAppId());
        game.setDescription(gogGameDetailsDto.getDescription());
        game.setShortDescription(gogGameDetailsDto.getShortDescription());
        game.setLoadedDetailsFromExternalApi(true);
        game.setUpdatedAt(LocalDateTime.now());
    }

    @Override
    protected String getMarketplaceName() {
        return MarketPlaceConstants.MARKETPLACE_NAME_GOG;
    }
}
