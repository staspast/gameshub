package com.wsiz.gameshub.decorator;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.dto.humblebundle.HumbleBundleGameDetailsDto;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.service.HumbleBundleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class HumbleBundleDecorator extends GameDecorator{

    private final HumbleBundleService humbleBundleService;

    @Override
    @Transactional
    public void decorate(Game game) {

        HumbleBundleGameDetailsDto detailsDto = humbleBundleService.getGameDetails(game.getExternalAppId());

        game.setDescription(detailsDto.getDescription());
        game.setCategories(getCategories(detailsDto.getCategories()));
        game.setPublisher(detailsDto.getPublisher());
        game.setDeveloper(detailsDto.getDeveloper());
        game.setGameImages(getGameImagesForGame(detailsDto.getImages(), game));
        game.setLoadedDetailsFromExternalApi(true);
        game.setUpdatedAt(LocalDateTime.now());

    }

    @Override
    protected String getMarketplaceName() {
        return MarketPlaceConstants.MARKETPLACE_NAME_HUMBLE_BUNDLE;
    }


}
