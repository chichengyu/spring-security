<%@page contentType="text/html;charset=utf-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>

<h2>登陆</h2>

<c:if test="${not empty param.error}">
    <font color="red">用户名或密码错误</font>
</c:if>

<form action="${pageContext.request.contextPath}/login" method="post">
    <input type="text" name="username"><br/>
    <input type="password" name="password">
    <input type="submit" value="登陆">
</form>
</body>
</html>
