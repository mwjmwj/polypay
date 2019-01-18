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
			<label class="layui-form-label">账单名</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${merchantbill.billName }" class="layui-input"> 
			</div>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">充值总金额</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${merchantbill.rechargeAmount }" class="layui-input"> 
			</div>
		</div>
		
	<%-- 	<div class="layui-form-item">
			<label class="layui-form-label">充值服务费</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${merchantbill.rechargeServiceAmount }" class="layui-input"> 
			</div>
		</div> --%>
		
		
		<div class="layui-form-item">
			<label class="layui-form-label">充值次数</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${merchantbill.rechargeNumber }" class="layui-input"> 
			</div>
		</div>
		
		
		<div class="layui-form-item">
			<label class="layui-form-label">结算总金额</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${merchantbill.settleAmount }" class="layui-input"> 
			</div>
		</div>
		
		<%-- <div class="layui-form-item">
			<label class="layui-form-label">结算服务费</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${merchantbill.settleServiceAmount }" class="layui-input"> 
			</div>
		</div> --%>
		
		
		<div class="layui-form-item">
			<label class="layui-form-label">结算次数</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${merchantbill.settleNumber }" class="layui-input"> 
			</div>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">代付总金额</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${merchantbill.placeAmount }" class="layui-input"> 
			</div>
		</div>
		
	<%-- 	<div class="layui-form-item">
			<label class="layui-form-label">代付服务费</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${merchantbill.placeServiceAmount }" class="layui-input"> 
			</div>
		</div> --%>
		
		
		<div class="layui-form-item">
			<label class="layui-form-label">代付次数</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${merchantbill.placeNumber }" class="layui-input"> 
			</div>
		</div>
		
	
	
		<div class="layui-form-item">
			<label class="layui-form-label">生成时间</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" class="layui-input" value="<fmt:formatDate value='${merchantbill.createTime}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>"></input>
			</div>
		</div>
		
		
		
	</form>


</body>
</html>