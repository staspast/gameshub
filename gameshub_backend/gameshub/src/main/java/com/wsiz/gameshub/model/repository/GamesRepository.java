package com.wsiz.gameshub.model.repository;

import com.wsiz.gameshub.model.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GamesRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM GAME g WHERE (:name is null or g.name LIKE CONCAT('%',:name,'%')) and (:marketplaceName is null or g.marketplaceName = :marketplaceName)")
    Page<Game> search(@Param("name") String name, @Param("marketplaceName") String marketplaceName, Pageable pageable);

    @Query("SELECT g FROM GAME g WHERE g.marketplaceName = :marketplaceName and g.loadedDetailsFromExternalApi is null")
    List<Game> findNonLoadedByMarketplaceName(String marketplaceName, Pageable pageable);

    @Query("SELECT g FROM GAME g WHERE g.loadedDetailsFromExternalApi = true ORDER BY g.updatedAt asc nulls first ")
    List<Game> findGamesForUpdate(Pageable pageable);

    Game findByNameAndMarketplaceName(String name, String marketplaceName);
}
