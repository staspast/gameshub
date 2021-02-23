package com.wsiz.gameshub.service;

import com.wsiz.gameshub.factory.GameDecoratorFactory;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.repository.GamesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GamesService {

    private final GamesRepository gamesRepository;
    private final GameDecoratorFactory gameDecoratorFactory;

    public List<Game> getGameList(String name){
        List<Game> games = gamesRepository.findByNameContaining(name);

        games.forEach(game -> {
            gameDecoratorFactory.getDecoratorForMarketplace(game.getMarketplaceName()).decorate(game);
        });

        return games;
    }
}
