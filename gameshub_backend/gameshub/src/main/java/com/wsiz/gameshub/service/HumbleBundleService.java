package com.wsiz.gameshub.service;

import com.wsiz.gameshub.dto.HumbleBundleGameDto;
import com.wsiz.gameshub.dto.HumbleBundleGamesListResponseDto;
import com.wsiz.gameshub.mapper.HumbleBundleMapper;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.repository.CategoryRepository;
import com.wsiz.gameshub.model.repository.GameImageRepository;
import com.wsiz.gameshub.model.repository.GamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HumbleBundleService implements GameProviderService {

    @Value("${external-api.humblebundle.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final GamesRepository gamesRepository;
    private final CategoryRepository categoryRepository;
    private final GameImageRepository gameImageRepository;
    private final HumbleBundleMapper mapper;

    private final static int HUMBLE_PAGE_MAX = 433;

    @Autowired
    public HumbleBundleService(GamesRepository gamesRepository, HumbleBundleMapper steamMapper, CategoryRepository categoryRepository, GameImageRepository gameImageRepository){
        this.restTemplate = new RestTemplate();
        this.gamesRepository = gamesRepository;
        this.mapper = steamMapper;
        this.categoryRepository = categoryRepository;
        this.gameImageRepository = gameImageRepository;
    }

    @Override
    public void loadData(){

        int page = 301;

        while (page <= HUMBLE_PAGE_MAX) {
            ResponseEntity<HumbleBundleGamesListResponseDto> response = restTemplate.getForEntity(apiUrl + "/store/api/search?sort=discount&filter=all&request=1&page=" + page, HumbleBundleGamesListResponseDto.class);

            List<Game> games = response.getBody().getResults().stream().map(mapper::map).collect(Collectors.toList());

            gamesRepository.saveAll(games);
            page++;

            System.out.println("page");
            System.out.println(page);
        }
    }

}
