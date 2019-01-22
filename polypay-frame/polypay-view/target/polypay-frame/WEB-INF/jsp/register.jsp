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
<title>源丰盛支付</title>

<link rel="shortcut icon" href="<%=basePath %>/favicon.ico"/>
<link href="static/css/style.css" rel="stylesheet">
<link href="static/js/css/layui.css" rel="stylesheet">
<link href="static/css/admin.css" rel="stylesheet">
<link href="static/css/pro.css" rel="stylesheet">
<link href="static/css/login.css" rel="stylesheet">
<script src="static/js/jquery.min.js" type="text/javascript"></script>
<script src="static/js/layui.js" type="text/javascript"></script>
<script src="static/js/gt.js"></script>
<style type="text/css">
</style>
</head>
<body>
	<!--导航栏部分-->
	<jsp:include page="userview/include/header.jsp" />

	<!-- 中间内容 -->
	<div class="container-fluid">
		<form id="regForm">
			<div class="layadmin-user-login layadmin-user-display-show"
				style="padding-top: 30px; padding-bottom: 30px;" id="LAY-user-login">
				<div class="layadmin-user-login-main">
					<div class="layadmin-user-login-box layadmin-user-login-header">
						<h2>用户注册</h2>
					</div>
					<div
						class="layadmin-user-login-box layadmin-user-login-body layui-form">
		
						<div class="layui-form-item">
							<label
								class="layadmin-user-login-icon layui-icon layui-icon-cellphone"
								for="LAY-user-login-cellphone"></label> <input type="text"
								name="mobileNumber" id="LAY-user-login-cellphone"
								onchange="isPoneAvailable()" lay-verify="phone"
								placeholder="手机" class="layui-input" />
								<span id="mobilespan" style="display: none"><em></em></span>
						</div>


						<div class="layui-form-item">
							<label
								class="layadmin-user-login-icon layui-icon layui-icon-vercode"
								for="LAY-user-login-code" style="z-index: 1;"></label>
							<!-- <label class="layui-form-label"><i class="layui-icon layui-icon-vercode"></i></label> -->
							<div class="layui-row inline-block">
								<div class="layui-col-xs8">
									<input name="verifyCode" id="code" title="输入验证码" type="text"
										lay-verify="required" placeholder="验证码" class="layui-input">

								</div>
								<div class="layui-col-xs4" style="padding-left: 20px">
									<button id="btn1" onclick="sendVerifyCode()"
										class="layui-btn layui-btn-radius layui-btn-normal"
										disabled="disabled">
										<span id="span1" style="display: none;">获取验证码 </span> <span
											id="span2" style="display: none;"><em>0</em>秒重新获取</span>
									</button>
								</div>
							</div>
						</div>
						
						<div class="layui-form-item">
							<label
								class="layadmin-user-login-icon layui-icon layui-icon-password"
								for="LAY-user-login-password"></label> <input type="password"
								name="passWord" id="LAY-user-login-password" lay-verify="pass|pwd"
								onblur="checkPwd(this)" placeholder="密码" class="layui-input">
							<span id="pwdspan" style="display: none"><em></em></span>
						</div>
							<div class="layui-form-item">
							<label
								class="layadmin-user-login-icon layui-icon layui-icon-password"
								for="LAY-user-login-repass"></label> <input type="password"
								name="repass" id="LAY-user-login-repass" lay-verify="required"
								placeholder="确认密码" class="layui-input">
						</div>
						<div class="layui-form-item">
							<label
								class="layadmin-user-login-icon layui-icon layui-icon-password"
								for="LAY-user-login-paypass"></label> <input type="password"
								name="payPassword" id="LAY-user-login-paypass"
								lay-verify="required|paypwd" placeholder="支付密码" class="layui-input">
						</div>
						 
						<div class="layui-form-item">
							<label
								class="layadmin-user-login-icon layui-icon layui-icon-password"
								for="LAY-user-login-repaypass"></label> <input type="password"
								name="repaypass" id="LAY-user-login-repaypass" lay-verify="required"
								placeholder="确认支付密码" class="layui-input">
						</div>

						<div class="layui-form-item" style="height: 50px;">
							<input type="checkbox" name="agreement" lay-skin="primary"
								title="同意用户协议" checked="">
							<div
								class="layui-unselect layui-form-checkbox layui-form-checked"
								lay-skin="primary">
								<span>同意用户协议</span><i class="layui-icon layui-icon-ok"></i>
							</div>
						</div>
						<div class="layui-form-item">
							<button class="layui-btn layui-btn-fluid" id="regSubmit"
								lay-submit="" type="button" lay-filter="reg-submit">注 册</button>
						</div>
						
					</div>
				</div>
			</div>
		</form>
	</div>
	<!--尾部-->
	<jsp:include page="userview/include/foot.jsp" />
	<script type="text/javascript">
		layui.use([ 'form', 'layer' ], function() {
			var form = layui.form;
			var layer = layui.layer;
			form.verify({
                  pwd: function(value) {
                	if($("#LAY-user-login-password").val()!=$("#LAY-user-login-repass").val())
                	{
                	 return "两次密码不一致";
                	}
                  },
				paypwd: function(value)
				{
					if($("#LAY-user-login-paypass").val()!=$("#LAY-user-login-repaypass").val())
                	{
                	 return "两次支付密码不一致";
                	}
					
				}
			
            });
			
			form.on('submit(reg-submit)', function() {
				$.post("merchant/register", $("#regForm").serialize(),
						function(data) {
							if (data.status == 0) {
								layer.msg("注册成功！即将转向登陆页面！", {
									icon : 1,
									anim : 4,
									time : 2000
								}, function() {
									window.location.href = "view/admin";
								});
							} else {
								layer.msg("注册失败！请重试！"+data.message, {
									icon : 5,
									anim : 6,
									time : 2000
								});
							}
						});
			});
		});
	
		//发送手机验证码的方法
		function sendVerifyCode() {
			$("#btn1").attr("disabled", "disabled");
			$("#btn1").addClass("layui-btn-disabled");

			var i = 60;
			$("#span1").hide();
			$("#span2 em").text(i);
			$("#span2").show();
			//$("#btn1").text("重新发送(60秒)");
			var mobileNumber = $("#LAY-user-login-cellphone").val();

			$.ajax({
				url : "merchant/verifycode",
				type : "post",
				dataType : "json",
				data : {
					mobileNumber : mobileNumber,
					verifyType : "REGISTER"
				},
				success : function(data) {
					// 调用 initGeetest 初始化参数
					// 参数1：配置参数
					// 参数2：回调，回调的第一个参数验证码对象，之后可以使用它调用相应的接口
				}
			});

			var close = setInterval(function() {
				i--;
				if (i <= 0) {
					$("#span1").show();
					$("#span2").hide();
					//$("#btn1").text("重新发送");
					$("#btn1").removeClass("layui-btn-disabled");
					$("#btn1").removeAttr("disabled");
					clearInterval(close);
				} else {
					$("#span2 em").text(i);
				}
			}, 1000);

		}

		function isPoneAvailable() {
			var mobileNumber = $("#LAY-user-login-cellphone").val();
			var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
			if (!myreg.test(mobileNumber)) {

				$("#mobilespan").show();
				$("#span1").hide();
				$("#btn1").attr("disabled", "disabled");
				$("#mobilespan em").css("color", "red");
				$("#mobilespan em").text("请输入手机号");

				return false;
			}

			$.ajax({
				url : "merchant/check",
				type : "post",
				dataType : "json",
				data : {
					mobileNumber : mobileNumber
				},
				success : function(data) {
					// 调用 initGeetest 初始化参数
					// 参数1：配置参数
					// 参数2：回调，回调的第一个参数验证码对象，之后可以使用它调用相应的接口
					if (data.status == 0) {
						$("#span1").show();
						$("#mobilespan em").text("");
						$("#mobilespan em").css("color", "green");
						$("#mobilespan").hide();
						$("#btn1").removeClass("layui-btn-disabled");
						$("#btn1").removeAttr("disabled");
						return true;
					} else {
						$("#mobilespan").show();
						$("#span1").hide();
						$("#btn1").attr("disabled", "disabled");
						$("#mobilespan em").css("color", "red");
						$("#mobilespan em").text(data.message);
						return false;
					}
				}
			});

		}

		function checkPwd(pwd) {
			var pwd = pwd.value;
			var patrn = /^(\w){6,20}$/;
			if (patrn.exec(pwd))
			{
				$("#pwdspan").hide();
				$("#pwdspan em").text("");
			}else{
				$("#pwdspan").show();
				$("#pwdspan em").css("color", "red");
				$("#pwdspan em").text("密码必须由6-20位字符+数字组成");
			}
		}
	</script>

	<script type="text/javascript">
		var handler1 = function(captchaObj) {
			$("#submit1").click(function(e) {
				var result = captchaObj.getValidate();
				if (!result) {
					$("#notice1").show();
					setTimeout(function() {
						$("#notice1").hide();
					}, 2000);
					e.preventDefault();
				}
			});
			console.log("captchaObj", captchaObj);
			// 将验证码加到id为captcha的元素里，同时会有三个input的值用于表单提交
			captchaObj.appendTo("#captcha1");
			//监听验证按钮生成完毕之后的事件
			captchaObj.onReady(function() {
				$("#wait1").hide();
			});
			//监听验证成功事件
			captchaObj.onSuccess(function() {
				$("#regSubmit").removeClass("layui-btn-disabled");
				$("#regSubmit").removeAttr("disabled");

			});

			// 更多接口参考：http://www.geetest.com/install/sections/idx-client-sdk.html
			// https://docs.geetest.com/install/deploy/client/web
		};
	</script>
</body>
</html>