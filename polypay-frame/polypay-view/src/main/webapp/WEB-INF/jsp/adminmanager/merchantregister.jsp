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
<!-- 注意 ：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->
</head>


<script type="text/javascript" src="../../static/js/layui.js"></script>
<script src="../../static/js/jquery.min.js" type="text/javascript"></script>

<body>

<blockquote class="layui-elem-quote layui-text">
		商户注册
		<p style="color: red;">提示：默认密码：123456，支付密码123456</p>
	</blockquote>

	<fieldset class="layui-elem-field layui-field-title"
		style="margin-top: 20px;">
		<legend>商户注册</legend>
	
	</fieldset>

	<form class="layui-form layui-form-pane" action="" id="regForm">
		<div class="layui-form-item">
			<label class="layui-form-label"> <i
				class="layui-icon layui-icon-cellphone"></i>
			</label>
			<div class="layui-input-inline">
				<input type="text" name="mobileNumber" id="mobile"
					lay-verify="phone|required" placeholder="商户手机号码" autocomplete="off"
					onchange="isPoneAvailable()" 
					class="layui-input">
			</div>
			<span id="mobilespan" style="display: none"><em></em></span>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label"> <i
				class="layui-icon layui-icon-vercode"></i>
			</label>
			<div class="layui-input-inline">
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
			<label class="layui-form-label">费率
			</label>
			<div class="layui-input-inline">
				<input type="text" name="rate" id="rate"
					lay-verify="required" placeholder="费率"
					class="layui-input">
			</div>
			<div class="layui-form-mid layui-word-aux">以千分位为计数单位</div>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">冻结率
			</label>
			<div class="layui-input-inline">
				<input type="text" name="payLevel" id="payLevel"
					lay-verify="number" placeholder="冻结率"
					class="layui-input">
			</div>
			<div class="layui-form-mid layui-word-aux">0 代表D+0, 数字代表 D+1 以及冻结率 比如 30 表示  D+1 冻结30%</div>
		</div>
		

		<div class="layui-form-item">
			<label class="layui-form-label">开通代付</label>
			<div class="layui-input-block">
				<input type="checkbox" name="switch" lay-skin="switch">
			</div>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">支付通道</label>
			<div class="layui-input-block">
				<select name="payChannel">
					<option value="1" >熊猫代币</option>
					<option value="2" >新网支付</option>
				</select>
			</div>
		</div>


		<div class="layui-form-item">
			<div class="layui-input-block">
				<button class="layui-btn" lay-submit lay-filter="reg-submit">注册商户</button>
				<button type="reset" class="layui-btn layui-btn-primary">重置</button>
			</div>
		</div>
	</form>

	<script type="text/javascript">
		
		
	
		layui.use([ 'form', 'layer' ], function() {
			var form = layui.form;
			//监听提交
			form.on('submit(reg-submit)', function(data) {
				$.ajax({
					url : '<%= basePath%>proxy/register/merchant',
					type : 'POST',
					datatype : 'json',
					data : $("#regForm").serialize(),
					success : function(data) {
						if (data.status == '0') {
							layer.alert('注册成功,确定关闭窗口?', {
								icon : 1
							}, function() {
								var index = parent.layer
										.getFrameIndex(window.name); //获取当前窗口的name
								parent.layer.close(index);
								window.parent.location.reload();
							});
						}
						else{
							layer.confirm('注册失败 '+data.message, {
								  btn: ['继续注册', '刷新页面']
							,time:5000
							}, function(index, layero){
								layer.close(layer.index);
								
							}, function(index){
								var index = parent.layer
										.getFrameIndex(window.name); //获取当前窗口的name
								parent.layer.close(index);
								window.location.reload();
								});
						
						}
					}
				});
				return false;
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
			var mobileNumber = '${sessionScope.merchant_user.mobileNumber}';

			$.ajax({
				url : "<%= basePath%>merchant/verifycode",
				type : "post",
				dataType : "json",
				data : {
					mobileNumber : mobileNumber,
					verifyType : "REGISTER_MERCHANT"
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
			var mobileNumber = $("#mobile").val();
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
				url : "<%= basePath%>/merchant/check",
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
		};
	</script>

</body>
</html>