<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Làm Bài Kiểm Tra | Online Learning</title>
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
            }
            
            .question-text {
                font-size: 1.1rem;
                color: #2c3e50;
                margin-bottom: 15px;
            }
            
            .options-list {
                list-style: none;
                padding: 0;
            }
            
            .option-item {
                margin-bottom: 10px;
            }
            
            .option-label {
                display: block;
                padding: 10px 15px;
                background: #f8f9fa;
                border: 1px solid #dee2e6;
                border-radius: 5px;
                cursor: pointer;
                transition: all 0.3s;
            }
            
            .option-label:hover {
                background: #e9ecef;
            }
            
            .quiz-controls {
                text-align: center;
                margin-top: 30px;
                padding-top: 20px;
                border-top: 1px solid #eee;
            }
            
            .btn-submit {
                background: #2ecc71;
                color: white;
                padding: 12px 30px;
                border: none;
                border-radius: 8px;
                font-weight: 500;
                transition: all 0.3s;
            }
            
            .btn-submit:hover {
                background: #27ae60;
                transform: translateY(-2px);
            }
            
            .timer {
                position: fixed;
                top: 20px;
                right: 20px;
                background: #fff;
                padding: 15px;
                border-radius: 10px;
                box-shadow: 0 0 15px rgba(0,0,0,0.1);
                text-align: center;
            }
            
            .timer-value {
                font-size: 1.5rem;
                font-weight: bold;
                color: #2c3e50;
            }
            
            .timer-label {
                font-size: 0.9rem;
                color: #7f8c8d;
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
                        
                        <form action="quiz" method="post" class="quiz-form">
                            <input type="hidden" name="action" value="submit-quiz">
                            <input type="hidden" name="quizId" value="${quiz.quizID}">
                            
                            <c:forEach items="${quiz.questions}" var="question" varStatus="status">
                                <div class="question-container">
                                    <p class="question-text">
                                        <strong>Câu ${status.index + 1}:</strong> ${question.questionText}
                                    </p>
                                    
                                    <ul class="options-list">
                                        <li class="option-item">
                                            <label class="option-label">
                                                <input type="radio" name="question-${question.questionID}" value="1" required>
                                                ${question.option1}
                                            </label>
                                        </li>
                                        <li class="option-item">
                                            <label class="option-label">
                                                <input type="radio" name="question-${question.questionID}" value="2" required>
                                                ${question.option2}
                                            </label>
                                        </li>
                                        <li class="option-item">
                                            <label class="option-label">
                                                <input type="radio" name="question-${question.questionID}" value="3" required>
                                                ${question.option3}
                                            </label>
                                        </li>
                                        <li class="option-item">
                                            <label class="option-label">
                                                <input type="radio" name="question-${question.questionID}" value="4" required>
                                                ${question.option4}
                                            </label>
                                        </li>
                                    </ul>
                                </div>
                            </c:forEach>
                            
                            <div class="quiz-controls">
                                <button type="submit" class="btn btn-submit">
                                    <i class="bi bi-check-circle me-2"></i>Nộp Bài
                                </button>
                            </div>
                        </form>
                        
                        <div class="timer">
                            <div class="timer-value">${quiz.timeLimit}:00</div>
                            <div class="timer-label">Thời gian còn lại</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Không sử dụng JavaScript cho timer, thay vào đó sử dụng server-side timing -->
        <%
            // Lưu thời điểm bắt đầu làm bài vào session
            session.setAttribute("quizStartTime", System.currentTimeMillis());
        %>
    </body>
</html>
