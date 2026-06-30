package com.example.mindcard.service;

import com.example.mindcard.dto.GeneratedCardDto;
import com.example.mindcard.dto.GeneratedDeckDto;
import com.example.mindcard.model.Card;
import com.example.mindcard.model.Deck;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Deck generateDeck(String userPrompt) throws Exception {
        String systemInstruction = "You are an expert language teacher. Create a deck of flashcards based on the user's request.\n" +
                "You MUST return a JSON object with the following schema:\n" +
                "{\n" +
                "  \"deckName\": \"Name of the deck\",\n" +
                "  \"category\": \"Math, Science, Languages, History, etc.\",\n" +
                "  \"cards\": [\n" +
                "    {\n" +
                "      \"englishWord\": \"Word/Phrase to learn\",\n" +
                "      \"pronunciation\": \"Phonetic pronunciation e.g. /\\u02ccser.\\u0259n\\u02c8d\\u026ap.\\u0259.ti/\",\n" +
                "      \"pos\": \"Noun, Verb, or Adj\",\n" +
                "      \"definition\": \"Clear concise translation/definition\",\n" +
                "      \"exampleSentence\": \"An illustrative example sentence using the word\",\n" +
                "      \"synonyms\": \"comma separated synonyms if any\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n" +
                "Create exactly 5 to 10 high-quality cards.";

        String fullPrompt = systemInstruction + "\n\nUser request: " + userPrompt;

        // Construct body
        Map<String, Object> requestBodyMap = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", fullPrompt))
                )),
                "generationConfig", Map.of("responseMimeType", "application/json")
        );
        String requestBody = objectMapper.writeValueAsString(requestBodyMap);

        // Try gemini-2.5-flash, and fallback to local demo data if it fails
        String urlStr = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;
        String responseBody;
        try {
            responseBody = makePostRequest(urlStr, requestBody);
        } catch (Exception e) {
            System.err.println("Gemini 2.5 Flash failed, generating local fallback demo deck: " + e.getMessage());
            return createFallbackDeck(userPrompt);
        }

        // Parse root response
        JsonNode root = objectMapper.readTree(responseBody);
        String jsonText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText().trim();

        // Parse inner JSON
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        GeneratedDeckDto dto = objectMapper.readValue(jsonText, GeneratedDeckDto.class);

        // Map to Entities
        Deck deck = new Deck();
        deck.setId(UUID.randomUUID().toString());
        deck.setName(dto.getDeckName() != null ? dto.getDeckName() : "AI Custom Set");
        deck.setCategory(dto.getCategory() != null ? dto.getCategory() : "AI Generated");
        deck.setMasteredPercentage(10);

        List<Card> cards = new ArrayList<>();
        if (dto.getCards() != null) {
            for (GeneratedCardDto cDto : dto.getCards()) {
                Card card = new Card(
                        UUID.randomUUID().toString(),
                        cDto.getEnglishWord(),
                        cDto.getPronunciation(),
                        cDto.getPos(),
                        cDto.getDefinition(),
                        cDto.getExampleSentence(),
                        cDto.getSynonyms()
                );
                card.setDeck(deck);
                cards.add(card);
            }
        }
        deck.setCards(cards);
        return deck;
    }

    private Deck createFallbackDeck(String userPrompt) {
        Deck deck = new Deck();
        deck.setId(UUID.randomUUID().toString());
        String deckName = userPrompt.toLowerCase().contains("food") ? "Food Vocabulary"
                        : userPrompt.toLowerCase().contains("travel") ? "Travel Phrases"
                        : "AI Custom Set";
        deck.setName(deckName);
        deck.setCategory("AI Generated (Demo)");
        deck.setMasteredPercentage(10);

        List<Card> cards = new ArrayList<>();
        Card card1 = new Card(
                UUID.randomUUID().toString(),
                "Greeting",
                "/ˈɡriːtɪŋ/",
                "Noun",
                "A polite word or sign of welcome.",
                "She raised her hand in greeting.",
                "Welcome, Salutation"
        );
        card1.setDeck(deck);
        cards.add(card1);

        Card card2 = new Card(
                UUID.randomUUID().toString(),
                "Gratitude",
                "/ˈɡrætɪtjuːd/",
                "Noun",
                "The quality of being thankful.",
                "She expressed her gratitude to the team.",
                "Thankfulness, Appreciation"
        );
        card2.setDeck(deck);
        cards.add(card2);

        deck.setCards(cards);
        return deck;
    }

    private String makePostRequest(String urlStr, String jsonBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlStr))
                .header("Content-Type", "application/json")
                .timeout(java.time.Duration.ofSeconds(60))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("API Call failed: Status " + response.statusCode() + ", Body: " + response.body());
        }
        return response.body();
    }
}
