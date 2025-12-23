<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Cafe Application</title>
</head>
<body>
<h1>Welcome to our Cafe!</h1>
<hr>
<a href="${pageContext.request.contextPath}/controller?command=go_to_login">Login</a> |
<a href="${pageContext.request.contextPath}/controller?command=go_to_register">Register</a>
</body>
</html>