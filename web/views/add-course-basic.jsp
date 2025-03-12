<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${isEdit ? 'Chỉnh Sửa' : 'Thêm'} Khóa Học | Online Learning</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            .course-form {
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
            
            .btn-primary {
                background-color: #3498db;
                border: none;
                padding: 12px 30px;
                border-radius: 8px;
                font-weight: 500;
                transition: all 0.3s;
            }
            
            .btn-primary:hover {
                background-color: #2980b9;
                transform: translateY(-2px);
            }
            
            .btn-secondary {
                background-color: #95a5a6;
                border: none;
                padding: 12px 30px;
                border-radius: 8px;
                font-weight: 500;
                transition: all 0.3s;
            }
            
            .btn-secondary:hover {
                background-color: #7f8c8d;
                transform: translateY(-2px);
            }
            
            .progress-steps {
                display: flex;
                justify-content: center;
                margin-bottom: 40px;
            }
            
            .step {
                display: flex;
                flex-direction: column;
                align-items: center;
                position: relative;
                z-index: 1;
            }
            
            .step:not(:last-child)::after {
                content: "";
                position: absolute;
                width: 100%;
                height: 2px;
                background-color: #e0e0e0;
                top: 25px;
                left: 50%;
                z-index: -1;
            }
            
            .step.active:not(:last-child)::after {
                background-color: #3498db;
            }
            
            .step-number {
                width: 50px;
                height: 50px;
                border-radius: 50%;
                background-color: #e0e0e0;
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: bold;
                color: #fff;
                margin-bottom: 10px;
            }
            
            .step.active .step-number {
                background-color: #3498db;
            }
            
            .step-label {
                font-size: 14px;
                color: #7f8c8d;
            }
            
            .step.active .step-label {
                color: #3498db;
                font-weight: 500;
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
        </style>
    </head>
    <body class="bg-light">
        <%@include file="/views/components/header.jsp"%>
        
        <div class="container py-5">
            <div class="progress-steps mb-5">
                <div class="step active me-5">
                    <div class="step-number">1</div>
                    <div class="step-label">Thông Tin Cơ Bản</div>
                </div>
                <div class="step me-5">
                    <div class="step-number">2</div>
                    <div class="step-label">Thông Tin Chi Tiết</div>
                </div>
            </div>
            
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="course-form">
                        <h2 class="text-center mb-4">${isEdit ? 'Chỉnh Sửa' : 'Thêm'} Khóa Học</h2>
                        
                        <c:if test="${not empty message}">
                            <div class="alert alert-${messageType == 'error' ? 'danger' : 'success'} alert-dismissible fade show" role="alert">
                                ${message}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <form action="${isEdit ? 'edit-course' : 'add-course'}?id=${course.courseID}&step=1" method="post" class="needs-validation" novalidate>
                            <div class="mb-4">
                                <label class="form-label required-field">Tên Khóa Học</label>
                                <input type="text" class="form-control" name="courseName" required 
                                       value="${course.courseName}" placeholder="Nhập tên khóa học">
                                <div class="form-text">Đặt tên ngắn gọn, dễ nhớ và thể hiện được nội dung chính của khóa học</div>
                            </div>
                            
                            <div class="mb-4">
                                <label class="form-label required-field">Mô Tả</label>
                                <textarea class="form-control" name="description" rows="5" required 
                                          placeholder="Mô tả chi tiết về khóa học">${course.description}</textarea>
                                <div class="form-text">Mô tả rõ những gì học viên sẽ học được và lợi ích khi tham gia khóa học</div>
                            </div>
                            
                            <div class="row mb-4">
                                <div class="col-md-6">
                                    <label class="form-label required-field">Danh Mục</label>
                                    <select class="form-select" name="subcategoryID" required>
                                        <option value="">Chọn danh mục</option>
                                        <c:forEach items="${categories}" var="category">
                                            <optgroup label="${category.categoryName}">
                                                <c:forEach items="${category.subcategories}" var="subcategory">
                                                    <option value="${subcategory.subcategoryID}" 
                                                            ${course.subcategoryID == subcategory.subcategoryID ? 'selected' : ''}>
                                                        ${subcategory.subcategoryName}
                                                    </option>
                                                </c:forEach>
                                            </optgroup>
                                        </c:forEach>
                                    </select>
                                </div>
                                
                                <div class="col-md-6">
                                    <label class="form-label required-field">Cấp Độ</label>
                                    <select class="form-select" name="levelName" required>
                                        <option value="">Chọn cấp độ</option>
                                        <option value="Beginner" ${course.levelName == 'Beginner' ? 'selected' : ''}>Beginner</option>
                                        <option value="Intermediate" ${course.levelName == 'Intermediate' ? 'selected' : ''}>Intermediate</option>
                                        <option value="Advanced" ${course.levelName == 'Advanced' ? 'selected' : ''}>Advanced</option>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="mb-4">
                                <label class="form-label required-field">Giá (VNĐ)</label>
                                <div class="input-group">
                                    <input type="number" class="form-control" name="price" required 
                                           min="0" step="1000"
                                           value="${empty course.price ? 0 : course.price}" 
                                           placeholder="Nhập giá khóa học (VD: 199000)">
                                    <span class="input-group-text">VNĐ</span>
                                </div>
                                <div class="form-text">Nhập 0 nếu là khóa học miễn phí. Giá tối thiểu là 0 VNĐ.</div>
                            </div>
                            
                            <div class="mb-4">
                                <label class="form-label required-field">URL Hình Ảnh</label>
                                <input type="url" class="form-control" name="imageURL" required 
                                       value="${course.imageURL}" placeholder="Nhập URL hình ảnh đại diện">
                                <div class="form-text">Sử dụng hình ảnh có tỷ lệ 16:9 để hiển thị đẹp nhất</div>
                            </div>
                            
                            <div class="text-center mt-5">
                                <a href="home" class="btn btn-secondary me-2">
                                    <i class="bi bi-arrow-left me-2"></i>Hủy
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    ${isEdit ? 'Lưu Thay Đổi' : 'Tiếp Tục'}<i class="bi bi-arrow-right ms-2"></i>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        
        <%@include file="/views/components/footer.jsp"%>
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
        </script>
    </body>
</html>
