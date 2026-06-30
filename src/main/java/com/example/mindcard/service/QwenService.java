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
public class QwenService {

    @Value("${qwen.api.key}")
    private String apiKey;
    private final GetDeck deck = new GetDeck();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Deck generateDeck(String userPrompt) throws Exception {
        String fullPrompt = Prompt.getFullPrompt(userPrompt);

        // Sử dụng định dạng API tương thích OpenAI của Qwen
        Map<String, Object> requestBodyMap = Map.of(
                "model", "qwen-plus",
                "messages", List.of(
                        Map.of("role", "system", "content", fullPrompt),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "response_format", Map.of("type", "json_object")
        );

        String requestBody = objectMapper.writeValueAsString(requestBodyMap);
        String urlStr = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

        String responseBody = makePostRequest(urlStr, requestBody);

        JsonNode root = objectMapper.readTree(responseBody);
        String jsonText = root.path("choices").get(0).path("message").path("content").asText().trim();

        return deck.getDeck(jsonText, objectMapper);
    }

    private String makePostRequest(String urlStr, String jsonBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlStr))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey) // Qwen dùng Bearer token
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Qwen API Call failed: Status " + response.statusCode() + ", Body: " + response.body());
        }
        return response.body();
    }
}