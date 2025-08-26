package com.wsiz.gameshub.dto.steam;

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
public class SteamGameDetailsDto {

    private String currency;
    private BigDecimal priceInitial;
    private BigDecimal priceFinal;
    private BigDecimal discountPercent;
    private String type;
    @JsonProperty("about_the_game")
    private String description;
    @JsonProperty("short_description")
    private String shortDescription;
    @JsonProperty("header_image")
    private String mainImageUrl;
    private List<SteamGenreDto> genres;
    private List<SteamCategoryDto> categories;
    private List<SteamImageDto> screenshots;
    private List<String> developers;
    private List<String> publishers;
    private Boolean isReleased;

    @SuppressWarnings("unused")
    @JsonProperty("price_overview")
    private void unpackFromNestedJson(Map<String, String> priceOverview){
        this.currency = priceOverview.get("currency");
        this.priceFinal = new BigDecimal(priceOverview.get("final")).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        this.priceInitial = new BigDecimal(priceOverview.get("initial")).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        this.discountPercent = new BigDecimal(priceOverview.get("discount_percent"));
    }

    @JsonProperty("release_date")
    private void unpackIsReleased(Map<String, String> releaseDate){
        this.isReleased = "false".equals(releaseDate.get("coming_soon"));
    }
}
