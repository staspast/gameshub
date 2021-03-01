package com.wsiz.gameshub.model.repository;

import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.request.SearchGamesFilter;
import org.hibernate.search.engine.search.query.SearchResult;
import org.springframework.data.domain.Page;

public interface GamesLuceneRepository {

    SearchResult<Game> search(SearchGamesFilter filter);
}
