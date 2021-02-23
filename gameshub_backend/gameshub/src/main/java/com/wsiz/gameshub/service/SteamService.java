package com.wsiz.gameshub.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wsiz.gameshub.dto.SteamGameDetailsDto;
import com.wsiz.gameshub.dto.SteamGamesListResponseDto;
import com.wsiz.gameshub.mapper.SteamMapper;
import com.wsiz.gameshub.model.entity.Game;
import com.wsiz.gameshub.model.repository.GamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SteamService implements GameProviderService {

    @Value("${external-api.steam.url}")
    private String apiUrl;

    @Value("${external-api.steam.store.url}")
    private String storeUrl;

    private final RestTemplate restTemplate;
    private final GamesRepository gamesRepository;
    private final SteamMapper mapper;

    @Autowired
    public SteamService(GamesRepository gamesRepository, SteamMapper steamMapper){
        this.restTemplate = new RestTemplate();
        this.gamesRepository = gamesRepository;
        this.mapper = steamMapper;
    }

    @Override
    public void loadData(){

        ResponseEntity<SteamGamesListResponseDto> response = restTemplate.getForEntity(apiUrl + "/ISteamApps/GetAppList/v2", SteamGamesListResponseDto.class);

        List<Game> games = mapper.mapList(response.getBody().getGamesList());

        gamesRepository.saveAll(games);
    }

    public SteamGameDetailsDto getGameDetails(String externalGameId) {

        SteamGameDetailsDto detailsDto = new SteamGameDetailsDto();

        ResponseEntity<String> respose = restTemplate.getForEntity(storeUrl + "/api/appdetails?appids=" + externalGameId, String.class);

        try {
            JSONObject jo = new JSONObject(respose.getBody());
            JSONObject statusObject = new JSONObject(jo.getString(String.valueOf(externalGameId)));

            if (statusObject.getBoolean("success")) {
                ObjectMapper mapper = new ObjectMapper();
                detailsDto = mapper.readValue(statusObject.getString("data"), SteamGameDetailsDto.class);
            }
        } catch (JSONException | JsonProcessingException e) {
            e.printStackTrace();
        }


        return detailsDto;
    }
}
