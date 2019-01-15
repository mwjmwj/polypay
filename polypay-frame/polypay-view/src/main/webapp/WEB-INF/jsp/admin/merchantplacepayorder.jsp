<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>layui</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="../../../static/css/layui.css">
<!-- 注意：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->


</head>
<script type="text/javascript" src="../../../static/js/layui.js"></script>

<script type="text/javascript" src="../../../static/js/jquery.min.js"></script>

<body style="background-color: white">
	<blockquote class="layui-elem-quote layui-text">
		代付注意事项：
		<p style="color: red;">1. 代付申请流程需后台审批，注意查看订单状态。</p>
		<p style="color: red;">2. 代付申请，后台审核通过后，2个工作日内到账,节假日顺延。</p>
	</blockquote>

	<fieldset class="layui-elem-field layui-field-title"
		style="margin-top: 20px;">
		<legend>代付结算</legend>
	</fieldset>

	<form class="layui-form" action="">

		<div class="layui-form-item">
			<label class="layui-form-label">可代付金额</label>
			<div class="layui-input-block">
				<input type="text" name="title" lay-verify="title"
					value="${merchantfinance.blanceAmount }元 " readonly="readonly"
					autocomplete="off" placeholder="可用金额" class="layui-input">
			</div>
		</div>


		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">代付金额</label>
				<div class="layui-input-inline">
					<input type="tel" name="phone" lay-verify="required|phone"
						placeholder="${merchantfinance.blanceAmount }元 "
						autocomplete="off" class="layui-input" id="postamount" onblur="textamount(this)" name="postamount">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">代付手续费</label>
				<div class="layui-input-inline">
					<input type="text" name="email" lay-verify="email" id="serviceamount"
						autocomplete="off" class="layui-input" readonly="readonly">
				</div>
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">支付密码</label>
			<div class="layui-input-inline">
				<input type="password" name="paypassword" lay-verify="pass"
					placeholder="请输入密码" autocomplete="off" class="layui-input">
			</div>
			<div class="layui-form-mid layui-word-aux">
				请填支付密码 初始密码查看短信信息 <a style="margin-left: 110px; color: red;" href="#">忘记支付密码?</a>
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">银行卡号</label>
			<div class="layui-input-block">
				<select name="interest" lay-filter="aihao">
					<c:forEach items="${bank}" var="b">
					<option value="${b.id }" >${b.accountName}|${b.bankName }|${b.accountNumber }</option>
					<c:if test="${b.defaultStatus == 0 }">
					<option value="${b.id }" selected="selected">${b.accountName}|${b.bankName }|${b.accountNumber }</option>
					</c:if>
					</c:forEach>
				
				</select>
			</div>
		</div>

		<div class="layui-form-item">
			<div class="layui-input-block">
				<button class="layui-btn" lay-submit="" lay-filter="demo1">提交结算</button>
				<button type="reset" class="layui-btn layui-btn-primary">重置</button>
			</div>
		</div>
	</form>


	<script>
layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form
  ,layer = layui.layer
  ,layedit = layui.layedit
  ,laydate = layui.laydate;

  //自定义验证规则
  form.verify({
    title: function(value){
      if(value.length < 5){
        return '标题至少得5个字符啊';
      }
    }
    ,pass: [
      /^[\S]{6,12}$/
      ,'密码必须6到12位，且不能出现空格'
    ]
    ,content: function(value){
      layedit.sync(editIndex);
    }
  });
  
  //监听提交
  form.on('submit(demo1)', function(data){
    layer.alert(JSON.stringify(data.field), {
      title: '最终的提交信息'
    })
    return false;
  });
 
  /* //表单初始赋值
  form.val('example', {
    "username": "贤心" // "name": "value"
    ,"password": "123456"
    ,"interest": 1
    ,"like[write]": true //复选框选中状态
    ,"close": true //开关状态
    ,"sex": "女"
    ,"desc": "我爱 layui"
  }) */
  
  
  
	});
	
	
	function textamount(t) {
	  
		if(!/^\d{0,30}$/.test(t.value)){t.value='';return;}
		
	  var postamount = $('#postamount').val();
	  
	  if (postamount >= '${merchantfinance.blanceAmount}') {
			$("#postamount").val('${merchantfinance.blanceAmount}');
		}
	  postamount = $("#postamount").val();
	  
      $("#serviceamount").val(postamount * 0.01);
      }
	</script>

</body>
</html>