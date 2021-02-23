package com.wsiz.gameshub.mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface GameMapper<T, R>{

    R map(T source);

    default List<R> mapList(List<T> sourceList){
        if(sourceList == null){
            throw new IllegalArgumentException("Source list is null");
        }

        return sourceList.stream().map(this::map).collect(Collectors.toList());
    }

    String getMarketplaceName();
}
