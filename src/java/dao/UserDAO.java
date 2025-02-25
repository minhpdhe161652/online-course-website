package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.User;
import util.DBUtil;

public class UserDAO {

    public static User getProfile(String username) {
        String query = "SELECT UserID, Username, Password, FirstName, LastName, Email, RoleID, RegistrationDate, IsActive, Avatar, Bio, StoredSalt, ProviderID, WalletID, phoneNumber, dob "
                + "FROM Users WHERE Username = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("Username"));
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setEmail(rs.getString("Email"));
                user.setRoleID(rs.getInt("RoleID"));
                user.setRegistrationDate(rs.getString("RegistrationDate"));
                user.setIsActive(rs.getBoolean("IsActive"));
                user.setAvatar(rs.getString("Avatar"));
                user.setBio(rs.getString("Bio"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setDob(rs.getDate("dob"));

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setEmail(rs.getString("Email"));
                user.setRoleID(rs.getInt("RoleID"));
                user.setRegistrationDate(rs.getString("RegistrationDate"));
                user.setIsActive(rs.getBoolean("IsActive"));
                user.setAvatar(rs.getString("Avatar"));
                user.setBio(rs.getString("Bio"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getUserById(int userId) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserID(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setFirstName(rs.getString("FirstName"));
                    user.setLastName(rs.getString("LastName"));
                    user.setEmail(rs.getString("Email"));
                    user.setRoleID(rs.getInt("RoleID"));
                    user.setRegistrationDate(rs.getString("RegistrationDate"));
                    user.setIsActive(rs.getBoolean("IsActive"));
                    user.setAvatar(rs.getString("Avatar"));
                    user.setBio(rs.getString("Bio"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean deleteUser(int userId) {
        String sql = "UPDATE Users SET IsActive = 0 WHERE UserID = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi vô hiệu hóa user: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        }
    }

    public boolean reactivateUser(int userId) {
        String sql = "UPDATE Users SET IsActive = 1 WHERE UserID = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi kích hoạt lại user: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        }
    }

    private String getLastNamePart(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }
        String[] parts = fullName.trim().split("\\s+");
        return parts.length > 0 ? parts[parts.length - 1] : "";
    }

    public List<User> getUsersByRole(int roleId, String sortBy, String sortOrder) {
        List<User> users = new ArrayList<>();
        String orderByClause = "";

        // Đảm bảo sortOrder chỉ là "ASC" hoặc "DESC"
        sortOrder = (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) ? "DESC" : "ASC";

        // Chỉ sắp xếp theo ID trong SQL, phần sắp xếp khác sẽ xử lý bằng Java
        String sql = "SELECT * FROM Users WHERE RoleID = ? ORDER BY UserID";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setFirstName(rs.getString("FirstName"));
                    user.setLastName(rs.getString("LastName"));
                    user.setEmail(rs.getString("Email"));
                    user.setRoleID(rs.getInt("RoleID"));
                    user.setRegistrationDate(rs.getString("RegistrationDate"));
                    user.setIsActive(rs.getBoolean("IsActive"));
                    user.setAvatar(rs.getString("Avatar"));
                    user.setBio(rs.getString("Bio"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Sắp xếp danh sách bằng Java
        Comparator<User> comparator = null;
        switch (sortBy) {
            case "name":
                comparator = (u1, u2) -> {
                    String name1 = getLastNamePart(u1.getFirstName() + " " + u1.getLastName());
                    String name2 = getLastNamePart(u2.getFirstName() + " " + u2.getLastName());
                    int result = name1.compareToIgnoreCase(name2); // Không phân biệt hoa thường
                    if (result == 0) {
                        // Nếu tên giống nhau, so sánh theo họ và tên đệm
                        result = (u1.getFirstName() + " " + u1.getLastName())
                                .compareToIgnoreCase(u2.getFirstName() + " " + u2.getLastName());
                    }
                    return result;
                };
                break;
            case "email":
                comparator = Comparator.comparing(User::getEmail, String.CASE_INSENSITIVE_ORDER);
                break;
            case "username":
                comparator = Comparator.comparing(User::getUsername, String.CASE_INSENSITIVE_ORDER);
                break;
            default:
                comparator = Comparator.comparing(User::getUserID);
        }

        // Đảo ngược thứ tự nếu là DESC
        if (sortOrder.equals("DESC")) {
            comparator = comparator.reversed();
        }

        users.sort(comparator);
        return users;
    }

    public boolean isEmailExist(String email) throws Exception {
        String sql = "SELECT COUNT(*) FROM [Users] WHERE email = ?";
        try (
                Connection connection = DBUtil.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean checkAccountBlock(String email) throws Exception {
        boolean checked = false;
        String sql = "Select u.UserID FROM [Users] u where u.email = ?";
        try (
                //Mở kết nối
                Connection connection = DBUtil.getConnection(); //Đưa câu query sang sql
                 PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                checked = true;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return checked;
    }

    public User getUserByEmail(String email) throws Exception {
        String sql = "Select * from [users] where email = ?";
        try (Connection connection = DBUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setFirstName(rs.getString("Firstname"));
                user.setLastName(rs.getString("Lastname"));
                user.setEmail(rs.getString("Email"));
                user.setRoleID(rs.getInt("RoleID"));
                user.setRegistrationDate(rs.getString("RegistrationDate"));
                user.setIsActive(rs.getBoolean("IsActive"));
                user.setAvatar(rs.getString("Avatar"));
                user.setBio(rs.getString("Bio"));
                return user;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return null;
    }

    public void changePassword(String pass, int id) throws Exception {
        String sql = "update [Users] set Password=? where UserID = ?";
        try (
                Connection connection = DBUtil.getConnection(); PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, pass);
            pre.setInt(2, id);
            pre.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Hàm lấy người dùng theo userID
    public User getUserByID(int userID) throws Exception {
        String sql = "Select * from [users] where UserID = ?";
        try (Connection connection = DBUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setFirstName(rs.getString("Firstname"));
                user.setLastName(rs.getString("Lastname"));
                user.setEmail(rs.getString("Email"));
                user.setRoleID(rs.getInt("RoleID"));
                user.setRegistrationDate(rs.getString("RegistrationDate"));
                user.setIsActive(rs.getBoolean("IsActive"));
                user.setAvatar(rs.getString("Avatar"));
                user.setBio(rs.getString("Bio"));
                return user;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return null;
    }

    // Hàm cập nhật mật khẩu
    public void updatePassword(User user) {
        String sql = "update [Users] set Password=? where UserID = ?";
        try (
                Connection connection = DBUtil.getConnection(); PreparedStatement pre = connection.prepareStatement(sql)) {
            pre.setString(1, user.getPassword());
            pre.setInt(2, user.getUserID());
            pre.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public User authenticateUser(String username, String password) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ? AND IsActive = 1";
        try (Connection connection = DBUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            rs = statement.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(""); // Don't send password back
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setEmail(rs.getString("Email"));
                user.setRoleID(rs.getInt("RoleID"));
                String registrationDateStr = user.getRegistrationDate();  // Giả sử đây là String
                java.util.Date registrationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(registrationDateStr);  // Chuyển String thành Date
                statement.setTimestamp(7, new java.sql.Timestamp(registrationDate.getTime()));  // Chuyển Date thành Timestamp
                user.setIsActive(rs.getBoolean("IsActive"));
                user.setAvatar(rs.getString("Avatar"));
                user.setBio(rs.getString("Bio"));
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean isUsernameTaken(String username) {
        boolean isTaken = false;
        String sql = "SELECT COUNT(*) FROM Users WHERE Username = ?";
        try (Connection connection = DBUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                isTaken = rs.getInt(1) > 0;
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTaken;

    }

    public boolean isEmailTaken(String email) {
        boolean isTaken = false;
        String sql = "SELECT COUNT(*) FROM Users WHERE Email = ?";
        try (Connection connection = DBUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                isTaken = rs.getInt(1) > 0;
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTaken;

    }

    public boolean registerLearner(User learner) {
        boolean isTaken = false;
        String sql = "INSERT INTO Users (Username, Password, FirstName, LastName, Email, RoleID, RegistrationDate, IsActive) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, learner.getUsername());
            statement.setString(2, learner.getPassword());
            statement.setString(3, learner.getFirstName());
            statement.setString(4, learner.getLastName());
            statement.setString(5, learner.getEmail());
            statement.setInt(6, learner.getRoleID());
            String registrationDateStr = learner.getRegistrationDate();  // Giả sử đây là String
            java.util.Date registrationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(registrationDateStr);  // Chuyển String thành Date
            statement.setTimestamp(7, new java.sql.Timestamp(registrationDate.getTime()));  // Chuyển Date thành Timestamp
            statement.setBoolean(8, learner.isIsActive());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                isTaken = rs.getInt(1) > 0;
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTaken;
    }

    public static boolean updateProfile(User user) {
        String sql = "UPDATE Users SET FirstName = ?, LastName = ?, dob = ?, phoneNumber = ?, Bio = ? WHERE UserID = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setDate(3, user.getDob() != null ? new java.sql.Date(user.getDob().getTime()) : null);
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getBio());
            stmt.setInt(6, user.getUserID());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}