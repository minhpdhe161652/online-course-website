package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.LectureProgress;
import util.DBUtil;

public class LectureProgressDAO {

    public List<LectureProgress> getLectureProgressByUserIdAndLectureId(int userId, int lectureId) {
        List<LectureProgress> lectureProgressList = new ArrayList<>();
        String sql = "SELECT * FROM LectureProgress WHERE userID = ? AND lectureID = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, lectureId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                LectureProgress lectureProgress = new LectureProgress();
                lectureProgress.setProgressID(rs.getInt("progressID"));
                lectureProgress.setUserID(rs.getInt("userID"));
                lectureProgress.setLectureID(rs.getInt("lectureID"));
                lectureProgress.setProgress(rs.getInt("progress"));
                lectureProgressList.add(lectureProgress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return lectureProgressList;
    }
}
