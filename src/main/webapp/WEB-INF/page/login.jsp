<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/page/_header.jsp"%>
    <body class='container'>
        <form action="/login" method="post" class="panel panel-inverse vertical-center modal-dialog modal-sm">
            <div class='panel-heading' style='text-align:center;'><b><%=mesh.util.rb.getString("app_name") %></b></div>
            <div class='list-group panel-body'>
                <div class='input-group <%=(request.getAttribute("error")==null?"":request.getAttribute("error"))%>'>
                    <span class='input-group-addon'><i class="glyphicon glyphicon-user"></i></span>
                    <input type='text' name='login' class='form-control input-sm' required autofocus value="operator"/>
                </div>
                <br>
                <div class='input-group <%=(request.getAttribute("error")==null?"":request.getAttribute("error"))%>'>
                    <span class='input-group-addon'><i class="glyphicon glyphicon-lock"></i></span>
                    <input type='password' name='password' class='form-control input-sm' required value="operator"/>
                </div>
            </div>
            <div class='panel-footer' style="padding-top:5px">
                <input type='submit' class='btn btn-success btn-block' value='<%=mesh.util.rb.getString("signin") %>'/>
                <a href="/register" class='btn btn-default btn-block disabled'><%=mesh.util.rb.getString("register") %></a>
            </div>
        </form>
    </body>
</html>
