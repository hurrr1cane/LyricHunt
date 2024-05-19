package com.mhorak.lyrichunter.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhorak.lyrichunter.models.Session;
import com.mhorak.lyrichunter.models.Song;
import com.mhorak.lyrichunter.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    private final SessionRepository sessionRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final GeniusApiService geniusService;

    @Autowired
    public GameService(SessionRepository sessionRepository, GeniusApiService geniusService) {
        this.sessionRepository = sessionRepository;
        this.geniusService = geniusService;
    }

    public Session startGame(Long artistId) {
        // Create a new session
        Session session = new Session();
        session.setSessionId(UUID.randomUUID());

        List<Long> songIds = (List<Long>) geniusService.getArtistSongIds(artistId);

        // Picking random song
        var randomSongId = songIds.get((int) (Math.random() * songIds.size()));


        // Get song details
        String songResponse = geniusService.getSongDetails(randomSongId).block();
        Song song = convertSongResponse(songResponse);
        session.setSong(song);

        session.setGuess("");
        session.setGuessed(false);

        // Save the session to DB
        sessionRepository.save(session);

        System.out.println("Lyrics: " + session.getSong().getLyrics());

        return session;
    }

    private Song convertSongResponse(String songResponse) {

        Song song = new Song();

        try {
            JsonNode root = objectMapper.readTree(songResponse);
            JsonNode songPath = root.path("response").path("song");

            song.setId(songPath.path("id").asLong());
            song.setTitle(songPath.path("title").asText());
            song.setArtist(songPath.path("primary_artist").path("name").asText());

            String lyrics = geniusService.getLyrics(songPath.path("url").asText());

            song.setLyrics(lyrics);

            System.out.println("Lyrics length: " + lyrics.length());

            //System.out.println("Lyrics: " + lyrics);


        } catch (Exception e) {
            throw new RuntimeException("Failed to parse song details from response", e);
        }

        return song;
    }
}
