<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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

<%
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	response.addHeader("Cache-Control", "post-check=0, pre-check=0");
	response.setHeader("Pragma", "no-cache");
%>
	<div class="container login">
		<fieldset class="border p-2">
			<legend class="w-auto">Login</legend>
			<form:form action="login" modelAttribute="loginForm">
				<div class="row">
					<div class="col-md-8">
						<form:input type="text" class="form-control" path="userName"
							placeholder="Username" id="uname" autocomplete="off" />
					</div>
				</div>
				<div class="row">
					<div class="col-md-8" style="margin-top: 15px;">
						<form:input type="password" class="form-control" path="password"
							placeholder="Password" id="pswd" />
					</div>
					<span style="margin-left: 32%; margin-top: 2%; font-size: 12px;">New User Please Register First**</span>
				</div>

				<div class="buttons" style="display: inline;">
					<button class="btn btn-success btn1" type="submit">Login</button>
					<a href="reg" class="btn btn-primary btn2">Register</a>
				</div>
			</form:form>
		</fieldset>
		<div style="color : red; text-align: center;">
    	<%String result  = (String) request.getAttribute("invalid");
    	if(result!=null){ 
    		out.println(result);
    	}
    	%>
    	</div>
    	<div style="color : green; text-align: center;">
    	<%String insert  = (String) request.getAttribute("insert");
    	if(insert!=null){ 
    		out.println(insert);
    	}
    	%>
    	</div>
	</div>
</body>
</html>
