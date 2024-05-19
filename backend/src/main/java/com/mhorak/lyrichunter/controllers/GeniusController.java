package com.mhorak.lyrichunter.controllers;

import com.mhorak.lyrichunter.services.GeniusApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class GeniusController {

    @Autowired
    private GeniusApiService geniusService;

    @GetMapping("/api/search")
    public Mono<ResponseEntity<String>> searchSong(@RequestParam String query) {
        return geniusService.searchSong(query)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/api/song")
    public Mono<ResponseEntity<String>> getSongDetails(@RequestParam Long songId) {
        return geniusService.getSongDetails(songId)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/api/artist")
    public Mono<ResponseEntity<String>> searchArtist(@RequestParam String query) {
        return geniusService.searchArtist(query)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/api/artist/songs")
    public ResponseEntity<Iterable<Long>> getArtistSongIds(@RequestParam Long artistId) {
        return ResponseEntity.ok(geniusService.getArtistSongIds(artistId));
    }


}
