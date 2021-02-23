package com.wsiz.gameshub.decorator;

import com.wsiz.gameshub.dto.GogGameDetailsDto;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.service.GogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class GogDecorator implements GameDecorator{

    private final GogService gogService;

    @Override
    @Transactional
    public void decorate(Game game) {
        if(!Boolean.TRUE.equals(game.getLoadedDetailsFromExternalApi())){

            GogGameDetailsDto gogGameDetailsDto = gogService.getGameDetails(game.getExternalAppId());

            game.setDescription(gogGameDetailsDto.getDescription());
            game.setShortDescription(gogGameDetailsDto.getShortDescription());
            game.setLoadedDetailsFromExternalApi(true);
        }
    }
}
