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

@WebServlet(name = "forgotPasswordServlet", value = "/forgot-password")
public class ForgotPasswordController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("./forgot-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String email = request.getParameter("email");

        // Kiểm tra email có trống hay không
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email cannot be empty.");
            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            return;
        }
        UserDAO userDAO = new UserDAO();
        try {
            User u = userDAO.getUserByEmail(email);
            if (u == null) {
                request.setAttribute("errorMessage", "Has not found the mail in the database.");
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            } else {
                userDAO.createResetOTP(email);
                request.setAttribute("email", email);
                request.setAttribute("successMessage", "A reset OTP has been sent to your mail. Please check then fill your reset OTP into the box.");
                request.getRequestDispatcher("/forgot-otp.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            Logger.getLogger(ForgotPasswordController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
