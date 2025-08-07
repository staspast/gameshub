package com.wsiz.gameshub.model.repository;

import org.hibernate.search.engine.search.query.SearchResult;

import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.request.SearchGamesFilter;

public interface GamesLuceneRepository {

    SearchResult<Game> search(SearchGamesFilter filter);
}
