package com.wsiz.gameshub.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchGamesFilter {

    private String name;
    private String marketplaceName;
    private int pageNumber = 0;
    private int pageSize = 10;
}
