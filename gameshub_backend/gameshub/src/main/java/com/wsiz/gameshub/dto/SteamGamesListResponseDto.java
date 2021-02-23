package com.wsiz.gameshub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SteamGamesListResponseDto {

    private List<SteamGameDto> gamesList;

    @SuppressWarnings("unchecked")
    @JsonProperty("applist")
    private void unpackNested(Map<String,Object> applist) {
        this.gamesList = new ArrayList<>();
        List<LinkedHashMap<String, Object>> gameAsMap = (List<LinkedHashMap<String, Object>>)applist.get("apps");

        gameAsMap.forEach(game -> {
            SteamGameDto steamGameDto = SteamGameDto.builder()
                    .name((String) game.get("name"))
                    .appId(Long.valueOf((Integer) game.get("appid")))
                    .build();
            gamesList.add(steamGameDto);
        });

    }
}
