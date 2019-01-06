<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>商户后台管理</title>
<link rel="stylesheet" href="resources/css/layui.css">
<script src="resources/js/jquery.1.12.4.min.js"></script>
<script src="resources/js/layui.js"></script>
</head>
<body class="layui-layout-body">
	<div class="layui-layout layui-layout-admin">
		<div class="layui-header">
			<div class="layui-logo">商户后台管理</div>
			<!-- 头部区域（可配合layui已有的水平导航） -->
			<ul class="layui-nav layui-layout-left">
				<li class="layui-nav-item"><a href="view/welcome" target="myframe">主页</a></li>
			</ul>
			<ul class="layui-nav layui-layout-right">
				<li class="layui-nav-item"><a href="javascript:;"> <img
						src="upload/headpic.jpg" class="layui-nav-img">${sessionScope.merchant_user.mobileNumber }</a>
				</li>
				<li class="layui-nav-item"><a href="merchant/exit">退出</a></li>
			</ul>
		</div>

		<div class="layui-side layui-bg-black">
			<div class="layui-side-scroll">
				<!-- 左侧导航区域（可配合layui已有的垂直导航） -->
				<ul class="layui-nav layui-nav-tree" lay-filter="test">
					<c:forEach items="${sessionScope.menu}" var="f">
						<c:if test="${ not empty f }">
							<li class="layui-nav-item">
							<a class=""	href="javascript:;">${f.menuName }</a>
								<dl class="layui-nav-child">
									<c:forEach items="${f.childMenu }" var="t">
										<dd>
											<a href="${pageContext.request.contextPath }/${t.menuUrl }" target="${t.menuTarget }">${t.menuName }</a>
										</dd>
									</c:forEach>
								</dl>
						</c:if>
						<c:if test="${empty f }">
							<li class="layui-nav-item">
							<a href="${pageContext.request.contextPath }/${f.menuUrl }" target="${f.menuTarget }">${f.menuName }</a>
						</c:if>
					</li>
					</c:forEach>
				</ul>
			</div>
		</div>

		<div class="layui-body">
			<!-- 内容主体区域 -->
			<iframe src="view/welcome" name="myframe" style="width:1140px;height:550px;border: 0;" ></iframe>
		</div>

		<div class="layui-footer">
			<!-- 底部固定区域 -->
			© LEenjoy.com - 底部固定区域
		</div>
	</div>
	
	<script>
		//JavaScript代码区域
		layui.use(['element','layer', 'table'], function() {
			var element = layui.element;
			var table = layui.table;
			var layer = layui.layer;
		});
	</script>
</body>
</html>