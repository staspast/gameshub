package com.wsiz.gameshub.decorator;

import com.wsiz.gameshub.dto.HumbleBundleGameDetailsDto;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.service.HumbleBundleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class HumbleBundleDecorator implements GameDecorator{

    private final HumbleBundleService humbleBundleService;

    @Override
    @Transactional
    public void decorate(Game game) {

        if(!Boolean.TRUE.equals(game.getLoadedDetailsFromExternalApi())) {
            HumbleBundleGameDetailsDto detailsDto = humbleBundleService.getGameDetails(game.getExternalAppId());

            game.setDescription(detailsDto.getDescription());
        }

    }
}
