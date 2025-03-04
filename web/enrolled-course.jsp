<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Danh sách lớp đã ghi danh</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 20px;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        h2 {
            color: #343a40;
        }
        .container {
            width: 70%;
            background: #ffffff;
            padding: 20px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background: #ffffff;
            border-radius: 8px;
            overflow: hidden;
        }
        th, td {
            border: 1px solid #dee2e6;
            padding: 12px;
            text-align: left;
        }
        th {
            background-color: #007bff;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        tr:hover {
            background-color: #e9ecef;
            transition: 0.3s;
        }
        p {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <c:if test="${sessionScope.user == null || sessionScope.user.roleID != 1}">
        <c:redirect url="enrolled-courses"/>
    </c:if>

    <div class="container">
        <h2>Enrolled Courses</h2>

        <c:if test="${not empty requestScope.error}">
            <p class="error">${requestScope.error}</p>
        </c:if>

        <c:if test="${empty requestScope.error}">
            <c:choose>
                <c:when test="${not empty requestScope.enrollments}">
                    <table>
                        <tr>
                            <th>Course Name</th>
                            <th>Enrolled Date</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                        <c:forEach items="${requestScope.enrollments}" var="enrollment">
                            <tr>
                                <td>${enrollment.courseName}</td>
                                <td><fmt:formatDate value="${enrollment.enrollmentDate}" pattern="dd/MM/yyyy"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${enrollment.completed}">
                                            <span style="color: green">Completed</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: orange">Learning</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="learn/course-dashboard?courseID=${enrollment.courseID}" class="btn btn-primary">
                                        Go to course
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:when>
                <c:otherwise>
                    <p>Not enrolled any course</p>
                </c:otherwise>
            </c:choose>
        </c:if>
    </div>
</body>
</html>
