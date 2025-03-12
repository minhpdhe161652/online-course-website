<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Kết Quả Bài Kiểm Tra | Online Learning</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            .result-container {
                background: #fff;
                border-radius: 15px;
                box-shadow: 0 0 20px rgba(0,0,0,0.1);
                padding: 30px;
                margin: 20px auto;
            }
            
            .result-header {
                text-align: center;
                margin-bottom: 30px;
                padding-bottom: 20px;
                border-bottom: 1px solid #eee;
            }
            
            .score-display {
                text-align: center;
                margin: 30px 0;
                padding: 20px;
                background: #f8f9fa;
                border-radius: 10px;
            }
            
            .score-value {
                font-size: 3rem;
                font-weight: bold;
                color: #2c3e50;
                margin: 10px 0;
            }
            
            .score-label {
                font-size: 1.1rem;
                color: #7f8c8d;
            }
            
            .result-status {
                text-align: center;
                padding: 15px;
                border-radius: 8px;
                margin: 20px 0;
            }
            
            .status-pass {
                background-color: #d4edda;
                color: #155724;
                border: 1px solid #c3e6cb;
            }
            
            .status-fail {
                background-color: #f8d7da;
                color: #721c24;
                border: 1px solid #f5c6cb;
            }
            
            .question-review {
                margin-top: 40px;
            }
            
            .question-item {
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
            
            .answer-option {
                padding: 10px 15px;
                margin: 5px 0;
                border-radius: 5px;
            }
            
            .answer-correct {
                background-color: #d4edda;
                border: 1px solid #c3e6cb;
            }
            
            .answer-incorrect {
                background-color: #f8d7da;
                border: 1px solid #f5c6cb;
            }
            
            .answer-neutral {
                background-color: #f8f9fa;
                border: 1px solid #dee2e6;
            }
            
            .btn-action {
                display: inline-block;
                padding: 12px 30px;
                border-radius: 8px;
                font-weight: 500;
                text-decoration: none;
                transition: all 0.3s;
                border: none;
            }
            
            .btn-primary {
                background-color: #3498db;
                color: white;
            }
            
            .btn-primary:hover {
                background-color: #2980b9;
                transform: translateY(-2px);
            }
            
            .result-summary {
                background: #f8f9fa;
                padding: 20px;
                border-radius: 10px;
                margin: 20px 0;
            }
            
            .summary-item {
                margin: 10px 0;
            }
        </style>
    </head>
    <body class="bg-light">
        <%@include file="../components/navbar.jsp"%>
        
        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="result-container">
                        <div class="result-header">
                            <h2>Kết Quả Bài Kiểm Tra</h2>
                            <p class="text-muted">${quiz.title}</p>
                        </div>
                        
                        <div class="score-display">
                            <div class="score-label">Điểm của bạn</div>
                            <div class="score-value">${score}/${quiz.questions.size()}</div>
                            <div class="score-label">
                                <c:set var="percentage" value="${(score * 100) / quiz.questions.size()}" />
                                (${percentage}%)
                            </div>
                        </div>
                        
                        <div class="result-status ${percentage >= quiz.passingScore ? 'status-pass' : 'status-fail'}">
                            <h4>
                                <c:choose>
                                    <c:when test="${percentage >= quiz.passingScore}">
                                        <i class="bi bi-check-circle-fill me-2"></i>Chúc mừng! Bạn đã đạt
                                    </c:when>
                                    <c:otherwise>
                                        <i class="bi bi-x-circle-fill me-2"></i>Bạn chưa đạt
                                    </c:otherwise>
                                </c:choose>
                            </h4>
                            <p class="mb-0">
                                Điểm đạt tối thiểu: ${quiz.passingScore}%
                            </p>
                        </div>
                        
                        <div class="result-summary">
                            <h4>Tổng Kết</h4>
                            <div class="summary-item">
                                <strong>Thời gian làm bài:</strong> ${quiz.timeLimit} phút
                            </div>
                            <div class="summary-item">
                                <strong>Số câu đúng:</strong> ${score}/${quiz.questions.size()}
                            </div>
                            <div class="summary-item">
                                <strong>Tỷ lệ đúng:</strong> ${percentage}%
                            </div>
                        </div>
                        
                        <div class="text-center mt-4">
                            <a href="course-details?courseId=${quiz.courseID}" class="btn-action btn-primary">
                                <i class="bi bi-arrow-left me-2"></i>Quay Lại Khóa Học
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
