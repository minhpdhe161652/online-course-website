<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Course Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f8f9fa; padding: 20px; }
        .container { width: 80%; margin: auto; background: white; padding: 20px; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); border-radius: 8px; }
        .section { margin-bottom: 20px; }
        .section-title { font-size: 18px; font-weight: bold; margin-bottom: 10px; }
        .lecture { margin-left: 20px; }
        .quiz { margin-left: 20px; color: #d9534f; }
        .progress-bar { width: 100%; background-color: #f3f3f3; border-radius: 5px; overflow: hidden; margin-top: 10px; }
        .progress { height: 20px; background-color: #4caf50; text-align: center; color: white; line-height: 20px; }
    </style>
</head>
<body>
    <div class="container">
        <h2>Course Dashboard: ${course.courseName}</h2>

        <c:forEach var="section" items="${sections}">
            <div class="section">
                <div class="section-title">Section: ${section.sectionName}</div>
                
                <c:forEach var="lecture" items="${lectures}">
                    <c:if test="${lecture.sectionID == section.sectionID}">
                        <div class="lecture">
                            📖 <a href="view-lecture?courseID=${course.courseID}&sectionID=${section.sectionID}&lectureId=${lecture.lectureID}">Lecture: ${lecture.lectureName}</a>
                        </div>
                    </c:if>
                </c:forEach>
                
            </div>
        </c:forEach>
        
        <br>
        <a href="${pageContext.request.contextPath}/enrolled-course">Back to Courses</a>

    </div>
</body>
</html>
