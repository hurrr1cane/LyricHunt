package com.mhorak.lyrichunter.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.mhorak.lyrichunter.mappers.Mapper;
import com.mhorak.lyrichunter.mappers.SessionMapper;
import com.mhorak.lyrichunter.models.Session;
import com.mhorak.lyrichunter.models.dtos.SessionDto;
import com.mhorak.lyrichunter.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    private final Mapper<Session, SessionDto> sessionMapper;

    public GameController(GameService gameService, SessionMapper sessionMapper) {
        this.gameService = gameService;
        this.sessionMapper = sessionMapper;
    }

    @PostMapping("/start")
    public ResponseEntity<SessionDto> startGame(@RequestParam String artistName) {
        return ResponseEntity.ok(sessionMapper.mapTo(gameService.startGame(artistName)));
    }

    @PostMapping("/reveal/{sessionId}")
    public ResponseEntity<SessionDto> revealWord(@PathVariable UUID sessionId, @RequestBody JsonNode guess) {
        return ResponseEntity.ok(sessionMapper.mapTo(gameService.revealWord(sessionId, guess)));
    }

    @PostMapping("/guess/{sessionId}")
    public ResponseEntity<?> guess(@PathVariable UUID sessionId, @RequestBody JsonNode guess) {
        Session session = gameService.guess(sessionId, guess);
        if (session.isGuessed()) {
            session.getSong().getLyrics();
            return ResponseEntity.ok(session);
        }
        return ResponseEntity.ok(sessionMapper.mapTo(session));
    }


}
