package com.wsiz.gameshub.dto.origin;

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
public class OriginGamesListResponseDto {

    private List<OriginGameDto> gamesDtoList = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @JsonProperty("games")
    private void unpackNested(Map<String,Object> applist) {

        List<LinkedHashMap<String, String>> gamesList = (List<LinkedHashMap<String, String>>) applist.get("game");

        gamesList.forEach(gameMap -> {
            gamesDtoList.add(OriginGameDto.builder()
                    .name(gameMap.get("gameName"))
                    .mainImageIrl(gameMap.get("image"))
                    .slug(gameMap.get("path"))
                    .build());
        });
    }
}
