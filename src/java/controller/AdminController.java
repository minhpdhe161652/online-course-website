package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import model.User;

@WebServlet("/admin/users")
public class AdminController extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String type = request.getParameter("type");
        String sort = request.getParameter("sort");

        if (action == null) {
            // Hiển thị danh sách người dùng theo loại
            if (type == null || type.isEmpty()) {
                type = "learner"; // Mặc định hiển thị learner
            }
            if (sort == null || sort.isEmpty()) {
                sort = "name"; // Mặc định sắp xếp theo tên
            }
            String order = request.getParameter("order");
            if (order == null || order.isEmpty()) {
                order = "ASC"; // Mặc định sắp xếp tăng dần
            }

            int roleId = type.equals("expert") ? 2 : 1; // 1: Learner, 2: Expert
            List<User> users = userDAO.getUsersByRole(roleId, sort, order);
            request.setAttribute("users", users);
            request.setAttribute("currentSort", sort);
            request.setAttribute("currentOrder", order);
            
            String jspPage = type.equals("expert") ? "/expert-list.jsp" : "/learner-list.jsp";
            request.getRequestDispatcher(jspPage).forward(request, response);
        } else if (action.equals("detail")) {
            // Hiển thị thông tin chi tiết người dùng
            int userId = Integer.parseInt(request.getParameter("id"));
            User user = userDAO.getUserById(userId);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/admin/user-detail.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            // Vô hiệu hóa người dùng
            int userId = Integer.parseInt(request.getParameter("id"));
            boolean isDeleted = userDAO.deleteUser(userId);
            if (isDeleted) {
                request.getSession().setAttribute("successMessage", "Vô hiệu hóa người dùng thành công!");
            } else {
                request.getSession().setAttribute("errorMessage", "Vô hiệu hóa người dùng thất bại!");
            }
            // Chuyển hướng về trang danh sách tương ứng
            String redirectType = request.getParameter("type");
            if (redirectType == null || redirectType.isEmpty()) {
                redirectType = "learner";
            }
            response.sendRedirect(request.getContextPath() + "/admin/users?type=" + redirectType);
        } else if (action.equals("reactivate")) {
            // Kích hoạt lại người dùng
            int userId = Integer.parseInt(request.getParameter("id"));
            boolean isReactivated = userDAO.reactivateUser(userId);
            if (isReactivated) {
                request.getSession().setAttribute("successMessage", "Kích hoạt lại người dùng thành công!");
            } else {
                request.getSession().setAttribute("errorMessage", "Kích hoạt lại người dùng thất bại!");
            }
            // Chuyển hướng về trang danh sách tương ứng
            String redirectType = request.getParameter("type");
            if (redirectType == null || redirectType.isEmpty()) {
                redirectType = "learner";
            }
            response.sendRedirect(request.getContextPath() + "/admin/users?type=" + redirectType);
        }
    }
}