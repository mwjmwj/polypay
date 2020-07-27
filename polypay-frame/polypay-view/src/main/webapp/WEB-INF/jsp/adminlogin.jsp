<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>大成支付</title>

<link rel="shortcut icon" href="./static/favicon.ico" />
<!--必要样式-->
<link href="static/css/styles.css" rel="stylesheet" type="text/css" />
<link href="static/css/demo.css" rel="stylesheet" type="text/css" />
<link href="static/css/loaders.css" rel="stylesheet" type="text/css" />


</head>
<body>
	<div class='login'>
		<div class='login_title'>
			<span>大成支付</span>
		</div>
		<div class='login_fields'>
			<div class='login_fields__user'>
				<div class='icon'>
					<img alt="" src='static/img/user_icon_copy.png'>
				</div>
				<input name="mobileNumber" placeholder='手机号' maxlength="11" type='text'
					autocomplete="off" style="width: 320px" />
				<div class='validation'>
					<img alt="" src='static/img/tick.png'>
				</div>
			</div>
			<div class='login_fields__password'>
				<div class='icon'>
					<img alt="" src='static/img/lock_icon_copy.png'>
				</div>
				<input name="passWord" placeholder='密码' maxlength="32" type='password'
					autocomplete="off" style="width: 320px">
				<div class='validation'>
					<img alt="" src='static/img/tick.png'>
				</div>
			</div>
			<div class='login_fields__password'>
				<div class='icon'>
					<img alt="" src='static/img/key.png'>
				</div>
				<input name="code" placeholder='验证码' maxlength="4" type='text'
					name="ValidateNum" autocomplete="off" style="width: 320px">
				<div class='validation' style="opacity: 1; right: -5px; top: -3px;">
					<canvas class="J_codeimg" id="myCanvas" onclick="Code();">对不起，您的浏览器不支持canvas，请下载最新版浏览器!</canvas>
				</div>
			</div>
			<div class='login_fields__submit'>
			
				<input type='button' value='登录' />
				
			</div>
		</div>
		<div class='success'></div>
		<div class='disclaimer'>
			<p>欢迎登录后台管理系统</p>
		</div>
	</div>
	<div class='authent'>
		<div class="loader"
			style="height: 44px; width: 44px; margin-left: 28px;">
			<div class="loader-inner ball-clip-rotate-multiple">
				<div></div>
				<div></div>
				<div></div>
			</div>
		</div>
		<p>认证中...</p>
	</div>
	<div class="OverWindows"></div>

	<link href="static/js/css/layui.css" rel="stylesheet" type="text/css" />

	<script type="text/javascript" src="static/js/jquery.min.js"></script>
	<script type="text/javascript" src="static/js/jquery-ui.min.js"></script>
	<script type="text/javascript" src='static/js/stopExecutionOnTimeout.js?t=1'></script>
	<script type="text/javascript" src="static/js/layui.all.js"></script>
	<script type="text/javascript" src="static/js/Particleground.js"></script>
	<script type="text/javascript" src="static/js/Treatment.js"></script>
	<script type="text/javascript" src="static/js/jquery.mockjax.js"></script>
	
	<script type="text/javascript" src="static/js/md5.js"></script>

	<script type="text/javascript">
		var canGetCookie = 0;//是否支持存储Cookie 0 不支持 1 支持
		var ajaxmockjax = 1;//是否启用虚拟Ajax的请求响 0 不启用  1 启用
		//默认账号密码
		
		var truelogin = "123456";
		var truepwd = "123456";
		
		var CodeVal = 0;
	    Code();
	    function Code() {
			if(canGetCookie == 1){
				createCode("AdminCode");
				var AdminCode = getCookieValue("AdminCode");
				showCheck(AdminCode);
			}else{
				showCheck(createCode(""));
			}
	    }
	    function showCheck(a) {
			CodeVal = a;
	        var c = document.getElementById("myCanvas");
	        var ctx = c.getContext("2d");
	        ctx.clearRect(0, 0, 1000, 1000);
	        ctx.font = "80px 'Hiragino Sans GB'";
	        ctx.fillStyle = "#E8DFE8";
	        ctx.fillText(a, 0, 100);
	    }
	    $(document).keypress(function (e) {
	        // 回车键事件  
	        if (e.which == 13) {
	            $('input[type="button"]').click();
	        }
	    });
	    //粒子背景特效
	  /*   $('body').particleground({
	        dotColor: '#E8DFE8',
	        lineColor: 'orange'
	    }); */
	    $('input[name="pwd"]').focus(function () {
	        $(this).attr('type', 'password');
	    });
	    $('input[type="text"]').focus(function () {
	        $(this).prev().animate({ 'opacity': '1' }, 200);
	    });
	    $('input[type="text"],input[type="password"]').blur(function () {
	        $(this).prev().animate({ 'opacity': '.5' }, 200);
	    });
	    $('input[name="login"],input[name="pwd"]').keyup(function () {
	        var Len = $(this).val().length;
	        if (!$(this).val() == '' && Len >= 5) {
	            $(this).next().animate({
	                'opacity': '1',
	                'right': '30'
	            }, 200);
	        } else {
	            $(this).next().animate({
	                'opacity': '0',
	                'right': '20'
	            }, 200);
	        }
	    });
	    var open = 0;
	    layui.use('layer', function () {
	
	        //非空验证
	        $('input[type="button"]').click(function () {
	            var login = $('input[name="mobileNumber"]').val();
	            var pwd = $('input[name="passWord"]').val();
	            var code = $('input[name="code"]').val();
	            
	            
	            
	            if (login == '') {
	                ErroAlert('请输入您的账号');
	            } else if (pwd == '') {
	                ErroAlert('请输入密码');
	            } else if (code != CodeVal) {
	                ErroAlert('输入正确验证码');
	            } else {
	            	
	            
	                //认证中..
	               /*  fullscreen(); */
	                
	                /* $('.login').addClass('test'); //倾斜特效
	                setTimeout(function () {
	                    $('.login').addClass('testtwo'); //平移特效
	                }, 300); */
	              /*   setTimeout(function () {
	                    $('.authent').show().animate({ right: -320 }, {
	                        easing: 'easeOutQuint',
	                        duration: 600,
	                        queue: false
	                    });
	                    $('.authent').animate({ opacity: 1 }, {
	                        duration: 200,
	                        queue: false
	                    }).addClass('visible');
	                }, 500);
 */
	                
	                var mdpwd = MD5(pwd);
	                //登录
	                var JsonData = { mobileNumber: login, passWord: mdpwd};
					//此处做为ajax内部判断
					var url = "";
				
					$.ajax({
						type:"post",
						url:"merchant/login",
						data:JsonData,
						success:function(data){
							if(data=="success"){
								layer.msg("登陆成功！",{icon:1,anim:2,time:100},function(){
									window.location.href="<%=basePath%>view/toAdminIndex";
								});
							}else{
								ErroAlert("登陆失败！请检查用户名和密码后重试！");
							}
						}
					});
					
					
					   
	            }
	        })
	    })
	    var fullscreen = function () {
	        elem = document.body;
	        if (elem.webkitRequestFullScreen) {
	            elem.webkitRequestFullScreen();
	        } else if (elem.mozRequestFullScreen) {
	            elem.mozRequestFullScreen();
	        } else if (elem.requestFullScreen) {
	            elem.requestFullscreen();
	        } else {
	            //浏览器不支持全屏API或已被禁用  
	        }
	    }  
		if(ajaxmockjax == 1){
			$.mockjax({  
				url: 'Ajax/Login',  
				status: 200,  
				responseTime: 50,          
				responseText: {"Status":"ok","Text":"登录成功<br /><br />欢迎回来"}  
			}); 
			$.mockjax({  
				url: 'Ajax/LoginFalse',  
				status: 200,  
				responseTime: 50,          
				responseText: {"Status":"Erro","Erro":"账号名或密码或验证码有误"}
			});   
		}
    </script>



	<script type="text/javascript" src="static/js/md5.js"></script>
	
</body>
</html>