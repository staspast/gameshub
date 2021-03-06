package com.wsiz.gameshub.dto.origin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OriginGameDto {

    private String mainImageIrl;
    private String name;
    private String slug;
}
