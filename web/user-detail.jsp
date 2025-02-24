<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thông tin người dùng</title>
</head>
<body>
    <h1>Thông tin người dùng</h1>
    <p>ID: ${user.userID}</p>
    <p>Tên đăng nhập: ${user.username}</p>
    <p>Họ và tên: ${user.firstName} ${user.lastName}</p>
    <p>Email: ${user.email}</p>
    <p>Vai trò: ${user.roleID == 1 ? 'Learner' : user.roleID == 2 ? 'Expert' : 'Admin'}</p>
    <p>Ngày đăng ký: ${user.registrationDate}</p>
    <p>Trạng thái: ${user.isActive ? 'Hoạt động' : 'Không hoạt động'}</p>
    <a href="${pageContext.request.contextPath}/admin/users">Quay lại</a>
</body>
</html>