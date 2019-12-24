<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 
<html>
<!-- Latest compiled and minified CSS -->
<head>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.0/css/bootstrap.min.css"
	integrity="sha384-SI27wrMjH3ZZ89r4o+fGIJtnzkAnFs3E4qz9DIYioCQ5l9Rd/7UAa8DHcaL8jkWt"
	crossorigin="anonymous">

<link rel="stylesheet" type="text/css" href="CSS/registration.css" />

<!-- jQuery library -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="JS/formvalidation.js"></script>

<style type="text/css">
.registration{
	width: 500px;
	height: 380px;
	margin-top: 30px;
	color: blue;
}
button {
	margin-left: 180px;
}
fieldset {
	border: #ff0000 3px solid;
}
form {
	padding-top: 10px
}
.login{
	width: 500px;
	height: 380px;
	margin-top: 30px;
	color: blue;
}
.col-md-8{
	margin-left: 80px;
}
input {
	text-align: center;
}
.btn1{
	margin-top: 3%;
	margin-left: 32%;
}
.btn2{
	margin-top: 3%;
	margin-left: 5%;
}

</style>
</head>

<body>

	<div class="container registration">
		<fieldset class="border p-2">
			<legend class="w-auto">Registration Form</legend>
			<form:form action="register" id="registrationForm"
				onsubmit="return validation();" modelAttribute="regForm">
				<div class="row" style="padding-bottom: 20px">
					<div class="col">
						<form:input type="text" class="form-control" path="firstName"
							placeholder="First name" id="fname" /> <span id="firstname"
							class="text-danger font-weight-bold"></span>
					</div>
					<div class="col">
						<form:input type="text" class="form-control" path="lastName"
							placeholder="Last name" id="lname" /> <span id="lastname"
							class="text-danger font-weight-bold"></span>
					</div>
				</div>

				<div class="row" style="padding-bottom: 20px">
					<div class="col">
						<form:input type="text" class="form-control" path="userName"
							placeholder="UserName" id="uname" onclick="hidevalue()" /> <span id="username"
							class="text-danger font-weight-thin"></span>
							<div id="ajaxGetUserServletResponse"></div>
					</div>
					
				</div>
				
				<!-- below jquery things triggered on onblur event and checks the username availability in the database -->
				<script type="text/javascript">
					/* $(document).ready(function() {
						alert("js is working");
						$('#uname').blur(function() {
							alert("blur is working")
							$.ajax({
									type : 'post',
									url : 'registration',
									data : {
												userName : $('#uname').val()
											},
											success : function(responseText) {
												$('#ajaxGetUserServletResponse').text(responseText);
											}
									});
							});
					});  */
					function hidevalue() {
						document.getElementById("ajaxGetUserServletResponse").innerHTML = "";
					}
					$(document).ready(function(){
						$('#uname').blur(function(){
							var userName = $('#uname').val();
							$.post('checkuseravailability',{uname:userName},function(responseText){
								$('#ajaxGetUserServletResponse').text(responseText).css({"color": "red","text-align":"center"});
							});
						});
					}); 
				</script>
			
				<div class="row" style="padding-bottom: 20px">
					<div class="col">
						<form:input type="text" class="form-control" path="email"
							placeholder="Email" id="email"/> <span id="mailid"
							class="text-danger font-weight-bold"></span>
					</div>
					<div class="col">
						<form:input type="number" class="form-control" path="mobile"
							placeholder="Contact Number" id="ph" /> <span id="mobile"
							class="text-danger font-weight-bold"></span>
					</div>
				</div>
				<div class="row" style="padding-bottom: 20px">
					<div class="col">
						<form:input type="password" class="form-control" path="password"
							placeholder="Password" id="pswd" /> <span id="pass"
							class="text-danger font-weight-bold"></span>
					</div>
					<div class="col">
						<form:input type="password" class="form-control" path="cnfPasword"
							placeholder="Confirm Password" id="cpswd" /> <span
							id="cpass" class="text-danger font-weight-bold"></span>
					</div>
				</div>
				<div>
					<button class="btn btn-success regbtn" type="submit">SUBMIT</button>
				</div>
			</form:form>
		</fieldset>
	</div>

	<script type="text/javascript">
		function validation() {
			//alert("Coming inside if block");
			var firstName = document.getElementById('fname').value;
			var lastName = document.getElementById('lname').value;
			var userName = document.getElementById('uname').value;
			var email = document.getElementById('email').value;
			var contact = document.getElementById('ph').value;
			var password = document.getElementById('pswd').value;
			var cpassword = document.getElementById('cpswd').value;

			var letters = /^[A-Za-z]+$/;
			var mobilePattern = /^[7-9][0-9]{9}$/;

			if (firstName == "") {
				document.getElementById('firstname').innerHTML = "Please fill the First Name field";
				return false;
			}
			if (!firstName.match(letters)) {
				document.getElementById('firstname').innerHTML = "Please Enter only alphabets";
				return false;
			}

			if (lastName == "") {
				document.getElementById('lastname').innerHTML = "Please fill the Last Name field";
				return false;
			}
			if (!lastName.match(letters)) {
				document.getElementById('lastname').innerHTML = "Please Enter only alphabets";
				return false;
			}

			if (userName == "") {
				document.getElementById('username').innerHTML = "Please fill the User Name field";
				return false;
			}
			if ((userName.length <= 5) || (userName.length > 10)) {
				document.getElementById('username').innerHTML = "Username must be greater than 5 and less than 10";
				return false;
			}

			if (email == "") {
				document.getElementById('mailid').innerHTML = "Please fill the Email field";
				return false;
			}
			if (contact == "") {
				document.getElementById('mobile').innerHTML = "Please fill the Contact field";
				return false;
			}
			if (contact.length > 10) {
				document.getElementById('mobile').innerHTML = "Invalid Contact Number";
				return false;
			}

			if (password == "") {
				document.getElementById('pass').innerHTML = "Please fill the Password field";
				return false;
			}
			if ((password.length <= 5) || (password.length > 10)) {
				document.getElementById('pass').innerHTML = "password must be greater than 5 and less than 10";
				return false;
			}
			if (cpassword == "") {
				document.getElementById('cpass').innerHTML = "Please fill the Confirm Password field";
				return false;
			}

			if (password != cpassword) {
				document.getElementById('pass').innerHTML = "password and confirm password doesn't match";
				return false;
			}
		}
	</script>
</body>
</html>
