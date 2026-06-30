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

    private Boolean isFavorite = false;
    private Double easeFactor = 2.5;

    @Column(name = "review_interval")
    private Double interval = 0.0;

    private Integer repetitions = 0;
    private Long nextReview = System.currentTimeMillis();
    private Long lastReview = 0L;
    private String reviewState = "New";

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
        this.isFavorite = false;
        this.easeFactor = 2.5;
        this.interval = 0.0;
        this.repetitions = 0;
        this.nextReview = System.currentTimeMillis();
        this.lastReview = 0L;
        this.reviewState = "New";
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

    public boolean isFavorite() {
        return isFavorite != null ? isFavorite : false;
    }

    public void setFavorite(Boolean favorite) {
        this.isFavorite = favorite;
    }

    public double getEaseFactor() {
        return easeFactor != null ? easeFactor : 2.5;
    }

    public void setEaseFactor(Double easeFactor) {
        this.easeFactor = easeFactor;
    }

    public double getInterval() {
        return interval != null ? interval : 0.0;
    }

    public void setInterval(Double interval) {
        this.interval = interval;
    }

    public int getRepetitions() {
        return repetitions != null ? repetitions : 0;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public long getNextReview() {
        return nextReview != null ? nextReview : System.currentTimeMillis();
    }

    public void setNextReview(Long nextReview) {
        this.nextReview = nextReview;
    }

    public long getLastReview() {
        return lastReview != null ? lastReview : 0L;
    }

    public void setLastReview(Long lastReview) {
        this.lastReview = lastReview;
    }

    public String getReviewState() {
        return reviewState != null ? reviewState : "New";
    }

    public void setReviewState(String reviewState) {
        this.reviewState = reviewState;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }
}
