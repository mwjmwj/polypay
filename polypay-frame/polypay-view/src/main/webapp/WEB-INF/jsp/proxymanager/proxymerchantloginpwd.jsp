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
		<legend>修改登录密码</legend>
	</fieldset>
	<form class="layui-form layui-form-pane" action="" id="updatepwdform" style="margin-left: 3%">

		<div class="layui-form-item">
			<label class="layui-form-label">绑定手机号</label>
			<div class="layui-input-inline">
				<input type="text" id="mobileNumber" name="mobileNumber"
					value="${sessionScope.merchant_user.mobileNumber }"
					readonly="readonly" lay-verify="required"
					autocomplete="off" class="layui-input">
				
					
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">验证码</label>
			<div class="layui-input-inline">
				<div class="layui-col-xs8">
					<input name="verifyCode" id="code" title="输入验证码" type="text"
						lay-verify="required" placeholder="请输入验证码" class="layui-input">
				</div>
				<div class="layui-col-xs4" style="padding-left: 20px">
					<button id="btn1" onclick="sendVerifyCode()"
						class="layui-btn layui-btn-radius layui-btn-normal">
						<span id="span1">获取验证码 </span> <span id="span2"
							style="display: none;"><em>0</em>秒重新获取</span>
					</button>
				</div>
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">新密码</label>
			<div class="layui-input-inline">
				<input type="password" id="pwd" lay-verify="require|uppwd"
					name="newPassword" placeholder="请输入新密码" autocomplete="off"
					class="layui-input">
			</div>
			<div class="layui-form-mid layui-word-aux">新密码不可与旧密码相同</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">确认新密码</label>
			<div class="layui-input-inline">
				<input type="password" id="repwd" name="confirmPassword"
					placeholder="确认新密码" autocomplete="off" class="layui-input">
			</div>
		</div>



		<div class="layui-form-item right">
			<button class="layui-btn"  type="button" lay-submit="" lay-filter="updatepwd-submit">提交</button>
		</div>
	</form>


	<script type="text/javascript" src="../static/js/md5.js"></script>
	<script type="text/javascript">
		layui.use([ 'form', 'layer' ], function() {
			var form = layui.form;
			var layer = layui.layer;
			form.verify({
				uppwd : function(value) {
					if ($("#pwd").val() != $("#repwd").val()) {
						return "两次密码不一致";
					}
				}
			});

			form.on('submit(updatepwd-submit)', function() {
				
				var mobileNumber = $("#mobileNumber").val();
				var newPassword = MD5($("#pwd").val());
				var verifyCode = $("#code").val();
				var data = {mobileNumber:mobileNumber,newPassword:newPassword,verifyCode:verifyCode,verifyType:"UPDATE_PWD"};
				
				$.post("../merchant/password/update", 
						data,
						function(data) {
					if (data.status == 0) {
						layer.msg("修改成功，退出后新密码登录！", {
							icon : 1,
							anim : 4,
							time : 2000
						});
					} else {
						layer.msg("修改失败！请重试！" + data.message, {
							icon : 5,
							anim : 6,
							time : 2000
						});
					}
				});
				return false;
			});
		});
	</script>
	<script type="text/javascript">
		//发送手机验证码的方法
		function sendVerifyCode() {
			$("#btn1").attr("disabled", "disabled");
			$("#btn1").addClass("layui-btn-disabled");

			var i = 60;
			$("#span1").hide();
			$("#span2 em").text(i);
			$("#span2").show();
			//$("#btn1").text("重新发送(60秒)");
			var mobileNumber = $("#mobileNumber").val();

			$.ajax({
				url : "../merchant/verifycode",
				type : "post",
				dataType : "json",
				data : {
					mobileNumber : mobileNumber,
					verifyType : "UPDATE_PWD"
				},
				success : function(data) {

					layer.msg(data.message);
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
	</script>


</body>
</html>