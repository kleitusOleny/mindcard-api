package com.example.mindcard.utils;

import com.example.mindcard.dto.GeneratedCardDto;
import com.example.mindcard.dto.GeneratedDeckDto;
import com.example.mindcard.model.Card;
import com.example.mindcard.model.Deck;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GetDeck {
    @NonNull
    public Deck getDeck(String jsonText, ObjectMapper objectMapper) throws com.fasterxml.jackson.core.JsonProcessingException {
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        GeneratedDeckDto dto = objectMapper.readValue(jsonText, GeneratedDeckDto.class);

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
}
