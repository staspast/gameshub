package com.wsiz.gameshub.controller;

import com.wsiz.gameshub.dto.game.GameDto;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.request.SearchCompareGamesFilter;
import com.wsiz.gameshub.request.SearchGamesFilter;
import com.wsiz.gameshub.service.GamesService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
    public List<GameDto> getSpecialOffers(@RequestParam String marketplaceName){
        return gamesService.getSpecialOffers(marketplaceName);
    }

    @GetMapping("/compare_with_other_stores")
    public List<Game> compareWithStore(SearchCompareGamesFilter filter){
        return gamesService.compareWithStore(filter);
    }
}
