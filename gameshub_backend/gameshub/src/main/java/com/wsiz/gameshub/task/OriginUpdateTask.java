package com.wsiz.gameshub.task;

import com.wsiz.gameshub.constant.MarketPlaceConstants;
import com.wsiz.gameshub.decorator.OriginDecorator;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.repository.GamesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OriginUpdateTask implements UpdateTask{

    private final GamesRepository gamesRepository;
    private final OriginDecorator decorator;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @Scheduled(fixedDelay = 10000)
    public void updateNonUpdatedGameData(){
        List<Game> gamesToProcess = gamesRepository.findNonLoadedByMarketplaceName(MarketPlaceConstants.MARKETPLACE_NAME_EPIC_ORIGIN, PageRequest.of(0, 10));

        log.info("Origin games to process {}", gamesToProcess.size());

        gamesToProcess.forEach(decorator::decorate);
    }
}
