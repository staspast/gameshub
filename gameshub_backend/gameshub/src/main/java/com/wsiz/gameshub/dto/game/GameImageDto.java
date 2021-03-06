package com.wsiz.gameshub.dto.game;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameImageDto implements Serializable {
    private Long id;
    private String imageUrl;
    private String imageThumbnailUrl;
}
