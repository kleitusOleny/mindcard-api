package com.example.mindcard.dto;

public class StudySessionRequest {
    private String deckId;
    private int accuracy;
    private int xp;
    private int timeMin;

    public StudySessionRequest() {}

    public StudySessionRequest(String deckId, int accuracy, int xp, int timeMin) {
        this.deckId = deckId;
        this.accuracy = accuracy;
        this.xp = xp;
        this.timeMin = timeMin;
    }

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getTimeMin() {
        return timeMin;
    }

    public void setTimeMin(int timeMin) {
        this.timeMin = timeMin;
    }
}
