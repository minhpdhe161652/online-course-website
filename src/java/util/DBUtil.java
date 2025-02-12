package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static final String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=OnlineLearningDB;encrypt=true;trustServerCertificate=true;";
    private static final String JDBC_USERNAME = "sa";
    private static final String JDBC_PASSWORD = "123456";

    static {
        try {
            // Đăng ký driver JDBC
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Không tìm thấy JDBC Driver!");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}