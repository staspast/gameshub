package com.wsiz.gameshub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpicGamesListResponseDto {

    private List<EpicGameDto> gamesList = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @JsonProperty("data")
    private void unpackNested(Map<String,Object> applist) {
        List<LinkedHashMap<String, Object>> gamesListMap = (List<LinkedHashMap<String, Object>>) ((Map<String, Object>)((Map<String,Object>)applist.get("Catalog")).get("searchStore")).get("elements");

        gamesListMap.forEach(gameMap -> {
            LinkedHashMap<String, Object> totalPrice = ((LinkedHashMap<String, LinkedHashMap<String, Object>>) gameMap.get("price")).get("totalPrice");
            gamesList.add(EpicGameDto.builder()
                    .id((String) gameMap.get("id"))
                    .title((String) gameMap.get("title"))
                    .description((String) gameMap.get("description"))
                    .currentPrice(new BigDecimal((Integer) totalPrice.get("discountPrice")).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                    .slug((String) gameMap.get("productSlug"))
                    .price(new BigDecimal((Integer) totalPrice.get("originalPrice")).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                    .discountPercent(getDiscountPercent(totalPrice))
                    .publisher(extractValueFromAttributeMap((List<LinkedHashMap<String, String>>) gameMap.get("customAttributes"), "publisherName"))
                    .developer(extractValueFromAttributeMap((List<LinkedHashMap<String, String>>) gameMap.get("customAttributes"), "developerName"))
                    .images(extractListFromMap((List<LinkedHashMap<String, String>>) gameMap.get("keyImages"), "url"))
                    .mainImage(extractMainImageFromAttributeMap((List<LinkedHashMap<String, String>>) gameMap.get("keyImages"), "Thumbnail"))
                    .build());
        });

    }

    private BigDecimal getDiscountPercent(LinkedHashMap<String, Object> totalPrice){
        BigDecimal originalPrice = new BigDecimal((Integer) totalPrice.get("originalPrice"));
        if(originalPrice.compareTo(BigDecimal.valueOf(0)) > 0) {
            return new BigDecimal((Integer) totalPrice.get("discount")).divide(new BigDecimal((Integer) totalPrice.get("originalPrice")), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }

    private String extractValueFromAttributeMap(List<LinkedHashMap<String, String>> attributes, String valueName){
        String value = null;
        for(LinkedHashMap<String, String> attribute : attributes){
            if(attribute.get("key") != null && attribute.get("key").equals(valueName)){
                value = attribute.get("value");
                break;
            }
        }
        return value;
    }

    private String extractMainImageFromAttributeMap(List<LinkedHashMap<String, String>> attributes, String valueName){
        String value = null;
        for(LinkedHashMap<String, String> attribute : attributes){
            if(attribute.get("type") != null && attribute.get("type").equals(valueName)){
                value = attribute.get("url");
                break;
            }
        }
        return value;
    }

    private List<String> extractListFromMap(List<LinkedHashMap<String, String>> attributes, String valueName){
        List<String> value = new ArrayList<>();
        for(LinkedHashMap<String, String> attribute : attributes){
            if(attribute.get(valueName) != null){
                value.add(attribute.get(valueName));
            }
        }
        return value;
    }
}
