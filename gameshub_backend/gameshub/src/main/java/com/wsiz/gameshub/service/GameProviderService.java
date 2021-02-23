package com.wsiz.gameshub.service;

public interface GameProviderService<T> {

    void loadData();

    T getGameDetails(String externalId);
}
