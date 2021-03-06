package com.wsiz.gameshub.service;

import com.wsiz.gameshub.model.entity.Game;

import java.util.List;

public interface GameProviderService<T> {

    void loadData();

    T getGameDetails(String externalId);

    List<Game> getSpecialOffers();
}
