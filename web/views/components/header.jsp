<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Header -->
<header class="header">
    <div class="container header-content">
        <a href="/" class="logo">LearnHub</a>
        <nav class="nav-menu">
            <a href="/#courses" class="nav-link">Khóa học</a>
            <a href="/#categories" class="nav-link">Danh mục</a>
            <c:choose>
                <c:when test="${empty sessionScope.user}">
                    <a href="login" class="nav-link btn btn-primary">Đăng nhập</a>
                </c:when>
                <c:otherwise>
                    <c:if test="${sessionScope.user.roleID == 2}">
                        <a href="instructor-courses" class="nav-link">Khóa học của tôi</a>
                        <a href="add-course" class="nav-link btn-create-course">Tạo khóa học</a>
                    </c:if>
                    <a href="user-profile" class="nav-link">Trang cá nhân</a>
                    <a href="logout" class="nav-link">Đăng xuất</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>

<style>
    .header {
        background-color: #fff;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        z-index: 1000;
    }

    .header-content {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 1rem;
    }

    .logo {
        font-size: 1.5rem;
        font-weight: bold;
        color: #333;
        text-decoration: none;
    }

    .nav-menu {
        display: flex;
        gap: 1rem;
        align-items: center;
    }

    .nav-link {
        color: #666;
        text-decoration: none;
        padding: 0.5rem 1rem;
        border-radius: 4px;
        transition: all 0.3s ease;
    }

    .nav-link:hover {
        color: #333;
        background-color: #f5f5f5;
    }

    .btn-primary {
        background-color: #007bff;
        color: white;
    }

    .btn-primary:hover {
        background-color: #0056b3;
    }

    .btn-create-course {
        background-color: #28a745;
        color: white;
        padding: 8px 16px;
        border-radius: 4px;
        text-decoration: none;
    }

    .btn-create-course:hover {
        background-color: #218838;
    }

    .container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 1rem;
    }
</style>
