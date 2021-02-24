package com.wsiz.gameshub.controller;

import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.request.SearchGamesFilter;
import com.wsiz.gameshub.service.GamesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@AllArgsConstructor
public class GamesController {

    private final GamesService gamesService;

    @GetMapping("/search")
    public List<Game> search(SearchGamesFilter filter){
        return gamesService.getGameList(filter);
    }
}
