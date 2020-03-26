<%@ page import="static mesh.util.rb" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/page/_header.jsp"%>
<body class="container">
    <%@include file="/WEB-INF/page/_navbar.jsp"%>
    <main class="row">
        <div class="col-lg-6 col-md-6">
        <table id="tblUsers" class="table table-bordered table-condensed table-hover">
            <thead><tr><td><b><%=rb.getString("login")%></b></td><td><b><%=rb.getString("fio")%></b></td><td><b><%=rb.getString("access")%></b></td></tr></thead>
            <tbody></tbody>
        </table>
        </div>
        <div class="col-lg-6 col-md-6">
        <table id="tblLoans" class="table table-bordered table-condensed table-hover">
            <thead><tr><td><%=rb.getString("name")%></td><td><%=rb.getString("fio")%></td><td><%=rb.getString("access")%></td></tr></thead>
            <tbody></tbody>
        </table>
        </div>

        <script type="text/javascript">
            function findUser() {
                var currentValue = null;
                try{currentValue=$(this).val();}catch(e){};
                $('#tblUsers tbody tr').remove();
                $.ajax({
                    type: "GET",
                    async: true,
                    url: "/api/user",
                    data: null,
                    xhrFields: {withCredentials: true}
                }).error(function (msg) {
                    console.log(msg);
                }).success(function(data) {
                    data.data.forEach(it => {
                        var tr = '<tr style="cursor:pointer;" uid="'+it.id+'"><td>'+it.login+'</td><td>'+it.fio+'</td><td>'+it.role.name+'</td></tr>';
                        $('#tblUsers tbody').append(tr);
                    });
                    $('#tblUsers tbody tr[uid!=""]').on('click', function(x) {
                        window.location.href = '/user?id='+$(this).attr('uid');
                    });
                })
            };
            findUser();
        </script>

    </main>
    <footer></footer>
</body>
</html>
