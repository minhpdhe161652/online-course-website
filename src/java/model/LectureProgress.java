package model;

public class LectureProgress {
    private int progressID;
    private int userID;
    private int lectureID;
    private int progress;

    public LectureProgress() {
    }

    public LectureProgress(int progressID, int userID, int lectureID, int progress) {
        this.progressID = progressID;
        this.userID = userID;
        this.lectureID = lectureID;
        this.progress = progress;
    }

    public int getProgressID() {
        return progressID;
    }

    public void setProgressID(int progressID) {
        this.progressID = progressID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getLectureID() {
        return lectureID;
    }

    public void setLectureID(int lectureID) {
        this.lectureID = lectureID;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
    
}
