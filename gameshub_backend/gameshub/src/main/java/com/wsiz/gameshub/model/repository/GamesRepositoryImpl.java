package com.wsiz.gameshub.model.repository;

import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.request.SearchGamesFilter;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional(readOnly = true)
@Component
public class GamesRepositoryImpl implements GamesLuceneRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public SearchResult<Game> search(SearchGamesFilter filter) {

        SearchSession searchSession = Search.session( entityManager );

        return searchSession.search(Game.class).where(game ->
                game.bool(bool -> {
                    bool.must( game.matchAll() );
                    if(filter.getName() != null){
                        bool.must(game.match().field("name").matching(filter.getName()).fuzzy());
                    }
                    if(filter.getMarketplaceName() != null){
                        bool.must(game.match().field("marketplaceName").matching(filter.getMarketplaceName()));
                    }
                    if(filter.getPriceFrom() != null){
                        bool.must(game.range().field("priceFinal").atLeast(filter.getPriceFrom()));
                    }
                    if(filter.getPriceTo() != null){
                        bool.must(game.range().field("priceFinal").lessThan(filter.getPriceTo()));
                    }
                    if(filter.getCategoryName() != null){
                        bool.must(game.match().field("categories.name").matching(filter.getCategoryName()));
                    }
                }))
                .fetch(filter.getPageNumber(), filter.getPageSize());
    }
}
