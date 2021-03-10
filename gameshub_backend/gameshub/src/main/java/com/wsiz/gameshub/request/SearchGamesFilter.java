package com.wsiz.gameshub.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SearchGamesFilter {

    private String name;
    private String marketplaceName;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private String categoryName;
    private int pageNumber = 0;
    private int pageSize = 10;
}
