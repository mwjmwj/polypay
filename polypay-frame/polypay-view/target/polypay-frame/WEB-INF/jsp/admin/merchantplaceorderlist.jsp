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

<style type="text/css">
.searchtable {
	
}
</style>
</head>
<script type="text/javascript" src="../static/js/layui.all.js"></script>
<body>
	<div class="layui-row" style="margin-top: 10px">
 	<div class="layui-inline">		
 		<input class="layui-input" name="id" id="orderNumber" autocomplete="off" placeholder="订单号" />
		</div>
		 <div class="layui-inline">
		<input class="layui-input" type="text" name="begintime" id="begintime" placeholder="订单提交时间" />
		</div>
		<div class="layui-inline">
		<input class="layui-input"type="text"  name="endtime" id="endtime"  placeholder="订单处理时间" />
		</div>
		<button id="search" class="layui-btn" data-type="reload">搜索</button>
		<button id="clear" class="layui-btn" data-type="reload">清除</button>
		</div>
	

	<table class="layui-hide" id="placelist" lay-filter="placelist"></table>

	
	


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
				elem : '#placelist',
				url : '../merchant/place/order/list',
				title : '用户数据表',
				toolbar : '#search',
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
				}
				, {
					field : 'bankNumber',
					title : '银行卡号',
					align : 'center',
					width : 200
				}, {
					field : 'type',
					title : '类型',
					width : 113,
					align: 'center',
					templet : function(row) {
						if (row.type == 2) {
							return "代付订单";
						}
					}
				}, {
					field : 'status',
					title : '状态',
					width : 80,
					align : 'center',
					templet : function(row) {
						if (row.status == 0) {
							return '<span style="color: green;">成功</span>';
						} else if (row.status == -1) {
							return '<span style="color: red;">失败</span>';
						}
						else if (row.status == 1) {
							return '<span style="color: orange;">审核中</span>';
						}
					}
				}, {
					field : 'payAmount',
					title : '代付金额',
					width : 143,
					style : 'color: red',
					templet : function(row) {
						return Number(row.payAmount).toFixed(4) + "元";
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
					field : 'arriveAmount',
					title : '到账金额',
					width : 143,
					style : 'color: red',
					templet : function(row) {
						return Number(row.arriveAmount).toFixed(4) + "元";
					}
				}, {
					field : 'createTime',
					title : '提交时间',
					width : 160,
					sort : true,
					templet : function(row) {
						return createTime(row.createTime);
					}
				}, {
					field : 'handlerTime',
					title : '处理时间',
					width : 160,
					templet : function(row) {
						if(row.handlerTime == '' || row.handlerTime == null)
						{
							return "";
						}
						return createTime(row.handlerTime);
					}
				}, {
					field : 'handlerName',
					title : '处理人',
					width : 120
				}, {
					field : 'descreption',
					title : '信息',
					width : 120,
					style : 'align'
				}, {
					fixed : 'right',
					title : '操作',
					toolbar : '#barDemo',
					width : 120
				} ] ],
				page : true,
				id : "placeReload"

			});

			
			var $ = layui.$, active = {
					reload : function() {
						var ordernumber = $('#orderNumber').val();
						var begintime = $('#begintime').val();
						var endtime = $('#endtime').val();
						//执行重载
						table.reload('placeReload', {
							page : {
								curr : 1
							//重新从第 1 页开始
							},
							where : {
								orderNumber : ordernumber,
								beginTime:begintime,
								endTime:endtime
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
			
			
			
			//监听工具条
			table.on('tool(placelist)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
			  var data = obj.data; //获得当前行数据
			  var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
			  var tr = obj.tr; //获得当前行 tr 的DOM对象
			 
			  if(layEvent === 'edit'){ //查看
			    //do somehing
				  layer.open({
					  area:['500px','600px'],
					  type: 2,
					  title:'订单详细',
					  content: '../merchant/place/query?id='+data.id
					}); 
			  } else if(layEvent === 'del'){ //删除
			    layer.confirm('真的删除行么', function(index){
			      obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
			      layer.close(index);
			      //向服务端发送删除指令
			    });
			  } else if(layEvent === 'edit'){ //编辑
			    //do something
			    
			    //同步更新缓存对应的值
			    obj.update({
			      username: '123'
			      ,title: 'xxx'
			    });
			  }
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