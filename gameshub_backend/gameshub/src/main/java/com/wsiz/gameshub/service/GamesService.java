package com.wsiz.gameshub.service;

import com.wsiz.gameshub.factory.GameDecoratorFactory;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.repository.GamesLuceneRepository;
import com.wsiz.gameshub.model.repository.GamesRepository;
import com.wsiz.gameshub.request.SearchGamesFilter;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.engine.search.query.SearchResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class GamesService {

    private final GamesRepository gamesRepository;
    private final GameDecoratorFactory gameDecoratorFactory;
    private final GamesLuceneRepository gamesLuceneRepository;

    public Page<Game> getGameList(SearchGamesFilter filter){
        Page<Game> games = gamesRepository.search(filter.getName() != null ? filter.getName() : "", filter.getMarketplaceName(), PageRequest.of(filter.getPageNumber(), filter.getPageSize()));
        decorateUnloadedGames(games.getContent());
        return games;
    }

    public Page<Game> searchGamesLucene(SearchGamesFilter filter){
        SearchResult<Game> result = gamesLuceneRepository.search(filter);
        decorateUnloadedGames(result.hits());
        return new PageImpl<>(result.hits(), PageRequest.of(filter.getPageNumber(), filter.getPageSize()), result.total().hitCount());
    }

    private void decorateUnloadedGames(List<Game> games){
        games.forEach(game -> {
            if(!Boolean.TRUE.equals(game.getLoadedDetailsFromExternalApi())) {
                gameDecoratorFactory.getDecoratorForMarketplace(game.getMarketplaceName()).decorate(game);
            }
        });
    }
}
