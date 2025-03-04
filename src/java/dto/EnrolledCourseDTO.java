/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.sql.Timestamp;

public class EnrolledCourseDTO {
    private int enrollmentID;
    private int courseID;
    private String courseName;
    private Timestamp enrollmentDate;
    private boolean isCompleted;

    // Constructor
    public EnrolledCourseDTO(int enrollmentID, int courseID, String courseName, Timestamp enrollmentDate, boolean isCompleted) {
        this.enrollmentID = enrollmentID;
        this.courseID = courseID;
        this.courseName = courseName;
        this.enrollmentDate = enrollmentDate;
        this.isCompleted = isCompleted;
    }

    // Getter và Setter
    public int getEnrollmentID() {
        return enrollmentID;
    }

    public void setEnrollmentID(int enrollmentID) {
        this.enrollmentID = enrollmentID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Timestamp getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Timestamp enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public boolean isCompleted() { 
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}