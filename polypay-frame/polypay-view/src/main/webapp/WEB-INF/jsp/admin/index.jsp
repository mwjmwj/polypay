<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<title>商户后台管理</title>
	<link rel="stylesheet" href="./static/css/font.css">
		<link rel="stylesheet" href="./static/css/weadmin.css">
		<script type="text/javascript" src="./static/js/layui.js" charset="utf-8"></script>

</head>
<body>
		<!-- 顶部开始 -->
		<div class="container">
			<div class="logo">
				<a href="./view/toAdminIndex">源盛丰支付</a>
			</div>
			<div class="left_open">
				<i title="展开左侧栏" class="iconfont">&#xe699;</i>
			</div>
			<ul class="layui-nav left fast-add" lay-filter="">
				<li class="layui-nav-item">
					<a href="javascript:;">常用功能</a>
					<dl class="layui-nav-child">
						<!-- 二级菜单 -->
						<dd>
							<a onclick="WeAdminShow('资讯','https://www.youfa365.com/')"><i class="iconfont">&#xe6a2;</i>我的订单</a>
						</dd>
						<dd>
							<a onclick="WeAdminShow('图片','http://www.baidu.com')"><i class="iconfont">&#xe6a8;</i>API管理</a>
						</dd>
						<dd>
							<a onclick="WeAdminShow('用户','https://www.youfa365.com/')"><i class="iconfont">&#xe6b8;</i>我的账户</a>
						</dd>
					</dl>
				</li>
			</ul>
			<ul class="layui-nav right" lay-filter="">
				<li class="layui-nav-item">
					<a href="javascript:;">${sessionScope.merchant_user.mobileNumber }</a>
					<dl class="layui-nav-child">
						<!-- 二级菜单 -->
						<dd>
							<a onclick="WeAdminShow('个人信息','http://www.baidu.com')">个人信息</a>
						</dd>
						<dd>
							<a onclick="WeAdminShow('切换帐号','.merchant/exit')">切换帐号</a>
						</dd>
						<dd>
							<a class="loginout" href="merchant/exit">退出</a>
						</dd>
					</dl>
				</li>
				<li class="layui-nav-item to-index">
					<a href="view/admin">退出账号</a>
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
							<li>
							<a href="javascript:;">
							<i class="iconfont">&#xe6b8;</i>
							<cite>${f.menuName }</cite>
							<i class="iconfont nav_right">&#xe697;</i>
							</a>
							<c:forEach items="${f.childMenu }" var="t">
							<ul class="sub-menu">
							<li>
								<a _href="${pageContext.request.contextPath }/${t.menuUrl }">
									<i class="iconfont">&#xe6a7;</i>
									<cite>${t.menuName }</cite>

								</a>
							</li>
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
			<div class="layui-tab tab" lay-filter="wenav_tab" id="WeTabTip" lay-allowclose="true">
				<ul class="layui-tab-title" id="tabName">
					<li>登录信息</li>
				</ul>
				<div class="layui-tab-content">
					<div class="layui-tab-item layui-show">
						<iframe src='./view/admin' frameborder="0" scrolling="yes" class="weIframe"></iframe>
					</div>
				</div>
			</div>
		</div>
		<div class="page-content-bg"></div>
		<!-- 右侧主体结束 -->
		<!-- 中部结束 -->
		<!-- 底部开始 -->
		<div class="footer">
			<div class="copyright">Copyright ©2018 源盛丰 V1.0</div>
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
			  base: './static/js/'
			  ,version: '101100'
			}).use('admin');
			layui.use(['jquery','admin'], function(){
				var $ = layui.jquery;
				$(function(){
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