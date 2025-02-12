/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dao.UserDAO;
import model.User;
import java.io.IOException;
import java.time.Instant;
import java.sql.Timestamp;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet handling learner registration process.
 * Validates user input, creates new learner accounts, and manages registration flow.
 */

@WebServlet(name = "registerLearnerServlet", value = "/register")
public class RegisterLearner extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RegisterLearner.class.getName());
    private static final int LEARNER_ROLE_ID = 1;
    private static final int MAX_FIELD_LENGTH = 255;
    private final UserDAO usersDAO;

    public RegisterLearner() {
        this.usersDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get input from registration form
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // Validate input
            String validationError = validateInput(firstName, lastName, email, username, password);
            if (validationError != null) {
                request.setAttribute("error", validationError);
                request.getRequestDispatcher("/register/learner/registerLearner.jsp").forward(request, response);
                return;
            }

            // Check for existing username/email
            if (usersDAO.isUsernameTaken(username) || usersDAO.isEmailTaken(email)) {
                request.setAttribute("error", "Username or email already exists.");
                request.getRequestDispatcher("/register/learner/registerLearner.jsp").forward(request, response);
                return;
            }

            // Create Users object for Learner
            User learner = new User();
            learner.setFirstName(firstName.trim());
            learner.setLastName(lastName.trim());
            learner.setEmail(email.trim());
            learner.setUsername(username.trim());
            learner.setPassword(password);
            learner.setRoleID(LEARNER_ROLE_ID);
            learner.setRegistrationDate(Timestamp.from(Instant.now()).toString());
            learner.setIsActive(true);

            // Save Learner to database
            boolean isRegistered = usersDAO.registerLearner(learner);

            if (!isRegistered) {
                LOGGER.log(Level.INFO, "New learner registered successfully: {0}", username);
                // Set success message and redirect to login page
                request.getSession().setAttribute("message", "Registration successful! Please login with your credentials.");
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                LOGGER.log(Level.WARNING, "Registration failed for username: {0}", username);
                request.setAttribute("error", "Registration failed. Please try again.");
                request.getRequestDispatcher("/register/learner/registerLearner.jsp").forward(request, response);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during registration process", e);
            request.setAttribute("error", "An unexpected error occurred. Please try again later.");
            request.getRequestDispatcher("/register/learner/registerLearner.jsp").forward(request, response);
        }
    }

    private String validateInput(String firstName, String lastName, String email, String username, String password) {
        // Check for null or empty fields
        if (firstName == null || lastName == null || email == null || username == null || password == null ||
            firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            return "All fields are required.";
        }

        // Check field lengths
        if (firstName.length() > MAX_FIELD_LENGTH || lastName.length() > MAX_FIELD_LENGTH ||
            email.length() > MAX_FIELD_LENGTH || username.length() > MAX_FIELD_LENGTH) {
            return "Input fields exceed maximum length.";
        }

        // Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "Invalid email format.";
        }

        // Validate password strength
        if (password.length() < 8 || !password.matches(".*[A-Z].*") || 
            !password.matches(".*[a-z].*") || !password.matches(".*\\d.*")) {
            return "Password must be at least 8 characters long and contain uppercase, lowercase, and numbers.";
        }

        return null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register/learner/registerLearner.jsp").forward(request, response);
    }
}
