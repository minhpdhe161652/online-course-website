package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.ForgotPassword;

import java.io.IOException;

@WebServlet(name = "forgotPasswordServlet", value = "/forgot-password")
public class ForgotPasswordController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("./forgot-password.jsp").forward(request, response);
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        String email = request.getParameter("email").trim();

        UserDAO userDAO = new UserDAO();

        if(userDAO.checkAccountBlock(email)){
            if (userDAO.isEmailExist(email)) {
                ForgotPassword forgot = new ForgotPassword();
                String new_pass = forgot.getNewPassword();
                boolean sent = true;

                if(sent){
                    UserDAO adao = new UserDAO();

                    if (userDAO.getUserByEmail(email) != null) {
                        adao.changePassword(new_pass, userDAO.getUserByEmail(email).getUserID());
                    } else {
                        adao.changePassword(new_pass, userDAO.getUserByEmail(email).getUserID());
                    }
                    request.setAttribute("success", "Reset password successfully! Please check your mail.");
                    request.getRequestDispatcher("./login.jsp").forward(request, response);
                } else {
                    request.setAttribute("mess", "Can not send mail.");
                    request.setAttribute("last_email", email);
                    request.getRequestDispatcher("./forgot-password.jsp").forward(request, response);
                }
            }
            else {
                request.setAttribute("mess", "Email is not linked to an account");
                request.setAttribute("last_email", email);
                request.getRequestDispatcher("./forgot-password.jsp").forward(request, response);
            }
        }else {
            request.setAttribute("mess", "Account is blocked");
            request.setAttribute("last_email", email);
            request.getRequestDispatcher("./forgot-password.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
