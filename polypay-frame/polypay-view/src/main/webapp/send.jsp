<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Map<String, String> data = (Map<String, String>) request.getAttribute("dataMap");
	String url = (String)request.getAttribute("action");
%>
<html>
<!--[if lt IE 10]>
<script>alert("为了更好的体验，不支持IE10以下的浏览器。请选择google chrome 或者 firefox 浏览器。"); location.href="http://www.ielpm.com";</script>
<![endif]-->
<script src="http://libs.baidu.com/jquery/1.11.3/jquery.js"></script>
<body>
<form name="form" id="form" action="<%=url %>" method="post">
    <% String tmp = "";
        for(Iterator<String> it=data.keySet().iterator(); it.hasNext(); ){
            tmp = it.next();
    %>
    <input type="hidden" name="<%=tmp%>" value='<%=data.get(tmp)%>'/>
    <%}%>
</form>

<script>
       $(function(){
        $("#form").submit();
    })   
</script>
</body>
</html>
