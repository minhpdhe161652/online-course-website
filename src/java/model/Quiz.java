package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private int quizID;
    private String title; 
    private String quizDescription;
    private String duration;
    private int timeLimit;
    private int passingScore;
    private int courseID;
    private Timestamp createdDate;
    private List<QuizQuestion> questions;

    public Quiz() {
        this.questions = new ArrayList<>();
        this.timeLimit = 30; 
        this.passingScore = 70; 
    }

    // Getters và Setters
    public int getQuizID() {
        return quizID;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuizDescription() {
        return quizDescription;
    }

    public void setQuizDescription(String quizDescription) {
        this.quizDescription = quizDescription;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizQuestion> questions) {
        this.questions = questions;
    }

    // Compatibility methods for old code
    public String getQuizName() {
        return this.title;
    }

    public void setQuizName(String quizName) {
        this.title = quizName;
    }

    public String getDescription() {
        return this.quizDescription;
    }

    public void setDescription(String description) {
        this.quizDescription = description;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
        this.duration = timeLimit + " minutes";
    }

    public int getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(int passingScore) {
        this.passingScore = passingScore;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public void addQuestion(QuizQuestion question) {
        if (this.questions == null) {
            this.questions = new ArrayList<>();
        }
        this.questions.add(question);
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "quizID=" + quizID +
                ", title='" + title + '\'' +
                ", quizDescription='" + quizDescription + '\'' +
                ", duration='" + duration + '\'' +
                ", timeLimit=" + timeLimit +
                ", passingScore=" + passingScore +
                ", courseID=" + courseID +
                ", createdDate=" + createdDate +
                ", questions=" + (questions != null ? questions.size() : 0) + " questions" +
                '}';
    }
}
