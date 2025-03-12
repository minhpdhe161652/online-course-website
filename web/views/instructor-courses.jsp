<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản Lý Khóa Học | LearnHub</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            .course-card {
                transition: all 0.3s ease;
                height: 100%;
            }
            
            .course-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }
            
            .course-image {
                height: 200px;
                object-fit: cover;
                border-top-left-radius: calc(0.375rem - 1px);
                border-top-right-radius: calc(0.375rem - 1px);
            }
            
            .course-stats {
                font-size: 0.9rem;
                color: #6c757d;
            }
            
            .course-price {
                font-size: 1.25rem;
                font-weight: bold;
                color: #28a745;
            }
            
            .course-level {
                position: absolute;
                top: 10px;
                right: 10px;
                padding: 5px 10px;
                border-radius: 20px;
                font-size: 0.8rem;
                font-weight: 500;
            }
            
            .level-Beginner {
                background-color: #e9ecef;
                color: #495057;
            }
            
            .level-Intermediate {
                background-color: #cff4fc;
                color: #055160;
            }
            
            .level-Advanced {
                background-color: #f8d7da;
                color: #842029;
            }
            
            .course-actions {
                visibility: hidden;
                opacity: 0;
                transition: all 0.3s ease;
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                z-index: 2;
            }
            
            .course-overlay {
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background: rgba(0,0,0,0);
                transition: all 0.3s ease;
                border-radius: calc(0.375rem - 1px);
            }
            
            .course-card:hover .course-overlay {
                background: rgba(0,0,0,0.5);
            }
            
            .course-card:hover .course-actions {
                visibility: visible;
                opacity: 1;
            }
            
            .btn-action {
                margin: 0 5px;
                width: 40px;
                height: 40px;
                border-radius: 50%;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                background: white;
                border: none;
                box-shadow: 0 2px 5px rgba(0,0,0,0.2);
                transition: all 0.3s ease;
            }
            
            .btn-action:hover {
                transform: scale(1.1);
            }
            
            .course-status {
                position: absolute;
                top: 10px;
                left: 10px;
                padding: 5px 10px;
                border-radius: 20px;
                font-size: 0.8rem;
                font-weight: 500;
            }
            
            .status-active {
                background-color: #d1e7dd;
                color: #0f5132;
            }
            
            .status-draft {
                background-color: #fff3cd;
                color: #664d03;
            }
            
            .status-pending {
                background-color: #cff4fc;
                color: #055160;
            }
        </style>
    </head>
    <body class="bg-light">
        <jsp:include page="components/header.jsp"></jsp:include>
        
        <div class="container py-5 mt-5">
            <!-- Hiển thị thông báo từ session -->
            <c:if test="${not empty sessionScope.message}">
                <div class="alert alert-${sessionScope.messageType == 'error' ? 'danger' : 'success'} alert-dismissible fade show" role="alert">
                    ${sessionScope.message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <% session.removeAttribute("message"); %>
                <% session.removeAttribute("messageType"); %>
            </c:if>
            
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2>Khóa Học Của Tôi</h2>
                <a href="add-course" class="btn btn-primary">
                    <i class="bi bi-plus-lg"></i> Tạo Khóa Học Mới
                </a>
            </div>
            
            <div class="row g-4">
                <c:forEach items="${courses}" var="course">
                    <div class="col-md-4">
                        <div class="card course-card">
                            <div class="position-relative">
                                <img src="${course.imageURL}" class="course-image w-100" alt="${course.courseName}">
                                <div class="course-overlay"></div>
                                <div class="course-actions">
                                    <a href="edit-course?id=${course.courseID}" class="btn btn-action" title="Chỉnh sửa">
                                        <i class="bi bi-pencil text-primary"></i>
                                    </a>
                                    <a href="course-details?id=${course.courseID}" class="btn btn-action" title="Xem chi tiết">
                                        <i class="bi bi-eye text-info"></i>
                                    </a>
                                    <button type="button" class="btn btn-action" title="Xóa" 
                                            onclick="deleteCourse(${course.courseID}, '${course.courseName}')">
                                        <i class="bi bi-trash text-danger"></i>
                                    </button>
                                </div>
                                <span class="course-level level-${course.levelName}">${course.levelName}</span>
                                <span class="course-status status-${course.status.toLowerCase()}">${course.status}</span>
                            </div>
                            <div class="card-body">
                                <h5 class="card-title mb-3">${course.courseName}</h5>
                                <p class="card-text text-muted mb-3">${course.description}</p>
                                <div class="d-flex justify-content-between align-items-center">
                                    <div class="course-stats">
                                        <i class="bi bi-people"></i> ${course.enrollmentCount} học viên
                                    </div>
                                    <div class="course-price">
                                        <c:choose>
                                            <c:when test="${course.price == 0}">Miễn phí</c:when>
                                            <c:otherwise>${course.price}đ</c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        
        <jsp:include page="components/footer.jsp"></jsp:include>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            function deleteCourse(courseId, courseName) {
                if (confirm('Bạn có chắc chắn muốn xóa khóa học "' + courseName + '"?')) {
                    const form = document.createElement('form');
                    form.method = 'POST';
                    form.action = 'instructor-courses';
                    
                    const actionInput = document.createElement('input');
                    actionInput.type = 'hidden';
                    actionInput.name = 'action';
                    actionInput.value = 'delete';
                    
                    const courseIdInput = document.createElement('input');
                    courseIdInput.type = 'hidden';
                    courseIdInput.name = 'courseId';
                    courseIdInput.value = courseId;
                    
                    form.appendChild(actionInput);
                    form.appendChild(courseIdInput);
                    document.body.appendChild(form);
                    form.submit();
                }
            }
        </script>
    </body>
</html>
