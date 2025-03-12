<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản Lý Câu Hỏi | Online Learning</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            .quiz-container {
                background: #fff;
                border-radius: 15px;
                box-shadow: 0 0 20px rgba(0,0,0,0.1);
                padding: 30px;
                margin: 20px auto;
            }
            
            .quiz-header {
                text-align: center;
                margin-bottom: 30px;
                padding-bottom: 20px;
                border-bottom: 1px solid #eee;
            }
            
            .quiz-info {
                background: #f8f9fa;
                padding: 15px;
                border-radius: 10px;
                margin-bottom: 30px;
            }
            
            .question-container {
                background: #fff;
                border: 1px solid #dee2e6;
                border-radius: 10px;
                padding: 20px;
                margin-bottom: 20px;
                position: relative;
            }
            
            .question-number {
                position: absolute;
                top: -10px;
                left: -10px;
                width: 30px;
                height: 30px;
                background: #3498db;
                color: white;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: bold;
            }
            
            .question-text {
                font-size: 1.1rem;
                color: #2c3e50;
                margin-bottom: 15px;
                padding-left: 20px;
            }
            
            .options-list {
                list-style: none;
                padding: 0;
                margin: 0 0 15px 0;
            }
            
            .option-item {
                padding: 8px 15px;
                margin-bottom: 8px;
                border-radius: 5px;
                background: #f8f9fa;
                border: 1px solid #dee2e6;
            }
            
            .option-item.correct {
                background: #d4edda;
                border-color: #c3e6cb;
                color: #155724;
            }
            
            .question-actions {
                display: flex;
                gap: 10px;
                justify-content: flex-end;
            }
            
            .btn {
                border: none;
                padding: 8px 20px;
                border-radius: 8px;
                font-weight: 500;
                transition: all 0.3s;
                text-decoration: none;
                display: inline-flex;
                align-items: center;
                color: white;
            }
            
            .btn:hover {
                transform: translateY(-2px);
            }
            
            .btn-primary {
                background-color: #3498db;
            }
            
            .btn-primary:hover {
                background-color: #2980b9;
            }
            
            .btn-success {
                background-color: #2ecc71;
            }
            
            .btn-success:hover {
                background-color: #27ae60;
            }
            
            .btn-danger {
                background-color: #e74c3c;
            }
            
            .btn-danger:hover {
                background-color: #c0392b;
            }
            
            .add-question-form {
                background: #f8f9fa;
                border-radius: 10px;
                padding: 20px;
                margin-top: 30px;
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
                    <div class="quiz-container">
                        <div class="quiz-header">
                            <h2>${quiz.title}</h2>
                            <p class="text-muted">${quiz.description}</p>
                        </div>
                        
                        <div class="quiz-info">
                            <div class="row">
                                <div class="col-md-6">
                                    <p><strong>Thời gian:</strong> ${quiz.timeLimit} phút</p>
                                    <p><strong>Số câu hỏi:</strong> ${quiz.questions.size()}</p>
                                </div>
                                <div class="col-md-6">
                                    <p><strong>Điểm đạt:</strong> ${quiz.passingScore}%</p>
                                    <p><strong>Tổng điểm:</strong> ${quiz.questions.size()} điểm</p>
                                </div>
                            </div>
                        </div>
                        
                        <c:if test="${not empty sessionScope.message}">
                            <div class="alert alert-${sessionScope.messageType == 'error' ? 'danger' : 'success'} alert-dismissible fade show" role="alert">
                                ${sessionScope.message}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                            <% session.removeAttribute("message"); %>
                            <% session.removeAttribute("messageType"); %>
                        </c:if>
                        
                        <div class="question-list">
                            <h3 class="mb-4">Danh Sách Câu Hỏi</h3>
                            
                            <c:forEach items="${quiz.questions}" var="question" varStatus="status">
                                <div class="question-container">
                                    <div class="question-number">${status.index + 1}</div>
                                    
                                    <p class="question-text">${question.questionText}</p>
                                    
                                    <ul class="options-list">
                                        <li class="option-item ${question.correctAnswer == '1' ? 'correct' : ''}">
                                            A. ${question.option1}
                                        </li>
                                        <li class="option-item ${question.correctAnswer == '2' ? 'correct' : ''}">
                                            B. ${question.option2}
                                        </li>
                                        <li class="option-item ${question.correctAnswer == '3' ? 'correct' : ''}">
                                            C. ${question.option3}
                                        </li>
                                        <li class="option-item ${question.correctAnswer == '4' ? 'correct' : ''}">
                                            D. ${question.option4}
                                        </li>
                                    </ul>
                                    
                                    <div class="question-actions">
                                        <form action="quiz" method="post" style="display: inline;">
                                            <input type="hidden" name="action" value="delete-question">
                                            <input type="hidden" name="questionId" value="${question.questionID}">
                                            <input type="hidden" name="quizId" value="${quiz.quizID}">
                                            <button type="submit" class="btn btn-danger" onclick="return confirm('Bạn có chắc muốn xóa câu hỏi này?');">
                                                <i class="bi bi-trash me-2"></i>Xóa
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </c:forEach>
                            
                            <div class="add-question-form">
                                <h4 class="mb-4">Thêm Câu Hỏi Mới</h4>
                                
                                <form action="quiz" method="post">
                                    <input type="hidden" name="action" value="add-question">
                                    <input type="hidden" name="quizId" value="${quiz.quizID}">
                                    
                                    <div class="mb-4">
                                        <label class="form-label required-field">Câu Hỏi</label>
                                        <textarea class="form-control ${not empty errors && errors.contains('Câu hỏi không được để trống') ? 'error-field' : ''}" 
                                                  name="questionText" rows="3" required>${param.questionText}</textarea>
                                        <c:if test="${not empty errors}">
                                            <c:forEach items="${errors}" var="error">
                                                <c:if test="${error.contains('Câu hỏi')}">
                                                    <div class="error-message">${error}</div>
                                                </c:if>
                                            </c:forEach>
                                        </c:if>
                                    </div>
                                    
                                    <div class="row mb-4">
                                        <div class="col-md-6">
                                            <label class="form-label required-field">Đáp án A</label>
                                            <input type="text" class="form-control" name="option1" value="${param.option1}" required>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label required-field">Đáp án B</label>
                                            <input type="text" class="form-control" name="option2" value="${param.option2}" required>
                                        </div>
                                    </div>
                                    
                                    <div class="row mb-4">
                                        <div class="col-md-6">
                                            <label class="form-label required-field">Đáp án C</label>
                                            <input type="text" class="form-control" name="option3" value="${param.option3}" required>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label required-field">Đáp án D</label>
                                            <input type="text" class="form-control" name="option4" value="${param.option4}" required>
                                        </div>
                                    </div>
                                    
                                    <div class="mb-4">
                                        <label class="form-label required-field">Đáp Án Đúng</label>
                                        <select class="form-control" name="correctAnswer" required>
                                            <option value="">Chọn đáp án đúng</option>
                                            <option value="1" ${param.correctAnswer == '1' ? 'selected' : ''}>A</option>
                                            <option value="2" ${param.correctAnswer == '2' ? 'selected' : ''}>B</option>
                                            <option value="3" ${param.correctAnswer == '3' ? 'selected' : ''}>C</option>
                                            <option value="4" ${param.correctAnswer == '4' ? 'selected' : ''}>D</option>
                                        </select>
                                    </div>
                                    
                                    <div class="text-end">
                                        <button type="submit" class="btn btn-success">
                                            <i class="bi bi-plus-circle me-2"></i>Thêm Câu Hỏi
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                        
                        <div class="text-center mt-5">
                            <a href="course-details?courseId=${quiz.courseID}" class="btn btn-primary">
                                <i class="bi bi-check-circle me-2"></i>Hoàn Thành
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
