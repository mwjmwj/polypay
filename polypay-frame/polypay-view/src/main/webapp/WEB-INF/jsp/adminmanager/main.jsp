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
<title>乾通支付</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
	
<meta HTTP-EQUIV="pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<meta HTTP-EQUIV="expires" CONTENT="0">
<link rel="stylesheet" href="<%=basePath %>/static/js/css/layui.css"
	media="all">
<link rel="stylesheet" href="<%=basePath %>/static/js/css/admin.css"
	media="all">
    
<body class="gray-bg">

    <div class="layui-fluid">
		<div class="layui-row layui-col-space15">

			<div class="layui-col-sm6 layui-col-md3">
				<div class="layui-card">
					<div class="layui-card-header">
						今日交易金额 <span style="float: right; margin-right: 20%;"> <a
							href="<%=basePath %>/managerview/merchantrechargelist"
							style="color: #01aaed">查看明细</a>
						</span> <span class="layui-badge layui-bg-blue layuiadmin-badge">日(成功)</span>

					</div>
					<div class="layui-card-body layuiadmin-card-list">
						<p class="layuiadmin-big-font">${todayMerchantGroupDate.merchantTodayRechargeAmount  }
						<c:if test="${todayMerchantGroupDate.merchantTodayRechargeAmount ==null }">0</c:if>
						</p>
						<p>
							&nbsp; <span class="layuiadmin-span-color">元<i
								class="layui-inline layui-icon layui-icon-dollar"></i></span>
						</p>
					</div>
				</div>
			</div>

			<div class="layui-col-sm6 layui-col-md3">
				<div class="layui-card">
					<div class="layui-card-header">
						今日交易笔数 <span class="layui-badge layui-bg-cyan layuiadmin-badge">日(成功)</span>
					</div>
					<div class="layui-card-body layuiadmin-card-list">
						<p class="layuiadmin-big-font">${todayMerchantGroupDate.merchantTodayOrderNumber }
						<c:if test="${todayMerchantGroupDate.merchantTodayOrderNumber ==null }">0</c:if>
						</p>
						<p>
							&nbsp; <span class="layuiadmin-span-color">笔<i
								class="layui-inline layui-icon layui-icon-flag"></i></span>
						</p>
					</div>
				</div>
			</div>
			<div class="layui-col-sm6 layui-col-md3">
				<div class="layui-card">
					<div class="layui-card-header">
						总交易金额
						<span class="layui-badge layui-bg-green layuiadmin-badge">总( 成功)</span>
					</div>
					<div class="layui-card-body layuiadmin-card-list">

						<p class="layuiadmin-big-font">${merchantGroupDate.merchantAllRechargeAmount }
						<c:if test="${merchantGroupDate.merchantAllRechargeAmount ==null }">0</c:if>
						</p>
						<p>
							&nbsp; <span class="layuiadmin-span-color">元<i
								class="layui-inline layui-icon layui-icon-dollar"></i></span>
						</p>
					</div>
				</div>
			</div>
			<div class="layui-col-sm6 layui-col-md3">
				<div class="layui-card">
					<div class="layui-card-header">
						总笔数 <span class="layui-badge layui-bg-orange layuiadmin-badge">总(成功)</span>
					</div>
					<div class="layui-card-body layuiadmin-card-list">

						<p class="layuiadmin-big-font">${merchantGroupDate.merchantAllOrderNumber }
						<c:if test="${merchantGroupDate.merchantAllOrderNumber ==null }">0</c:if>
						</p>
						<p>
							&nbsp; <span class="layuiadmin-span-color">笔 <i
								class="layui-inline layui-icon layui-icon-flag"></i></span>
						</p>
					</div>
				</div>
			</div>



			<div class="layui-col-sm6 layui-col-md3">
				<div class="layui-card">
					<div class="layui-card-header">
						商户账户总余额<span class="layui-badge layui-bg-blue layuiadmin-badge">总</span>
					</div>
					<div class="layui-card-body layuiadmin-card-list">
						<p class="layuiadmin-big-font">${allMerchantFinance.blanceAmount+allMerchantFinance.fronzeAmount }
						<c:if test="${allMerchantFinance.blanceAmount ==null }">0</c:if>
						</p>
						<p>
							&nbsp; <span class="layuiadmin-span-color">元 <i
								class="layui-inline layui-icon layui-icon-dollar"></i></span>
						</p>
					</div>
				</div>
			</div>

			<div class="layui-col-sm6 layui-col-md3">
				<div class="layui-card">
					<div class="layui-card-header">
						商户账户总冻结金额 <span class="layui-badge layui-bg-cyan layuiadmin-badge">总</span>
					</div>
					<div class="layui-card-body layuiadmin-card-list">

						<p class="layuiadmin-big-font">${allMerchantFinance.fronzeAmount }
						<c:if test="${allMerchantFinance.fronzeAmount ==null }">0</c:if>
						</p>
						<p>
							&nbsp; <span class="layuiadmin-span-color">元<i
								class="layui-inline layui-icon layui-icon-user"></i></span>
						</p>
					</div>
				</div>
			</div>

			<div class="layui-col-sm6 layui-col-md3">
				<div class="layui-card">
					<div class="layui-card-header">
						商户账户总可结算金额 <span class="layui-badge layui-bg-green layuiadmin-badge">总</span>
					</div>
					<div class="layui-card-body layuiadmin-card-list">
						<p class="layuiadmin-big-font">
						${allMerchantFinance.blanceAmount }
						</p>
						<p>

							&nbsp; <span class="layuiadmin-span-color">元 <i
								class="layui-inline layui-icon layui-icon-dollar"></i></span>

						</p>
					</div>
				</div>
			</div>
			
			<div class="layui-col-sm6 layui-col-md3">
				<div class="layui-card">
					<div class="layui-card-header">
						提现手续费 <span class="layui-badge layui-bg-orange layuiadmin-badge">当前</span>
					</div>
					<div class="layui-card-body layuiadmin-card-list">

						<p class="layuiadmin-big-font">${sessionScope.merchant_user.handAmount }</p>
						<p>
							&nbsp; <span class="layuiadmin-span-color">元/笔 <i
								class="layui-inline layui-icon layui-icon-dollar"></i></span>
						</p>
					</div>
				</div>
			</div>

		</div>

	</div>


	<script src="<%=basePath %>/static/js/layui.js"></script>
	<script>
	  layui.config({
	    base: '../static/js/' //静态资源所在路径
	  }).extend({
	    index: 'lib/index' //主入口模块
	  }).use(['index', 'console','trans7report']);
  </script>
</body>
</html>