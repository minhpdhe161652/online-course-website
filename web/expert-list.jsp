<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Expert | Learning Management System</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
    <div class="header">
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/admin/users?type=learner" class="${param.type == 'learner' || param.type == null ? 'current-sort' : ''}">
                <i class="fas fa-user-graduate"></i> Learners
            </a>
            <a href="${pageContext.request.contextPath}/admin/users?type=expert" class="${param.type == 'expert' ? 'current-sort' : ''}">
                <i class="fas fa-chalkboard-teacher"></i> Experts
            </a>
        </div>

        <h1><i class="fas fa-chalkboard-teacher"></i> Quản lý Expert</h1>
    </div>

    <c:if test="${not empty sessionScope.successMessage}">
        <div class="message success">
            <i class="fas fa-check-circle"></i>
            ${sessionScope.successMessage}
            <c:remove var="successMessage" scope="session" />
        </div>
    </c:if>
    
    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="message error">
            <i class="fas fa-exclamation-circle"></i>
            ${sessionScope.errorMessage}
            <c:remove var="errorMessage" scope="session" />
        </div>
    </c:if>
    
    <div class="sort-section">
        <span><i class="fas fa-sort"></i> Sắp xếp theo:</span>
        <a href="?type=expert&sort=name&order=${param.sort == 'name' && param.order == 'ASC' ? 'DESC' : 'ASC'}" 
           class="sort-link ${param.sort == 'name' ? 'current-sort' : ''}">
            <i class="fas fa-font"></i> Họ và tên
            <c:if test="${param.sort == 'name'}">
                <i class="fas fa-arrow-${param.order == 'ASC' ? 'up' : 'down'}"></i>
            </c:if>
        </a>
        <a href="?type=expert&sort=email&order=${param.sort == 'email' && param.order == 'ASC' ? 'DESC' : 'ASC'}" 
           class="sort-link ${param.sort == 'email' ? 'current-sort' : ''}">
            <i class="fas fa-envelope"></i> Email
            <c:if test="${param.sort == 'email'}">
                <i class="fas fa-arrow-${param.order == 'ASC' ? 'up' : 'down'}"></i>
            </c:if>
        </a>
        <a href="?type=expert&sort=username&order=${param.sort == 'username' && param.order == 'ASC' ? 'DESC' : 'ASC'}" 
           class="sort-link ${param.sort == 'username' ? 'current-sort' : ''}">
            <i class="fas fa-user"></i> Tên đăng nhập
            <c:if test="${param.sort == 'username'}">
                <i class="fas fa-arrow-${param.order == 'ASC' ? 'up' : 'down'}"></i>
            </c:if>
        </a>
    </div>

    <table class="data-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên đăng nhập</th>
                <th>Họ và tên</th>
                <th>Email</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="user" items="${users}">
                <tr>
                    <td>${user.userID}</td>
                    <td>${user.username}</td>
                    <td>${user.firstName} ${user.lastName}</td>
                    <td>${user.email}</td>
                    <td>
                        <span class="status ${user.isActive ? 'status-active' : 'status-inactive'}">
                            <i class="fas fa-${user.isActive ? 'check-circle' : 'times-circle'}"></i>
                            ${user.isActive ? 'Đang hoạt động' : 'Đã vô hiệu hóa'}
                        </span>
                    </td>
                    <td>
                        <div class="action-buttons">
                            <a href="${pageContext.request.contextPath}/admin/users?action=detail&id=${user.userID}" 
                               class="btn btn-view">
                                <i class="fas fa-eye"></i> Xem
                            </a>
                            <c:if test="${user.isActive}">
                                <a href="javascript:void(0);" 
                                   onclick="confirmDelete(${user.userID})"
                                   class="btn btn-disable">
                                    <i class="fas fa-user-slash"></i> Vô hiệu hóa
                                </a>
                            </c:if>
                            <c:if test="${!user.isActive}">
                                <a href="javascript:void(0);" 
                                   onclick="confirmReactivate(${user.userID})"
                                   class="btn btn-activate">
                                    <i class="fas fa-user-check"></i> Kích hoạt lại
                                </a>
                            </c:if>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <script>
        function confirmDelete(userId) {
            if (confirm('Bạn có chắc chắn muốn vô hiệu hóa người dùng này không?')) {
                window.location.href = '${pageContext.request.contextPath}/admin/users?action=delete&id=' + userId + '&type=expert';
            }
        }
        
        function confirmReactivate(userId) {
            if (confirm('Bạn có chắc chắn muốn kích hoạt lại người dùng này không?')) {
                window.location.href = '${pageContext.request.contextPath}/admin/users?action=reactivate&id=' + userId + '&type=expert';
            }
        }
    </script>
</body>
</html>
