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
<base href="<%=basePath%>">
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta HTTP-EQUIV="pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<meta HTTP-EQUIV="expires" CONTENT="0">
<title>大成支付</title>

<link rel="shortcut icon" href="./static/favicon.ico"/>
<link rel="stylesheet" href="./static/css/font.css">
<link rel="stylesheet" href="./static/css/weadmin.css">

<link rel="stylesheet" href="./static/js/css/layui.css">
<script type="text/javascript" src="./static/js/layui.js" charset="utf-8"></script>


</head>
<body>
	<!-- 顶部开始 -->
	<div class="container">
		<div class="logo" >
			<a href="./view/toAdminIndex"><img src="./static/images/logo.png" style="width:200px;height:46px;"></img></a>
		</div>
		<div class="left_open">
			<i title="展开左侧栏" class="iconfont">&#xe668;</i>
		</div>
		
		<c:if test="${sessionScope.merchant_user.roleId == '1' }">
		<ul class="layui-nav left fast-add" lay-filter="" >
			<li class="layui-nav-item"><a href="javascript:;" style="line-height:46px;color:black;">常用功能</a>
				<dl class="layui-nav-child" >
					<!-- 二级菜单 -->
					<dd>
						<a onclick="WeAdminShow('充值订单','view/merchantRechargeList')"><i
							class="iconfont">&#xe6a2;</i>我的订单</a>
					</dd>
					<dd>
						<a onclick="WeAdminShow('api管理','merchant/api')"><i
							class="iconfont">&#xe6a8;</i>API管理</a>
					</dd>
					<dd>
						<a onclick="WeAdminShow('用户','merchant/accountinfo')"><i
							class="iconfont">&#xe6b8;</i>我的账户</a>
					</dd>
				</dl></li>
		</ul>
		</c:if>
		
		<ul class="layui-nav right" lay-filter="">
			<c:if test="${sessionScope.merchant_user.roleId == '1' }">
			<li class="layui-nav-item"><a href="javascript:;" style="line-height:46px;color:black;">${sessionScope.merchant_user.mobileNumber }</a>
				<dl class="layui-nav-child">
					<!-- 二级菜单 -->
					<dd>
						<a onclick="WeAdminShow('个人信息','merchant/accountinfo')">个人信息</a>
					</dd>
					<dd>
						<a class="loginout" href="merchant/exit" style="margin-top: 0px">退出</a>
					</dd>
				</dl>
			</li>
			</c:if>
			<li class="layui-nav-item to-index"><a href="merchant/exit" style="line-height:46px;color:black">退出账号</a>
			</li>
		</ul>

		
		
		
		

	</div>
	<!-- 顶部结束 -->
	<!-- 中部开始 -->
	<!-- 左侧菜单开始 -->
	<div class="left-nav">
		<div id="side-nav">
			<ul id="nav">
				<c:forEach items="${sessionScope.menu}" var="f">
					<c:if test="${ not empty f }">
						<li><a href="javascript:;"> <i class="iconfont">${f.menuTarget }</i>
								<cite>${f.menuName }</cite> <i class="iconfont nav_right">&#xe602;</i>
						</a> <c:forEach items="${f.childMenu }" var="t">
								<ul class="sub-menu">
									<li><a
										_href="${pageContext.request.contextPath }/${t.menuUrl }">
											<i class="iconfont">&#xe602;</i> <cite>${t.menuName }</cite>

									</a></li>
								</ul>
							</c:forEach>
					</c:if>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
	<!-- <div class="x-slide_left"></div> -->
	<!-- 左侧菜单结束 -->
	<!-- 右侧主体开始 -->
	<div class="page-content">
		<div class="layui-tab tab" lay-filter="wenav_tab" id="WeTabTip"
			lay-allowclose="true">
			<ul class="layui-tab-title" id="tabName">
				 <li>首页</li>
			</ul>
			<div class="layui-tab-content" style="background-color: #F5F5F5;margin-left: 10px;margin-top: 10px">
				 <div class="layui-tab-item layui-show">
					
					<c:if test="${sessionScope.merchant_user.roleId == 3 }">
					
					<iframe src='./main/mainmenu' frameborder="0" scrolling="yes"
						class="weIframe"></iframe>
						
					</c:if>
					
					<c:if test="${sessionScope.merchant_user.roleId == 2}">
					
					<iframe src='./proxy/mainmenu' frameborder="0" scrolling="yes"
						class="weIframe"></iframe>
						
					</c:if>
					
					
					<c:if test="${sessionScope.merchant_user.roleId == 1 }">
					
					<iframe src='./index/mainmenu' frameborder="0" scrolling="yes"
						class="weIframe"></iframe>
					</c:if>
						
						
				</div>
			</div>
		</div>
	</div>
	<div class="page-content-bg"></div>
	<!-- 右侧主体结束 -->
	<!-- 中部结束 -->
	<!-- 底部开始 -->
	<div class="footer">
		<div class="copyright">Copyright ©2018 大成支付V1.0</div>
	</div>
	<!-- 底部结束 -->
	<script type="text/javascript">
		//			layui扩展模块的两种加载方式-示例
		//		    layui.extend({
		//			  admin: '{/}../../static/js/admin' // {/}的意思即代表采用自有路径，即不跟随 base 路径
		//			});
		//			//使用拓展模块
		//			layui.use('admin', function(){
		//			  var admin = layui.admin;
		//			});
		layui.config({
			base : './static/js/',
			version : '101100'
		}).use('admin');
		layui.use([ 'jquery', 'admin' ], function() {
			var $ = layui.jquery;
			$(function() {
				var login = JSON.parse(localStorage.getItem("login"));
				/* if(login){
					if(login=0){
						window.location.href='./login.html';
						return false;
					}else{
						return false;
					}
				}else{
					window.location.href='./login.html';
					return false;
				} */
			});
		});
	</script>
</body>
<!--Tab菜单右键弹出菜单-->
<ul class="rightMenu" id="rightMenu">
	<li data-type="fresh">刷新</li>
	<li data-type="current">关闭当前</li>
	<li data-type="other">关闭其它</li>
	<li data-type="all">关闭所有</li>
</ul>
</html>