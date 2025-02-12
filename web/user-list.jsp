<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách người dùng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .message {
            padding: 10px;
            margin: 10px 0;
            border-radius: 4px;
        }
        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>
    <h1>Danh sách người dùng</h1>
    
    <c:if test="${not empty sessionScope.successMessage}">
        <div class="message success">
            ${sessionScope.successMessage}
            <c:remove var="successMessage" scope="session" />
        </div>
    </c:if>
    
    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="message error">
            ${sessionScope.errorMessage}
            <c:remove var="errorMessage" scope="session" />
        </div>
    </c:if>

    <table border="1">
        <tr>
            <th>ID</th>
            <th>Tên đăng nhập</th>
            <th>Họ và tên</th>
            <th>Email</th>
            <th>Vai trò</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
        </tr>
        <c:forEach var="user" items="${users}">
            <tr>
                <td>${user.userID}</td>
                <td>${user.username}</td>
                <td>${user.firstName} ${user.lastName}</td>
                <td>${user.email}</td>
                <td>${user.roleID == 1 ? 'Learner' : user.roleID == 2 ? 'Expert' : 'Admin'}</td>
                <td>
                    <span style="color: ${user.isActive ? 'green' : 'red'}">
                        ${user.isActive ? 'Đang hoạt động' : 'Đã vô hiệu hóa'}
                    </span>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/users?action=detail&id=${user.userID}">Xem</a>
                    <c:if test="${user.isActive}">
                        <a href="javascript:void(0);" onclick="confirmDelete(${user.userID})">Vô hiệu hóa</a>
                    </c:if>
                    <c:if test="${!user.isActive}">
                        <a href="javascript:void(0);" onclick="confirmReactivate(${user.userID})">Kích hoạt lại</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>

    <script>
        function confirmDelete(userId) {
            if (confirm('Bạn có chắc chắn muốn vô hiệu hóa người dùng này không?')) {
                window.location.href = '${pageContext.request.contextPath}/admin/users?action=delete&id=' + userId;
            }
        }
        
        function confirmReactivate(userId) {
            if (confirm('Bạn có chắc chắn muốn kích hoạt lại người dùng này không?')) {
                window.location.href = '${pageContext.request.contextPath}/admin/users?action=reactivate&id=' + userId;
            }
        }
    </script>
</body>
</html>