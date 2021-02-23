package com.wsiz.gameshub.model.repository;

import com.wsiz.gameshub.model.entity.GameImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameImageRepository extends JpaRepository<GameImage, Long> {
}
