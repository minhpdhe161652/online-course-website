<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Duyệt Khóa Học</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h2>Danh Sách Khóa Học Chờ Duyệt</h2>
        
        <c:if test="${not empty message}">
            <div class="alert ${success ? 'alert-success' : 'alert-danger'}" role="alert">
                ${message}
            </div>
        </c:if>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên Khóa Học</th>
                    <th>Giảng Viên</th>
                    <th>Giá</th>
                    <th>Ngày Tạo</th>
                    <th>Thao Tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${pendingCourses}" var="course">
                    <tr>
                        <td>${course.courseID}</td>
                        <td>
                            <strong>${course.courseName}</strong>
                            <p class="text-muted small">${course.description}</p>
                        </td>
                        <td>${course.instructorName}</td>
                        <td>${course.price}</td>
                        <td>${course.createdDate}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin/course-approval" method="post" style="display: inline;">
                                <input type="hidden" name="courseId" value="${course.courseID}">
                                <input type="hidden" name="action" value="approve">
                                <button type="submit" class="btn btn-success btn-sm">Phê duyệt</button>
                            </form>
                            
                            <button type="button" class="btn btn-danger btn-sm" 
                                    data-bs-toggle="modal" 
                                    data-bs-target="#rejectModal${course.courseID}">
                                Từ chối
                            </button>
                            
                            <!-- Modal từ chối -->
                            <div class="modal fade" id="rejectModal${course.courseID}" tabindex="-1">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title">Từ chối khóa học</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                        </div>
                                        <form action="${pageContext.request.contextPath}/admin/course-approval" method="post">
                                            <div class="modal-body">
                                                <input type="hidden" name="courseId" value="${course.courseID}">
                                                <input type="hidden" name="action" value="reject">
                                                <div class="mb-3">
                                                    <label for="reason" class="form-label">Lý do từ chối:</label>
                                                    <textarea class="form-control" name="reason" required></textarea>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                                <button type="submit" class="btn btn-danger">Xác nhận từ chối</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
