package com.mhorak.lyrichunter.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhorak.lyrichunter.models.Session;
import com.mhorak.lyrichunter.models.Song;
import com.mhorak.lyrichunter.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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

    public Session startGame(String artistName) {
        // Create a new session
        Session session = new Session();
        session.setSessionId(UUID.randomUUID());

        Long artistId = geniusService.getArtist(artistName);

        List<Long> songIds = (List<Long>) geniusService.getArtistSongIds(artistId);

        // Picking random song
        var randomSongId = songIds.get((int) (Math.random() * songIds.size()));


        // Get song details
        String songResponse = geniusService.getSongDetails(randomSongId).block();
        Song song = convertSongResponse(songResponse);
        session.setSong(song);

        session.setGuess(getStarterGuess(song.getLyrics()));
        session.setGuessed(false);

        // Save the session to DB
        sessionRepository.save(session);

        System.out.println("Lyrics: " + session.getSong().getLyrics());

        return session;
    }

    public Session revealWord(UUID sessionId, JsonNode guessJson) {

        String guess = guessJson.get("word").asText();

        Session session = sessionRepository.findById(sessionId).
                orElseThrow(() -> new RuntimeException("Session not found"));

        guess = guess.trim();
        // Make sure that guess is one word
        // If guess contains more than one word, only the first word will be used
        if (guess.contains(" ")) {
            guess = guess.split(" ")[0];
        }
        // Remove all non-alphabetic characters or ' - from the guess
        guess = guess.replaceAll("[^a-zA-Z'-]", "");


        // Replace all occurrences of the guessed word in the guess with the actual word
        String lyrics = session.getSong().getLyrics();
        // Find the index of the guessed word in the lyrics
        // check lowercase
        int index = lyrics.toLowerCase().indexOf(guess.toLowerCase());
        while (index != -1) {

            // If the word is surrounded by " " or "\n" or "," or "." or "!" or "?" or ":", replace it
            String[] surroundingChars = {" ", "\n", ",", ".", "!", "?", ":", "", ";"};

            String[] wordSurroundings = new String[2];

            if (index == 0) {
                wordSurroundings[0] = "";
            } else {
                wordSurroundings[0] = lyrics.substring(index - 1, index);
            }

            if (index + guess.length() == lyrics.length()) {
                wordSurroundings[1] = "";
            } else {
                wordSurroundings[1] = lyrics.substring(index + guess.length(), index + guess.length() + 1);
            }

            // If wordSurroundings[0] and wordSurroundings[1] are in surroundingChars, replace the word
            if (List.of(surroundingChars).contains(wordSurroundings[0]) && List.of(surroundingChars).contains(wordSurroundings[1])) {
                // Replace the word in the guess
                session.setGuess(
                        session.getGuess().substring(0, index)
                                + lyrics.substring(index, index + guess.length())
                                + session.getGuess().substring(index + guess.length())
                );
            }

            index = lyrics.toLowerCase().indexOf(guess.toLowerCase(), index + guess.length());
        }

        return sessionRepository.save(session);
    }

    public Session guess(UUID sessionId, JsonNode guessJson) {
        String guess = guessJson.get("guess").asText();

        Session session = sessionRepository.findById(sessionId).
                orElseThrow(() -> new RuntimeException("Session not found"));

        if (isPartlyMatched(session.getSong().getTitle(), guess)) {
            session.setGuessed(true);
        }

        return sessionRepository.save(session);
    }

    private boolean isPartlyMatched(String songName, String guess) {
        // Normalize strings
        String normalizedSongName = songName.toLowerCase();
        String normalizedGuess = guess.toLowerCase();

        // Tokenize strings
        String[] songNameWords = normalizedSongName.split("\\s+");
        String[] guessWords = normalizedGuess.split("\\s+");

        // Define minimum word match threshold
        int minWordMatchThreshold = Math.min(2, songNameWords.length);

        // Check partial match
        int matchedWords = 0;
        for (int i = 0, j = 0; i < songNameWords.length && j < guessWords.length; i++) {
            if (songNameWords[i].equals(guessWords[j])) {
                matchedWords++;
                j++;
            }
        }

        // Verify if the number of matched words meets the threshold
        return matchedWords >= minWordMatchThreshold || guessWords.length == songNameWords.length;
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

    private String getStarterGuess(String lyrics) {
        String[] words = lyrics.split(" ");
        StringBuilder guess = new StringBuilder();
        for (String word : words) {
            guess.append("_".repeat(word.length()));
            guess.append(" ");
        }

        Character[] punctuation = {'.', ',', '!', '?', ':', ';', '\n'};
        for (int i = 0; i < lyrics.length(); i++) {
            if (List.of(punctuation).contains(lyrics.charAt(i))) {
                guess.setCharAt(i, lyrics.charAt(i));
            }
        }


        return guess.toString();
    }
}
