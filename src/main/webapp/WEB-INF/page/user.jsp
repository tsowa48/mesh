<%@ page import="static mesh.util.rb" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/page/_header.jsp"%>
<body class="container">
<main class="panel panel-primary vertical-center modal-dialog">
    <div class='panel-heading' style='text-align:center;'><b><%=rb.getString("user") %></b></div>
    <% mesh.db.User user = (mesh.db.User)request.getAttribute("user");
        List<mesh.db.Role> roles = (List<mesh.db.Role>)request.getAttribute("roles"); %>
        <div id='userInfo' class='list-group panel-body' data-id="<%=user.getId()%>">
            <div class='input-group'>
                <span class='input-group-addon'><%=rb.getString("fio") %></span>
                <input type='text' name='fio' class='form-control input-sm' autofocus value="<%=user.getFio()%>"/>
            </div>
            <br>
            <div class='input-group'>
                <span class='input-group-addon'><%=rb.getString("login")%></span>
                <input type='text' name='login' class='form-control input-sm' value="<%=user.getLogin()%>"/>
            </div>
            <br>
            <div class='input-group'>
                <span class='input-group-addon'><%=rb.getString("role")%></span>
                <select name='rid' class='form-control input-sm'>
                <% for(mesh.db.Role role : roles)
                    out.println("<option " + (user.getRole().getId().equals(role.getId()) ? "selected" : "") + " value=\"" + role.getId() + "\">" + role.getName() + "</option>");
                %></select>
            </div>
        </div>
        <script type="text/javascript">
            $('#userInfo .form-control').change(function() {
                var control = $(this);
                control.parent().removeClass('has-error');
                var id = $('#userInfo').data('id');
                var field = $(this).attr('name');
                var value = $(this).val();
                var newData = {"id": id, "field": field, "value": value};
                $.ajax({
                    type: "PUT",
                    async: false,
                    url: "/api/user",
                    data: JSON.stringify(newData),
                    xhrFields: {withCredentials: true}
                }).error(function (msg) {
                    console.log(msg);
                    control.parent().addClass('has-error');
                }).success(function (data) {
                    if(data.status !== 200)
                        control.parent().addClass('has-error');
                });
            });
        </script>
    </div>
    <div class='panel-footer'>
        <div id='removeUser' class='btn btn-danger btn-block'><%=rb.getString("remove") %></div>
        <a href="/" class='btn btn-primary btn-block'><%=rb.getString("back") %></a>
        <script type="text/javascript">
            $('#removeUser').click(function() {
                $.ajax({
                    type: "DELETE",
                    async: false,
                    url: "/api/user",
                    data: JSON.stringify({"id": <%=request.getParameter("id")%>}),
                    xhrFields: {withCredentials: true}
                }).error(function (msg) {
                    console.log(msg);
                }).success(function (data) {
                    window.location.href = '/';
                });
            });
        </script>
    </div>
</main>
</body>
</html>