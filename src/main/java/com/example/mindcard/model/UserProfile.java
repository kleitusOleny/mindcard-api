package com.example.mindcard.model;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    private String userId; // Firebase UID
    
    private String name;
    private String title;
    private int level;
    private int currentStreak;
    private int bestStreak;
    private int totalXp;
    private int totalWordsLearned;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_study_history", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "study_date")
    @Column(name = "completed")
    private Map<String, Boolean> studyHistory = new HashMap<>();

    public UserProfile() {}

    public UserProfile(String userId, String name, String title, int level, int currentStreak, int bestStreak, int totalXp, int totalWordsLearned) {
        this.userId = userId;
        this.name = name;
        this.title = title;
        this.level = level;
        this.currentStreak = currentStreak;
        this.bestStreak = bestStreak;
        this.totalXp = totalXp;
        this.totalWordsLearned = totalWordsLearned;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getBestStreak() {
        return bestStreak;
    }

    public void setBestStreak(int bestStreak) {
        this.bestStreak = bestStreak;
    }

    public int getTotalXp() {
        return totalXp;
    }

    public void setTotalXp(int totalXp) {
        this.totalXp = totalXp;
    }

    public int getTotalWordsLearned() {
        return totalWordsLearned;
    }

    public void setTotalWordsLearned(int totalWordsLearned) {
        this.totalWordsLearned = totalWordsLearned;
    }

    public Map<String, Boolean> getStudyHistory() {
        return studyHistory;
    }

    public void setStudyHistory(Map<String, Boolean> studyHistory) {
        this.studyHistory = studyHistory;
    }
}
