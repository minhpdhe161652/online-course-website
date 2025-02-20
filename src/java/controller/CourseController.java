/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dao.CourseDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Course;

/**
 *
 * @author Administrator
 */
@WebServlet(name="CourseController", urlPatterns={"/courses"})
public class CourseController extends HttpServlet {
   private final CourseDAO courseDAO = new CourseDAO();
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doGet(request, response);
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        List<Course> courses;
        
        String level = request.getParameter("level");
        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");
        String search = request.getParameter("search");
        
        // Handle search first
        if (search != null && !search.trim().isEmpty()) {
            courses = courseDAO.searchCourses(search.trim());
        }
        // Handle level filtering
        else if (level != null && !level.trim().isEmpty()) {
            courses = courseDAO.getCoursesByLevel(level.trim());
        }
        // Handle sorting
        else if (sortBy != null && !sortBy.trim().isEmpty() && order != null && !order.trim().isEmpty()) {
            if (sortBy.equals("date")) {
                courses = courseDAO.getCoursesSortedByDate(order.equals("newest"));
            } else if (sortBy.equals("price")) {
                courses = courseDAO.getCoursesSortedByPrice(order.equals("high"));
            } else {
                courses = courseDAO.getAllCourses();
            }
        }
        // Default: show all courses
        else {
            courses = courseDAO.getAllCourses();
        }
        
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("courseList.jsp").forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
