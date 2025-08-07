package com.wsiz.gameshub.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wsiz.gameshub.model.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByNameAndMarketplaceName(String name, String marketplaceName);
}
