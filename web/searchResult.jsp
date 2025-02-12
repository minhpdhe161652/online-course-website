<%-- 
    Document   : searchResult
    Created on : Feb 11, 2025, 9:46:42 AM
    Author     : Administrator
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Course Search Results</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f4f4f4;
                padding: 20px;
                max-width: 1200px;
                margin: 0 auto;
            }
            .header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 30px;
            }
            .back-button {
                display: inline-block;
                padding: 10px 20px;
                background-color: #007bff;
                color: white;
                text-decoration: none;
                border-radius: 4px;
                transition: background-color 0.3s;
            }
            .back-button:hover {
                background-color: #0056b3;
            }
            .message {
                background-color: #e2e3e5;
                color: #383d41;
                padding: 15px;
                border-radius: 4px;
                margin-bottom: 20px;
                text-align: center;
            }
            .courses-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
                gap: 20px;
            }
            .course {
                background-color: #fff;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                transition: transform 0.2s;
            }
            .course:hover {
                transform: translateY(-5px);
            }
            .course img {
                width: 100%;
                height: 200px;
                object-fit: cover;
                border-radius: 8px;
                margin-bottom: 15px;
            }
            .course h2 {
                color: #333;
                margin-top: 0;
                font-size: 1.5em;
            }
            .course p {
                color: #666;
                line-height: 1.5;
            }
            .price {
                font-size: 1.25em;
                color: #28a745;
                font-weight: bold;
            }
            .level-badge {
                display: inline-block;
                padding: 5px 10px;
                background-color: #17a2b8;
                color: white;
                border-radius: 4px;
                font-size: 0.9em;
                margin-bottom: 10px;
            }
            .no-results {
                background-color: #fff;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                text-align: center;
                margin-top: 20px;
            }
        </style>
    </head>
    <body>
        <div class="header">
            <a href="Search.jsp" class="back-button">← Back to Search</a>
            <h1>Courses - ${param.courseLevel} Level</h1>
        </div>

        <c:choose>
            <c:when test="${empty courses}">
                <div class="no-results">
                    <h2>No Courses Found</h2>
                    <p>We couldn't find any courses for the ${param.courseLevel} level. Please try selecting a different level.</p>
                    <a href="Search.jsp" class="back-button" style="margin-top: 20px;">Try Another Search</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="courses-grid">
                    <c:forEach var="course" items="${courses}">
                        <div class="course">
                            <img src="${course.imageURL}" alt="${course.courseName}" onerror="this.src='https://via.placeholder.com/300x200?text=Course+Image'">
                            <span class="level-badge">${course.levelName}</span>
                            <h2>${course.courseName}</h2>
                            <p>${course.description}</p>
                            <p class="price">$${course.price}</p>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>  
        </c:choose>
    </body>
</html>
