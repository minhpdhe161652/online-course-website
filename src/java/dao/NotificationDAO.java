package dao;

import utils.DBContext;
import model.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    private final DBContext dbContext = new DBContext();
    
    public boolean addNotification(Notification notification) {
        String sql = "INSERT INTO Notifications (UserID, Message, Type, Target, TargetID, TimeStamp, IsRead) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, notification.getUserId());
            ps.setString(2, notification.getMessage());
            ps.setString(3, notification.getType());
            ps.setString(4, notification.getTarget());
            ps.setInt(5, notification.getTargetId());
            ps.setTimestamp(6, notification.getTimeStamp());
            ps.setBoolean(7, notification.isIsRead());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
            System.err.println("Error adding notification: " + notification.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Notification> getNotificationsByUser(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM Notifications WHERE UserID = ? ORDER BY TimeStamp DESC";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Notification notification = new Notification();
                notification.setNotificationId(rs.getInt("NotificationID"));
                notification.setUserId(rs.getInt("UserID"));
                notification.setMessage(rs.getString("Message"));
                notification.setType(rs.getString("Type"));
                notification.setTarget(rs.getString("Target"));
                notification.setTargetId(rs.getInt("TargetID"));
                notification.setTimeStamp(rs.getTimestamp("TimeStamp"));
                notification.setIsRead(rs.getBoolean("IsRead"));
                notifications.add(notification);
            }
            
        } catch (Exception e) {
            System.err.println("Error getting notifications for user: " + userId);
            e.printStackTrace();
        }
        return notifications;
    }
    
    public boolean markAsRead(int notificationId) {
        String sql = "UPDATE Notifications SET IsRead = 1 WHERE NotificationID = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, notificationId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
            System.err.println("Error marking notification as read: " + notificationId);
            e.printStackTrace();
            return false;
        }
    }
}
