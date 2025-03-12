<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Online Learning Platform</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .btn-create-course {
            background-color: #28a745;
            color: white;
            padding: 8px 16px;
            border-radius: 4px;
            text-decoration: none;
            margin-left: 10px;
        }
        .btn-create-course:hover {
            background-color: #218838;
        }
        .instructor-link {
            color: #666;
            text-decoration: none;
            padding: 0.5rem 1rem;
            border-radius: 4px;
            transition: all 0.3s ease;
        }
        .instructor-link:hover {
            color: #333;
            background-color: #f5f5f5;
        }
    </style>
</head>
<body>
    <!-- Header -->
    <header class="header">
        <div class="container header-content">
            <a href="index.jsp" class="logo">LearnHub</a>
            <nav class="nav-menu">
                <a href="#courses" class="nav-link">Khóa học</a>
                <a href="#categories" class="nav-link">Danh mục</a>
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <a href="login" class="nav-link btn btn-primary">Đăng nhập</a>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${sessionScope.user.roleID == 2}">
                            <a href="instructor-courses" class="instructor-link">Khóa học của tôi</a>
                            <a href="add-course" class="nav-link btn-create-course">Tạo khóa học</a>
                        </c:if>
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
            <h1>Học từ những người giỏi nhất</h1>
            <p>Khám phá hàng ngàn khóa học từ các giảng viên chuyên nghiệp</p>
            <a href="#courses" class="btn btn-primary">Khám phá ngay</a>
        </div>
    </section>

    <!-- Featured Courses -->
    <section id="courses" class="section">
        <div class="container">
            <h2 class="section-title">Khóa học nổi bật</h2>
            <div class="courses-grid">
                <c:choose>
                    <c:when test="${not empty featuredCourses}">
                        <c:forEach items="${featuredCourses}" var="course">
                            <div class="course-card">
                                <img src="${course.imageURL}" alt="${course.courseName}" class="course-image">
                                <div class="course-content">
                                    <h3 class="course-title">${course.courseName}</h3>
                                    <p class="course-instructor">Giảng viên: ${course.instructorName}</p>
                                    <p class="course-price">${course.price} VNĐ</p>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <!-- Temporary static course cards -->
                        <div class="course-card">
                            <img src="https://res.cloudinary.com/dieoz2rgk/image/upload/v1721542914/o5gbhgrtmflpaumlf24x.jpg" alt="Machine Learning" class="course-image">
                            <div class="course-content">
                                <h3 class="course-title">Nhập môn Machine Learning</h3>
                                <p class="course-instructor">Giảng viên: Jason Kim</p>
                                <p class="course-price">59.99$</p>
                            </div>
                        </div>
                        <div class="course-card">
                            <img src="https://res.cloudinary.com/dieoz2rgk/image/upload/v1721543114/nsg2didzwtjc2fsix3ws.jpg" alt="Graphic Design" class="course-image">
                            <div class="course-content">
                                <h3 class="course-title">Thiết kế đồ họa Master</h3>
                                <p class="course-instructor">Giảng viên: Jane Smith</p>
                                <p class="course-price">49.99$</p>
                            </div>
                        </div>
                        <div class="course-card">
                            <img src="https://res.cloudinary.com/dieoz2rgk/image/upload/v1721543257/owkf84oeqgolqfrinqnd.png" alt="Python" class="course-image">
                            <div class="course-content">
                                <h3 class="course-title">Lập trình Python nâng cao</h3>
                                <p class="course-instructor">Giảng viên: Jason Kim</p>
                                <p class="course-price">39.99$</p>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </section>

    <!-- Latest Courses -->
    <section id="latest-courses" class="section">
        <div class="container">
            <h2 class="section-title">Khóa học mới nhất</h2>
            <div class="courses-grid">
                <c:choose>
                    <c:when test="${not empty latestCourses}">
                        <c:forEach items="${latestCourses}" var="course">
                            <div class="course-card">
                                <img src="${course.imageURL}" alt="${course.courseName}" class="course-image">
                                <div class="course-content">
                                    <h3 class="course-title">${course.courseName}</h3>
                                    <p class="course-instructor">Giảng viên: ${course.instructorName}</p>
                                    <p class="course-price">${course.price} VNĐ</p>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p>Chưa có khóa học mới nào được phê duyệt.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </section>

    <!-- Categories -->
    <section id="categories" class="section">
        <div class="container">
            <h2 class="section-title">Danh mục phổ biến</h2>
            <div class="categories-grid">
                <div class="category-card">
                    <h3>Phát triển phần mềm</h3>
                </div>
                <div class="category-card">
                    <h3>Kinh doanh</h3>
                </div>
                <div class="category-card">
                    <h3>Khoa học dữ liệu</h3>
                </div>
                <div class="category-card">
                    <h3>Marketing</h3>
                </div>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer class="footer">
        <div class="container footer-content">
            <p>&copy; 2025 LearnHub. All rights reserved.</p>
        </div>
    </footer>
</body>
</html>
