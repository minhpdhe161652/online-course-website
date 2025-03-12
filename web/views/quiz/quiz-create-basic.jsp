<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tạo Bài Kiểm Tra | Online Learning</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            .quiz-form {
                background: #fff;
                border-radius: 15px;
                box-shadow: 0 0 20px rgba(0,0,0,0.1);
                padding: 30px;
                margin-top: 20px;
            }
            
            .form-label {
                font-weight: 500;
                color: #2c3e50;
                margin-bottom: 8px;
            }
            
            .form-control {
                border-radius: 8px;
                border: 1px solid #e0e0e0;
                padding: 12px;
                transition: all 0.3s;
            }
            
            .form-control:focus {
                border-color: #3498db;
                box-shadow: 0 0 0 0.2rem rgba(52,152,219,0.25);
            }
            
            .required-field::after {
                content: "*";
                color: #e74c3c;
                margin-left: 4px;
            }
            
            .btn {
                border: none;
                padding: 12px 30px;
                border-radius: 8px;
                font-weight: 500;
                transition: all 0.3s;
            }
            
            .btn:hover {
                transform: translateY(-2px);
            }
            
            .btn-primary {
                background-color: #3498db;
                color: white;
            }
            
            .btn-primary:hover {
                background-color: #2980b9;
            }
            
            .btn-secondary {
                background-color: #95a5a6;
                color: white;
            }
            
            .btn-secondary:hover {
                background-color: #7f8c8d;
            }
            
            .alert {
                border-radius: 8px;
                margin-bottom: 20px;
            }
            
            .form-text {
                color: #7f8c8d;
                font-size: 13px;
                margin-top: 5px;
            }
            
            .error-message {
                color: #e74c3c;
                font-size: 0.875rem;
                margin-top: 5px;
            }
            
            .error-field {
                border-color: #e74c3c;
            }
            
            .error-field:focus {
                border-color: #e74c3c;
                box-shadow: 0 0 0 0.2rem rgba(231,76,60,0.25);
            }
        </style>
    </head>
    <body class="bg-light">
        <%@include file="../components/navbar.jsp"%>
        
        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="quiz-form">
                        <h2 class="text-center mb-4">Tạo Bài Kiểm Tra Mới</h2>
                        
                        <c:if test="${not empty sessionScope.message}">
                            <div class="alert alert-${sessionScope.messageType == 'error' ? 'danger' : 'success'} alert-dismissible fade show" role="alert">
                                ${sessionScope.message}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                            <% session.removeAttribute("message"); %>
                            <% session.removeAttribute("messageType"); %>
                        </c:if>
                        
                        <form action="quiz" method="post">
                            <input type="hidden" name="action" value="create">
                            <input type="hidden" name="courseId" value="${courseId}">
                            
                            <div class="mb-4">
                                <label class="form-label required-field">Tiêu Đề</label>
                                <input type="text" class="form-control ${not empty errors && errors.contains('Tiêu đề không được để trống') ? 'error-field' : ''}" 
                                       name="title" value="${param.title}" maxlength="100" required>
                                <c:if test="${not empty errors}">
                                    <c:forEach items="${errors}" var="error">
                                        <c:if test="${error.contains('Tiêu đề')}">
                                            <div class="error-message">${error}</div>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                                <div class="form-text">Đặt tiêu đề ngắn gọn và dễ hiểu</div>
                            </div>
                            
                            <div class="mb-4">
                                <label class="form-label required-field">Mô Tả</label>
                                <textarea class="form-control ${not empty errors && errors.contains('Mô tả không được để trống') ? 'error-field' : ''}" 
                                          name="description" rows="4" required>${param.description}</textarea>
                                <c:if test="${not empty errors}">
                                    <c:forEach items="${errors}" var="error">
                                        <c:if test="${error.contains('Mô tả')}">
                                            <div class="error-message">${error}</div>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                                <div class="form-text">Mô tả rõ mục tiêu và nội dung của bài kiểm tra</div>
                            </div>
                            
                            <div class="row mb-4">
                                <div class="col-md-6">
                                    <label class="form-label required-field">Thời Gian Làm Bài (phút)</label>
                                    <input type="number" class="form-control ${not empty errors && errors.contains('Thời gian làm bài') ? 'error-field' : ''}" 
                                           name="timeLimit" value="${empty param.timeLimit ? '30' : param.timeLimit}" 
                                           min="1" max="180" required>
                                    <c:if test="${not empty errors}">
                                        <c:forEach items="${errors}" var="error">
                                            <c:if test="${error.contains('Thời gian')}">
                                                <div class="error-message">${error}</div>
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                    <div class="form-text">Thời gian từ 1 đến 180 phút</div>
                                </div>
                                
                                <div class="col-md-6">
                                    <label class="form-label required-field">Điểm Đạt Tối Thiểu (%)</label>
                                    <input type="number" class="form-control ${not empty errors && errors.contains('Điểm đạt') ? 'error-field' : ''}" 
                                           name="passingScore" value="${empty param.passingScore ? '70' : param.passingScore}" 
                                           min="0" max="100" required>
                                    <c:if test="${not empty errors}">
                                        <c:forEach items="${errors}" var="error">
                                            <c:if test="${error.contains('Điểm đạt')}">
                                                <div class="error-message">${error}</div>
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                    <div class="form-text">Nhập số từ 0 đến 100</div>
                                </div>
                            </div>
                            
                            <div class="text-center mt-5">
                                <a href="course-details?courseId=${courseId}" class="btn btn-secondary me-2">
                                    <i class="bi bi-arrow-left me-2"></i>Quay Lại
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    Tiếp Tục<i class="bi bi-arrow-right ms-2"></i>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
