package com.mhorak.lyrichunter.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeniusApiService {

    private final WebClient webClient;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Value("${genius.api.token}")
    private String apiToken;

    public GeniusApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.genius.com").build();
    }

    public Mono<String> searchSong(String query) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/search").queryParam("q", query).build())
                .header("Authorization", "Bearer " + apiToken)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getSongDetails(Long songId) {
        return this.webClient.get()
                .uri("/songs/{id}", songId)
                .header("Authorization", "Bearer " + apiToken)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> searchArtist(String query) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/search").queryParam("q", query).queryParam("type", "artist").build())
                .header("Authorization", "Bearer " + apiToken)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Long getArtist(String query) {
        var response = this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/search").queryParam("q", query).queryParam("type", "artist").build())
                .header("Authorization", "Bearer " + apiToken)
                .retrieve()
                .bodyToMono(String.class);

        return retrieveArtistId(response.block());
    }

    public Iterable<Long> getArtistSongIds(Long artistId) {
        List<Long> allSongIds = new ArrayList<>();
        int currentPage = 1;
        boolean hasMorePages = true;

        while (hasMorePages) {
            int finalCurrentPage = currentPage;
            String artistResponse = this.webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/artists/{id}/songs")
                            .queryParam("page", finalCurrentPage)
                            .queryParam("per_page", 50)
                            .build(artistId))
                    .header("Authorization", "Bearer " + apiToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Assuming retrieveSongIds is a method that extracts song IDs from the JSON response
            List<Long> songIds = retrieveSongIds(artistResponse);

            if (songIds.isEmpty()) {
                hasMorePages = false;
            } else {
                allSongIds.addAll(songIds);
                currentPage++;
            }
        }

        return allSongIds;
    }


    public String getLyrics(String url) {
        try {
            // Fetch the HTML content of the page
            String html = new RestTemplate().getForObject(url, String.class);

            // Parse the HTML using Jsoup
            assert html != null;
            Document document = Jsoup.parse(html);

            // Select the element that contains the lyrics
            // The actual selector might vary depending on the HTML structure of the page


            Element lyricsElement = document.selectFirst("#lyrics-root > div.Lyrics-sc-1bcc94c6-1");

            if (lyricsElement != null) {

                // Remove all <a> and <span> tags from the lyrics, but save their content
                lyricsElement.select("a, span, i").forEach(Node::unwrap);

                // Extract the text content of the lyrics element
                String lyricsText = lyricsElement.html();

                // Replace <br> tags with newline characters
                lyricsText = lyricsText.replaceAll("<br>", "\n");

                // Remove everything inside square brackets (e.g., [Chorus], [Verse])
                lyricsText = lyricsText.replaceAll("\\[.*?]", "");

                // Return the lyrics with newline characters added at the end of each line
                return lyricsText;
            } else {
                throw new RuntimeException("Lyrics element not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve lyrics", e);
        }
    }


    private Long retrieveArtistId(String artistResponse) {
        try {
            JsonNode root = objectMapper.readTree(artistResponse);
            JsonNode hits = root.path("response").path("hits");
            if (hits.isArray() && !hits.isEmpty()) {
                JsonNode primaryArtist = hits.get(0).path("result").path("primary_artist");
                return primaryArtist.path("id").asLong();
            } else {
                throw new IllegalArgumentException("No hits found in the response");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse artist ID from response", e);
        }
    }

    private List<Long> retrieveSongIds(String artistResponse) {
        List<Long> ids = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(artistResponse);
            JsonNode songs = root.path("response").path("songs");

            if (songs.isArray() && !songs.isEmpty()) {
                for (JsonNode song : songs) {
                    // Add the song ID
                    ids.add(song.path("id").asLong());

                    // Check if there are children and add their IDs
                    JsonNode children = song.path("children");
                    if (children.isArray() && !children.isEmpty()) {
                        for (JsonNode child : children) {
                            ids.add(child.path("id").asLong());
                        }
                    }
                }
            } else {
                return ids;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse song IDs from response", e);
        }

        return ids;
    }
}
