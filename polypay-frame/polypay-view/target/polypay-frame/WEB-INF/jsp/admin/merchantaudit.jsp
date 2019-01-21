<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>poly-pay</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="../static/js/css/layui.css">
<!-- 注意：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->

</head>
<script type="text/javascript" src="../static/js/layui.all.js"></script>
<script src="../static/js/jquery.min.js" type="text/javascript"></script>
<body>
	<fieldset class="layui-elem-field layui-field-title"
		style="margin-top: 50px;">
		<legend>认证信息</legend>
	</fieldset>

</body>
</html>