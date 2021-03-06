package com.wsiz.gameshub.dto.game;

import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameDto implements Serializable {

    private Long id;
    private String name;
    private String externalAppId;
    private String marketplaceName;
    private BigDecimal priceInitial;
    private BigDecimal priceFinal;
    private BigDecimal discountPercent;
    private Boolean loadedDetailsFromExternalApi;
    private LocalDateTime addedAt;
    private LocalDateTime updatedAt;
    private String currency;
    private Boolean isReleased;
    private Boolean isGame;
    private String mainImageUrl;
    private List<GameImageDto> gameImages;
    private String description;
    private String shortDescription;
    private List<CategoryDto> categories;
    private String developer;
    private String publisher;
}
