package model;

public class CourseLevels {
    private int levelID;
    private String levelName;
    private String levelDescription;

    public CourseLevels() {
    }

    public CourseLevels(int levelID, String levelName, String levelDescription) {
        this.levelID = levelID;
        this.levelName = levelName;
        this.levelDescription = levelDescription;
    }

    public int getLevelID() {
        return levelID;
    }

    public void setLevelID(int levelID) {
        this.levelID = levelID;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelDescription() {
        return levelDescription;
    }

    public void setLevelDescription(String levelDescription) {
        this.levelDescription = levelDescription;
    }
    
}
