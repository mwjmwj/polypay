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
<link rel="stylesheet" href="../../static/js/css/layui.css">
<!-- 注意：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->

</head>
<script type="text/javascript" src="../../static/js/layui.all.js"></script>
<body>

	<form class="layui-form layui-form-pane">
		<!-- 提示：如果你不想用form，你可以换成div等任何一个普通元素 -->
		<div class="layui-form-item">
			<label class="layui-form-label">订单号</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${settleorder.orderNumber }" class="layui-input"> 
			</div>
		</div>
		
		
		
		<div class="layui-form-item">
			<label class="layui-form-label">类型</label>
			<div class="layui-input-block">
				<c:if test="${settleorder.type ==1 }">
				<input type="text" readonly="readonly" value="充值订单" class="layui-input">
				</c:if>
				
				<c:if test="${settleorder.type ==2 }">
				<input type="text" readonly="readonly" value="代付订单" class="layui-input">
				</c:if>
				<c:if test="${settleorder.type ==3 }">
				<input type="text" readonly="readonly" value="结算订单" class="layui-input">
				</c:if>
			</div>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">状态</label>
			<div class="layui-input-block">
			
				<c:if test="${settleorder.status ==0 }">
				<input type="text" readonly="readonly" value="成功" class="layui-input" style="color:green"> 
				</c:if>
				
				<c:if test="${settleorder.status ==-1 }">
				<input type="text" readonly="readonly" value="失败" class="layui-input" style="color:red"> 
				</c:if>
				
				
					<c:if test="${settleorder.status ==1 }">
				<input type="text" readonly="readonly" value="审核中" class="layui-input" style="color:orange"> 
				</c:if>
				
					<c:if test="${settleorder.status ==2 }">
				<input type="text" readonly="readonly" value="已处理" class="layui-input" style="color:#00FF00"> 
				</c:if>
				
			</div>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">结算金额</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${settleorder.postalAmount }" class="layui-input"> 
			</div>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">服务费</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${settleorder.serviceAmount }" class="layui-input"> 
			</div>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">到账金额</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${settleorder.arrivalAmount }" class="layui-input"> 
			</div>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">发起时间</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" class="layui-input" value="<fmt:formatDate value='${settleorder.createTime}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>"></input>
			</div>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">处理时间</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" class="layui-input" value="<fmt:formatDate value='${settleorder.payTime}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>"> 
			</div>
		</div>
	
		
		<div class="layui-form-item">
			<label class="layui-form-label">描述</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${settleorder.descreption }" class="layui-input"> 
			</div>
		</div>
		
		
	</form>


</body>
</html>