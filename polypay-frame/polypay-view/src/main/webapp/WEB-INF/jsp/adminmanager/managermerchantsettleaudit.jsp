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
<script type="text/javascript" src="../../static/js/layui.js"></script>
<script src="../../static/js/jquery.min.js" type="text/javascript"></script>
<body>

	<form class="layui-form layui-form-pane" id="settleform">
		<!-- 提示：如果你不想用form，你可以换成div等任何一个普通元素 -->
		<div class="layui-form-item">
			<label class="layui-form-label">订单号</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${settleorder.orderNumber }" class="layui-input"> 
			</div>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">商户ID</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${settleorder.merchantId }" class="layui-input"> 
			</div>
		</div>
		
		<input type="hidden"  name="id" value="${settleorder.id }" />
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
				<input type="text" readonly="readonly" value="待审核" class="layui-input" style="color:red"> 
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
			<label class="layui-form-label">银行卡号</label>
			<div class="layui-input-block">
				<input type="text" readonly="readonly" value="${settleorder.merchantBindBank }" class="layui-input"> 
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
				<input type="text"  name="descreption" value="${settleorder.descreption }" class="layui-input"> 
			</div>
		</div>
		
		
		<div class="layui-form-item">
    		<div class="layui-input-block">
	      		<button class="layui-btn" lay-submit lay-filter="audit">确认结算</button>
	      		<button class="layui-btn" lay-submit lay-filter="fail">取消订单</button>
	   		 </div>
  		</div>
		
	</form>
	
	<script>
	layui.use('form', function(){
  	var form = layui.form;
  
	//监听提交
	  form.on('submit(audit)', function(data){
				$.ajax({
					url:"<%= basePath%>merchantmanager/settleorder/audit",
					type:"post",
					data:$("#settleform").serialize(),
					datatype:'JSON',
					success:function(data){
						if (data == 'success') {
							layer.alert('修改成功,确定关闭窗口?', {
								icon : 1
							}, function() {
								var index = parent.layer
										.getFrameIndex(window.name); //获取当前窗口的name
								parent.layer.close(index);
								window.parent.location.reload();
							});
						}
						else{
							layer.confirm('修改失败 '+data.message, {
								  btn: ['继续修改', '退出修改']
							,time:2000
							}, function(index, layero){
								layer.close(layer.index);
								
							}, function(index){
								var index = parent.layer
										.getFrameIndex(window.name); //获取当前窗口的name
								parent.layer.close(index);
								});
						
						}
					}
				});
				return false;
	  });
	
	   form.on('submit(fail)', function(data){
			$.ajax({
				url:"<%= basePath%>merchantmanager/settleorder/fail",
				type:"post",
				data:$("#settleform").serialize(),
				dataType:'JSON',
				success:function(data){
					 if(data.status == '0'){
						layer.msg('提交成功,2秒后关闭', {
							  icon: 1,
							  time: 2000 //2秒关闭（如果不配置，默认是3秒）
							}, function(){
							  //do something
							});   
					}else{
						layer.msg('提交异常!', {icon: 5});
					} 
				}
			});
			return false;
		});
  //各种基于事件的操作，下面会有进一步介绍
	});

</script>


</body>
</html>