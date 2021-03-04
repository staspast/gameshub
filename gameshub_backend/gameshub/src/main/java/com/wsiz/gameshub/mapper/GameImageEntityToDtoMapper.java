package com.wsiz.gameshub.mapper;

import com.wsiz.gameshub.dto.game.GameImageDto;
import com.wsiz.gameshub.model.entity.GameImage;
import org.springframework.stereotype.Component;

@Component
public class GameImageEntityToDtoMapper implements Mapper<GameImage, GameImageDto> {

    @Override
    public GameImageDto map(GameImage source) {
        return GameImageDto.builder()
                .id(source.getId())
                .imageUrl(source.getImageUrl())
                .imageThumbnailUrl(source.getImageThumbnailUrl())
                .build();
    }

}
