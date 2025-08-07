package com.wsiz.gameshub.dto.gog;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GogGameDetailsDto {

    private String description;
    private String shortDescription;

    @JsonProperty("description")
    private void unpackDescription(Map<String, String> description){
        this.description = description.get("full");
        this.shortDescription = description.get("lead");
    }
}
