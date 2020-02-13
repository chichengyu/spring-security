<%@page contentType="text/html;charset=utf-8" language="java" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<html>
<body>
<h2>Hello World!</h2>

<a href="${pageContext.request.contextPath}/product/add">商品添加</a>
<br/>
<a href="${pageContext.request.contextPath}/product/list">商品查询</a>
<br/>
<a href="${pageContext.request.contextPath}/product/edit">商品修改</a>
<br/>
<a href="${pageContext.request.contextPath}/product/delete">商品删除</a>
</body>
</html>
