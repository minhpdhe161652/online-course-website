<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm Câu Hỏi - LearnHub</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <jsp:include page="/views/components/header.jsp"></jsp:include>
    
    <div class="container mt-4">
        <!-- Toast container cho thông báo -->
        <div class="toast-container"></div>
        
        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h4 class="card-title">Thêm Câu Hỏi Cho Bài Kiểm Tra</h4>
                            <a href="${pageContext.request.contextPath}/add-course?step=2" class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left"></i> Quay Lại
                            </a>
                        </div>
                        
                        <!-- Hiển thị thông báo -->
                        <c:if test="${not empty message}">
                            <div class="alert alert-${messageType == 'error' ? 'danger' : messageType}">
                                ${message}
                            </div>
                        </c:if>
                        
                        <!-- Thông tin bài kiểm tra -->
                        <div class="card mb-4">
                            <div class="card-header bg-light">
                                <h5 class="mb-0">Thông Tin Bài Kiểm Tra</h5>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <p><strong>Tên bài kiểm tra:</strong> ${quiz.title}</p>
                                        <p><strong>Mô tả:</strong> ${quiz.quizDescription}</p>
                                    </div>
                                    <div class="col-md-6">
                                        <p><strong>Thời gian làm bài:</strong> ${quiz.timeLimit} phút</p>
                                        <p><strong>Điểm đạt:</strong> ${quiz.passingScore}%</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Form thêm câu hỏi -->
                        <div class="card">
                            <div class="card-header bg-light">
                                <h5 class="mb-0">Thêm Câu Hỏi Mới</h5>
                            </div>
                            <div class="card-body">
                                <form action="add-course" method="post">
                                    <input type="hidden" name="step" value="2">
                                    <input type="hidden" name="action" value="add-question">
                                    <input type="hidden" name="quizId" value="${quiz.quizID}">
                                    
                                    <div class="mb-3">
                                        <label for="questionText" class="form-label">Nội dung câu hỏi <span class="text-danger">*</span></label>
                                        <textarea class="form-control" id="questionText" name="questionText" rows="3" required></textarea>
                                        <div class="form-text">Nội dung câu hỏi phải từ 10-1000 ký tự</div>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label for="questionType" class="form-label">Loại câu hỏi</label>
                                        <select class="form-select" id="questionType" name="questionType">
                                            <option value="Multiple Choice" selected>Trắc nghiệm</option>
                                        </select>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label class="form-label">Các lựa chọn <span class="text-danger">*</span></label>
                                        <div class="mb-2">
                                            <div class="input-group">
                                                <div class="input-group-text">
                                                    <input class="form-check-input mt-0" type="radio" name="correctAnswer" value="option1" required>
                                                </div>
                                                <input type="text" class="form-control" name="option1" placeholder="Lựa chọn 1" required>
                                            </div>
                                        </div>
                                        <div class="mb-2">
                                            <div class="input-group">
                                                <div class="input-group-text">
                                                    <input class="form-check-input mt-0" type="radio" name="correctAnswer" value="option2">
                                                </div>
                                                <input type="text" class="form-control" name="option2" placeholder="Lựa chọn 2" required>
                                            </div>
                                        </div>
                                        <div class="mb-2">
                                            <div class="input-group">
                                                <div class="input-group-text">
                                                    <input class="form-check-input mt-0" type="radio" name="correctAnswer" value="option3">
                                                </div>
                                                <input type="text" class="form-control" name="option3" placeholder="Lựa chọn 3" required>
                                            </div>
                                        </div>
                                        <div class="mb-2">
                                            <div class="input-group">
                                                <div class="input-group-text">
                                                    <input class="form-check-input mt-0" type="radio" name="correctAnswer" value="option4">
                                                </div>
                                                <input type="text" class="form-control" name="option4" placeholder="Lựa chọn 4" required>
                                            </div>
                                        </div>
                                        <div class="form-text">Chọn đáp án đúng bằng cách click vào nút radio bên trái</div>
                                    </div>
                                    
                                    <div class="d-flex justify-content-between">
                                        <a href="${pageContext.request.contextPath}/add-course?step=2" class="btn btn-secondary">
                                            Hủy
                                        </a>
                                        <button type="submit" class="btn btn-primary">Thêm Câu Hỏi</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                        
                        <!-- Danh sách câu hỏi hiện có -->
                        <c:if test="${not empty quiz.questions}">
                            <div class="card mt-4">
                                <div class="card-header bg-light">
                                    <h5 class="mb-0">Danh Sách Câu Hỏi Hiện Có (${quiz.questions.size()})</h5>
                                </div>
                                <div class="card-body">
                                    <div class="list-group">
                                        <c:forEach items="${quiz.questions}" var="question" varStatus="status">
                                            <div class="list-group-item">
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <div>
                                                        <h6 class="mb-1">Câu ${status.index + 1}: ${question.questionText}</h6>
                                                        <p class="mb-1"><strong>Đáp án đúng:</strong> ${question.correctAnswer}</p>
                                                        <small>
                                                            <span class="badge bg-primary">${question.questionType}</span>
                                                        </small>
                                                    </div>
                                                    <div>
                                                        <a href="${pageContext.request.contextPath}/quiz?action=edit-question&questionId=${question.questionID}" class="btn btn-sm btn-outline-primary">
                                                            <i class="bi bi-pencil"></i>
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/quiz?action=delete-question&questionId=${question.questionID}" class="btn btn-sm btn-outline-danger" onclick="return confirm('Bạn có chắc chắn muốn xóa câu hỏi này?')">
                                                            <i class="bi bi-trash"></i>
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <jsp:include page="/views/components/footer.jsp"></jsp:include>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
