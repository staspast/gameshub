package com.wsiz.gameshub.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class SearchGamesFilter {

    private String name;
    private String marketplaceName;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private List<String> categoryName;
    private String sort;
    private String sortOrder;
    private int pageNumber = 0;
    private int pageSize = 10;
}
