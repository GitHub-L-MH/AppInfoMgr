<%--
  Created by IntelliJ IDEA.
  User: L-PC
  Date: 2017/11/2
  Time: 16:13
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<input type="hidden" id="path" name="path" value="<%=basePath%>"/>
<script type="text/javascript" src="${pageContext.request.contextPath }/statics/js/jquery-1.8.3-mh-community.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/statics/js/common.js"></script>
</body>
</html>
