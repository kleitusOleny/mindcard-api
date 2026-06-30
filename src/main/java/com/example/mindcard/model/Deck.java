package com.example.mindcard.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "decks")
public class Deck {
    @Id
    private String id;
    
    private String userId; // scoped by user ID
    private String name;
    private String category;
    private String coverUrl;
    private int masteredPercentage;

    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Card> cards = new ArrayList<>();

    public Deck() {}

    public Deck(String id, String userId, String name, String category, String coverUrl, int masteredPercentage) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.coverUrl = coverUrl;
        this.masteredPercentage = masteredPercentage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getMasteredPercentage() {
        return masteredPercentage;
    }

    public void setMasteredPercentage(int masteredPercentage) {
        this.masteredPercentage = masteredPercentage;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
        if (cards != null) {
            for (Card card : cards) {
                card.setDeck(this);
            }
        }
    }

    public void addCard(Card card) {
        this.cards.add(card);
        card.setDeck(this);
    }

    public void removeCard(Card card) {
        this.cards.remove(card);
        card.setDeck(null);
    }
}
