package com.wsiz.gameshub.dto.epic;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpicGameDto {

    private List<String> images;
    private BigDecimal currentPrice;
    private String publisher;
    private String developer;
    private String description;
    private String id;
    private String slug;
    private BigDecimal price;
    private BigDecimal discountPercent;
    private String title;
    private String mainImage;
}
