<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<%
    User user = (User) request.getAttribute("user");

    if (user == null) {
        out.println("<p class='error-message'>Lỗi: Không tìm thấy thông tin người dùng.</p>");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Hồ sơ cá nhân</title>
        <link rel="stylesheet" href="user-profile.css">
    </head>
    <body>
        <div class="container">
            <div class="profile-card">
                <div class="avatar">
                </div>
                <h2>Thông tin cá nhân</h2>
                <p><strong>Họ:</strong> <%= user.getFirstName() %></p>
                <p><strong>Tên:</strong> <%= user.getLastName() %></p>
                <p><strong>Email:</strong> <%= user.getEmail() %></p>
                <p><strong>Ngày đăng ký:</strong> <%= user.getRegistrationDate() %></p>
                <p><strong>Trạng thái:</strong> <span class="<%= user.isIsActive() ? "active" : "inactive" %>">
                    <%= user.isIsActive() ? "Hoạt động" : "Không hoạt động" %>
                </span></p>
                <p><strong>Giới thiệu:</strong> <%= user.getBio() %></p>
                <div class="actions">
                    <a href="editProfile.jsp" class="btn">Chỉnh sửa hồ sơ</a>
                    <a href="logout" class="btn logout">Đăng xuất</a>
                </div>
            </div>
        </div>
    </body>
</html>
