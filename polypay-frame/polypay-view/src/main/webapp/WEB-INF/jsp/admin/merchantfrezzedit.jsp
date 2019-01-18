<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
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
<link rel="stylesheet" href="../../static/css/layui.css">
<!-- 注意：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->

</head>
<script type="text/javascript" src="../../static/js/layui.all.js"></script>
<body>

	<form class="layui-form layui-form-pane">
		<!-- 提示：如果你不想用form，你可以换成div等任何一个普通元素 -->
		<div class="layui-form-item">
			<label class="layui-form-label">订单号</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${merchantFrezz.orderNumber }" class="layui-input"> 
			</div>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">冻结金额</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${merchantFrezz.amount }" class="layui-input"> 
			</div>
		</div>
		
		
		<div class="layui-form-item">
			<label class="layui-form-label">状态</label>
			<div class="layui-input-block">
			
				<c:if test="${merchantFrezz.status ==0 }">
				<input type="text" readonly="readonly" value="已解冻" class="layui-input" style="color:green"> 
				</c:if>
				
				<c:if test="${merchantFrezz.status ==-1 }">
				<input type="text" readonly="readonly" value="冻结中" class="layui-input" style="color:red"> 
				</c:if>
				
			</div>
		</div>
	
		<div class="layui-form-item">
			<label class="layui-form-label">预计到账时间</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" class="layui-input" value="<fmt:formatDate value='${merchantFrezz.arrivalTime}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>"></input>
			</div>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">冻结时间</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" class="layui-input" value="<fmt:formatDate value='${merchantFrezz.frezzTime}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>"> 
			</div>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">实际到账时间</label>
			<div class="layui-input-block">
				<c:if test="${merchantFrezz.reallyArrivalTime != null }">
				<input type="text" readonly="readonly" class="layui-input" value="<fmt:formatDate value='${merchantFrezz.reallyArrivalTime}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>"> 
				</c:if>
			</div>
		</div>
		
	</form>


</body>
</html>