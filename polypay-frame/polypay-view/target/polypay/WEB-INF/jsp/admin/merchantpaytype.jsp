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
<body style="background-color: white">

	<blockquote class="layui-elem-quote layui-text">
		费率须知
		<p style="color: red;">1. 费率会实时调控</p>
		<p style="color: red;">2. 费率计算方式用于入金、出金；计算规则：  千分之10-1000 元扣去手续费 10元</p>
	</blockquote>
	
	<fieldset class="layui-elem-field layui-field-title"
		style="margin-top: 20px;">
		<legend>通道费率</legend>
	</fieldset>

		<table class="layui-hide" id="merchantpaytype"></table>


	<script>
		layui.use('table', function() {
			var table = layui.table;                                                               

			table.render({
				elem : '#merchantpaytype',
				url : '../paytype/list',
				cellMinWidth : 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
				,
				response : {
					statusName : 'status' //规定数据状态的字段名称，默认：code
					,
					statusCode : 0 //规定成功的状态码，默认：0
					,
					msgName : 'message' //规定状态信息的字段名称，默认：msg
					,
					countName : 'count' //规定数据总数的字段名称，默认：count
					,
					dataName : 'data' //规定数据列表的字段名称，默认：data
				},
				cols : [ [ 
					 {
					field : 'name',
					width : 108,
					title : '通道',
					align:'center'
				}, {
					field : 'rate',
					width : 134,
					title : '费率',
					align:'center',
					templet : function(row) {
							return '千分之'+row.rate;
					}
				}] ]
			});
		});
	</script>

</body>
</html>