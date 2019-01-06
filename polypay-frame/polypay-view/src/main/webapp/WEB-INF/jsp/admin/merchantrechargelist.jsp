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
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<link rel="stylesheet" href="../resources/css/layui.css">
  <!-- 注意：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->
</head>
<script type="text/javascript" src="../resources/js/layui.all.js"></script>
<body>
 <div class="demoTable">
  搜索ID：
  <div class="layui-inline">
    <input class="layui-input" name="id" id="orderNumber" autocomplete="off">
  </div>
  <button class="layui-btn" data-type="reload">搜索</button>
</div>
<table class="layui-hide" id="rechargelist" lay-filter="rechargelist"></table>

 
<script type="text/html" id="barDemo">
  <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
  <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
</script>
<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 --> 
 
<script>
layui.use('table', function(){
  var table = layui.table;
  
  table.render({
    elem: '#rechargelist'
    ,url:'../merchant/recharge/order/list'
    ,toolbar: '#demoTable'
    ,title: '用户数据表'
    ,response: {
    statusName: 'status' //规定数据状态的字段名称，默认：code
    ,statusCode: 0  //规定成功的状态码，默认：0
    ,msgName: 'message' //规定状态信息的字段名称，默认：msg
     ,countName: 'page.total' //规定数据总数的字段名称，默认：count
     ,dataName: 'data' //规定数据列表的字段名称，默认：data
    } 
    ,cols: [[
      {type: 'checkbox', fixed: 'left'}
      ,{field:'id', title:'ID', width:80, fixed: 'left', unresize: true, sort: true}
      ,{field:'merchantId', title:'商户ID', width:120}
      ,{field:'email', title:'邮箱', width:150, templet: function(res){
        return '<em>'+ res.email +'</em>'
      }}
      ,{field:'sex', title:'性别', width:80, sort: true}
      ,{field:'city', title:'城市', width:100}
      ,{field:'sign', title:'签名'}
      ,{field:'experience', title:'积分', width:80, sort: true}
      ,{field:'ip', title:'IP', width:120}
      ,{field:'logins', title:'登入次数', width:100, sort: true}
      ,{field:'joinTime', title:'加入时间', width:120}
      ,{fixed: 'right', title:'操作', toolbar: '#barDemo', width:150}
    ]]
    ,page: true
    ,id:"rechargeReload"
  });
  
  
  var $ = layui.$, active = {
		    reload: function(){
		      var demoReload = $('#orderNumber');
		      
		      //执行重载
		      table.reload('rechargeReload', {
		        page: {
		          curr: 1 //重新从第 1 页开始
		        }
		        ,where: {
		          orderNumber:demoReload.val()
		          ,orderNumber1:demoReload.val()
		        }
		      });
		    }
		  };
		  
		  $('.demoTable .layui-btn').on('click', function(){
		    var type = $(this).data('type');
		    active[type] ? active[type].call(this) : '';
		  });
		});
</script>

</body>
</html>