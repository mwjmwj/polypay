<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<head>
<base href="<%=basePath%>">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>poly-pay</title>
<link href="static/css/style.css" rel="stylesheet">
<link href="static/js/css/layui.css" rel="stylesheet">
<link href="static/css/admin.css" rel="stylesheet">
<link href="static/css/pro.css" rel="stylesheet">
<link href="static/css/login.css" rel="stylesheet">
<script src="static/js/jquery.min.js" type="text/javascript"></script>
<script src="static/js/layui.js" type="text/javascript"></script>
<style type="text/css">
</style>
</head>
<body>

<jsp:include page="userview/include/header.jsp" />
	<div class="container-fluid">
	<form id="loginForm">
		<div class="layadmin-user-login layadmin-user-display-show" style="padding-bottom:0px;padding-top:50px;height:250px;"
			id="LAY-user-login">
			<div class="layadmin-user-login-main" style="height:200px;">
				<div class="layadmin-user-login-box layadmin-user-login-header">
					<h2>商户后台管理</h2>
				</div>
				<div
					class="layadmin-user-login-box layadmin-user-login-body layui-form" style="height:100px;">
					<div class="layui-form-item">
						<label
							class="layadmin-user-login-icon layui-icon layui-icon-username"
							for="LAY-user-login-username"></label> <input type="text"
							name="mobileNumber" id="mobileNumber"
							lay-verify="phone" placeholder="手机号"
							class="layui-input layui-form-danger">
					</div>
				
					<div class="layui-form-item">
						<label
							class="layadmin-user-login-icon layui-icon layui-icon-password"
							for="LAY-user-login-password"></label> <input type="password"
							name="passWord" id="LAY-user-login-password"
							lay-verify="required" placeholder="密码" class="layui-input">
					</div>
					
					<div class="layui-form-item">
						<div class="verify-wrap" id="verify-wrap2"></div>
					</div>
					
					<!-- <div class="layui-form-item">
						<label
							class="layadmin-user-login-icon layui-icon layui-icon-password"
							for="LAY-user-login-code"></label> <input type="password"
							name="verifyCode" id="LAY-user-login-code"
							lay-verify="required" placeholder="验证码" class="layui-input">
					</div> -->
					
					<!-- <div class="layui-form-item" style="height:50px;">
						<input type="checkbox" name="remember" lay-skin="primary"
							title="记住密码">
						<div class="layui-unselect layui-form-checkbox" lay-skin="primary">
							<span>记住密码</span><i class="layui-icon layui-icon-ok"></i>
						</div>
						<a lay-href="/user/forget"
							class="layadmin-user-jump-change layadmin-link"
							style="margin-top: 7px;">忘记密码？</a>
					</div> -->
					<div class="layui-form-item">
						<button id="btn1" class="layui-btn layui-btn-fluid layui-btn-disabled" disabled="disabled" type="button" lay-submit=""
							lay-filter="loginSubmit">登录</button>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
	<!--尾部-->
	<jsp:include page="userview/include/foot.jsp" />
	
	
	<script src="<%=basePath %>/static/js/jquery.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%= basePath %>/static/js/js2.js"></script>

	<script type="text/javascript" defer="defer">
	//var inputwidth = parseInt($(".layui-form-item").width());
	var slideVerify2 = new slideVerifyPlug('#verify-wrap2',{
				wrapWidth: '100%',
	            initText:'请按住滑块',
	            sucessText:'验证通过',
	           	successFun: function(){
	           		$("#btn1").removeClass("layui-btn-disabled");
					$("#btn1").removeAttr("disabled");
				}
			});
	</script>
	
	<script type="text/javascript" src="static/js/md5.js"></script>
	<script type="text/javascript">
		layui.use(['form','layer'], function() {
			var form = layui.form;
			var layer=layui.layer;
			form.on('submit(loginSubmit)',function(){
				
				var password = MD5($("#LAY-user-login-password").val());
				var data = {mobileNumber:$("#mobileNumber").val(),passWord:password};
				$.ajax({
					type:"post",
					url:"merchant/login",
					data:data,
					success:function(data){
						if(data=="success"){
							layer.msg("登陆成功！",{icon:1,anim:2,time:100},function(){
								window.location.href="view/toAdminIndex";
							});
						}else{
							layer.msg("登陆失败！请检查用户名和密码后重试！",{icon:5,anim:6,time:3000});
						}
					}
				});
			});
		});
	</script>
</body>
</html>