package com.wsiz.gameshub.decorator;

import com.wsiz.gameshub.dto.GogGameDetailsDto;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.service.GogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class HumbleBundleDecorator implements GameDecorator{


    @Override
    @Transactional
    public void decorate(Game game) {
        //empty for now
    }
}
