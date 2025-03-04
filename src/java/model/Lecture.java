package model;

import java.sql.Timestamp;

public class Lecture {
    private int lectureID;
    private int sectionID;
    private String lectureName;
    private String lectureURL;
    private Timestamp createdDate;
    private String lectureDescription;

    public Lecture() {
    }

    public Lecture(int lectureID, int sectionID, String lectureName, String lectureURL, Timestamp createdDate, String lectureDescription) {
        this.lectureID = lectureID;
        this.sectionID = sectionID;
        this.lectureName = lectureName;
        this.lectureURL = lectureURL;
        this.createdDate = createdDate;
        this.lectureDescription = lectureDescription;
    }

    public int getLectureID() {
        return lectureID;
    }

    public void setLectureID(int lectureID) {
        this.lectureID = lectureID;
    }

    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public String getLectureURL() {
        return lectureURL;
    }

    public void setLectureURL(String lectureURL) {
        this.lectureURL = lectureURL;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getLectureDescription() {
        return lectureDescription;
    }

    public void setLectureDescription(String lectureDescription) {
        this.lectureDescription = lectureDescription;
    }
}
