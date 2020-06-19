<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/page/_header.jsp"%>
    <body class='container'>
        <form action="/login" method="post" class="panel panel-inverse vertical-center modal-dialog modal-sm">
            <div class='panel-heading' style='text-align:center;'><b><%=mesh.util.rb.getString("app_name") %></b></div>
            <div class='list-group panel-body'>
                <div class='input-group <%=(request.getAttribute("error")==null?"":request.getAttribute("error"))%>'>
                    <span class='input-group-addon'><i class="glyphicon glyphicon-user"></i></span>
                    <input type='text' <%=request.getAttribute("error") != null ? "data-toggle='tooltip' title='Неверный логин или пароль' data-placement='right'" : "" %> name='login' class='form-control input-sm' required autofocus value=""/>
                </div>
                <br>
                <div class='input-group <%=(request.getAttribute("error")==null?"":request.getAttribute("error"))%>'>
                    <span class='input-group-addon'><i class="glyphicon glyphicon-lock"></i></span>
                    <input type='password' <%=request.getAttribute("error") != null ? "data-toggle='tooltip' title='Неверный логин или пароль' data-placement='right'" : "" %> name='password' class='form-control input-sm' required value=""/>
                </div>
            </div>
            <div class='panel-footer' style="padding-top:5px">
                <input type='submit' class='btn btn-success btn-block' value='<%=mesh.util.rb.getString("signin") %>'/>
            </div>
            <%if(request.getAttribute("error") != null) { %>
            <script type="text/javascript">
                $('.form-control').tooltip().mouseover();
            </script>
            <% } %>
        </form>
    </body>
</html>
