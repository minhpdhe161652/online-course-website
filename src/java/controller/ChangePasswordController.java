package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;


@WebServlet(name = "changePasswordServlet", value = "/change-password")
public class ChangePasswordController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Hiển thị trang thay đổi mật khẩu
        request.getRequestDispatcher("/change-password.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Lấy các tham số từ form
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            int userID = 1;
            
            // Kiểm tra điều kiện mật khẩu
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserByID(userID);
            
            // Kiểm tra mật khẩu cũ có đúng không
            if (user != null && user.getPassword().equals(oldPassword)) {
                if (newPassword.equals(confirmPassword)) {
                    // Cập nhật mật khẩu mới vào DB
                    user.setPassword(newPassword);
                    userDAO.updatePassword(user);
                    
                    // Chuyển hướng về trang thông báo thành công
                    request.setAttribute("message", "Password updated successfully!");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                } else {
                    // Nếu mật khẩu mới và xác nhận không giống nhau
                    request.setAttribute("error", "New passwords do not match!");
                    request.getRequestDispatcher("/change-password.jsp").forward(request, response);
                }
            } else {
                // Nếu mật khẩu cũ sai
                request.setAttribute("error", "Old password is incorrect!");
                request.getRequestDispatcher("/change-password.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            Logger.getLogger(ChangePasswordController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}