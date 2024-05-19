package com.mhorak.lyrichunter.controllers;

import com.mhorak.lyrichunter.mappers.Mapper;
import com.mhorak.lyrichunter.mappers.SessionMapper;
import com.mhorak.lyrichunter.models.Session;
import com.mhorak.lyrichunter.models.dtos.SessionDto;
import com.mhorak.lyrichunter.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

    private GameService gameService;

    private Mapper<Session, SessionDto> sessionMapper;

    public GameController(GameService gameService, SessionMapper sessionMapper) {
        this.gameService = gameService;
        this.sessionMapper = sessionMapper;
    }

    @PostMapping("/start/{artistId}")
    public ResponseEntity<SessionDto> startGame(@PathVariable Long artistId) {
        return ResponseEntity.ok(sessionMapper.mapTo(gameService.startGame(artistId)));
    }


}
