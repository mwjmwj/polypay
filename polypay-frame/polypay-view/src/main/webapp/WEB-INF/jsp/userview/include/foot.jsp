<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<%=basePath%>">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>源盛丰支付</title>
    <link href="lib/css/bootstrap.min.css" rel="stylesheet">
    <link href="lib/css/style.css" rel="stylesheet">
    <script src="lib/js/bootstrap.min.js"></script>
</head>
<body>
    <!-- 尾部 -->
    <hr/>
    <div class="foot-style">
      	Copyright © 2018 - 2019  ysfzf.com 版权所有|消费者维权热线：xxxxxxxx
        <br/>
         源丰盛支付
    </div>
    <!-- 结束啦 -->

</body>
</html>