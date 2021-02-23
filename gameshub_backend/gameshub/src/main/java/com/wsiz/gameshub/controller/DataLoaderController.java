package com.wsiz.gameshub.controller;

import com.wsiz.gameshub.service.GogService;
import com.wsiz.gameshub.service.HumbleBundleService;
import com.wsiz.gameshub.service.SteamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/api/data_load"))
@RequiredArgsConstructor
public class DataLoaderController {

    private final SteamService steamService;
    private final GogService gogService;
    private final HumbleBundleService humbleBundleService;

    @PostMapping("/steam")
    public ResponseEntity<Void> loadSteamData(){
        steamService.loadData();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/gog")
    public ResponseEntity<Void> loadGogData(){
        gogService.loadData();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/humblehubdle")
    public ResponseEntity<Void> loadHumbleBundleData(){
        humbleBundleService.loadData();
        return ResponseEntity.ok().build();
    }
}
