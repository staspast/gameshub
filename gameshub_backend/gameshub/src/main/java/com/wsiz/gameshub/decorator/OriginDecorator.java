package com.wsiz.gameshub.decorator;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.dto.epic.EpicGameDetailsDto;
import com.wsiz.gameshub.dto.origin.OriginGameDetailsDto;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.service.EpicGamesService;
import com.wsiz.gameshub.service.OriginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OriginDecorator extends GameDecorator{

    private final OriginService originService;

    @Override
    @Transactional
    public void decorate(Game game) {
        if(!Boolean.TRUE.equals(game.getLoadedDetailsFromExternalApi())) {
            OriginGameDetailsDto detailsDto = originService.getGameDetails(game.getExternalAppId());

            game.setDescription(detailsDto.getDescription());
            game.setCategories(getCategories(detailsDto.getCategories()));
            game.setGameImages(getGameImagesForGame(detailsDto.getImages(), game));
            game.setDeveloper(detailsDto.getDeveloper());
            game.setPublisher(detailsDto.getPublisher());
            game.setPriceFinal(detailsDto.getPrice());
            game.setPriceInitial(detailsDto.getPrice());
            game.setDiscountPercent(BigDecimal.ZERO);
            game.setAddedAt(LocalDateTime.now());
            game.setCurrency("USD");
            game.setLoadedDetailsFromExternalApi(Boolean.TRUE);
            game.setIsGame(true);
            game.setIsReleased(true);
        }
    }

    @Override
    protected String getMarketplaceName() {
        return MarketPlaceConstants.MARKETPLACE_NAME_EPIC_ORIGIN;
    }
}
