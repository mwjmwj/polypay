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

	<fieldset class="layui-elem-field layui-field-title"
		style="margin-top: 20px;">
		<legend>支付类型新增</legend>
	</fieldset>

	<form class="layui-form layui-form-pane" action="" id="payForm">
		<input type="hidden" id="status" name="status" value="0">
		<div class="layui-form-item">
			<label class="layui-form-label">支付名称
			</label>
			<div class="layui-input-inline">
				<input type="text" name="name" id="name"
					lay-verify="required" placeholder="支付名称" 
					class="layui-input">
			</div>
			<span id="namespan" style="display: none"><em></em></span>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">费率
			</label>
			<div class="layui-input-inline">
				<input type="number" name="rate" id="rate"
					lay-verify="required" placeholder="费率" 
					class="layui-input">
			</div>
			<span id="ratespan" style="display: none"><em></em></span>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">姓名
			</label>
			<div class="layui-input-inline">
				<input type="text" name="merchantId" id="merchantId"
					lay-verify="required" placeholder="姓名" 
					class="layui-input">
			</div>
			<span id="merchantIdspan" style="display: none"><em></em></span>
		</div>
		
		<div class="layui-form-item">
			<div class="layui-input-block">
				<button class="layui-btn" lay-submit lay-filter="pay-submit">新增</button>
				<button type="reset" class="layui-btn layui-btn-primary">重置</button>
			</div>
		</div>
	</form>

	<script type="text/javascript">
		layui.use([ 'form', 'layer' ], function() {
			var form = layui.form;
			var layer = layui.layer;
			
			form.on('submit(pay-submit)', function() {
				$.post("../paytype/add", $("#payForm").serialize(),
						function(data) {
							console.log(data);
							if (data.status == 0) {
								layer.msg(data.message, {
									icon : 1,
									anim : 4,
									time : 2000
								}, function() {//新增成功，重新刷新表格
									//table.reload();
									var index = parent.layer.getFrameIndex(window.name);
									parent.layer.close(index);
								});
							} else {
								layer.msg(data.message, {
									icon : 5,
									anim : 6,
									time : 2000
								});
							}
						});
				return false;
			});
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