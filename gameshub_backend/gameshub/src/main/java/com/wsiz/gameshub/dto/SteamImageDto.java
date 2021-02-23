package com.wsiz.gameshub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamImageDto {

    @JsonProperty("path_thumbnail")
    private String imageThumbnail;
    @JsonProperty("path_full")
    private String imageFull;
}
