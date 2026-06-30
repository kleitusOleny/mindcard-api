package com.example.mindcard.service;

import com.example.mindcard.model.Deck;
import com.example.mindcard.utils.GetDeck;
import com.example.mindcard.utils.Prompt;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;
    private final GetDeck deck = new GetDeck();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Deck generateDeck(String userPrompt) throws Exception {
        String fullPrompt = Prompt.getFullPrompt(userPrompt);

        // Construct body
        Map<String, Object> requestBodyMap = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", fullPrompt))
                )),
                "generationConfig", Map.of("responseMimeType", "application/json")
        );
        String requestBody = objectMapper.writeValueAsString(requestBodyMap);

        // Try gemini-2.5-flash, fallback to gemini-2.0-flash
        String urlStr = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;
        String responseBody;
        try {
            responseBody = makePostRequest(urlStr, requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback
            urlStr = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;
            responseBody = makePostRequest(urlStr, requestBody);
        }

        // Parse root response
        JsonNode root = objectMapper.readTree(responseBody);
        String jsonText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText().trim();

        // Parse inner JSON
        return deck.getDeck(jsonText, objectMapper);
    }

    private String makePostRequest(String urlStr, String jsonBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlStr))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("API Call failed: Status " + response.statusCode() + ", Body: " + response.body());
        }
        return response.body();
    }
}
