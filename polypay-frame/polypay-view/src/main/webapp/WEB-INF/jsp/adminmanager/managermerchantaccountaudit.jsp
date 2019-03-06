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
		<legend>商戶個人信息</legend>
	</fieldset>
	
<div >

	<form class="layui-form">
		<!-- 提示：如果你不想用form，你可以换成div等任何一个普通元素 -->
		
		<div class="layui-form-item layui-form-pane">
			<label class="layui-form-label">账户名</label>
			<div class="layui-input-block">
			<div class="layui-inline">
				<input type="text" name="accountName" placeholder="请输入" autocomplete="off" value="${merchantAccount.accountName }"
					class="layui-input" style="width: 212px">
					</div>
			</div>
		</div>
		
		<input type="hidden" name="id" value="${merchantAccount.id }"  />
		
		<div class="layui-form-item layui-form-pane">
			<label class="layui-form-label">绑定手机号</label>
			<div class="layui-input-block">
			<div class="layui-inline">
				<input type="text" name="mobileNumber" id="mobileNumber" placeholder="请输入" autocomplete="off" value="${merchantAccount.mobileNumber }"
					class="layui-input" style="width: 212px" lay-filter="phone">
					</div>
			</div>
		</div>
		
		<div class="layui-form-item layui-form-pane">
			<label class="layui-form-label">商戶密码</label>
			<div class="layui-input-inline">
				<input type="text" name="passWord" lay-verify="required" id="passWord" placeholder="请输入" autocomplete="off" value="${merchantAccount.passWord }"
					class="layui-input" style="width: 212px" lay-filter="password" onchange="securityPas()">
			</div>
			<div class="layui-form-mid layui-word-aux" style="margin-left: 20px">密码显示为加密密码。</div>
		</div>
		
		<div class="layui-form-item layui-form-pane">
			<label class="layui-form-label">创建时间</label>
			<div class="layui-input-block">
			<div class="layui-inline">
			
				<input type="text" name="createTime" autocomplete="off" value="<fmt:formatDate value='${merchantAccount.createTime}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>"
					class="layui-input" style="width: 212px" disabled="disabled">
					</div>
			</div>
		</div>
		
		<c:if test="${merchantAccount.helppayStatus ==1 }">
			<div class="layui-form-item layui-form-pane">
				<label class="layui-form-label">代付功能</label>
				<div class="layui-input-block">
					<input type="checkbox" name="helppayoff" lay-skin="switch">
				</div>
			</div>
		</c:if>
		
		<c:if test="${merchantAccount.helppayStatus ==0 }">
		<div class="layui-form-item layui-form-pane">
			<label class="layui-form-label">代付功能</label>
			<div class="layui-input-block">
				<input type="checkbox" name="helppayoff" checked lay-skin="switch">
			</div>
		</div>
		</c:if>
	
		<div class="layui-form-item layui-form-pane">
			<label class="layui-form-label">冻结费率</label>
			
			<div class="layui-input-inline">
				<input type="text" lay-verify="required" name="payLevel" autocomplete="off" value="${merchantAccount.payLevel}"
					class="layui-input" style="width: 212px">
			</div>
			<div class="layui-form-mid layui-word-aux" style="margin-left: 20px">商户等级关系支付费率</div>
		</div>
		
		
		<div class="layui-form-item layui-form-pane">
			<label class="layui-form-label">审核状态</label>
		 <div class="layui-input-block">
     		 <div class="layui-inline">
      		  <select name="status" style="width: 212px">
	          <option value="">审核状态</option>
	          
	          	<c:if test="${merchantAccount.status ==0}">
	          	<option value="0" selected="selected">审核通过</option>
	          	<option value="1">待审核</option>
	          	<option value="-1">审核不通过</option>
     	 		</c:if>
     	 		<c:if test="${merchantAccount.status ==-1}">
	         	 <option value="0" >审核通过</option>
	          	<option value="-1" selected="selected">审核不通过</option>
	          	<option value="1">待审核</option>
     	 		</c:if>
     	 	
     	 		<c:if test="${merchantAccount.status ==1}">
     	 		<option value="1" selected="selected">待审核</option>
	         	 <option value="0" >审核通过</option>
	          	<option value="-1" >审核不通过</option>
     	 		</c:if>
	          
       		 </select>
      				</div>
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
					url:"<%= basePath%>merchantmanager/accountinfo/update",
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
		$("#passWord").val(MD5($("#passWord").val()));
	}

</script>
</body>
</html>