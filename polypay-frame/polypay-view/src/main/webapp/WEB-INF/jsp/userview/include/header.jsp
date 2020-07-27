<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<meta HTTP-EQUIV="pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<meta HTTP-EQUIV="expires" CONTENT="0">
<title>大成支付</title>
<link href="static/css/bootstrap.min.css" rel="stylesheet">
<link href="static/css/style.css" rel="stylesheet">
<script type="text/javascript" src="static/js/jquery.1.12.4.min.js"></script>
<script src="static/js/bootstrap.min.js" type="text/javascript"></script>
</head>
<body>
	<!--导航栏部分-->
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container-fluid" style="background-color: #F0F0F0">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
					aria-expanded="false">
					<span class="sr-only">poly pay</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="view/admin">乾通支付</a>
			</div>

			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">

				<ul class="nav navbar-nav navbar-right" style="padding-right:30px;">
					<c:if test="${ empty sessionScope.user}">
						<!-- <li><a href="view/register" >注册</a></li> -->
						<li><a href="view/admin">登录</a></li>
					</c:if>
					
				</ul>

				<div class="navbar-form navbar-right">
			</div>
		</div>
		</div>
	</nav>
</body>
</html>