<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Course Detail</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            body {
                font-family: Arial, sans-serif;
            }
            .navbar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 1rem 2rem;
                background-color: white;
                border-bottom: 1px solid #ddd;
            }
            .logo {
                color: #007bff;
                font-size: 24px;
                font-weight: bold;
                text-decoration: none;
            }
            .nav-menu {
                display: flex;
                gap: 2rem;
                align-items: center;
            }
            .nav-item {
                text-decoration: none;
                color: #333;
            }
            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 2rem;
            }
            .course-header {
                display: grid;
                grid-template-columns: 3fr 250px 300px;
                gap: 2rem;
                margin-bottom: 3rem;
                align-items: start;
            }
            .course-info {
                display: flex;
                flex-direction: column;
                gap: 1rem;
            }
            .course-image {
                width: 250px;
                height: 180px;
                object-fit: contain;
            }
            .course-title {
                font-size: 24px;
                margin-bottom: 0.5rem;
            }
            .course-meta {
                color: #666;
                font-size: 14px;
            }
            .price-box {
                border: 1px solid #ddd;
                padding: 2rem;
                border-radius: 8px;
                text-align: center;
                height: fit-content;
                background-color: #fff;
                box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            }
            .price {
                font-size: 36px;
                font-weight: bold;
                margin-bottom: 1.5rem;
            }
            .btn {
                display: block;
                padding: 1rem;
                margin: 1rem 0;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                width: 100%;
                font-size: 16px;
                font-weight: 500;
            }
            .btn-cart {
                background-color: white;
                border: 1px solid #007bff;
                color: #007bff;
                margin-bottom: 0.8rem;
            }
            .btn-buy {
                background-color: #007bff;
                color: white;
            }
            .course-content {
                max-width: 900px;
                margin: 0 auto;
                padding: 0;
            }
            .tabs {
                display: flex;
                gap: 3rem;
                margin-bottom: 2rem;
                border-bottom: 1px solid #ddd;
                justify-content: flex-start;
                padding: 0 1rem;
            }
            .tab {
                padding: 1rem 0;
                cursor: pointer;
                border: none;
                background: none;
                font-size: 16px;
                position: relative;
                color: #666;
                transition: color 0.3s;
            }
            .tab:hover {
                color: #007bff;
            }
            .tab.active {
                color: #007bff;
                font-weight: 500;
            }
            .tab.active::after {
                content: '';
                position: absolute;
                bottom: -1px;
                left: 0;
                width: 100%;
                height: 2px;
                background-color: #007bff;
            }
            .tab-content {
                display: none;
                padding: 2rem 1rem;
                line-height: 1.6;
            }
            .tab-content.active {
                display: block;
            }
            .instructor-profile {
                display: flex;
                align-items: center;
                gap: 1.5rem;
                margin-bottom: 1.5rem;
            }
            .instructor-avatar {
                width: 80px;
                height: 80px;
                border-radius: 50%;
                object-fit: cover;
            }
            .instructor-info {
                flex: 1;
            }
            .instructor-info h3 {
                margin: 0 0 0.5rem 0;
                font-size: 1.25rem;
            }
            .instructor-info p {
                margin: 0;
                color: #666;
            }
        </style>
    </head>
    <body>
        <nav class="navbar">
            <a href="home" class="logo">LearnHub</a>
            <div class="nav-menu">
                <a href="categories" class="nav-item">Category</a>
                <a href="my-courses" class="nav-item">My Course</a>
                <a href="profile" class="nav-item">User</a>
            </div>
        </nav>

        <div class="container">
            <div class="course-header">
                <div class="course-info">
                    <h1 class="course-title">${course.courseName}</h1>
                    <div class="course-meta">
                        Created by: ${instructor.firstName} ${instructor.lastName}<br>
                        Last update: ${course.createdDate}
                    </div>
                </div>
                
                <img src="${course.imageURL}" alt="Course Image" class="course-image">

                <div class="price-box">
                    <div class="price">$${course.price}</div>
                    <button class="btn btn-cart">Add to cart</button>
                    <button class="btn btn-buy">Buy</button>
                </div>
            </div>

            <div class="course-content">
                <div class="tabs">
                    <button class="tab active" onclick="openTab(event, 'overview')">Overview</button>
                    <button class="tab" onclick="openTab(event, 'instructor')">Instructor</button>
                    <button class="tab" onclick="openTab(event, 'reviews')">Review</button>
                </div>

                <div id="overview" class="tab-content active">
                    ${course.description}
                </div>

                <div id="instructor" class="tab-content">
                    <div class="instructor-profile">
                        <img src="${empty instructor.avatar ? 'assets/images/default-avatar.jpg' : instructor.avatar}" 
                             alt="Instructor Avatar" 
                             class="instructor-avatar"
                             onerror="this.src='assets/images/default-avatar.jpg'">
                        <div class="instructor-info">
                            <h3>${instructor.firstName} ${instructor.lastName}</h3>
                            <p>${instructor.email}</p>
                            <c:if test="${not empty instructor.bio}">
                                <p>${instructor.bio}</p>
                            </c:if>
                        </div>
                    </div>
                </div>

                <div id="reviews" class="tab-content">
                    <c:forEach items="${feedbacks}" var="feedback">
                        <div class="review">
                            <div class="rating">${feedback.Rating} / 5</div>
                            <p>${feedback.FeedbackText}</p>
                            <div class="review-meta">
                                <span>Date: ${feedback.FeedBackDate}</span>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

        <script>
            function openTab(evt, tabName) {
                var i, tabcontent, tablinks;
                tabcontent = document.getElementsByClassName("tab-content");
                for (i = 0; i < tabcontent.length; i++) {
                    tabcontent[i].style.display = "none";
                }
                tablinks = document.getElementsByClassName("tab");
                for (i = 0; i < tablinks.length; i++) {
                    tablinks[i].className = tablinks[i].className.replace(" active", "");
                }
                document.getElementById(tabName).style.display = "block";
                evt.currentTarget.className += " active";
            }
        </script>
    </body>
</html>
