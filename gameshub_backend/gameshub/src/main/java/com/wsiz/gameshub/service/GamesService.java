package com.wsiz.gameshub.service;

import com.wsiz.gameshub.factory.GameDecoratorFactory;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.repository.GamesRepository;
import com.wsiz.gameshub.request.SearchGamesFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GamesService {

    private final GamesRepository gamesRepository;
    private final GameDecoratorFactory gameDecoratorFactory;

    public List<Game> getGameList(SearchGamesFilter filter){
        List<Game> games = gamesRepository.search(filter.getName() != null ? filter.getName() : "", filter.getMarketplaceName());

        games.forEach(game -> {
            gameDecoratorFactory.getDecoratorForMarketplace(game.getMarketplaceName()).decorate(game);
        });

        return games;
    }
}
