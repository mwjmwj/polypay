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
<link rel="stylesheet" href="../static/css/layui.css">
<!-- 注意：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->

</head>
<script type="text/javascript" src="../static/js/layui.js"></script>
<script src="../static/js/jquery.min.js" type="text/javascript"></script>
<body>
<div >
	<form class="layui-form">
		<!-- 提示：如果你不想用form，你可以换成div等任何一个普通元素 -->
		
		<div class="layui-form-item layui-form-pane">
			<label class="layui-form-label">账户名</label>
			<div class="layui-input-block">
			<div class="layui-inline">
				<input type="text" name="" placeholder="请输入" autocomplete="off" value="${merchantAccount.accountName }"
					class="layui-input" style="width: 200px">
					</div>
			</div>
		</div>
		
		
		<div class="layui-form-item layui-form-pane">
			<label class="layui-form-label">绑定手机号</label>
			<div class="layui-input-block">
			<div class="layui-inline">
				<input type="text" name="mobileNumber" id="mobileNumber" placeholder="请输入" autocomplete="off" value="${merchantAccount.mobileNumber }"
					class="layui-input" style="width: 200px;background-color: #DDDDDD" disabled="disabled" lay-filter="phone">
					</div>
					<div onclick="updatemobile()" class="layui-btn layui-btn-normal" id="mobilebtn">解除锁定</div>
			</div>
		</div>
		
		<div class="layui-form-item layui-form-pane">
			<label class="layui-form-label">创建时间</label>
			<div class="layui-input-block">
			<div class="layui-inline">
			
				<input type="text" name="" autocomplete="off" value="<fmt:formatDate value='${merchantAccount.createTime}' type='date' pattern='yyyy-MM-dd HH:mm:ss'/>"
					class="layui-input" style="width: 200px" disabled="disabled">
					</div>
			</div>
		</div>
		
		<c:if test="${merchantAccount.helppayStatus ==1 }">
			<div class="layui-form-item layui-form-pane">
				<label class="layui-form-label">开关关</label>
				<div class="layui-input-block">
					<input type="checkbox" lay-skin="switch">
				</div>
			</div>
		</c:if>
		
		<c:if test="${merchantAccount.helppayStatus ==0 }">
		<div class="layui-form-item layui-form-pane">
			<label class="layui-form-label">代付功能</label>
			<div class="layui-input-block">
				<input type="checkbox" checked lay-skin="switch">
			</div>
		</div>
		</c:if>
	
		<div class="layui-form-item layui-form-pane">
			<label class="layui-form-label">商户号等级</label>
			<div class="layui-input-block">
			<div class="layui-inline">
			
				<input type="text" name="" autocomplete="off" value="${merchantAccount.payLevel}"
					class="layui-input" style="width: 200px" disabled="disabled">
					</div>
			</div>
		</div>
		
		<div class="layui-form-item">
    	<div class="layui-input-block">
      <button class="layui-btn" lay-submit lay-filter="save">保存</button>
      <button type="reset" class="layui-btn layui-btn-primary">重置</button>
   	 </div>
  </div>
	
		<!-- 更多表单结构排版请移步文档左侧【页面元素-表单】一项阅览 -->
	</form>
	
	</div>
	<script>
	layui.use('form', function(){
  var form = layui.form;
  
	//监听提交
	  form.on('submit(save)', function(data){
	    layer.msg(JSON.stringify(data.field));
	    return false;
	  });

  //各种基于事件的操作，下面会有进一步介绍
	});
	function updatemobile()
	{
		$("#mobileNumber").removeAttr("disabled");
		$("#mobileNumber").attr("style","width: 200px;background-color: white");
	};

</script>
</body>
</html>