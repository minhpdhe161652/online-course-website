<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="model.User" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<%
    User user = (User) request.getAttribute("user");

    if (user == null) {
        out.println("<p class='error-message'>Lỗi: Không tìm thấy thông tin người dùng.</p>");
        return;
    }
    
    String fullName = user.getLastName() + " " + user.getFirstName();
    String[] nameParts = fullName.trim().split("\\s+", 2);
    String firstName = (nameParts.length > 0) ? nameParts[0] : "";
    String lastName = (nameParts.length > 1) ? nameParts[1] : "";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
    String formattedDob = (user.getDob() != null) ? sdf.format(user.getDob()) : "";
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>LearnHub - Profile</title>
        <link rel="stylesheet" href="css/edit-profile.css">
    </head>
    <body>
        <header>
            <h1>LearnHub</h1>
            <nav>
                <a href="courses">Khóa học</a>
                <a href="#">Danh mục</a>
                <button class="logout-btn">Đăng xuất</button>
            </nav>
        </header>
        <main>
            <aside>
                <div class="profile-picture">
                    <img src="https://res.cloudinary.com/dmkanq2n0/image/upload/v1740413485/cld-sample-3.jpg" alt="User Avatar">
                </div>
                <h1 class="username">@<%= user.getUsername() %></h1>
                <ul class="sidebar-menu">
                    <li class="active">Profile</li>
                    <li>Transaction History</li>
                    <li><a href="enrolled-course">Course enrolled</a></li>
                </ul>
            </aside>
            <section class="profile-form">
                <div class="form-group">
                    <label for="name">Tên</label>
                    <input type="text" id="name" value="<%= firstName %> <%= lastName %>" readonly>
                </div>
                <div class="form-group">
                    <label for="dob">Ngày sinh</label>
                    <input type="date" id="dob" value="<%= user.getDob() %>" readonly>
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" value="<%= user.getEmail() %>" readonly>
                </div>
                <div class="form-group">
                    <label for="phone">SĐT</label>
                    <input type="text" id="phone" value="<%= user.getPhoneNumber() %>" readonly>
                </div>
                <div class="form-group">
                    <label for="register-date">Ngày đăng kí</label>
                    <input type="text" id="register-date" value="<%= user.getRegistrationDate() %>">
                </div>
                <div class="form-group bio">
                    <label for="bio">Bio</label>
                    <textarea id="bio" readonly><%= user.getBio() %></textarea>
                </div>
                <button class="edit-profile-btn">Edit profile</button>
            </section>
        </main>
        <div id="editProfileModal" class="modal">
            <div class="modal-content">
                <span class="close">&times;</span>
                <h2>Edit Profile</h2>
                <div id="message"></div>
                <form id="update-profile-form" name="profile-form" onsubmit="return validateForm()">
                    <div class="form-group">
                        <label for="modal-firstname">First Name</label>
                        <input name="firstName" type="text" id="modal-firstname" value="<%= firstName %>">
                    </div>
                    <div class="form-group">
                        <label for="modal-lastname">Last Name</label>
                        <input name="lastName" type="text" id="modal-lastname" value="<%= lastName %>">
                    </div>
                    <div class="form-group">
                        <label for="modal-dob">Ngày sinh</label>
                        <input name="dob" type="date" id="modal-dob" value="<%= user.getDob() %>">
                    </div>
                    <div class="form-group">
                        <label for="modal-phone">SĐT</label>
                        <input name="phone" type="text" id="modal-phone" value="<%= user.getPhoneNumber() %>">
                    </div>
                    <div class="form-group">
                        <label for="modal-bio">Bio</label>
                        <textarea name="bio" id="modal-bio"><%= user.getBio() %></textarea>
                    </div>
                    <button class="save-btn" type="submit">Confirm</button>
                </form>
            </div>
        </div>
    </body>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
                    function validateForm() {
                        let firstName = document.forms["profile-form"]["firstName"].value.trim();
                        let lastName = document.forms["profile-form"]["lastName"].value.trim();
                        let phone = document.forms["profile-form"]["phone"].value.trim();
                        let birthDate = document.forms["profile-form"]["dob"].value;
                        let bio = document.forms["profile-form"]["bio"].value.trim();

                        let namePattern = /^[a-zA-ZÀ-ỹ\s]+$/; // Tên không chứa số
                        let phonePattern = /^[0-9]{10,11}$/; // Số điện thoại 10-11 số

                        if (!firstName.match(namePattern)) {
                            alert("Tên không được chứa số hoặc ký tự đặc biệt!");
                            return false;
                        }

                        if (!lastName.match(namePattern)) {
                            alert("Tên không được chứa số hoặc ký tự đặc biệt!");
                            return false;
                        }

                        if (!phone.match(phonePattern)) {
                            alert("Số điện thoại phải có 10-11 số!");
                            return false;
                        }

                        if (birthDate === "") {
                            alert("Vui lòng chọn ngày sinh!");
                            return false;
                        }

                        let today = new Date();
                        let birthDateObj = new Date(birthDate);
                        if (birthDateObj > today) {
                            alert("Ngày sinh không hợp lệ!");
                            return false;
                        }

                        if (bio.length > 255) {
                            alert("Giới thiệu không được quá 255 ký tự!");
                            return false;
                        }

                        return true;
                    }

                    $(document).ready(function () {
                        $("#update-profile-form").submit(function (event) {
                            event.preventDefault(); // Ngăn chặn submit form thông thường

                            $.ajax({
                                type: "POST",
                                url: "edit-profile",
                                data: {
                                    firstName: $("#modal-firstname").val(),
                                    lastName: $("#modal-lastname").val(),
                                    phone: $("#modal-phone").val(),
                                    dob: $("#modal-dob").val(),
                                    bio: $("#modal-bio").val()
                                },
                                dataType: "json",
                                success: function (response) {
                                    $("#message").html("<p style='color: green;'>" + response.message + "</p>");
                                },
                                error: function () {
                                    $("#message").html("<p style='color: red;'>Update failed</p>");
                                }
                            });
                        });
                    });

                    document.addEventListener("DOMContentLoaded", function () {
                        const modal = document.getElementById("editProfileModal");
                        modal.style.display = "none";

                        const openModalBtn = document.querySelector(".edit-profile-btn");
                        const closeModalBtn = document.querySelector(".close");
                        const saveBtn = document.querySelector(".save-btn");

                        // Lấy input gốc từ form chính
                        const mainFullName = document.getElementById("name");
                        const mainDob = document.getElementById("dob");
                        const mainPhone = document.getElementById("phone");
                        const mainBio = document.getElementById("bio");

                        // Input trong modal
                        const modalFirstName = document.getElementById("modal-firstname");
                        const modalLastName = document.getElementById("modal-lastname");
                        const modalDob = document.getElementById("modal-dob");
                        const modalPhone = document.getElementById("modal-phone");
                        const modalBio = document.getElementById("modal-bio");

                        // Mở modal
                        openModalBtn.addEventListener("click", function () {
                            modal.style.display = "flex";

                            // Lấy tên đầy đủ từ input
                            let fullName = mainFullName.value.trim();
                            let nameParts = fullName.split(/\s+/);

                            if (nameParts.length > 1) {
                                modalLastName.value = nameParts[0]; // Họ (Last Name)
                                modalFirstName.value = nameParts.slice(1).join(" "); // Tên (First Name)
                            } else {
                                modalLastName.value = fullName;
                                modalFirstName.value = "";
                            }

                            modalDob.value = mainDob.value;
                            modalPhone.value = mainPhone.value;
                            modalBio.value = mainBio.value;
                        });

                        // Đóng modal
                        closeModalBtn.addEventListener("click", function () {
                            modal.style.display = "none";
                        });

                        // Lưu thông tin từ modal vào form chính
                        saveBtn.addEventListener("click", function () {
                            let firstName = modalFirstName.value.trim();
                            let lastName = modalLastName.value.trim();

                            // Đảm bảo thứ tự đúng: Họ (Last Name) đứng trước, Tên (First Name) đứng sau
                            mainFullName.value = lastName + " " + firstName;

                            mainDob.value = modalDob.value;
                            mainPhone.value = modalPhone.value;
                            mainBio.value = modalBio.value;

                            modal.style.display = "none"; // Đóng modal sau khi lưu                         
                        });

                        // Đóng modal khi nhấn bên ngoài nội dung
                        window.addEventListener("click", function (event) {
                            if (event.target === modal) {
                                modal.style.display = "none";
                            }
                        });
                    });
    </script>
</html>