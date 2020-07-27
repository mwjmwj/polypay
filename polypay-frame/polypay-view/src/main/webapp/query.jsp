<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="com.polypay.platform.pay.common.Config"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head> 
<meta http-equiv="Content-Type"	content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!--[if lt IE 10]>
<script>alert("为了更好的体验，不支持IE10以下的浏览器。请选择google chrome 或者 firefox 浏览器。"); location.href="http://www.ielpm.com";</script>
<![endif]-->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<script src="http://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<title>乾通支付</title> 
<style type="text/css">
	body { padding-top: 70px; }
</style>

</head> 
<body> 
<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
  <div class="container">
  	<a class="navbar-brand" href="index.html"><strong>首页</strong></a>
  	<a class="navbar-brand"><strong>查询（<s>ie1-9</s>）</strong></a>
  </div>
</nav>

<div class="container-fluid">
	<div class="row">
		<div class="col-md-6 col-md-offset-3">
			<form role="form" action="query.do" method="post"  name="payForm" >
				<div class="form-group">
					<label for="merchantNo">商户号</label>
					<input type="text" class="form-control" name="merchantNo" id="merchantNo" value="<%=Config.getInstance().getMerchantNo()%>">
				</div>
				<div class="form-group">
					<label for="tranSerialNum">原交易流水号</label>
					<input type="text" class="form-control" name="tranSerialNum" id="tranSerialNum"  required>
				</div>
				
				<div class="form-group">
					<label for="remark">备注字段</label>
					<input type="text" class="form-control" name="remark" >
				</div>
					
				<div class="form-group">
					<label for="YUL1">预留字段1</label>
					<input type="text" class="form-control" name="YUL1" >
				</div>
	
				<button type="submit" class="btn btn-primary">查询</button>
			</form>
		</div>
	</div>
</div>

		
</body>

<script>
	$(function(){
		$("#tranSerialNum").val(random());
	});
	
	function random(){
		var d = new Date();
		return d.getFullYear() + "" + d.getMonth() + 1 + "" + d.getDay() + "" + d.getHours() + "" + d.getMinutes() + "" +  d.getMilliseconds() + "" + parseInt(Math.random() * 1000);
	}
</script>
 
</html>