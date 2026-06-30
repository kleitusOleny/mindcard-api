package com.example.mindcard.dto;

public class GeneratedCardDto {
    private String englishWord;
    private String pronunciation;
    private String pos;
    private String definition;
    private String exampleSentence;
    private String synonyms;

    public GeneratedCardDto() {}

    public GeneratedCardDto(String englishWord, String pronunciation, String pos, String definition, String exampleSentence, String synonyms) {
        this.englishWord = englishWord;
        this.pronunciation = pronunciation;
        this.pos = pos;
        this.definition = definition;
        this.exampleSentence = exampleSentence;
        this.synonyms = synonyms;
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
}
