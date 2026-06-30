package com.example.mindcard.dto;

import java.util.List;

public class GeneratedDeckDto {
    private String deckName;
    private String category;
    private List<GeneratedCardDto> cards;

    public GeneratedDeckDto() {}

    public GeneratedDeckDto(String deckName, String category, List<GeneratedCardDto> cards) {
        this.deckName = deckName;
        this.category = category;
        this.cards = cards;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<GeneratedCardDto> getCards() {
        return cards;
    }

    public void setCards(List<GeneratedCardDto> cards) {
        this.cards = cards;
    }
}
