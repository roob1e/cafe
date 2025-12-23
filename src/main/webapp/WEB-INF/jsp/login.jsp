<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
  <title>Login - Cafe Service</title>
  <style>
      .error { color: red; margin-bottom: 10px; }
  </style>
</head>
<body>
<h2>Sign In</h2>

<c:if test="${not empty errorMessage}">
  <div class="error">
      ${errorMessage}
  </div>
</c:if>

<form action="${pageContext.request.contextPath}/controller" method="POST">
  <input type="hidden" name="command" value="login">
  <div>
    <label>Email:</label><br>
    <input type="email" name="email" required>
  </div>
  <br>
  <div>
    <label>Password:</label><br>
    <input type="password" name="password" required>
  </div>
  <br>
  <button type="submit">Login</button>
</form>

<hr>
<p>New user?
  <a href="${pageContext.request.contextPath}/controller?command=go_to_register">Create an account</a>
</p>
</body>
</html>