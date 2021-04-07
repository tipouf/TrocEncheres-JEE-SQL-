<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	String error = (String)request.getAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<h1>Connexion</h1>
	
	<form action="" method="POST">
	
		<input type="text" name="emailOrPseudo" />
		<input type="password" name="password" />
		
		<input type="submit" />
		
		<% if(error != null) { %>
			<span class=""><%= error %></span>
		<% } %>
	
	</form>
	
</body>
</html>