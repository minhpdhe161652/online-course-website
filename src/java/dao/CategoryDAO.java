/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Category;
import util.DBUtil;

/**
 *
 * @author toila
 */
public class CategoryDAO {

    private final DBUtil dbContext = new DBUtil();

    public List<Category> getCategories() throws Exception {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT CategoryID, CategoryName FROM Categories";

        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryID(rs.getInt("CategoryID"));
                category.setCategoryName(rs.getString("CategoryName"));
                categories.add(category);
            }
        }
        return categories;
    }
}
