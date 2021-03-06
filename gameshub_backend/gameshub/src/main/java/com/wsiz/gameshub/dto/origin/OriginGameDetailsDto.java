package com.wsiz.gameshub.dto.origin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OriginGameDetailsDto {

    private String description;
    private String shortDescription;
    private List<String> images;
    private List<String> categories;
    private String developer;
    private String publisher;
    private BigDecimal price;
}
