package com.wsiz.gameshub.decorator;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.dto.epic.EpicGameDetailsDto;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.service.EpicGamesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EpicGamesDecorator extends GameDecorator{

    private final EpicGamesService epicGamesService;

    @Override
    @Transactional
    public void decorate(Game game) {
        if(!Boolean.TRUE.equals(game.getLoadedDetailsFromExternalApi())) {
            EpicGameDetailsDto detailsDto = epicGamesService.getGameDetails(game.getExternalAppId());
            game.setDescription(detailsDto.getDescription());
            game.setCategories(getCategories(detailsDto.getCategories()));
            game.setLoadedDetailsFromExternalApi(true);
            game.setUpdatedAt(LocalDateTime.now());
        }
    }

    @Override
    protected String getMarketplaceName() {
        return MarketPlaceConstants.MARKETPLACE_NAME_EPIC_GAMES;
    }
}
