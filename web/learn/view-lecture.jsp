<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>View Lecture</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f8f9fa; padding: 20px; }
        .container { width: 80%; margin: auto; background: white; padding: 20px; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); border-radius: 8px; }
        .lecture-title { font-size: 24px; font-weight: bold; margin-bottom: 20px; }
        .lecture-description { margin-bottom: 20px; }
        .back-link { display: block; margin-top: 20px; }
        .video-container { margin-bottom: 20px; }
        .video-container iframe { width: 100%; height: 480px; border: none; }
    </style>
</head>
<body>
    <div class="container">
        <div class="lecture-title">${lecture.lectureName}</div>
        <div class="video-container">
            <iframe src="${lecture.lectureURL}" allowfullscreen></iframe>
        </div>
        <div class="lecture-description">${lecture.lectureDescription}</div>
        <a href="${pageContext.request.contextPath}/learn/course-dashboard?courseID=${courseID}">Back to Course Dashboard</a>


    </div>
</body>
</html>
