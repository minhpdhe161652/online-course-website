<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.EnrolledCourseDTO" %>
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
    <div class="container">
        <h2>Danh sách lớp đã ghi danh</h2>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <p><%= error %></p>
        <%
            } else {
                List<EnrolledCourseDTO> enrollments = (List<EnrolledCourseDTO>) request.getAttribute("enrollments");
                if (enrollments != null && !enrollments.isEmpty()) {
        %>
            <table>
                <tr>
                    <th>Tên khóa học</th>
                    <th>Ngày Đăng ký</th>
                </tr>
                <%
                    for (EnrolledCourseDTO enrollment : enrollments) {
                %>
                    <tr>
                        <td><%= enrollment.getCourseName() %></td>
                        <td><%= enrollment.getEnrollmentDate() %></td>
                    </tr>
                <%
                    }
                %>
            </table>
        <%
                } else {
        %>
            <p>Không tìm thấy dữ liệu.</p>
        <%
                }
            }
        %>
    </div>
</body>
</html>
