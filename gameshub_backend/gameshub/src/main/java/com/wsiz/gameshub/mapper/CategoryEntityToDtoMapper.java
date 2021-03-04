package com.wsiz.gameshub.mapper;

import com.wsiz.gameshub.dto.game.CategoryDto;
import com.wsiz.gameshub.dto.game.GameDto;
import com.wsiz.gameshub.model.entity.Category;
import com.wsiz.gameshub.model.entity.Game;
import org.springframework.stereotype.Component;

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
