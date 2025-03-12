<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="newLineChar" value="\n"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm Khóa Học - Chi Tiết</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .chapter-number {
            width: 35px;
            text-align: center;
        }
        .chapter-number .badge {
            width: 30px;
            height: 30px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
        }
        .chapter-item {
            transition: all 0.3s ease;
            border-left: 3px solid transparent;
            cursor: default;
        }
        .chapter-item:hover {
            background-color: #f8f9fa;
        }
        .chapter-item.active {
            border-left-color: #0d6efd;
            background-color: #f8f9fa;
        }
        .chapter-actions {
            visibility: hidden;
            opacity: 0;
            transition: all 0.2s ease;
        }
        .chapter-item:hover .chapter-actions {
            visibility: visible;
            opacity: 1;
        }
        .drag-handle {
            cursor: move;
            padding: 5px;
            margin: -5px;
        }
        .chapter-item.sortable-ghost {
            opacity: 0.5;
            background-color: #e9ecef;
        }
        .toast-container {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 9999;
            min-width: 250px;
        }
        .alert {
            margin-bottom: 10px;
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
        }
        .alert-dismissible {
            padding: 1rem;
            margin-bottom: 1rem;
        }
        .btn-close {
            position: absolute;
            right: 0;
            top: 0;
            padding: 1.25rem 1rem;
        }
    </style>
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
                        <h4 class="card-title">Thêm Chi Tiết Khóa Học</h4>
                        
                        <!-- Hiển thị thông báo -->
                        <c:if test="${not empty message}">
                            <div class="alert alert-${messageType == 'error' ? 'danger' : 'success'} alert-dismissible fade show" role="alert">
                                ${message}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        
                        <!-- Hiển thị thông báo từ session -->
                        <c:if test="${not empty sessionScope.message}">
                            <div class="alert alert-${sessionScope.messageType == 'error' ? 'danger' : 'success'} alert-dismissible fade show" role="alert">
                                ${sessionScope.message}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                            <% session.removeAttribute("message"); %>
                            <% session.removeAttribute("messageType"); %>
                        </c:if>
                        
                        <!-- Tab Navigation -->
                        <ul class="nav nav-tabs" role="tablist">
                            <li class="nav-item">
                                <a class="nav-link ${empty param.action || (param.action ne 'add-question' && param.action ne 'add-quiz') ? 'active' : ''}" data-bs-toggle="tab" href="#chapters">Chương</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${param.action eq 'add-question' || param.action eq 'add-quiz' ? 'active' : ''}" data-bs-toggle="tab" href="#quiz">Bài Kiểm Tra</a>
                            </li>
                        </ul>
                        
                        <!-- Tab Content -->
                        <div class="tab-content mt-3">
                            <!-- Tab Chương -->
                            <div class="tab-pane fade ${empty param.action || (param.action ne 'add-question' && param.action ne 'add-quiz') ? 'show active' : ''}" id="chapters">
                                <div class="row">
                                    <div class="col-md-4">
                                        <h5>Danh Sách Chương</h5>
                                        <div class="list-group chapters-list" id="chaptersList">
                                            <c:forEach items="${chapters}" var="chapter" varStatus="status">
                                                <div class="list-group-item chapter-item" data-chapter-id="${chapter.chapterID}">
                                                    <div class="d-flex justify-content-between align-items-center">
                                                        <div class="d-flex align-items-center">
                                                            <div class="me-3 chapter-number">
                                                                <span class="badge bg-secondary rounded-circle p-2">
                                                                    ${status.index + 1}
                                                                </span>
                                                            </div>
                                                            <div>
                                                                <div class="fw-bold">${chapter.chapterName}</div>
                                                                <small class="text-muted">
                                                                    <i class="bi bi-journal-text"></i>
                                                                    ${chapter.lessons.size()} bài học
                                                                </small>
                                                            </div>
                                                        </div>
                                                        <div class="chapter-actions">
                                                            <!-- Nút di chuyển lên -->
                                                            <c:if test="${not status.first}">
                                                                <form action="add-course" method="post" style="display: inline;">
                                                                    <input type="hidden" name="step" value="2">
                                                                    <input type="hidden" name="action" value="moveChapter">
                                                                    <input type="hidden" name="chapterId" value="${chapter.chapterID}">
                                                                    <input type="hidden" name="direction" value="up">
                                                                    <button type="submit" class="btn btn-sm btn-outline-secondary me-1">
                                                                        <i class="bi bi-arrow-up"></i>
                                                                    </button>
                                                                </form>
                                                            </c:if>
                                                            
                                                            <!-- Nút di chuyển xuống -->
                                                            <c:if test="${not status.last}">
                                                                <form action="add-course" method="post" style="display: inline;">
                                                                    <input type="hidden" name="step" value="2">
                                                                    <input type="hidden" name="action" value="moveChapter">
                                                                    <input type="hidden" name="chapterId" value="${chapter.chapterID}">
                                                                    <input type="hidden" name="direction" value="down">
                                                                    <button type="submit" class="btn btn-sm btn-outline-secondary me-1">
                                                                        <i class="bi bi-arrow-down"></i>
                                                                    </button>
                                                                </form>
                                                            </c:if>
                                                            
                                                            <button type="button" class="btn btn-sm btn-outline-primary me-1" 
                                                                    onclick="showChapterDetail(${chapter.chapterID})">
                                                                <i class="bi bi-eye"></i>
                                                            </button>
                                                            <button type="button" class="btn btn-sm btn-outline-primary me-1" 
                                                                    onclick="editChapter(${chapter.chapterID})">
                                                                <i class="bi bi-pencil"></i>
                                                            </button>
                                                            <button type="button" class="btn btn-sm btn-outline-danger" 
                                                                    onclick="deleteChapter(${chapter.chapterID})">
                                                                <i class="bi bi-trash"></i>
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                        <button type="button" class="btn btn-primary mt-3" data-bs-toggle="modal" data-bs-target="#addChapterModal">
                                            <i class="bi bi-plus"></i> Thêm Chương
                                        </button>
                                    </div>
                                    
                                    <div class="col-md-8">
                                        <h5>Chi Tiết Chương</h5>
                                        <div id="chapterDetails">
                                            <c:choose>
                                                <c:when test="${not empty viewingLesson}">
                                                    <!-- Hiển thị chi tiết bài giảng -->
                                                    <div class="card">
                                                        <div class="card-body">
                                                            <h5 class="card-title">${viewingLesson.lessonName}</h5>
                                                            <p class="text-muted">${viewingLesson.description}</p>
                                                            
                                                            <c:if test="${not empty viewingLesson.videoURL}">
                                                                <div class="mb-4">
                                                                    <h6>Video bài giảng:</h6>
                                                                    <a href="${viewingLesson.videoURL}" target="_blank" class="btn btn-outline-primary">
                                                                        <i class="bi bi-play-circle"></i> Xem video
                                                                    </a>
                                                                </div>
                                                            </c:if>
                                                            
                                                            <form action="add-course" method="post">
                                                                <input type="hidden" name="step" value="2">
                                                                <input type="hidden" name="action" value="closeLesson">
                                                                <button type="submit" class="btn btn-secondary">
                                                                    <i class="bi bi-arrow-left"></i> Quay lại
                                                                </button>
                                                            </form>
                                                        </div>
                                                    </div>
                                                </c:when>
                                                <c:when test="${not empty chapters}">
                                                    <c:forEach items="${chapters}" var="chapter" varStatus="status">
                                                        <div class="card mb-3 chapter-detail" id="chapter-${chapter.chapterID}" 
                                                             style="${status.first ? '' : 'display: none;'}">
                                                            <div class="card-body">
                                                                <div class="d-flex justify-content-between align-items-center mb-3">
                                                                    <h5 class="card-title mb-0">${chapter.chapterName}</h5>
                                                                    <button type="button" class="btn btn-primary btn-sm" 
                                                                            onclick="showAddLessonModal(${chapter.chapterID})">
                                                                        <i class="bi bi-plus"></i> Thêm bài giảng
                                                                    </button>
                                                                </div>
                                                                <p class="text-muted">${chapter.description}</p>
                                                                
                                                                <h6 class="mt-4 mb-3">Danh sách bài giảng</h6>
                                                                <div class="list-group">
                                                                    <c:choose>
                                                                        <c:when test="${not empty chapter.lessons}">
                                                                            <c:forEach items="${chapter.lessons}" var="lesson">
                                                                                <div class="list-group-item">
                                                                                    <div class="d-flex justify-content-between align-items-center">
                                                                                        <div>
                                                                                            <h6 class="mb-1">${lesson.lessonName}</h6>
                                                                                            <p class="text-muted mb-1">${lesson.description}</p>
                                                                                            <small class="text-muted">
                                                                                                <c:if test="${not empty lesson.videoURL}">
                                                                                                    <i class="bi bi-play-circle"></i> 
                                                                                                    <a href="${lesson.videoURL}" target="_blank">Xem video</a>
                                                                                                </c:if>
                                                                                            </small>
                                                                                        </div>
                                                                                        <div class="btn-group">
                                                                                            <form action="add-course" method="post" style="display: inline;">
                                                                                                <input type="hidden" name="step" value="2">
                                                                                                <input type="hidden" name="action" value="viewLesson">
                                                                                                <input type="hidden" name="lessonId" value="${lesson.lessonID}">
                                                                                                <button type="submit" class="btn btn-sm btn-outline-primary me-1">
                                                                                                    <i class="bi bi-eye"></i> Xem
                                                                                                </button>
                                                                                            </form>
                                                                                            <form action="add-course" method="post" style="display: inline;">
                                                                                                <input type="hidden" name="step" value="2">
                                                                                                <input type="hidden" name="action" value="editLesson">
                                                                                                <input type="hidden" name="lessonId" value="${lesson.lessonID}">
                                                                                                <button type="submit" class="btn btn-sm btn-outline-primary me-1">
                                                                                                    <i class="bi bi-pencil"></i> Sửa
                                                                                                </button>
                                                                                            </form>
                                                                                            <form action="add-course" method="post" style="display: inline;"
                                                                                                  onsubmit="return confirm('Bạn có chắc chắn muốn xóa bài giảng này?');">
                                                                                                <input type="hidden" name="step" value="2">
                                                                                                <input type="hidden" name="action" value="deleteLesson">
                                                                                                <input type="hidden" name="lessonId" value="${lesson.lessonID}">
                                                                                                <button type="submit" class="btn btn-sm btn-outline-danger">
                                                                                                    <i class="bi bi-trash"></i> Xóa
                                                                                                </button>
                                                                                            </form>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </c:forEach>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <div class="text-center text-muted py-3">
                                                                                <i class="bi bi-info-circle"></i>
                                                                                <p class="mb-0">Chưa có bài giảng nào trong chương này</p>
                                                                            </div>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="text-center text-muted py-5">
                                                        <i class="bi bi-info-circle fs-1"></i>
                                                        <p class="mt-2">Chưa có chương nào. Vui lòng thêm chương mới.</p>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Tab Bài Kiểm Tra -->
                            <div class="tab-pane fade ${param.action eq 'add-question' || param.action eq 'add-quiz' ? 'show active' : ''}" id="quiz">
                                <div class="row">
                                    <div class="col-md-12 mb-3">
                                        <div class="d-flex justify-content-between align-items-center">
                                            <h5>Bài Kiểm Tra</h5>
                                        </div>
                                    </div>
                                    
                                    <div class="col-md-12">
                                        <c:choose>
                                            <c:when test="${not empty quiz}">
                                                <!-- Hiển thị thông tin bài kiểm tra -->
                                                <div class="card">
                                                    <div class="card-header d-flex justify-content-between align-items-center">
                                                        <h5 class="card-title mb-0">${quiz.title}</h5>
                                                        <div>
                                                            <a href="${pageContext.request.contextPath}/add-course?step=2&action=add-question&quizId=${quiz.quizID}" class="btn btn-success btn-sm">
                                                                <i class="bi bi-plus"></i> Thêm Câu Hỏi
                                                            </a>
                                                            <a href="${pageContext.request.contextPath}/quiz?action=edit&quizId=${quiz.quizID}&courseId=${course.courseID}" class="btn btn-primary btn-sm">
                                                                <i class="bi bi-pencil"></i> Chỉnh Sửa
                                                            </a>
                                                            <button type="button" class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#deleteQuizModal">
                                                                <i class="bi bi-trash"></i> Xóa
                                                            </button>
                                                        </div>
                                                    </div>
                                                    <div class="card-body">
                                                        <div class="row mb-3">
                                                            <div class="col-md-12">
                                                                <h6>Mô tả:</h6>
                                                                <p>${quiz.quizDescription}</p>
                                                            </div>
                                                        </div>
                                                        <div class="row mb-3">
                                                            <div class="col-md-4">
                                                                <h6>Thời gian làm bài:</h6>
                                                                <p>${quiz.timeLimit} phút</p>
                                                            </div>
                                                            <div class="col-md-4">
                                                                <h6>Điểm đạt:</h6>
                                                                <p>${quiz.passingScore}%</p>
                                                            </div>
                                                            <div class="col-md-4">
                                                                <h6>Số câu hỏi:</h6>
                                                                <p>${quiz.questions.size()}</p>
                                                            </div>
                                                        </div>
                                                        
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <h6>Danh sách câu hỏi:</h6>
                                                                <c:choose>
                                                                    <c:when test="${not empty quiz.questions}">
                                                                        <div class="list-group">
                                                                            <c:forEach items="${quiz.questions}" var="question" varStatus="status">
                                                                                <div class="list-group-item">
                                                                                    <div class="d-flex justify-content-between align-items-center">
                                                                                        <div>
                                                                                            <h6 class="mb-1">${status.index + 1}. ${question.questionText}</h6>
                                                                                            <div class="mt-2">
                                                                                                <div class="form-check ${question.correctAnswer eq '1' ? 'text-success fw-bold' : ''}">
                                                                                                    <label class="form-check-label">
                                                                                                        ${question.option1}
                                                                                                    </label>
                                                                                                </div>
                                                                                                <div class="form-check ${question.correctAnswer eq '2' ? 'text-success fw-bold' : ''}">
                                                                                                    <label class="form-check-label">
                                                                                                        ${question.option2}
                                                                                                    </label>
                                                                                                </div>
                                                                                                <div class="form-check ${question.correctAnswer eq '3' ? 'text-success fw-bold' : ''}">
                                                                                                    <label class="form-check-label">
                                                                                                        ${question.option3}
                                                                                                    </label>
                                                                                                </div>
                                                                                                <div class="form-check ${question.correctAnswer eq '4' ? 'text-success fw-bold' : ''}">
                                                                                                    <label class="form-check-label">
                                                                                                        ${question.option4}
                                                                                                    </label>
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </c:forEach>
                                                                        </div>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <div class="alert alert-warning">
                                                                            <i class="bi bi-exclamation-triangle"></i>
                                                                            Chưa có câu hỏi nào trong bài kiểm tra này.
                                                                        </div>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <!-- Hiển thị nút tạo bài kiểm tra mới -->
                                                <div class="text-center py-5">
                                                    <i class="bi bi-journal-check fs-1 text-muted mb-3"></i>
                                                    <h5 class="mb-3">Chưa có bài kiểm tra nào cho khóa học này</h5>
                                                    <p class="text-muted mb-4">Tạo bài kiểm tra để đánh giá kiến thức của học viên</p>
                                                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createQuizModal">
                                                        <i class="bi bi-plus"></i> Tạo Bài Kiểm Tra
                                                    </button>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Nút Hoàn Thành -->
                        <div class="mt-4 text-end">
                            <form action="add-course" method="post" style="display: inline-block;" id="cancelCourseForm">
                                <input type="hidden" name="step" value="2">
                                <input type="hidden" name="action" value="cancel-course">
                                <button type="button" class="btn btn-danger me-2" onclick="confirmCancelCourse()">
                                    <i class="bi bi-x-circle"></i> Hủy bỏ khóa học
                                </button>
                            </form>
                            
                            <form action="add-course" method="post" style="display: inline-block;">
                                <input type="hidden" name="step" value="2">
                                <input type="hidden" name="action" value="finish">
                                <button type="submit" class="btn btn-success">
                                    <i class="bi bi-check-circle"></i> Hoàn thành khóa học
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Modal Thêm Chương -->
    <div class="modal fade" id="addChapterModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <form action="add-course" method="post">
                    <input type="hidden" name="step" value="2">
                    <input type="hidden" name="action" value="add-chapter">
                    
                    <div class="modal-header">
                        <h5 class="modal-title">Thêm Chương Mới</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label">Tên Chương</label>
                            <input type="text" name="chapterName" class="form-control" required>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Mô Tả</label>
                            <textarea name="description" class="form-control" rows="3"></textarea>
                        </div>
                    </div>
                    
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-primary">Thêm Chương</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Modal sửa chương -->
    <div class="modal fade" id="editChapterModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Sửa Chương</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="add-course" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="step" value="2">
                        <input type="hidden" name="action" value="editChapter">
                        <input type="hidden" name="chapterId" id="editChapterId">
                        
                        <div class="mb-3">
                            <label class="form-label">Tên chương</label>
                            <input type="text" class="form-control" name="chapterName" id="editChapterName" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Mô tả</label>
                            <textarea class="form-control" name="description" id="editChapterDescription"></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Modal Thêm Bài Giảng -->
    <div class="modal fade" id="addLessonModal">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <form action="add-course" method="post">
                    <input type="hidden" name="step" value="2">
                    <input type="hidden" name="action" value="add-lesson">
                    <input type="hidden" name="chapterID" id="lessonChapterID">
                    
                    <div class="modal-header">
                        <h5 class="modal-title">Thêm Bài Giảng Mới</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label">Tên Bài Giảng</label>
                            <input type="text" name="lessonName" class="form-control" required>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Mô Tả</label>
                            <textarea name="description" class="form-control" rows="3"></textarea>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">URL Video</label>
                            <input type="url" name="videoURL" class="form-control">
                            <small class="text-muted">Nhập URL video YouTube (không bắt buộc)</small>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Nội Dung Bài Giảng</label>
                            <textarea name="content" class="form-control" rows="10" required></textarea>
                        </div>
                    </div>
                    
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-primary">Thêm Bài Giảng</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Modal sửa bài giảng -->
    <div class="modal fade" id="editLessonModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Sửa Bài Giảng</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="add-course" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="step" value="2">
                        <input type="hidden" name="action" value="editLesson">
                        <input type="hidden" name="lessonId" id="editLessonId">
                        
                        <div class="mb-3">
                            <label class="form-label">Tên bài giảng</label>
                            <input type="text" class="form-control" name="lessonName" id="editLessonName" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Mô tả</label>
                            <textarea class="form-control" name="description" id="editLessonDescription"></textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">URL Video</label>
                            <input type="url" class="form-control" name="videoURL" id="editLessonVideo">
                            <small class="text-muted">Nhập URL video YouTube (không bắt buộc)</small>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Nội dung bài giảng</label>
                            <textarea class="form-control" name="content" id="editLessonContent" rows="10" required></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Modal Thêm Câu Hỏi -->
    <div class="modal fade" id="addQuestionModal">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Thêm Câu Hỏi</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="alert alert-info">QuizID: ${quiz.quizID}</div>
                    
                    <form id="addQuestionForm" action="add-course" method="post">
                        <input type="hidden" name="step" value="2">
                        <input type="hidden" name="action" value="add-question">
                        <input type="hidden" name="quizId" value="${quiz.quizID}">
                        
                        <div class="mb-3">
                            <label for="questionText" class="form-label">Nội dung câu hỏi <span class="text-danger">*</span></label>
                            <textarea class="form-control" id="questionText" name="questionText" rows="3" required></textarea>
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
                            <small class="text-muted">Chọn đáp án đúng bằng cách click vào nút radio bên trái</small>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary" form="addQuestionForm">Thêm Câu Hỏi</button>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Modal Tạo Bài Kiểm Tra -->
    <div class="modal fade" id="createQuizModal">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Tạo Bài Kiểm Tra</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="createQuizForm" action="add-course" method="post">
                        <input type="hidden" name="step" value="2">
                        <input type="hidden" name="action" value="add-quiz">
                        <input type="hidden" name="courseId" value="${course.courseID}">
                        
                        <div class="mb-3">
                            <label for="quizTitle" class="form-label">Tiêu đề bài kiểm tra <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="quizTitle" name="title" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="quizDescription" class="form-label">Mô tả</label>
                            <textarea class="form-control" id="quizDescription" name="description" rows="3"></textarea>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="timeLimit" class="form-label">Thời gian làm bài (phút) <span class="text-danger">*</span></label>
                                <input type="number" class="form-control" id="timeLimit" name="timeLimit" min="1" value="30" required>
                            </div>
                            
                            <div class="col-md-6 mb-3">
                                <label for="passingScore" class="form-label">Điểm đạt (%) <span class="text-danger">*</span></label>
                                <input type="number" class="form-control" id="passingScore" name="passingScore" min="0" max="100" value="70" required>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary" form="createQuizForm">Tạo Bài Kiểm Tra</button>
                </div>
            </div>
        </div>
    </div>
    
    <jsp:include page="/views/components/footer.jsp"></jsp:include>
    
    <!-- Modal Xóa Bài Kiểm Tra -->
    <div class="modal fade" id="deleteQuizModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Xóa Bài Kiểm Tra</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p>Bạn có chắc chắn muốn xóa bài kiểm tra <strong>${quiz.title}</strong> không?</p>
                    <p class="text-danger">Lưu ý: Hành động này không thể hoàn tác.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <form action="${pageContext.request.contextPath}/quiz" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="quizId" value="${quiz.quizID}">
                        <input type="hidden" name="courseId" value="${course.courseID}">
                        <button type="submit" class="btn btn-danger">Xóa</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Thêm các thư viện JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Sortable/1.14.0/Sortable.min.js"></script>
    
    <script>
        // Hiển thị chi tiết chương
        function showChapterDetail(chapterId) {
            document.querySelectorAll('.chapter-detail').forEach(detail => {
                detail.style.display = 'none';
            });
            document.getElementById('chapter-' + chapterId).style.display = 'block';
            
            // Đánh dấu chương đang active
            document.querySelectorAll('.chapter-item').forEach(item => {
                item.classList.remove('active');
            });
            document.querySelector(`.chapter-item[data-chapter-id="${chapterId}"]`).classList.add('active');
        }
        
        // Hiển thị modal thêm bài giảng
        function showAddLessonModal(chapterId) {
            document.getElementById('lessonChapterID').value = chapterId;
            var modal = new bootstrap.Modal(document.getElementById('addLessonModal'));
            modal.show();
        }
        
        // Xóa chương
        function deleteChapter(chapterId) {
            if (confirm('Bạn có chắc chắn muốn xóa chương này? Tất cả bài giảng trong chương cũng sẽ bị xóa.')) {
                window.location.href = 'add-course?step=2&action=deleteChapter&chapterId=' + chapterId;
            }
        }
        
        // Chỉnh sửa chương
        function editChapter(chapterId) {
            var chapterItem = document.querySelector(`.chapter-item[data-chapter-id="${chapterId}"]`);
            var chapterName = chapterItem.querySelector('.fw-bold').textContent;
            
            document.getElementById('editChapterId').value = chapterId;
            document.getElementById('editChapterName').value = chapterName;
            
            // Lấy mô tả chương từ chi tiết chương
            var chapterDetail = document.getElementById('chapter-' + chapterId);
            var description = chapterDetail.querySelector('p.text-muted').textContent;
            document.getElementById('editChapterDescription').value = description;
            
            var modal = new bootstrap.Modal(document.getElementById('editChapterModal'));
            modal.show();
        }
        
        // Xóa bài giảng
        function deleteLesson(lessonId) {
            if (confirm('Bạn có chắc chắn muốn xóa bài giảng này?')) {
                window.location.href = 'add-course?step=2&action=deleteLesson&lessonId=' + lessonId;
            }
        }
        
        // Chỉnh sửa bài giảng
        function editLesson(lessonId, chapterId) {
            // Lấy thông tin bài giảng từ server bằng AJAX
            fetch('add-course?step=2&action=getLessonData&lessonId=' + lessonId)
                .then(response => response.json())
                .then(data => {
                    document.getElementById('editLessonId').value = data.lessonID;
                    document.getElementById('editLessonName').value = data.lessonName;
                    document.getElementById('editLessonDescription').value = data.description;
                    document.getElementById('editLessonVideoURL').value = data.videoURL;
                    document.getElementById('editLessonContent').value = data.content;
                    document.getElementById('editLessonChapterId').value = data.chapterID;
                    
                    var modal = new bootstrap.Modal(document.getElementById('editLessonModal'));
                    modal.show();
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Có lỗi xảy ra khi lấy thông tin bài giảng');
                });
        }
        
        // Khởi tạo Sortable.js cho danh sách chương
        var chaptersList = document.getElementById('chaptersList');
        if (chaptersList) {
            new Sortable(chaptersList, {
                animation: 150,
                ghostClass: 'sortable-ghost',
                handle: '.drag-handle',
                onEnd: function(evt) {
                    // Lấy danh sách ID chương theo thứ tự mới
                    var chapterIds = Array.from(document.querySelectorAll('.chapter-item'))
                        .map(item => item.getAttribute('data-chapter-id'));
                    
                    // Cập nhật thứ tự chương lên server
                    fetch('add-course', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({
                            step: 2,
                            action: 'updateChapterOrder',
                            chapterIds: chapterIds
                        })
                    })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            showToast('Cập nhật thứ tự chương thành công', 'success');
                        } else {
                            showToast('Có lỗi xảy ra khi cập nhật thứ tự chương', 'error');
                            // Reload trang để khôi phục thứ tự cũ
                            window.location.reload();
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        showToast('Có lỗi xảy ra khi cập nhật thứ tự chương', 'error');
                        // Reload trang để khôi phục thứ tự cũ
                        window.location.reload();
                    });
                }
            });
        }
        
        // Hiển thị toast thông báo
        function showToast(message, type) {
            var toastContainer = document.querySelector('.toast-container');
            var toast = document.createElement('div');
            toast.className = 'alert alert-' + (type === 'error' ? 'danger' : 'success') + ' alert-dismissible fade show';
            toast.innerHTML = `
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            `;
            toastContainer.appendChild(toast);
            
            // Tự động ẩn toast sau 5 giây
            setTimeout(() => {
                toast.classList.remove('show');
                setTimeout(() => {
                    toast.remove();
                }, 300);
            }, 5000);
        }
        
        // Xử lý form chỉnh sửa chương
        document.getElementById('editChapterForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            const chapterId = document.getElementById('editChapterId').value;
            const chapterName = document.getElementById('editChapterName').value;
            const description = document.getElementById('editChapterDescription').value;
            
            if (!chapterName.trim()) {
                alert('Tên chương không được để trống');
                return;
            }
            
            // Gửi form
            this.submit();
        });
        
        // Xử lý form tạo bài kiểm tra
        document.querySelectorAll('form.needs-validation').forEach(form => {
            form.addEventListener('submit', function(e) {
                if (!form.checkValidity()) {
                    e.preventDefault();
                    e.stopPropagation();
                }
                
                form.classList.add('was-validated');
            });
        });
        
        // Xử lý nút tạo bài kiểm tra
        document.getElementById('submitQuizBtn').addEventListener('click', function() {
            var form = document.getElementById('createQuizForm');
            if (form.checkValidity()) {
                form.submit();
            } else {
                form.classList.add('was-validated');
            }
        });
        
        // Xử lý nút hủy bỏ khóa học
        function confirmCancelCourse() {
            if (confirm('Bạn có chắc chắn muốn hủy bỏ khóa học này?')) {
                document.getElementById('cancelCourseForm').submit();
            }
        }
    </script>
</body>
</html>
