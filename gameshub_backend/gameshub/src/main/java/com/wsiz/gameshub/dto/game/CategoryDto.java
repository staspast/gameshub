package com.wsiz.gameshub.dto.game;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto implements Serializable {

    private Long id;
    private String name;
    private String marketplaceName;
    private Long externalId;
}
