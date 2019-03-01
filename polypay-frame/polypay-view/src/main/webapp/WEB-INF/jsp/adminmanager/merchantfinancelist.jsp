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
<script type="text/javascript" src="../static/js/layui.js"></script>
<body>
<div class="layui-form">
	 <div class="layui-row" style="margin-top: 10px" >
	 
 		<div class="layui-inline">		
 		<input class="layui-input" name="merchantId" id="merchantId" autocomplete="off" placeholder="商户号" />
		</div>
		
		 <div class="layui-inline">
      <div class="layui-input-inline">
        <select id="status" name="status" lay-verify=" lay-search="">
          <option value="">选择状态</option>
          <option value="0">可用</option>
 		 <option value="-1">冻结</option>
        </select>
      </div>
    	</div>
	
		<button id="search" class="layui-btn" data-type="reload">搜索</button>
		<button id="clear" class="layui-btn" data-type="reload">清空</button>
		</div>
</div>
	<table class="layui-hide" id="financelist" lay-filter="financelist"></table>

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
				elem : '#financelist',
				url : '../merchantmanager/finance/list',
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
					field : 'zizeng',
					title : '序号',
					width : 60,
					align : 'center',
					fixed : 'left',
					templet : '#zizeng'
				}, {
					field : 'merchantId',
					title : '商户号',
					width : 200,
					align : 'center',
					fixed : 'left',
					style : 'color:red'
				}, {
					field : 'blanceAmount',
					title : '账户余额',
					width : 200,
					align : 'center',
					fixed : 'left',
					style : 'color:red',
					templet : function(row) {
						return Number(row.blanceAmount).toFixed(4) + "元";
					}
				}, {
					field : 'fronzeAmount',
					title : '冻结金额',
					align : 'center',
					width : 200,
					style : 'color:red',
					templet : function(row) {
						return Number(row.fronzeAmount).toFixed(4) + "元";
					}
				}
				, {
					field : 'payPassword',
					title : '支付密码',
					align : 'center',
					width : 350,
					style : 'color:red',
					templet : function(row) {
						return Number(row.fronzeAmount).toFixed(4) + "元";
					}
				}
				, {
					field : 'status',
					title : '状态',
					align : 'center',
					width : 80,
					style : 'color:red',
					templet : function(row) {
						if (row.status == 0) {
							return '<span style="color: green;">可用</span>';
						}
						else if (row.status == -1) {
							return '<span style="color: red;">冻结</span>';
						}
					}
				}, {
					fixed : 'right',
					title : '操作',
					toolbar : '#barDemo',
					width : 120
				} ] ],
				page : true,
				id : "financeReload"

			});

			var $ = layui.$, active = {
				reload : function() {
					var merchantId = $('#merchantId');
					var status = $('#status');

					//执行重载
					table.reload('financeReload', {
						page : {
							curr : 1
						//重新从第 1 页开始
						},
						where : {
							merchantId : merchantId.val(),
							status : status.val()
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
			table.on('tool(financelist)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
			  var data = obj.data; //获得当前行数据
			  var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
			  var tr = obj.tr; //获得当前行 tr 的DOM对象
			 
			  if(layEvent === 'edit'){ //查看
			    //do somehing
				  layer.open({
					  area:['500px','600px'],
					  type: 2,
					  title:'财务详情',
					  content: '../merchantmanager/finance/query?financeId='+data.id
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