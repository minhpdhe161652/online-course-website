<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chỉnh Sửa Bài Kiểm Tra | Online Learning</title>
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
                padding: 8px 20px;
                border-radius: 8px;
                font-weight: 500;
                transition: all 0.3s;
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
            
            .btn-secondary {
                background-color: #95a5a6;
            }
            
            .btn-secondary:hover {
                background-color: #7f8c8d;
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
            
            .alert {
                border-radius: 8px;
                margin-bottom: 20px;
            }
            
            .form-text {
                color: #7f8c8d;
                font-size: 13px;
                margin-top: 5px;
            }
            
            .question-list {
                margin-top: 40px;
            }
            
            .question-item {
                background: #f8f9fa;
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
                font-weight: 500;
                color: #2c3e50;
                margin-bottom: 15px;
            }
            
            .option-list {
                list-style: none;
                padding: 0;
                margin: 0;
            }
            
            .option-item {
                padding: 8px 15px;
                margin-bottom: 8px;
                border-radius: 5px;
                background: white;
                border: 1px solid #e0e0e0;
            }
            
            .option-item.correct {
                background: #2ecc71;
                color: white;
                border-color: #27ae60;
            }
            
            .question-actions {
                margin-top: 15px;
                display: flex;
                gap: 10px;
            }
            
            .add-question-form {
                background: #f8f9fa;
                border-radius: 10px;
                padding: 20px;
                margin-top: 30px;
            }
        </style>
    </head>
    <body class="bg-light">
        <%@include file="../components/navbar.jsp"%>
        
        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="quiz-form">
                        <h2 class="text-center mb-4">Chỉnh Sửa Bài Kiểm Tra</h2>
                        
                        <c:if test="${not empty message}">
                            <div class="alert alert-${messageType == 'error' ? 'danger' : 'success'} alert-dismissible fade show" role="alert">
                                ${message}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <!-- Form chỉnh sửa thông tin quiz -->
                        <form action="quiz" method="post" class="needs-validation" novalidate>
                            <input type="hidden" name="action" value="edit">
                            <input type="hidden" name="quizId" value="${quiz.quizID}">
                            <input type="hidden" name="courseId" value="${quiz.courseID}">
                            <input type="hidden" name="sectionId" value="${quiz.sectionID}">
                            
                            <div class="mb-4">
                                <label class="form-label required-field">Tiêu Đề</label>
                                <input type="text" class="form-control" name="title" required 
                                       value="${quiz.title}" placeholder="Nhập tiêu đề bài kiểm tra">
                            </div>
                            
                            <div class="mb-4">
                                <label class="form-label required-field">Mô Tả</label>
                                <textarea class="form-control" name="description" rows="4" required 
                                          placeholder="Mô tả về bài kiểm tra">${quiz.description}</textarea>
                            </div>
                            
                            <div class="row mb-4">
                                <div class="col-md-6">
                                    <label class="form-label required-field">Thời Gian Làm Bài (phút)</label>
                                    <input type="number" class="form-control" name="timeLimit" required 
                                           min="1" value="${quiz.timeLimit}" placeholder="VD: 30">
                                </div>
                                
                                <div class="col-md-6">
                                    <label class="form-label required-field">Điểm Đạt Tối Thiểu (%)</label>
                                    <input type="number" class="form-control" name="passingScore" required 
                                           min="0" max="100" value="${quiz.passingScore}" placeholder="VD: 80">
                                </div>
                            </div>
                            
                            <div class="text-center">
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-save me-2"></i>Lưu Thay Đổi
                                </button>
                            </div>
                        </form>
                        
                        <!-- Danh sách câu hỏi -->
                        <div class="question-list">
                            <h3 class="mb-4">Danh Sách Câu Hỏi</h3>
                            
                            <c:forEach items="${quiz.questions}" var="question" varStatus="status">
                                <div class="question-item">
                                    <div class="question-number">${status.index + 1}</div>
                                    <div class="question-text">${question.questionText}</div>
                                    
                                    <ul class="option-list">
                                        <li class="option-item ${question.correctAnswer == 'A' ? 'correct' : ''}">
                                            A. ${question.option1}
                                        </li>
                                        <li class="option-item ${question.correctAnswer == 'B' ? 'correct' : ''}">
                                            B. ${question.option2}
                                        </li>
                                        <li class="option-item ${question.correctAnswer == 'C' ? 'correct' : ''}">
                                            C. ${question.option3}
                                        </li>
                                        <li class="option-item ${question.correctAnswer == 'D' ? 'correct' : ''}">
                                            D. ${question.option4}
                                        </li>
                                    </ul>
                                    
                                    <div class="question-actions">
                                        <button type="button" class="btn btn-primary btn-sm" 
                                                onclick="editQuestion(${question.questionID})">
                                            <i class="bi bi-pencil me-1"></i>Sửa
                                        </button>
                                        <button type="button" class="btn btn-danger btn-sm" 
                                                onclick="deleteQuestion(${question.questionID})">
                                            <i class="bi bi-trash me-1"></i>Xóa
                                        </button>
                                    </div>
                                </div>
                            </c:forEach>
                            
                            <!-- Form thêm câu hỏi mới -->
                            <div class="add-question-form">
                                <h4 class="mb-4">Thêm Câu Hỏi Mới</h4>
                                <form action="quiz" method="post" class="needs-validation" novalidate>
                                    <input type="hidden" name="action" value="add-question">
                                    <input type="hidden" name="quizId" value="${quiz.quizID}">
                                    
                                    <div class="mb-3">
                                        <label class="form-label required-field">Câu Hỏi</label>
                                        <textarea class="form-control" name="questionText" required 
                                                  placeholder="Nhập nội dung câu hỏi"></textarea>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label class="form-label required-field">Đáp Án A</label>
                                        <input type="text" class="form-control" name="option1" required>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label class="form-label required-field">Đáp Án B</label>
                                        <input type="text" class="form-control" name="option2" required>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label class="form-label required-field">Đáp Án C</label>
                                        <input type="text" class="form-control" name="option3" required>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label class="form-label required-field">Đáp Án D</label>
                                        <input type="text" class="form-control" name="option4" required>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label class="form-label required-field">Đáp Án Đúng</label>
                                        <select class="form-select" name="correctAnswer" required>
                                            <option value="">Chọn đáp án đúng</option>
                                            <option value="A">A</option>
                                            <option value="B">B</option>
                                            <option value="C">C</option>
                                            <option value="D">D</option>
                                        </select>
                                    </div>
                                    
                                    <div class="text-center">
                                        <button type="submit" class="btn btn-success">
                                            <i class="bi bi-plus-circle me-2"></i>Thêm Câu Hỏi
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Modal chỉnh sửa câu hỏi -->
        <div class="modal fade" id="editQuestionModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Chỉnh Sửa Câu Hỏi</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form action="quiz" method="post" id="editQuestionForm" class="needs-validation" novalidate>
                            <input type="hidden" name="action" value="edit-question">
                            <input type="hidden" name="quizId" value="${quiz.quizID}">
                            <input type="hidden" name="questionId" id="editQuestionId">
                            
                            <div class="mb-3">
                                <label class="form-label required-field">Câu Hỏi</label>
                                <textarea class="form-control" name="questionText" id="editQuestionText" required></textarea>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label required-field">Đáp Án A</label>
                                <input type="text" class="form-control" name="option1" id="editOption1" required>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label required-field">Đáp Án B</label>
                                <input type="text" class="form-control" name="option2" id="editOption2" required>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label required-field">Đáp Án C</label>
                                <input type="text" class="form-control" name="option3" id="editOption3" required>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label required-field">Đáp Án D</label>
                                <input type="text" class="form-control" name="option4" id="editOption4" required>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label required-field">Đáp Án Đúng</label>
                                <select class="form-select" name="correctAnswer" id="editCorrectAnswer" required>
                                    <option value="">Chọn đáp án đúng</option>
                                    <option value="A">A</option>
                                    <option value="B">B</option>
                                    <option value="C">C</option>
                                    <option value="D">D</option>
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" form="editQuestionForm" class="btn btn-primary">Lưu Thay Đổi</button>
                    </div>
                </div>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Form validation
            (function () {
                'use strict'
                var forms = document.querySelectorAll('.needs-validation')
                Array.prototype.slice.call(forms).forEach(function (form) {
                    form.addEventListener('submit', function (event) {
                        if (!form.checkValidity()) {
                            event.preventDefault()
                            event.stopPropagation()
                        }
                        form.classList.add('was-validated')
                    }, false)
                })
            })()
            
            // Xử lý sửa câu hỏi
            function editQuestion(questionId) {
                // Gọi API để lấy thông tin câu hỏi
                fetch('quiz?action=get-question&questionId=' + questionId)
                    .then(response => response.json())
                    .then(question => {
                        document.getElementById('editQuestionId').value = question.questionID
                        document.getElementById('editQuestionText').value = question.questionText
                        document.getElementById('editOption1').value = question.option1
                        document.getElementById('editOption2').value = question.option2
                        document.getElementById('editOption3').value = question.option3
                        document.getElementById('editOption4').value = question.option4
                        document.getElementById('editCorrectAnswer').value = question.correctAnswer
                        
                        // Hiển thị modal
                        new bootstrap.Modal(document.getElementById('editQuestionModal')).show()
                    })
                    .catch(error => console.error('Error:', error))
            }
            
            // Xử lý xóa câu hỏi
            function deleteQuestion(questionId) {
                if (confirm('Bạn có chắc chắn muốn xóa câu hỏi này?')) {
                    const form = document.createElement('form')
                    form.method = 'POST'
                    form.action = 'quiz'
                    
                    const actionInput = document.createElement('input')
                    actionInput.type = 'hidden'
                    actionInput.name = 'action'
                    actionInput.value = 'delete-question'
                    
                    const quizIdInput = document.createElement('input')
                    quizIdInput.type = 'hidden'
                    quizIdInput.name = 'quizId'
                    quizIdInput.value = ${quiz.quizID}
                    
                    const questionIdInput = document.createElement('input')
                    questionIdInput.type = 'hidden'
                    questionIdInput.name = 'questionId'
                    questionIdInput.value = questionId
                    
                    form.appendChild(actionInput)
                    form.appendChild(quizIdInput)
                    form.appendChild(questionIdInput)
                    
                    document.body.appendChild(form)
                    form.submit()
                }
            }
        </script>
    </body>
</html>
