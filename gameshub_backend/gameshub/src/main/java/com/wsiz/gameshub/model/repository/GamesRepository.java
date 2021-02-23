package com.wsiz.gameshub.model.repository;

import com.wsiz.gameshub.model.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GamesRepository extends JpaRepository<Game, Long> {

    List<Game> findByNameContaining(String name);
}
