/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.Date;

/**
 *
 * @author FPTSHOP
 */
public class EnrolledCourseDTO {
    private int enrollmentId;
    private int courseID;
    private String courseName;
    private Date enrollmentDate;

    public EnrolledCourseDTO() {
    }

    public EnrolledCourseDTO(int enrollmentId, int courseID, String courseName, Date enrollmentDate) {
        this.enrollmentId = enrollmentId;
        this.courseID = courseID;
        this.courseName = courseName;
        this.enrollmentDate = enrollmentDate;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public int getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    
}
