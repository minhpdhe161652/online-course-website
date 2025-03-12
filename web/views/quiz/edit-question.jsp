<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chỉnh Sửa Câu Hỏi | Online Learning</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            .question-form {
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
                padding: 8px 20px;
                border-radius: 8px;
                font-weight: 500;
                transition: all 0.3s;
            }
            
            .btn-primary {
                background-color: #3498db;
            }
            
            .btn-primary:hover {
                background-color: #2980b9;
            }
            
            .btn-secondary {
                background-color: #95a5a6;
            }
            
            .btn-secondary:hover {
                background-color: #7f8c8d;
            }
        </style>
    </head>
    <body class="bg-light">
        <%@include file="../components/navbar.jsp"%>
        
        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="question-form">
                        <h2 class="text-center mb-4">Chỉnh Sửa Câu Hỏi</h2>
                        
                        <form action="edit-question" method="post" class="needs-validation" novalidate>
                            <input type="hidden" name="questionId" value="${question.questionID}">
                            <input type="hidden" name="quizId" value="${quiz.quizID}">
                            
                            <div class="mb-3">
                                <label class="form-label required-field">Câu Hỏi</label>
                                <textarea class="form-control" name="questionText" required>${question.questionText}</textarea>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label required-field">Đáp Án A</label>
                                <input type="text" class="form-control" name="option1" value="${question.option1}" required>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label required-field">Đáp Án B</label>
                                <input type="text" class="form-control" name="option2" value="${question.option2}" required>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label required-field">Đáp Án C</label>
                                <input type="text" class="form-control" name="option3" value="${question.option3}" required>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label required-field">Đáp Án D</label>
                                <input type="text" class="form-control" name="option4" value="${question.option4}" required>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label required-field">Đáp Án Đúng</label>
                                <select class="form-select" name="correctAnswer" required>
                                    <option value="">Chọn đáp án đúng</option>
                                    <option value="A" ${question.correctAnswer == 'A' ? 'selected' : ''}>A</option>
                                    <option value="B" ${question.correctAnswer == 'B' ? 'selected' : ''}>B</option>
                                    <option value="C" ${question.correctAnswer == 'C' ? 'selected' : ''}>C</option>
                                    <option value="D" ${question.correctAnswer == 'D' ? 'selected' : ''}>D</option>
                                </select>
                            </div>
                            
                            <div class="d-flex justify-content-between mt-4">
                                <a href="quiz?action=edit&quizId=${quiz.quizID}" class="btn btn-secondary">
                                    <i class="bi bi-arrow-left me-2"></i>Quay Lại
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-save me-2"></i>Lưu Thay Đổi
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
