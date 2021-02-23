package com.wsiz.gameshub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HumbleBundleGamesListResponseDto {

    @JsonProperty("num_pages")
    private Long numPages;
    private List<HumbleBundleGameDto> results;
}
