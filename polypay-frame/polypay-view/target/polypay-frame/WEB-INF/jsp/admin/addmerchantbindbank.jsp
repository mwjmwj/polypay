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
<!-- 注意 ：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->
</head>


<script type="text/javascript" src="../static/js/layui.js"></script>
<script src="../static/js/jquery.min.js" type="text/javascript"></script>
<body>
	<div>
		<form class="layui-form" id="bankform">
			<!-- 提示：如果你不想用form，你可以换成div等任何一个普通元素 -->

			<div class="layui-form-item layui-form-pane">
				<label class="layui-form-label">账户名</label>
				<div class="layui-input-block">
					<div class="layui-inline">
						<input type="text" name="accountName" placeholder="卡号所属人"
							autocomplete="off" id="accountName" class="layui-input"
							style="width: 300px" lay-filter="account">
					</div>
				</div>
			</div>

			<div class="layui-form-item layui-form-pane">
				<label class="layui-form-label">银行卡号</label>
				<div class="layui-input-block">
					<div class="layui-inline">
						<input type="text" name="accountNumber" autocomplete="off"
							id="bankcode" class="layui-input" style="width: 300px">
					</div>
				</div>
			</div>

			<div class="layui-form-item layui-form-pane">
				<label class="layui-form-label">设置为默认</label>
				<div class="layui-input-block">
					<input type="checkbox" id="defaultStatus" name="defaultFlag" lay-skin="switch">
				</div>
			</div>



			<div class="layui-form-item layui-form-pane">
				<label class="layui-form-label">银行名称</label>
				<div class="layui-input-block">
					<div class="layui-inline">
						<input type="text" name="bankName" autocomplete="off"
							id="bankname" class="layui-input" style="width: 300px">
					</div>
				</div>
			</div>


			<div class="layui-form-item layui-form-pane">
				<label class="layui-form-label">分行全称</label>
				<div class="layui-input-block">
					<div class="layui-inline">
						<input type="text" name="branchName" autocomplete="off"
							class="layui-input" style="width: 300px"
							placeholder="例：湖北省武汉市八一路支行">
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
		layui.use('form', function() {
			var form = layui.form;

			form.verify({
				account : function(value) {
					if ($("#accountName").val() == ''
							|| $("#accountName").val() == null) {
						return "账户名不能为空";
					}
				}
			});

			//监听提交
			form.on('submit(save)', function(data) {
				
				
				
				$.ajax({
					url : '../merchant/bindbank/save',
					type : 'POST',
					datatype : 'json',
					data : $("#bankform").serialize(),
					success : function(data) {
						if (data.status == '0') {
							layer.alert('添加成功,确定关闭窗口?', {
								icon : 1
							}, function() {
								var index = parent.layer
										.getFrameIndex(window.name); //获取当前窗口的name
								parent.layer.close(index);
								window.location.reload();

							});
						}
						else{
							layer.confirm('添加失败 '+data.message, {
								  btn: ['继续添加', '退出添加']
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
		function updatemobile() {
			$("#mobileNumber").removeAttr("disabled");
			$("#mobileNumber").attr("style",
					"width: 200px;background-color: white");
		};
	</script>
	<script type="text/javascript">
		window.onload = function() {
			document.getElementById('bankcode').onkeyup = function(event) {
				if ((event.which >= 48 && event.which <= 57)
						|| (event.which >= 96 && event.which <= 105)) {
					var v = this.value;
					if (/\S{5}/.test(v)) {
						this.value = v.replace(/\s/g, '').replace(/(.{4})/g,
								"$1 ");
					}
				}

				var index = $("#bankcode").val().length;
				if (index >= 8) {
					if ($("#bankname").val() == null
							|| $("#bankname").val() == '') {
						$.ajax({
							url : '../merchant/bindbank/getname?cardnumber='
									+ $("#bankcode").val().replace(' ', ''),
							success : function(data) {
								$("#bankname").val(data.data);
							}
						})

					}
				}
				if (index < 8) {
					$("#bankname").val("");
				}

			}
		}
	</script>
</body>
</html>