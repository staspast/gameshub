package com.wsiz.gameshub.controller;

import com.wsiz.gameshub.dto.game.GameDto;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.request.SearchGamesFilter;
import com.wsiz.gameshub.service.GamesService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/games")
@AllArgsConstructor
public class GamesController {

    private final GamesService gamesService;

    @GetMapping("/search")
    public Page<Game> search(SearchGamesFilter filter){
        return gamesService.getGameList(filter);
    }

    @GetMapping("/lucene_search")
    public Page<Game> searchLucene(SearchGamesFilter filter){
        return gamesService.searchGamesLucene(filter);
    }

    @GetMapping("/get/{id}")
    public Game search(@PathVariable Long id){
        return gamesService.getGame(id);
    }

    @GetMapping("/offers")
    public List<GameDto> getSpecialOffers(){
        return gamesService.getSpecialOffers();
    }
}
