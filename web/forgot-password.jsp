<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 4/9/2024
  Time: 11:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>forgot password</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="./assets/bootstrap-5.0.2-dist/bootstrap-5.0.2-dist/js/bootstrap.bundle.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" integrity="sha512-DTOQO9RWCH3ppGqcWaEA1BIZOC6xxalwEsw9c2QQeAIftl+Vegovlnee1c9QX4TctnWMn13TZye+giMm8e2LwA==" crossorigin="anonymous" referrerpolicy="no-referrer"
    />
    <style>
        .back-button {
            display: inline-block;
            padding: 8px 16px;
            background-color: #CCCCCC;
            color: #000;
            text-decoration: none;
            border-radius: 4px;
            margin-left: 30px;
            margin-top: 20px;
            width: 150px;
            height: 40px;
        }
    </style>
</head>

<body>
<div class="d-flex justify-content-center align-items-center" style="height: 100vh">
    <div class="pt-5 pb-5 ps-3 pe-3" style="width: 600px; background-color: beige;">
        <form action="forgot-password" method="post">
            <div class="mb-3 d-flex justify-content-center align-items-center">
                <i class="fa-brands fa-dev me-3" style="font-size: 4rem"></i>
                <h3>Online Learning System</h3>
            </div>
            <div class="note-message text-center">
                <p class="m-0">Please enter your email</p>
                <p>and we'll send you a link to get back your account</p>
            </div>
            <div class="mb-5 row">
                <label for="Email" class="col-sm-2 col-form-label">Email:</label>
                <div class="col-sm-10">
                    <input type="email" class="form-control border-0" id="Email" name="email" />
                </div>
            </div>
            <p style="color: tomato">${mess}</p>
            <div class="d-flex justify-content-around">
                <button type="submit" class="back-button" style=" background-color:#CCC">Send</button>
                <a class="back-button" href="/">Back to List</a>
            </div>
        </form>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>

</html>