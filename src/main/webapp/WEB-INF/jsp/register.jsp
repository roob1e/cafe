<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Подключение JSTL тегов --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
  <title>Registration - Cafe Application</title>
</head>
<body>

<div style="max-width: 400px; margin: 0 auto;">
  <h2>Create Account</h2>

  <%-- Вывод ошибки через тег условия --%>
  <c:if test="${not empty errorMessage}">
    <p style="color: red;">
      <c:out value="${errorMessage}" />
    </p>
  </c:if>

  <%-- Формируем чистый URL к контроллеру --%>
  <c:url var="actionUrl" value="/controller" />

  <form action="<c:out value='${actionUrl}' />" method="post">

    <%-- Команда для MainServlet --%>
    <input type="hidden" name="command" value="register">

    <div>
      <label for="reg_name">Name:</label><br>
      <input type="text" id="reg_name" name="name" required>
    </div>
    <br>
    <div>
      <label for="reg_email">Email:</label><br>
      <input type="email" id="reg_email" name="email" required>
    </div>
    <br>
    <div>
      <label for="reg_pass">Password:</label><br>
      <input type="password" id="reg_pass" name="password" required>
    </div>
    <br>
    <button type="submit">Register Now</button>
  </form>

  <hr>
  <p>
    Already have an account?
    <c:url var="loginLink" value="/controller">
      <c:param name="command" value="go_to_login" />
    </c:url>
    <a href="<c:out value='${loginLink}' />">Back to Login</a>
  </p>
</div>

</body>
</html>