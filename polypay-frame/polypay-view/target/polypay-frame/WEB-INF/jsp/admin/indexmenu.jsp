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
<title>poly-pay</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
	<link rel="stylesheet" href="../static/js/css/layui.css">
	<link href="../static/js/maincss/bootstrap.min.css?v=3.3.6" rel="stylesheet">
	<link href="../static/js/maincss/font-awesome.css?v=4.4.0" rel="stylesheet">

    <!-- Morris -->
    <link href="../static/js/maincss/morris-0.4.3.min.css" rel="stylesheet">

    <!-- Gritter -->
    <link href="../static/js/maincss/jquery.gritter.css" rel="stylesheet">

    <link href="../static/js/maincss/animate.css" rel="stylesheet">
    <link href="../static/js/maincss/style.css?v=4.1.0" rel="stylesheet">


<link rel="stylesheet" href="../static/js/css/layui.css">
<!-- 注意：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->

</head>
<script type="text/javascript" src="../static/js/layui.all.js"></script>
   <!-- 全局js -->
    <script src="../static/js/jquery.min.js"></script>
    <script src="../static/js/bootstrap.min.js?v=3.3.7"></script>

    <!-- jQuery UI -->
    <script src="../static/js/jquery-ui.min.js"></script>
    
     <script src='../static/js/echarts.js'></script>
    
<body class="gray-bg">

    <div class="wrapper wrapper-content">
        <div class="row">
            <div class="col-sm-3">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <span class="label label-success pull-right">总数</span>
                        <h5>充值金额</h5>
                    </div>
                    <div class="ibox-content">
                        <h1 class="no-margins">
                        <c:if test="${data.merchantAllRechargeAmount ==null }">
                        0
                        </c:if>
                         <c:if test="${data.merchantAllRechargeAmount !=null }">
                        ${data.merchantAllRechargeAmount }
                        </c:if>
                        
                        </h1>
                        <!-- <div class="stat-percent font-bold text-success">98% <i class="fa fa-bolt"></i>
                        </div> -->
                        <small>元</small>
                    </div>
                </div>
            </div>
            <div class="col-sm-3">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <span class="label label-info pull-right">总数</span>
                        <h5>充值订单数</h5>
                    </div>
                    <div class="ibox-content">
                        <h1 class="no-margins">${data.merchantAllOrderNumber }</h1>
                     <!--     <div class="stat-percent font-bold text-info">笔<i class="fa fa-level-up"></i>
                         </div> -->
                        <small>笔</small>
                    </div>
                </div>
            </div>
            <div class="col-sm-3">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <span class="label label-primary pull-right">今日</span>
                        <h5>充值金额</h5>
                    </div>
                    <div class="ibox-content">
                        <h1 class="no-margins">
                         <c:if test="${data.merchantTodayRechargeAmount ==null }">
                        0
                        </c:if>
                         <c:if test="${data.merchantTodayRechargeAmount !=null }">
                        ${data.merchantTodayRechargeAmount }
                        </c:if>
                        </h1>
                        <!-- <div class="stat-percent font-bold text-navy">44% <i class="fa fa-level-up"></i>
                        </div> -->
                        <small>元</small>
                    </div>
                </div>
            </div>
            <div class="col-sm-3">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <span class="label label-danger pull-right">今日</span>
                        <h5>充值订单数</h5>
                    </div>
                    <div class="ibox-content">
                        <h1 class="no-margins">
                         <c:if test="${data.merchantTodayOrderNumber ==null }">
                        0
                        </c:if>
                         <c:if test="${data.merchantTodayOrderNumber !=null }">
                        ${data.merchantTodayOrderNumber }
                        </c:if>
                        </h1>
                       <!--  <div class="stat-percent font-bold text-danger">38% <i class="fa fa-level-down"></i>
                        </div> -->
                        <small>笔</small>
                    </div>
                </div>
            </div>
        </div>
        </div>
        
        <div>
        <div id='ordernumbermain' style='width:50%;height:400px;float:left'></div>
        <div id='orderamountmain' style='width:50%;height:400px;float:left'></div>
    	</div>
        
        
        <fieldset class="layui-elem-field layui-field-title"
		style="margin-top: 20px;">
		<legend>登录日志</legend>
		</fieldset>
	
	
        <div>
        <table class="layui-hide" id="loginlog" lay-filter="loginlog" style="margin-top: 1000px;"></table>
		</div>

	<script>
		layui.use('table', function() {
			var table = layui.table;
			table.render({
				elem : '#loginlog',
				url : '../merchant/loginlog/list',
				title : '用户登录数据表',
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
					field : 'ip',
					title : '登录IP',
					width : 150,
					align : 'center',
					fixed : 'left'
				}, {
					field : 'loginAddress',
					title : '登录地址',
					width : 150,
					align : 'center',
					fixed : 'left'
				}, {
					field : 'loginTime',
					title : '登录时间',
					width : 200,
					align : 'center',
					fixed : 'left',
					templet : function(row) {
						return createTime(row.loginTime);
					}
				}] ],
				page : true

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
        
        var orderNumberChart = echarts.init(document.getElementById('ordernumbermain'));
        var orderAmountChart = echarts.init(document.getElementById('orderamountmain'));
        var times = ${ehtimes};
        var orderNumbers = ${ehOrderNumbers};
        var orderAmounts = ${ehOrderAmounts};
        var option = {
                title:{
                    text:'订单数'
                },
                //提示框组件
                tooltip:{
                    //坐标轴触发，主要用于柱状图，折线图等
                    trigger:'axis'
                },
                //图例
                legend:{
                    data:['笔数']
                },
                //横轴
                xAxis:{
                    data:times
                },
                //纵轴
                yAxis:{},
                //系列列表。每个系列通过type决定自己的图表类型
                series:[{
                    name:'笔数',
                    //折线图
                    type:'line',
                    data:orderNumbers
                }]
            };
        
        
        var option1 = {
                title:{
                    text:'充值订单成功金额'
                },
                //提示框组件
                tooltip:{
                    //坐标轴触发，主要用于柱状图，折线图等
                    trigger:'axis'
                },
                //图例
                legend:{
                    data:['金额']
                },
                //横轴
                xAxis:{
                    data:times
                },
                //纵轴
                yAxis:{},
                //系列列表。每个系列通过type决定自己的图表类型
                series:[{
                    name:'金额',
                    //折线图
                    type:'line',
                    data:orderAmounts
                }]
            };
            //使用刚指定的配置项和数据显示图表
            orderNumberChart.setOption(option);
            orderAmountChart.setOption(option1);
        </script>
</body>
</html>