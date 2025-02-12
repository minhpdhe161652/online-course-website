/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CourseDAO;
import model.Course;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author Administrator
 */

@WebServlet(name = "levelCourseController", value = "/level-course")
public class LevelCourseController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String courseLevel = request.getParameter("courseLevel");
        if (courseLevel == null || courseLevel.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please select a course level");
            request.getRequestDispatcher("Search.jsp").forward(request, response);
            return;
        }

        courseLevel = courseLevel.trim();
        try {
            CourseDAO courseDAO = new CourseDAO();
            List<Course> courses = courseDAO.getCoursesByLevel(courseLevel);
            
            if (courses.isEmpty()) {
                request.setAttribute("message", "No courses found for level: " + courseLevel);
            }
            
            request.setAttribute("courses", courses);
            request.setAttribute("selectedLevel", courseLevel);
            request.getRequestDispatcher("searchResult.jsp").forward(request, response);
        } catch (Exception e) {
            // Log the error with more details
            String errorMsg = String.format("Error processing request for level '%s': %s", courseLevel, e.getMessage());
            System.err.println(errorMsg);
            e.printStackTrace();
            
            // Set error message and forward back to search page
            request.setAttribute("errorMessage", "An error occurred while searching for courses. Please try again later.");
            request.getRequestDispatcher("Search.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Course Level Search Servlet";
    }// </editor-fold>

}
