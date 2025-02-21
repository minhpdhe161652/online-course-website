/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Administrator
 */
public class CourseFeedback {
    private int FeedbackID;
    private int CourseID;
    private int StudentID;
    private String FeedbackText;
    private int Rating;
    private String FeedBackDate;

    public CourseFeedback() {
    }

    public CourseFeedback(int FeedbackID, int CourseID, int StudentID, String FeedbackText, int Rating, String FeedBackDate) {
        this.FeedbackID = FeedbackID;
        this.CourseID = CourseID;
        this.StudentID = StudentID;
        this.FeedbackText = FeedbackText;
        this.Rating = Rating;
        this.FeedBackDate = FeedBackDate;
    }

    public int getFeedbackID() {
        return FeedbackID;
    }

    public void setFeedbackID(int FeedbackID) {
        this.FeedbackID = FeedbackID;
    }

    public int getCourseID() {
        return CourseID;
    }

    public void setCourseID(int CourseID) {
        this.CourseID = CourseID;
    }

    public int getStudentID() {
        return StudentID;
    }

    public void setStudentID(int StudentID) {
        this.StudentID = StudentID;
    }

    public String getFeedbackText() {
        return FeedbackText;
    }

    public void setFeedbackText(String FeedbackText) {
        this.FeedbackText = FeedbackText;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int Rating) {
        this.Rating = Rating;
    }

    public String getFeedBackDate() {
        return FeedBackDate;
    }

    public void setFeedBackDate(String FeedBackDate) {
        this.FeedBackDate = FeedBackDate;
    }
    
    
}
