package com.wsiz.gameshub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class HumbleBundleGameDto {

    @JsonProperty("human_name")
    private String name;
    @JsonProperty("human_url")
    private String appId;
    private String currency;
    private BigDecimal priceInitial;
    private BigDecimal priceFinal;
    @JsonProperty("large_capsule")
    private String image;

    @JsonProperty("current_price")
    private void unpackCurrentPrice(Map<String, String> priceOverview){
        this.currency = priceOverview.get("currency");
        this.priceFinal = new BigDecimal(priceOverview.get("amount"));
    }

    @JsonProperty("full_price")
    private void unpackFullPrice(Map<String, String> priceOverview){
        this.priceInitial = new BigDecimal(priceOverview.get("amount"));
    }

}
