package model;

import java.sql.Timestamp;

public class Quizzes {
    private int quizID;
    private String quizDescription;
    private int sectionID;
    private String quizName;
    private Timestamp createdDate;
    private String duration;

    public Quizzes() {
    }

    public Quizzes(int quizID, String quizDescription, int sectionID, String quizName, Timestamp createdDate, String duration) {
        this.quizID = quizID;
        this.quizDescription = quizDescription;
        this.sectionID = sectionID;
        this.quizName = quizName;
        this.createdDate = createdDate;
        this.duration = duration;
    }

    public int getQuizID() {
        return quizID;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }

    public String getQuizDescription() {
        return quizDescription;
    }

    public void setQuizDescription(String quizDescription) {
        this.quizDescription = quizDescription;
    }

    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
