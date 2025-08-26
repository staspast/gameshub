package com.wsiz.gameshub.model.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wsiz.gameshub.model.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findByExternalIdInAndMarketplaceName(Collection<Long> ids, String marketplaceName);

}
