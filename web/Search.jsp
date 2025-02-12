<%-- 
    Document   : Search
    Created on : Feb 11, 2025, 12:00:57 AM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Course Level Selector</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f4f4f4;
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                margin: 0;
            }
            .container {
                background-color: #fff;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                width: 300px;
                text-align: center;
            }
            h2 {
                margin-bottom: 20px;
                color: #333;
            }
            select {
                width: 100%;
                padding: 10px;
                margin-bottom: 20px;
                border-radius: 4px;
                border: 1px solid #ccc;
                font-size: 16px;
                outline: none;
            }
            select:focus {
                border-color: #007bff;
                box-shadow: 0 0 5px rgba(0, 123, 255, 0.3);
            }
            input[type="submit"] {
                background-color: #007bff;
                color: white;
                padding: 10px 20px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 16px;
                width: 100%;
                transition: background-color 0.3s;
            }
            input[type="submit"]:hover {
                background-color: #0056b3;
            }
            .error {
                color: #dc3545;
                margin-top: 10px;
                display: none;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Select Course Level</h2>
            <c:if test="${not empty errorMessage}">
                <div class="error" style="display: block; margin-bottom: 15px;">
                    ${errorMessage}
                </div>
            </c:if>
            
            <form action="level-course" method="post" onsubmit="return validateForm()">
                <select name="courseLevel" id="courseLevel" required>
                    <option value="">Select a level</option>
                    <option value="Beginner">Beginner</option>
                    <option value="Intermediate">Intermediate</option>
                    <option value="Advanced">Advanced</option>
                </select>
                <div id="error-message" class="error">Please select a course level</div>
                <br><br>
                <input type="submit" value="Search Courses">
            </form>
        </div>

        <script>
            function validateForm() {
                const courseLevel = document.getElementById('courseLevel').value;
                const errorMessage = document.getElementById('error-message');

                if (!courseLevel) {
                    errorMessage.style.display = 'block';
                    return false;
                }

                errorMessage.style.display = 'none';
                return true;
            }
        </script>
    </body>
</html>
