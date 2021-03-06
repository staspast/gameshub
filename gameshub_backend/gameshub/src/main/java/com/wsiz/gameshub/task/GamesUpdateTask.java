package com.wsiz.gameshub.task;

import com.wsiz.gameshub.factory.GameDecoratorFactory;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.repository.GamesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GamesUpdateTask {

    private final GamesRepository gamesRepository;
    private final GameDecoratorFactory decoratorFactory;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @Scheduled(fixedDelay = 60000)
    public void updateNonUpdatedGameData(){
        List<Game> gamesToProcess = gamesRepository.findGamesForUpdate(PageRequest.of(0, 10));

        log.info("Update games to process {}", gamesToProcess.size());

        gamesToProcess.forEach(game -> decoratorFactory.getDecoratorForMarketplace(game.getMarketplaceName()).decorate(game));
    }
}
