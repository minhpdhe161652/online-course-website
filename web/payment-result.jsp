<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Kết quả thanh toán</title>
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            font-family: Arial, sans-serif;
        }
        .container {
            text-align: center;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        a {
            display: inline-block;
            margin-top: 10px;
            padding: 10px 20px;
            text-decoration: none;
            color: white;
            background-color: #007bff;
            border-radius: 5px;
        }
        a:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Trạng thái thanh toán: ${param.status == '00' ? 'Thành công' : 'Thất bại'}</h2>
        <a href="courses">Quay lại trang chủ</a>
    </div>
</body>
</html>
