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

	<!-- <div class="layui-row" style="margin-top: 10px" >
 	<div class="layui-inline">		
 		<input class="layui-input" name="id" id="orderNumber" autocomplete="off" placeholder="订单号" />
		</div>
		 <div class="layui-inline">
		<input class="layui-input" type="text" name="begintime" id="begintime" placeholder="订单提交时间" />
		</div>
		<div class="layui-inline">
		<input class="layui-input"type="text"  name="endtime" id="endtime"  placeholder="到账时间" />
		</div>
	
		<button id="search" class="layui-btn" data-type="reload">搜索</button>
		<button id="clear" class="layui-btn" data-type="reload">清空</button>
		</div> -->
		
		
		
		
<blockquote class="layui-elem-quote layui-text">
		个人账单
		<p style="color: red;">个人账单每月1号生成上一个月所有的成功订单，包含订单金额，服务费，笔数</p>
	</blockquote>

	<fieldset class="layui-elem-field layui-field-title"
		style="margin-top: 20px;">
		<legend>个人账单管理</legend>
	</fieldset>

	<table class="layui-hide" id="billlist" lay-filter="billlist"></table>



	<script type="text/html" id="barDemo">
 		 <a class="layui-btn layui-btn-xs" lay-event="edit">查看</a>
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
				elem : '#billlist',
				url : '../merchant/bill/list',
				toolbar  : '#search',
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
					field : 'zizeng',
					title : '序号',
					width : 60,
					align : 'center',
					fixed : 'left',
					templet : '#zizeng'
				}, {
					field : 'billName',
					title : '账单名',
					width : 136,
					align : 'center',
					fixed : 'left',
					style : 'color:red'
				}, {
					field : 'rechargeAmount',
					title : '充值金额',
					align : 'center',
					width : 230,
					templet : function(row) {
						return Number(row.rechargeAmount).toFixed(4) + "元";
					}
				}
				, {
					field : 'rechargeServiceAmount',
					title : '充值服务费',
					align : 'center',
					width : 230,
					templet : function(row) {
						return Number(row.rechargeServiceAmount).toFixed(4) + "元";
					}
				}, {
					field : 'rechargeNumber',
					title : '充值订单数',
					width : 100,
					align : 'center'
				}
				, {
					field : 'settleAmount',
					title : '结算金额',
					align : 'center',
					width : 230,
					templet : function(row) {
						return Number(row.settleAmount).toFixed(4) + "元";
					}
				}
				, {
					field : 'settleServiceAmount',
					title : '结算服务费',
					align : 'center',
					width : 230,
					templet : function(row) {
						return Number(row.settleServiceAmount).toFixed(4) + "元";
					}
				}, {
					field : 'settleNumber',
					title : '结算订单数',
					width : 100,
					align : 'center'
				}
				, {
					field : 'placeAmount',
					title : '代付金额',
					align : 'center',
					width : 230,
					templet : function(row) {
						return Number(row.placeAmount).toFixed(4) + "元";
					}
				}
				, {
					field : 'placeServiceAmount',
					title : '代付服务费',
					align : 'center',
					width : 230,
					templet : function(row) {
						return Number(row.placeServiceAmount).toFixed(4) + "元";
					}
				}, {
					field : 'placeNumber',
					title : '代付订单数',
					width : 100,
					align : 'center'
				},  {
					field : 'createTime',
					title : '创建时间',
					width : 160,
					sort : true,
					templet : function(row) {
						return createTime(row.createTime);
					}
				}, {
					fixed : 'right',
					title : '操作',
					toolbar : '#barDemo',
					width : 120
				} ] ],
				page : true,
				id : "rechargeReload"

			});

			var $ = layui.$, active = {
				reload : function() {
					var demoReload = $('#orderNumber');

					//执行重载
					table.reload('rechargeReload', {
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
			
			
			//监听工具条
			table.on('tool(billlist)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
			  var data = obj.data; //获得当前行数据
			  var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
			  var tr = obj.tr; //获得当前行 tr 的DOM对象
			 
			  if(layEvent === 'edit'){ //查看
			    //do somehing
				  layer.open({
					  area:['500px','600px'],
					  type: 2,
					  title:'订单详细',
					  content: '../merchant/bill/get?billid='+data.id
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
	<script type="text/javascript">
	
	
	
	function done(res, curr, count){
	    $('#div').find('.layui-table-body').find("table" ).find("tbody").children("tr").on('dblclick',function(){
	        var id = JSON.stringify($('#div').find('.layui-table-body').find("table" ).find("tbody").find(".layui-table-hover").data('index'));
	        var obj = res.data[id];
	        fun.openLayer(obj);
	    })
	}
	
	</script>


</body>
</html>