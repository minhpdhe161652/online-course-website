package controller;

import dao.UserDAO;
import model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet handling user login process.
 * Validates credentials and manages user sessions.
 */

@WebServlet(name = "loginServlet", value = "/login")
public class LoginController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());
    
    UserDAO usersDAO = new UserDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        doGet(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get and sanitize input
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // Validate input
            if (username == null || password == null
                    || username.isEmpty() || password.isEmpty()) {
                request.setAttribute("error", "Username and password are required.");
                request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
                return;
            }

            // Attempt to authenticate user
            User user = usersDAO.authenticateUser(username, password);

            if (user != null) {
                // Create session and store user information
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userID", user.getUserID());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("roleID", user.getRoleID());

                LOGGER.log(Level.INFO, "User logged in successfully: {0}", username);

                // Redirect based on role
                switch (user.getRoleID()) {
                    case 1: // Learner
                        response.sendRedirect(request.getContextPath() + "/home");
                        break;
                    case 2: // Instructor
                        response.sendRedirect(request.getContextPath() + "/home");
                        break;
                    case 3: // Admin
                        response.sendRedirect("./admin/users");
                        break;
                    default:
                        response.sendRedirect(request.getContextPath() + "/index.jsp");
                }
            } else {
                LOGGER.log(Level.WARNING, "Failed login attempt for username: {0}", username);
                request.setAttribute("error", "Invalid username or password.");
                request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during login process", e);
            request.setAttribute("error", "An unexpected error occurred. Please try again later.");
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // Redirect to appropriate dashboard based on role
            User user = (User) session.getAttribute("user");
            switch (user.getRoleID()) {
                case 1:
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                    break;
                case 2:
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                    break;
                case 3:
                    response.sendRedirect(request.getContextPath() + "/user-list.jsp");
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
            return;
        }

        // Forward to login page
        request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
    }
}
