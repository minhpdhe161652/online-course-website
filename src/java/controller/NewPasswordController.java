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

@WebServlet(name = "newPasswordServlet", value = "/new-password")
public class NewPasswordController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String resetOTP = request.getParameter("resetOTP");
        if (resetOTP == null || resetOTP.trim().isEmpty()) {
            request.getRequestDispatcher("./auth/login.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("./create-new-password.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String email = request.getParameter("email");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        String resetOTP = request.getParameter("resetOTP");
        if (resetOTP != null) {
            // Kiểm tra email có trống hay không
            if (email == null || email.trim().isEmpty()
                    || newPassword == null || newPassword.trim().isEmpty()
                    || confirmPassword == null || confirmPassword.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Tất cả các trường không được để trống.");
                request.setAttribute("email", email);
                request.setAttribute("newPassword", newPassword);
                request.getRequestDispatcher("/create-new-password.jsp").forward(request, response);
            } else {
                UserDAO userDAO = new UserDAO();
                try {
                    User u = userDAO.getUserByEmail(email);
                    if (u == null) {
                        request.setAttribute("errorMessage", "Email không tồn tại trên hệ thống.");
                        request.setAttribute("email", email);
                        request.setAttribute("newPassword", newPassword);
                        request.getRequestDispatcher("/create-new-password.jsp").forward(request, response);
                    } else {
                        if (u.getResetOTP() != null && u.getResetOTP().equals(resetOTP.trim())) {
                            userDAO.changePassword(newPassword, u.getUserID());
                            request.setAttribute("email", email);
                            request.setAttribute("successMessage", "Tạo mật khẩu mới thành công.");
                            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
                        } else {
                            request.setAttribute("errorMessage", "Có vẻ như bạn đang thay đổi mật khẩu trái phép cho một Email khác. Vui lòng nhập Email đúng với OTP khôi phục đã gửi.");
                            request.setAttribute("email", email);
                            request.setAttribute("resetOTP", resetOTP);
                            request.getRequestDispatcher("/create-new-password.jsp").forward(request, response);
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(NewPasswordController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
        }
    }
}
