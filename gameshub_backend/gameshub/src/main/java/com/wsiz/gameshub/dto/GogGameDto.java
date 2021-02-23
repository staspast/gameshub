package com.wsiz.gameshub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GogGameDto {

    @JsonProperty("title")
    private String name;
    @JsonProperty("id")
    private Long appId;
    private String currency;
    private BigDecimal priceInitial;
    private BigDecimal priceFinal;
    private BigDecimal discountPercent;
    private String developer;
    private String publisher;
    private Boolean isTBA;
    private List<String> gallery;
    private String image;
    private List<String> genres;
    private Boolean isGame;

    @JsonProperty("price")
    private void unpackFromNestedJson(Map<String, String> priceOverview){
        this.currency = "PLN";//FIXME
        this.priceFinal = new BigDecimal(priceOverview.get("finalAmount"));
        this.priceInitial = new BigDecimal(priceOverview.get("baseAmount"));
        this.discountPercent = new BigDecimal(priceOverview.get("discountPercentage"));
    }

}
