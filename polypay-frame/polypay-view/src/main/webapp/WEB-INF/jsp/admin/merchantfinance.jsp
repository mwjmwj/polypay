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
<link rel="stylesheet" href="../static/js/css/layui.css">
<!-- 注意：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->

<link rel="stylesheet" href="../static/css/finance.css">

</head>
<script type="text/javascript" src="../static/js/layui.js"></script>
<script src="../static/js/jquery.min.js" type="text/javascript"></script>
<body style="background-color: white;">

	<script type="text/html" id="financeedit">
	
  	<a class="layui-btn layui-btn-xs" lay-event="edit">查看</a>
	</script>



	
	<blockquote class="layui-elem-quote layui-text">
		资金管理如有疑问咨询：<a href="http://wpa.qq.com/msgrd?v=3&uin=1693752540&site=qq&menu=yes" target="_blank">客服QQ：878785192</a>
	</blockquote>

	<fieldset class="layui-elem-field layui-field-title"
		style="margin-top: 20px;">
		<legend>个人资金</legend>
	</fieldset>



	<div class="layui-form" action="">
		<div class="layui-form-item">
			<label class="layui-form-label">账户总金额</label>
			<div class="layui-input-block">
				<input type="text" name="title" lay-verify="title"
					style="width: 50%;" autocomplete="off" placeholder="账户总金额"
					value="${merchantFinance.blanceAmount+merchantFinance.fronzeAmount }元"
					class="layui-input" readonly="readonly">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">可提现金额</label>
			<div class="layui-input-block">
				<input type="text" name="title" lay-verify="title"
					style="width: 50%;" autocomplete="off" placeholder="可提现金额"
					value="${merchantFinance.blanceAmount }元" class="layui-input"
					readonly="readonly">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">冻结金额</label>
			<div class="layui-input-block">
				<input type="text" name="title" lay-verify="title"
					style="width: 50%;" autocomplete="off" placeholder="冻结金额"
					value="${merchantFinance.fronzeAmount }元" class="layui-input"
					readonly="readonly">
			</div>
		</div>

		<div class="layui-form-item">
			<div class="layui-input-block">
				<button class="layui-btn" onclick="bill()" lay-submit=""
					lay-filter="demo1">立即提现</button>
			</div>
		</div>
	</div>



	<c:if test="${sessionScope.merchant_user.roleId ==1 }">
	<fieldset class="layui-elem-field layui-field-title"
		style="margin-top: 20px;">
		<legend>近期明细</legend>
	</fieldset>
	<table class="layui-hide" id="merchantfinancelist" lay-filter="merchantfinancelist"></table>
	</c:if>

	<script>
		layui.use('table',
			function() {
			var table = layui.table;
			table.render({
			elem : '#merchantfinancelist',
			url : '../merchantfinance/amountoutin/list',
			cellMinWidth : 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
			,
			title : '近期出入金',
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
			},cols : [ [{field : 'createTime',
						width : 200,
						title : '日期',
						align : 'center',
						templet : function(row) {return createTime(row.createTime);
													}
						},
						{
						field : 'amount',
						width : 150,
						title : '金额',
						align : 'center',
						templet : function(row) {
							if (row.type == 0) {
								return '<font style="color:green;align:right">'
										+ '+'
										+ Number(
												row.amount)
												.toFixed(
														4)
										+ '元<font>';
							} else if (row.type == -1) {
								return '<font style="color:red">'
										+ '-'
										+ Number(
												row.amount)
												.toFixed(
														4)
										+ '元<font>';
							}
						}
					}, {
						fixed : 'right',
						title : '查看',
						toolbar : '#financeedit',
						width : 80,
						align : 'center'
					} ] ]
					});
			

			
			//监听工具条
			table.on('tool(merchantfinancelist)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
			  var data = obj.data; //获得当前行数据
			  var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
			  var tr = obj.tr; //获得当前行 tr 的DOM对象
			 
			  if(layEvent === 'edit'){ //查看
			    //do somehing
			    var url;
			    if(data.orderType == 1) //充值
			    {
			    	url='../merchant/recharge/query?id='+data.id;
			    }else if(data.orderType ==2) //代付
			    {
			    	url='../merchant/place/query?id='+data.id;
			    }else if(data.orderType ==3) //结算
			    {
			    	url='../merchant/settle/query?id='+data.id;
			    }
			    
				  layer.open({
					  area:['500px','600px'],
					  type: 2,
					  title:'订单详细',
					  content: url
					}); 
			  } else if(layEvent === 'del'){ //删除
			    layer.confirm('真的删除行么', function(index){
			      obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
			      layer.close(index);
			      //向服务端发送删除指令
			    });
			  }
			});
			
			
			
				});
	</script>


	<script type="text/javascript">
		function createTime(v) {
			var date = new Date(v);
			var y = date.getFullYear();
			var m = date.getMonth() + 1;
			m = m < 10 ? '0' + m : m;
			var d = date.getDate();
			d = d < 10 ? ("0" + d) : d;
			var h = date.getHours();
			h = h < 10 ? ("0" + h) : h;
			var M = date.getMinutes();
			M = M < 10 ? ("0" + M) : M;
			var S = date.getSeconds();
			S = S < 10 ? ("0" + S) : S;
			var str = y + "-" + m + "-" + d + " " + h + ":" + M + ":" + S;
			return str;
		}
	</script>

	<script type="text/javascript">
	function bill()
	{
		window.location.href="<%=basePath%>merchant/settle/order/pre";

		}
	</script>

</body>
</html>