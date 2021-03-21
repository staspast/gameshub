package com.wsiz.gameshub.model.repository;

import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.request.SearchGamesFilter;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.SearchSort;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Locale;

@Transactional(readOnly = true)
@Component
public class GamesRepositoryImpl implements GamesLuceneRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public SearchResult<Game> search(SearchGamesFilter filter) {

        SearchSession searchSession = Search.session( entityManager );

        String sortField = getSort(filter.getSort());
        String order = filter.getSortOrder() != null ? filter.getSortOrder().toUpperCase(Locale.ROOT) : "ASC";

        return searchSession.search(Game.class).where(game ->
                game.bool(bool -> {
                    bool.must( game.matchAll() );
                    if(filter.getName() != null){
                        bool.must(game.match().field("name").matching(filter.getName()).fuzzy(1));
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
                    if(filter.getCategoryName() != null && !filter.getCategoryName().isEmpty()){
                        for (String category : filter.getCategoryName()) {
                            bool.must(game.bool().should(game.match().field("categories.name").matching(category)));
                        }
                    }
                }))
                .sort(sort -> sort.composite(s -> {
                    if(sortField == null){
                        s.add(sort.score());
                    } else {
                        s.add(sort.field(sortField).order(SortOrder.valueOf(order)));
                    }
                }))
                .fetch(filter.getPageNumber() * filter.getPageSize(), filter.getPageSize());
    }

    private String getSort(String sortField){
        if(sortField != null){
            switch (sortField){
                case "name":
                    return "nameSort";
                case "price":
                    return "priceFinal";
            }
        }

        return null;
    }
}
