<%-- 
    Document   : forgot-otp
    Created on : Feb 22, 2025, 3:03:29 PM
    Author     : toila
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Press Your Reset OTP</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
    <style>
        .form-container {
            max-width: 450px;
            margin: 2rem auto;
            padding: 2rem;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
            background-color: #fff;
        }
        .error-message {
            color: #dc3545;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <!-- Header -->
    <header class="header">
        <div class="container header-content">
            <a href="index.jsp" class="logo">LearnHub</a>
            <nav class="nav-menu">
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <a href="login" class="nav-link btn btn-primary">Đăng nhập</a>
                    </c:when>
                    <c:otherwise>
                        <a href="/OnlineLearningSystem/user-profile" class="nav-link">Trang cá nhân</a>
                        <a href="logout" class="nav-link">Đăng xuất</a>
                    </c:otherwise>
                </c:choose>
            </nav>
        </div>
    </header>
    <!-- Hero Section -->
    <section class="hero">
        <div class="container">
            <h1>Quên mật khẩu</h1>
            <p>Vui lòng nhập email của bạn để nhận liên kết khôi phục mật khẩu.</p>
        </div>
    </section>
    
    <!-- Hiển thị thông báo lỗi -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert">
                    ${errorMessage}
                </div>
            </c:if>

            <!-- Hiển thị thông báo thành công -->
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success" role="alert">
                    ${successMessage}
                </div>
            </c:if>

    <!-- Forgot Password Form -->
    <section class="section">
        <div class="container">
            <div class="form-container">
                <h2 class="text-center mb-4">Enter Reset OTP</h2>

                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger" role="alert">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <% 
                    String message = (String) session.getAttribute("message");
                    if (message != null) {
                %>
                    <div class="alert alert-success" role="alert">
                        <%= message %>
                    </div>
                <%
                        session.removeAttribute("message");
                    }
                %>

                <form action="forgot-otp" method="POST" class="needs-validation" novalidate>
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email" required autofocus value=${email}>
                        <div class="invalid-feedback">
                            Please enter your email.
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="resetOTP" class="form-label">Reset OTP</label>
                        <input type="text" class="form-control" id="resetOTP" name="resetOTP" required autofocus value=${resetOTP}>
                    </div>

                    <button type="submit" class="btn btn-primary w-100">Gửi yêu cầu</button>

                    <div class="text-center mt-3">
                        <p><a href="${pageContext.request.contextPath}/auth/login.jsp">Quay lại trang đăng nhập</a></p>
                    </div>
                </form>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer class="footer">
        <div class="container footer-content">
            <p>&copy; 2025 LearnHub. All rights reserved.</p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        // Form validation
        (function () {
            'use strict'
            const forms = document.querySelectorAll('.needs-validation')
            Array.from(forms).forEach(form => {
                form.addEventListener('submit', event => {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    }
                    form.classList.add('was-validated')
                }, false)
            })
        })()
    </script>
</body>
</html>