<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String url = (String)request.getAttribute("action");
%>
<html>
<!--[if lt IE 10]>
<script>alert("为了更好的体验，不支持IE10以下的浏览器。请选择google chrome 或者 firefox 浏览器。"); location.href="http://www.ielpm.com";</script>
<![endif]-->
<script src="http://libs.baidu.com/jquery/1.11.3/jquery.js"></script>
<body>

<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
  <div class="container">
  	<a class="navbar-brand"><strong>跳转支付页（<s>ie1-9</s>）</strong></a>
  </div>
</nav>
    <%=url
    %>

</body>
</html>
