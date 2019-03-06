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
<link rel="stylesheet" href="../../static/js/css/layui.css">
<!-- 注意：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->

</head>
<script type="text/javascript" src="../../static/js/layui.js"></script>
<script src="../../static/js/jquery.min.js" type="text/javascript"></script>
<body style="background-color: white">


   <fieldset class="layui-elem-field layui-field-title"
		style="margin-top: 20px;">
		<legend>商户财务信息</legend>
	</fieldset>
	
<div >
		<form class="layui-form">
			<div class="layui-form-item">
				<label class="layui-form-label">商户号</label>
				<div class="layui-input-block">
					<input type="text" name="merchantId" lay-verify="title"
						style="width: 50%;" autocomplete="off" placeholder="商户号"
						value="${merchantfinance.merchantId }" class="layui-input"
						readonly="readonly">
				</div>
			</div>

				<div class="layui-form-item">
					<label class="layui-form-label">账户总金额</label>
					<div class="layui-input-block">
						<input type="text" lay-verify="title"
							style="width: 50%;" autocomplete="off" placeholder="账户总余额"
							value="${merchantfinance.blanceAmount+merchantfinance.fronzeAmount }元"
							class="layui-input" readonly="readonly">
					</div>
				</div>


			<input type="hidden" name="id" value="${merchantfinance.id}">
				<div class="layui-form-item">
					<label class="layui-form-label">可提现金额</label>
					<div class="layui-input-block">
						<input type="text" lay-verify="title"
							style="width: 50%;" autocomplete="off" placeholder="可提现金额"
							value="${merchantfinance.blanceAmount }元" class="layui-input"
							readonly="readonly">
					</div>
				</div>

				<div class="layui-form-item">
					<label class="layui-form-label">冻结金额</label>
					<div class="layui-input-block">
						<input type="text"lay-verify="title"
							style="width: 50%;" autocomplete="off" placeholder="冻结金额"
							value="${merchantfinance.fronzeAmount }元" class="layui-input"
							readonly="readonly">
					</div>
				</div>


				<div class="layui-form-item">
					<label class="layui-form-label">支付密码</label>
					<div class="layui-input-block">
						<input type="text" name="payPassword" lay-verify="title" id="payPassword"
							style="width: 50%;" autocomplete="off" placeholder="支付密码"
							value="${merchantfinance.payPassword }" class="layui-input" onchange="securityPas()"
							>
					</div>
				</div>


				<div class="layui-form-item">
					<label class="layui-form-label">支付状态</label>
					<div class="layui-input-block" style="width:195px;">
						
						<select name="status">
							<option value="">审核状态</option>
							<c:if test="${merchantfinance.status ==0}">
								<option value="0" selected="selected">可用</option>
								<option value="-1">冻结</option>
							</c:if>
							<c:if test="${merchantfinance.status ==-1}">
								<option value="0">可用</option>
								<option value="-1" selected="selected">冻结</option>
							</c:if>
						</select>
					</div>
				</div>
				

				<div class="layui-form-item">
		    	<div class="layui-input-block">
			      <button class="layui-btn" lay-submit lay-filter="audit">保存</button>
			      <button type="reset" class="layui-btn layui-btn-primary">重置</button>
			   	 </div>
		  		</div>
		</form>
	</div>
	
	
	<script type="text/javascript" src="../../static/js/md5.js"></script>
	<script>
	
	
	layui.use('form', function(){
  	var form = layui.form;
	//监听提交
	  form.on('submit(audit)', function(data){
				$.ajax({
					url:"<%= basePath%>merchantmanager/merchantfinance/update",
					type:"post",
					data:$(".layui-form").serialize(),
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

  //各种基于事件的操作，下面会有进一步介绍
	});
	
	function securityPas()
	{
		$("#payPassword").val(MD5($("#payPassword").val()));
	}

</script>


</body>
</html>