package com.wsiz.gameshub.mapper;

import org.springframework.stereotype.Component;

import com.wsiz.gameshub.dto.game.CategoryDto;
import com.wsiz.gameshub.model.entity.Category;

@Component
public class CategoryEntityToDtoMapper implements Mapper<Category, CategoryDto> {

    @Override
    public CategoryDto map(Category source) {
        return CategoryDto.builder()
                .id(source.getId())
                .name(source.getName())
                .marketplaceName(source.getMarketplaceName())
                .externalId(source.getExternalId())
                .build();
    }

}
