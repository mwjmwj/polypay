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
<script type="text/javascript" src="../static/js/layui.all.js"></script>
<body>
	
	<div class="layui-row"style="margin-top: 10px">
	<button class="layui-btn" id="addbank" style="margin-left: 20px">
  	<i class="layui-icon">&#xe608;</i> 添加
	</button>
	</div>
	<script type="text/javascript" src="../static/js/jquery.min.js"></script>
	<table class="layui-hide" id="bindbanklist" lay-filter="bindbanklist"></table>
	
	<script type="text/html" id="func">
  	<a class="layui-btn layui-btn-xs" lay-event="edit">设置默认</a>
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
				elem : '#bindbanklist',
				url : '../merchant/bindbank/list',
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
					field : 'bankName',
					title : '银行名称',
					width : 150,
					align : 'center',
					fixed : 'left'
				}, {
					field : 'branchName',
					title : '开户行全称',
					width : 250,
					align : 'center',
					fixed : 'left'
				}, {
					field : 'accountNumber',
					title : '卡号',
					align : 'center',
					width : 200
				}
				, {
					field : 'provice',
					title : '省',
					align : 'center',
					width : 140
				}
				, {
					field : 'city',
					title : '市',
					align : 'center',
					width : 140
				}
				, {
					field : 'accountName',
					title : '姓名',
					align : 'center',
					width : 100
				}, {
					field : 'defaultStatus',
					title : '状态',
					width : 100,
					templet : function(row) {
						if (row.defaultStatus == 0) {
							return "默认卡";
						}else
						{
							return "-";
						}
					}
				}
				, {
					title : '操作',
					width : 155,
					align : 'center',
					templet : "#func"
				}] ],
				page : true

			});
			
			
			
			
		//监听工具条
			table.on('tool(bindbanklist)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
				var data = obj.data; //获得当前行数据
				var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
				var tr = obj.tr; //获得当前行 tr 的DOM对象

				if (layEvent === 'edit') { //查看
					//do somehing

					
					layer.confirm('确定默认卡号：<span style="color:orange">【</span><span>'+data.accountNumber+'</span><span style="color:orange">】</span>', function(index){
	    	  	$.ajax({
	  			type: 'get',
	  			url: '${pageContext.request.contextPath}/merchant/bindbank/bind/'+data.id,
	  			dataType: 'json',
	  			async:false,
	  			success: function(data){
	  				if(data.status=='0'){
	  					layer.msg('设置成功!',{icon:1,time:1000});
	  					location.reload();
	  				}else{
	  					layer.msg('程序异常!'+data.message,{icon:5,time:1000});
	  				}
	  				
	  			},
	  			error:function(msg) {
	  				layer.msg('程序异常!',{icon:5,time:1000});
	  			}
	  			});	
	    	  
	        
	        layer.close(index);
	      });

				} else if (layEvent === 'del') { //删除
					layer.confirm('真的删除行么', function(index) {
						obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
						layer.close(index);
						//向服务端发送删除指令
						
						$.ajax({
				  			type: 'get',
				  			url: '${pageContext.request.contextPath}/merchant/bindbank/delete/'+data.id,
				  			dataType: 'json',
				  			async:false,
				  			success: function(data){
				  				if(data.status=='0'){
				  					layer.msg('删除成功!',{icon:1,time:1000});
				  					location.reload();
				  				}else{
				  					layer.msg('程序异常!'+data.message,{icon:5,time:1000});
				  				}
				  				
				  			},
				  			error:function(msg) {
				  				layer.msg('程序异常!',{icon:5,time:1000});
				  			}
				  			});	
						
					});
				} else if (layEvent === 'edit') { //编辑
					//do something

					//同步更新缓存对应的值
					obj.update({
						username : '123',
						title : 'xxx'
					});
				}
			});

		});
	</script>
	<script type="text/javascript">
		layui.use([ 'laydate' ], function() {
			var $ = layui.$;
			var laydate = layui.laydate;
			var nowTime = new Date().valueOf();
			var max = null;

			var start = laydate.render({
				elem : '#begintime',
				type : 'datetime',
				max : nowTime,
				btns : [ 'clear', 'confirm' ],
				done : function(value, date) {
					endMax = end.config.max;
					end.config.min = date;
					end.config.min.month = date.month - 1;
				}
			});
			var end = laydate.render({
				elem : '#endtime',
				type : 'datetime',
				max : nowTime,
				done : function(value, date) {
					if ($.trim(value) == '') {
						var curDate = new Date();
						date = {
							'date' : curDate.getDate(),
							'month' : curDate.getMonth() + 1,
							'year' : curDate.getFullYear()
						};
					}
					start.config.max = date;
					start.config.max.month = date.month - 1;
				}
			});
			
		 
	
		$('#addbank').on('click',function(){
			
			layer.open({
				area:['500px','600px'],
				title:'添加绑定银行卡',
				type:2,
				icon:1,
				anim:5,
				maxmin: true,
				offset: ['100px', '50px'],
				content:'../view/merchantBindBank'
			});
		
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