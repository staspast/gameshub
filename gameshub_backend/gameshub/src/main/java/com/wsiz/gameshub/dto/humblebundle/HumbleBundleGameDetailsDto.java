package com.wsiz.gameshub.dto.humblebundle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class HumbleBundleGameDetailsDto {

    private String description;
    private String publisher;
    private String developer;
    private List<String> categories;
    private List<String> images;

}
