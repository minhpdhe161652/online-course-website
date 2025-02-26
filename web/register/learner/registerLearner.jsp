<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Learner Registration</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .password-requirements {
            font-size: 0.8rem;
            color: #6c757d;
        }
        .error-message {
            color: #dc3545;
            margin-bottom: 1rem;
        }
        .form-container {
            max-width: 600px;
            margin: 2rem auto;
            padding: 2rem;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
            background-color: #fff;
        }
        body {
            background-color: #f8f9fa;
        }
        .password-strength {
            margin-top: 0.5rem;
            height: 5px;
            border-radius: 2px;
            transition: all 0.3s ease;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h2 class="text-center mb-4">Learner Registration</h2>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger" role="alert">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>

            <form action="register" method="POST" id="registrationForm" class="needs-validation" novalidate>
                <div class="row mb-3">
                    <div class="col-md-6">
                        <label for="firstName" class="form-label">First Name</label>
                        <input type="text" class="form-control" id="firstName" name="firstName" required
                               maxlength="255" pattern="[A-Za-z\s]+" title="Please enter a valid first name">
                        <div class="invalid-feedback">Please enter your first name.</div>
                    </div>
                    <div class="col-md-6">
                        <label for="lastName" class="form-label">Last Name</label>
                        <input type="text" class="form-control" id="lastName" name="lastName" required
                               maxlength="255" pattern="[A-Za-z\s]+" title="Please enter a valid last name">
                        <div class="invalid-feedback">Please enter your last name.</div>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" name="email" required maxlength="255">
                    <div class="invalid-feedback">Please enter a valid email address.</div>
                </div>

                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" id="username" name="username" required
                           maxlength="255" pattern="[A-Za-z0-9_]+" title="Username can only contain letters, numbers, and underscores">
                    <div class="invalid-feedback">Please enter a valid username (letters, numbers, and underscores only).</div>
                </div>

                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                    <div class="invalid-feedback">Please enter a valid password.</div>
                    <div class="password-requirements mt-2">
                        Password must contain:
                        <ul class="mb-0">
                            <li id="length">At least 8 characters</li>
                            <li id="uppercase">At least one uppercase letter</li>
                            <li id="lowercase">At least one lowercase letter</li>
                            <li id="number">At least one number</li>
                        </ul>
                    </div>
                    <div class="password-strength bg-secondary"></div>
                </div>

                <button type="submit" class="btn btn-primary w-100">Register</button>
                
                <div class="text-center mt-3">
                    <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Login here</a></p>
                </div>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Form validation
        (function () {
            'use strict'
            const forms = document.querySelectorAll('.needs-validation')
            Array.from(forms).forEach(form => {
                form.addEventListener('submit', event => {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    }
                    form.classList.add('was-validated')
                }, false)
            })
        })()

        // Password strength checker
        const password = document.getElementById('password');
        const length = document.getElementById('length');
        const uppercase = document.getElementById('uppercase');
        const lowercase = document.getElementById('lowercase');
        const number = document.getElementById('number');
        const strengthBar = document.querySelector('.password-strength');

        password.addEventListener('input', function() {
            const val = password.value;
            let strength = 0;

            // Check length
            if (val.length >= 8) {
                length.style.color = 'green';
                strength++;
            } else {
                length.style.color = '#6c757d';
            }

            // Check uppercase
            if (val.match(/[A-Z]/)) {
                uppercase.style.color = 'green';
                strength++;
            } else {
                uppercase.style.color = '#6c757d';
            }

            // Check lowercase
            if (val.match(/[a-z]/)) {
                lowercase.style.color = 'green';
                strength++;
            } else {
                lowercase.style.color = '#6c757d';
            }

            // Check number
            if (val.match(/\d/)) {
                number.style.color = 'green';
                strength++;
            } else {
                number.style.color = '#6c757d';
            }

            // Update strength bar
            switch(strength) {
                case 0:
                    strengthBar.style.width = '0%';
                    strengthBar.className = 'password-strength bg-secondary';
                    break;
                case 1:
                    strengthBar.style.width = '25%';
                    strengthBar.className = 'password-strength bg-danger';
                    break;
                case 2:
                    strengthBar.style.width = '50%';
                    strengthBar.className = 'password-strength bg-warning';
                    break;
                case 3:
                    strengthBar.style.width = '75%';
                    strengthBar.className = 'password-strength bg-info';
                    break;
                case 4:
                    strengthBar.style.width = '100%';
                    strengthBar.className = 'password-strength bg-success';
                    break;
            }
        });
    </script>
</body>
</html>
