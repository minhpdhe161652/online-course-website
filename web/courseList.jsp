<%-- 
    Document   : courseList
    Created on : Feb 20, 2025, 4:04:45 PM
    Author     : Administrator
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Course List</title>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="css/style.css">
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
                font-family: 'Inter', sans-serif;
            }

            body {
                background-color: #f5f7fa;
            }

            .navbar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 1rem 2rem;
                background: #ffffff;
                border-bottom: 1px solid #e1e4e8;
                box-shadow: 0 1px 3px rgba(0,0,0,0.05);
            }

            .back-button {
                text-decoration: none;
                color: #333;
                display: flex;
                align-items: center;
                font-weight: 500;
                transition: color 0.2s;
            }

            .back-button:hover {
                color: #0066cc;
            }

            .right-nav {
                display: flex;
                gap: 1.5rem;
                align-items: center;
            }

            .my-course-btn {
                padding: 0.6rem 1.2rem;
                background: #f8f9fa;
                border: 1px solid #e1e4e8;
                border-radius: 6px;
                text-decoration: none;
                color: #333;
                font-weight: 500;
                transition: all 0.2s;
            }

            .my-course-btn:hover {
                background: #e9ecef;
                border-color: #dee2e6;
            }

            .user-profile {
                width: 40px;
                height: 40px;
                background: #e9ecef;
                border-radius: 50%;
                cursor: pointer;
                transition: transform 0.2s;
            }

            .user-profile:hover {
                transform: scale(1.05);
            }

            .filters {
                display: flex;
                gap: 1rem;
                padding: 1.5rem 2rem;
                align-items: center;
                background: #ffffff;
                margin: 1rem 2rem;
                border-radius: 8px;
                box-shadow: 0 1px 3px rgba(0,0,0,0.05);
            }

            select, .search-box {
                padding: 0.6rem 1rem;
                border: 1px solid #e1e4e8;
                border-radius: 6px;
                font-size: 0.95rem;
                color: #333;
                background: #ffffff;
                transition: all 0.2s;
            }

            select:hover, .search-box:hover {
                border-color: #0066cc;
            }

            select:focus, .search-box:focus {
                outline: none;
                border-color: #0066cc;
                box-shadow: 0 0 0 3px rgba(0,102,204,0.1);
            }

            .search-box {
                flex-grow: 1;
                max-width: 300px;
                padding-left: 2.5rem;
                background-image: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23999"><path d="M15.5 14h-.79l-.28-.27a6.5 6.5 0 1 0-.7.7l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0A4.5 4.5 0 1 1 14 9.5 4.5 4.5 0 0 1 9.5 14z"/></svg>');
                background-repeat: no-repeat;
                background-position: 0.75rem center;
                background-size: 1rem;
            }

            .course-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
                gap: 2rem;
                padding: 1rem 2rem 2rem;
            }

            .course-card {
                background: #ffffff;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 4px rgba(0,0,0,0.05);
                transition: transform 0.2s, box-shadow 0.2s;
                text-decoration: none;
                color: inherit;
                display: block;
            }

            .course-card:hover {
                transform: translateY(-4px);
                box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            }

            .course-image {
                width: 100%;
                height: 180px;
                background-size: cover;
                background-position: center;
                position: relative;
                overflow: hidden;
            }

            .course-image::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background: linear-gradient(180deg, rgba(0,0,0,0) 0%, rgba(0,0,0,0.8) 100%);
                z-index: 1;
            }

            .course-image h2 {
                position: absolute;
                bottom: 1rem;
                left: 1rem;
                right: 1rem;
                z-index: 2;
                color: white;
                font-size: 1.25rem;
                font-weight: 600;
                text-shadow: 0 1px 3px rgba(0,0,0,0.3);
                margin: 0;
            }

            .course-info {
                padding: 1.25rem;
            }

            .course-title {
                font-size: 1.1rem;
                font-weight: 600;
                color: #1a1a1a;
                margin-bottom: 0.75rem;
                line-height: 1.4;
            }

            .instructor {
                color: #666;
                font-size: 0.95rem;
                margin-bottom: 1rem;
                display: flex;
                align-items: center;
            }

            .instructor i {
                margin-right: 0.5rem;
                color: #999;
            }

            .price {
                color: #0066cc;
                font-weight: 600;
                font-size: 1.1rem;
                display: flex;
                align-items: center;
            }

            .price i {
                margin-right: 0.5rem;
            }

            .filter-label {
                font-weight: 500;
                color: #666;
                margin-right: 0.5rem;
            }

            .filter-group {
                display: flex;
                align-items: center;
            }
        </style>
        <script>
            function updateSort(sortBy, order) {
                window.location.href = 'courses?sortBy=' + sortBy + '&order=' + order;
            }

            function updateLevel(selectElement) {
                const level = selectElement.value;
                if (level === '') {
                    window.location.href = 'courses'; // Reset to show all courses
                } else {
                    window.location.href = 'courses?level=' + level;
                }
            }

            function searchCourses(input) {
                const searchTerm = input.value.trim();
                if (searchTerm.length > 2) { // Search after 3 characters
                    window.location.href = 'courses?search=' + encodeURIComponent(searchTerm);
                }
            }
        </script>
    </head>
    <body>
        <form action="courses" method="post">
            <nav class="navbar">
                <a href="index.jsp" class="logo">LearnHub</a>
                <div class="right-nav">
                    <a href="#" class="my-course-btn">
                        <i class="fas fa-book-open"></i>
                        <span style="margin-left: 0.5rem;">My Courses</span>
                    </a>
                    <div class="user-profile">
                        <i class="fas fa-user" style="display: flex; justify-content: center; align-items: center; height: 100%; color: #666;"></i>
                    </div>
                </div>
            </nav>

            <div class="filters">
                <div class="filter-group">
                    <span class="filter-label">Level:</span>
                    <select onchange="updateLevel(this)">
                        <option value="">All Levels</option>
                        <option value="1" ${param.level == '1' ? 'selected' : ''}>Beginner</option>
                        <option value="2" ${param.level == '2' ? 'selected' : ''}>Intermediate</option>
                        <option value="3" ${param.level == '3' ? 'selected' : ''}>Advanced</option>
                    </select>
                </div>

                <div class="filter-group">
                    <span class="filter-label">Date:</span>
                    <select onchange="updateSort('date', this.value)">
                        <option value="">Sort by Date</option>
                        <option value="newest" ${param.sortBy == 'date' && param.order == 'newest' ? 'selected' : ''}>Newest First</option>
                        <option value="oldest" ${param.sortBy == 'date' && param.order == 'oldest' ? 'selected' : ''}>Oldest First</option>
                    </select>
                </div>

                <div class="filter-group">
                    <span class="filter-label">Price:</span>
                    <select onchange="updateSort('price', this.value)">
                        <option value="">Sort by Price</option>
                        <option value="high" ${param.sortBy == 'price' && param.order == 'high' ? 'selected' : ''}>High to Low</option>
                        <option value="low" ${param.sortBy == 'price' && param.order == 'low' ? 'selected' : ''}>Low to High</option>
                    </select>
                </div>

                <input type="search" name = "search"
                       placeholder="Search courses..." 
                       class="search-box" 
                       oninput="searchCourses(this)"
                       value="${param.search}">
            </div>

            <div class="course-grid">
                <c:forEach var="course" items="${courses}">
                    <a href="course-detail?id=${course.courseID}" class="course-card">
                        <div class="course-image" style="background-image: url('${course.imageURL}')">
                            <h2>${course.courseName}</h2>
                        </div>
                        <div class="course-info">
                            <h3 class="course-title">${course.courseName}</h3>
                            <p class="instructor">
                                <i class="fas fa-chalkboard-teacher"></i>
                                Giảng viên: ${course.instructorName}
                            </p>
                            <p class="price">
                                <i class="fas fa-tag"></i>
                                $${course.price}
                            </p>
                        </div>
                    </a>
                </c:forEach>
            </div>
        </form>
    </body>
</html>
