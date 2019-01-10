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
<link rel="stylesheet" href="../static/css/layui.css">
<!-- 注意：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->

<style type="text/css">
.searchtable {
	
}
</style>
</head>
<script type="text/javascript" src="../static/js/layui.all.js"></script>
<body>
	<div class="layui-row"style="margin-top: 10px" >
 	<div class="layui-inline">		
 		<input class="layui-input" name="id" id="orderNumber" autocomplete="off" placeholder="订单号" />
		</div>
		 <div class="layui-inline">
		<input class="layui-input" type="text" name="begintime" id="begintime" placeholder="订单提交时间" />
		</div>
		<div class="layui-inline">
		<input class="layui-input"type="text"  name="endtime" id="endtime"  placeholder="订单审核时间" />
		</div>
			<button id="search" class="layui-btn" data-type="reload">搜索</button>
		<button id="clear" class="layui-btn" data-type="reload">清除</button>
		</div>

	<table class="layui-hide" id="settlelist" lay-filter="settlelist"></table>



	<script type="text/html" id="barDemo">
  <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
  <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
</script>
	<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->


	<script type="text/html" id="zizeng">
    {{d.LAY_TABLE_INDEX+1}}
</script>

	<script>
		layui.use('table', function() {
			var table = layui.table;
			table.render({
				elem : '#settlelist',
				url : '../merchant/settle/order/list',
				toolbar : '#search',
				title : '用户数据表',
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
				cols : [ [ {
					type : 'checkbox',
					fixed : 'left'
				}, {
					field : 'tradeType',
					title : '交易方式',
					width : 86,
					align : 'center',
					fixed : 'left',
					style : 'color:red',
					templet : function(row) {
						return "T+1"
					}
				}, {
					field : 'zizeng',
					title : '序号',
					width : 60,
					align : 'center',
					fixed : 'left',
					templet : '#zizeng'
				}, {
					field : 'orderNumber',
					title : '订单号',
					align : 'center',
					width : 235,
					sort : true
				}, {
					field : 'type',
					title : '类型',
					width : 80,
					align:'center',
					templet : function(row) {
						if (row.type == 3) {
							return "结算订单";
						}
					}
				}, {
					field : 'status',
					title : '状态',
					width : 60,
					templet : function(row) {
						if (row.status == 0) {
							return '<span style="color: green;">成功</span>';
						} else if (row.status == -1) {
							return '<span style="color: red;">失败</span>';
						}
					}
				}, {
					field : 'postalAmount',
					title : '结算金额',
					width : 143,
					style : 'color: red',
					templet : function(row) {
						return Number(row.postalAmount).toFixed(4) + "元";
					}
				}, {
					field : 'serviceAmount',
					title : '服务费',
					width : 126,
					style : 'color: red',
					templet : function(row) {
						return Number(row.serviceAmount).toFixed(4) + "元";
					}
				}, {
					field : 'arrivalAmount',
					title : '到账金额',
					width : 143,
					style : 'color: red',
					templet : function(row) {
					return Number(row.arrivalAmount).toFixed(4) + "元";
					}
				}, {
					field : 'createTime',
					title : '提交时间',
					width : 160,
					align:'center',
					templet : function(row) {
						return createTime(row.createTime);
					}
				}, {
					field : 'payTime',
					title : '审核时间',
					width : 170,
					templet : function(row) {
						if(null ==  row.payTime){ return "";}
						return createTime(row.payTime);
					}
				}, {
					field : 'descreption',
					title : '审核描述',
					width : 170
				}
				, {
					field : 'handlePeople',
					title : '审核人',
					width : 70
				}
				, {
					field : 'bankName',
					title : '银行账号',
					width : 65
				}, {
					field : 'bankAccountNumber',
					title : '银行账号',
					width : 200
				}, {
					fixed : 'right',
					title : '操作',
					toolbar : '#barDemo',
					width : 120
				} ] ],
				page : true,
				id : "settleReload"

			});

			var $ = layui.$, active = {
				reload : function() {
					var demoReload = $('#orderNumber');

					//执行重载
					table.reload('settleReload', {
						page : {
							curr : 1
						//重新从第 1 页开始
						},
						where : {
							orderNumber : demoReload.val(),
							orderNumber1 : demoReload.val()
						}
					});
				}
			};


			$('.layui-row #search').on('click', function() {
				var type = $(this).data('type');
				active[type] ? active[type].call(this) : '';
			});
			
			$('.layui-row #clear').on('click', function() {
				$("#orderNumber").val("");
				$("#begintime").val("");
				$("#endtime").val("");
			});
		});
	</script>
	<script type="text/javascript">
		layui.use([ 'laydate' ], function() {
			var $ = layui.$;
			var laydate = layui.laydate;
			 laydate.render({
				    elem: '#begintime'
				    ,range: true
				  });
			 laydate.render({
				    elem: '#endtime'
				    ,range: true
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


</body>
</html>