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

@WebServlet(name = "forgotOTPServlet", value = "/forgot-otp")
public class ForgotOTPController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("./forgot-otp.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String email = request.getParameter("email");
        String resetOTP = request.getParameter("resetOTP");
        // Kiểm tra email có trống hay không
        if (email == null || email.trim().isEmpty() || resetOTP == null || resetOTP.trim().isEmpty()) {
            request.setAttribute("errorMessage", "All of these values cannot be empty.");
            request.setAttribute("email", email);
            request.setAttribute("resetOTP", resetOTP);
            request.getRequestDispatcher("/forgot-otp.jsp").forward(request, response);
            return;
        } else {
            UserDAO userDAO = new UserDAO();
            try {
                User u = userDAO.getUserByEmailAndResetOTP(email, resetOTP);
                if (u == null) {
                    request.setAttribute("errorMessage", "Email or Reset OTP may be wrong.");
                    request.setAttribute("email", email);
                    request.setAttribute("resetOTP", resetOTP);
                    request.getRequestDispatcher("/forgot-otp.jsp").forward(request, response);
                } else {
                    request.setAttribute("email", email);
                    request.setAttribute("resetOTP", resetOTP);
                    request.setAttribute("successMessage", "Xác thực tài khoản thành công!");
                    request.getRequestDispatcher("/create-new-password.jsp").forward(request, response);
                }
            } catch (Exception ex) {
                Logger.getLogger(ForgotOTPController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
