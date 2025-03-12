<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chỉnh Sửa Khóa Học | LearnHub</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <jsp:include page="components/header.jsp"/>
        
        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-header">
                            <h3 class="text-center">Chỉnh Sửa Khóa Học</h3>
                        </div>
                        <div class="card-body">
                            <% if (request.getAttribute("message") != null) { %>
                            <div class="alert alert-<%= request.getAttribute("messageType").equals("success") ? "success" : "danger" %>" role="alert">
                                <%= request.getAttribute("message") %>
                            </div>
                            <% } %>
                            
                            <form action="edit-course?id=${course.courseID}" method="POST">
                                <div class="mb-3">
                                    <label for="courseName" class="form-label">Tên Khóa Học *</label>
                                    <input type="text" class="form-control" id="courseName" name="courseName" 
                                           value="${course.courseName}" required>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="description" class="form-label">Mô Tả *</label>
                                    <textarea class="form-control" id="description" name="description" 
                                              rows="4" required>${course.description}</textarea>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="levelName" class="form-label">Cấp Độ *</label>
                                    <select class="form-select" id="levelName" name="levelName" required>
                                        <option value="">Chọn cấp độ</option>
                                        <option value="Beginner" ${course.levelName == 'Beginner' ? 'selected' : ''}>Beginner</option>
                                        <option value="Intermediate" ${course.levelName == 'Intermediate' ? 'selected' : ''}>Intermediate</option>
                                        <option value="Advanced" ${course.levelName == 'Advanced' ? 'selected' : ''}>Advanced</option>
                                    </select>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="price" class="form-label">Giá (VNĐ) *</label>
                                    <input type="number" class="form-control" id="price" name="price" 
                                           value="${course.price}" min="0" step="1000" required>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="imageURL" class="form-label">URL Hình Ảnh *</label>
                                    <input type="url" class="form-control" id="imageURL" name="imageURL" 
                                           value="${course.imageURL}" required>
                                    <div class="form-text">Nhập URL hình ảnh đại diện cho khóa học</div>
                                </div>
                                
                                <div class="text-center">
                                    <button type="submit" class="btn btn-primary">Lưu Thay Đổi</button>
                                    <a href="instructor-courses" class="btn btn-secondary ms-2">Hủy</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <jsp:include page="components/footer.jsp"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
