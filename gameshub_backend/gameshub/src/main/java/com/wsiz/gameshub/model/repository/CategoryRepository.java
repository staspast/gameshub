package com.wsiz.gameshub.model.repository;

import com.wsiz.gameshub.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByNameAndMarketplaceName(String name, String marketplaceName);
}
