package com.example.mindcard.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "daily_study_records")
@IdClass(DailyStudyRecord.DailyStudyRecordId.class)
public class DailyStudyRecord {

    @Id
    private String userId;

    @Id
    private String date; // Định dạng "YYYY-MM-DD"

    private int dueCards;
    private int wordsLearned;
    private int xpEarned;
    private int timeSpentMin;
    private int mastered;

    public DailyStudyRecord() {}

    public DailyStudyRecord(String userId, String date, int dueCards, int wordsLearned, int xpEarned, int timeSpentMin, int mastered) {
        this.userId = userId;
        this.date = date;
        this.dueCards = dueCards;
        this.wordsLearned = wordsLearned;
        this.xpEarned = xpEarned;
        this.timeSpentMin = timeSpentMin;
        this.mastered = mastered;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDueCards() {
        return dueCards;
    }

    public void setDueCards(int dueCards) {
        this.dueCards = dueCards;
    }

    public int getWordsLearned() {
        return wordsLearned;
    }

    public void setWordsLearned(int wordsLearned) {
        this.wordsLearned = wordsLearned;
    }

    public int getXpEarned() {
        return xpEarned;
    }

    public void setXpEarned(int xpEarned) {
        this.xpEarned = xpEarned;
    }

    public int getTimeSpentMin() {
        return timeSpentMin;
    }

    public void setTimeSpentMin(int timeSpentMin) {
        this.timeSpentMin = timeSpentMin;
    }

    public int getMastered() {
        return mastered;
    }

    public void setMastered(int mastered) {
        this.mastered = mastered;
    }

    // Class đại diện cho khóa chính hỗn hợp (Composite Primary Key)
    public static class DailyStudyRecordId implements Serializable {
        private String userId;
        private String date;

        public DailyStudyRecordId() {}

        public DailyStudyRecordId(String userId, String date) {
            this.userId = userId;
            this.date = date;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DailyStudyRecordId that = (DailyStudyRecordId) o;
            return userId.equals(that.userId) && date.equals(that.date);
        }

        @Override
        public int hashCode() {
            int result = userId.hashCode();
            result = 31 * result + date.hashCode();
            return result;
        }
    }
}
