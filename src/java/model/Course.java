package model;

public class Course {
    private int courseID;
    private String courseName;
    private String description;
    private String levelName;
    private double price;
    private String imageURL;
    private String instructorName;
    private String language;
    private String createdDate;
    private int instructorId;

    public Course() {
    }

    public Course(int courseID, String courseName, String description, String levelName, double price, String imageURL, String instructorName, String language, String createdDate, int instructorId) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.description = description;
        this.levelName = levelName;
        this.price = price;
        this.imageURL = imageURL;
        this.instructorName = instructorName;
        this.language = language;
        this.createdDate = createdDate;
        this.instructorId = instructorId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }
}