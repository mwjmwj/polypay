<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
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

	<fieldset class="layui-elem-field layui-field-title" style="margin-top: 30px;">
  		<legend>API  对接管理</legend>
	</fieldset>
	
	
<div class="layui-form layui-form-pane" action="">


<div class="layui-form-item layui-form-text">
    <label class="layui-form-label">秘钥</label>
    <div class="layui-input-block">
      <textarea placeholder="请输入内容" class="layui-textarea">${merchantapi.secretKey }</textarea>
    </div>
  </div>
  
  
  <div class="layui-form-item layui-form-text">
    <label class="layui-form-label">加密私钥</label>
    <div class="layui-input-block">
      <textarea placeholder="请输入内容" class="layui-textarea">${merchantapi.md5Key }</textarea>
    </div>
  </div>

  
  </div>
  
  <div class="layui-form-item">
    <div class="layui-input-block">
		  <button class="layui-btn layui-btn-normal" onclick="upload()">下载API文档</button>
    </div>
  </div>
  
  <script type="text/javascript">
  function upload()
  {
		window.location.href="<%=basePath %>/static/源盛丰支付对接文档.docx";	  
  }
  </script>

</body>
</html>