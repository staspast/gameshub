package com.wsiz.gameshub.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GogGamesListResponseDto {

    private List<GogGameDto> products;
}
