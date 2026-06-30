package com.example.mindcard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    private String id;
    
    private String englishWord;
    private String pronunciation;
    private String pos;
    private String definition;
    
    @Column(length = 1000)
    private String exampleSentence;
    
    private String synonyms;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id")
    @JsonIgnore
    private Deck deck;

    public Card() {}

    public Card(String id, String englishWord, String pronunciation, String pos, String definition, String exampleSentence, String synonyms) {
        this.id = id;
        this.englishWord = englishWord;
        this.pronunciation = pronunciation;
        this.pos = pos;
        this.definition = definition;
        this.exampleSentence = exampleSentence;
        this.synonyms = synonyms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExampleSentence() {
        return exampleSentence;
    }

    public void setExampleSentence(String exampleSentence) {
        this.exampleSentence = exampleSentence;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }
}
