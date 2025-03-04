package model;

public class LectureExtraMaterials {
    private int materialID;
    private int lectureID;
    private String materialTitle;
    private String materialContent;
    private String materialType;

    public LectureExtraMaterials() {
    }

    public LectureExtraMaterials(int materialID, int lectureID, String materialTitle, String materialContent, String materialType) {
        this.materialID = materialID;
        this.lectureID = lectureID;
        this.materialTitle = materialTitle;
        this.materialContent = materialContent;
        this.materialType = materialType;
    }

    public int getMaterialID() {
        return materialID;
    }

    public void setMaterialID(int materialID) {
        this.materialID = materialID;
    }

    public int getLectureID() {
        return lectureID;
    }

    public void setLectureID(int lectureID) {
        this.lectureID = lectureID;
    }

    public String getMaterialTitle() {
        return materialTitle;
    }

    public void setMaterialTitle(String materialTitle) {
        this.materialTitle = materialTitle;
    }

    public String getMaterialContent() {
        return materialContent;
    }

    public void setMaterialContent(String materialContent) {
        this.materialContent = materialContent;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }
}
