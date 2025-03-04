package model;

public class CourseSection {
    private int sectionID;
    private int courseID;
    private String sectionName;
    private String sectionDescription;
    private int sectionOrder;

    public CourseSection() {
    }

    public CourseSection(int sectionID, int courseID, String sectionName, String sectionDescription, String sectionContent, int sectionOrder) {
        this.sectionID = sectionID;
        this.courseID = courseID;
        this.sectionName = sectionName;
        this.sectionDescription = sectionDescription;
        this.sectionOrder = sectionOrder;
    }

    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionDescription() {
        return sectionDescription;
    }

    public void setSectionDescription(String sectionDescription) {
        this.sectionDescription = sectionDescription;
    }

    public int getSectionOrder() {
        return sectionOrder;
    }

    public void setSectionOrder(int sectionOrder) {
        this.sectionOrder = sectionOrder;
    }
    
}
