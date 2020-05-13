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
            <thead><tr><td><b><%=rb.getString("name")%></b></td><td><b><%=rb.getString("summ")%></b></td><td><b><%=rb.getString("term")%></b></td><td><b><%=rb.getString("percent")%></b></td></tr></thead>
            <tbody></tbody>
        </table>
        </div>

        <script type="text/javascript">
            function findUser() {
                $('#tblUsers tbody tr').remove();
                $.ajax({
                    type: "GET",
                    async: false,
                    cache: false,
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

            function findLoan() {
                $('#tblLoans tbody tr').remove();
                $.ajax({
                    type: "GET",
                    async: false,
                    cache: false,
                    url: "/api/loan",
                    data: null,
                    xhrFields: {withCredentials: true}
                }).error(function (msg) {
                    console.log(msg);
                }).success(function(data) {
                    data.data.forEach(it => {
                        var tr = '<tr style="cursor:pointer;" lid="'+it.id+'"><td>'+it.name+'</td><td>'+it.min_amount+' - '+it.max_amount+'</td><td>'+it.min_term+' - '+it.max_term+'</td><td>'+it.min_percent+' - '+it.max_percent+'</td></tr>';
                        $('#tblLoans tbody').append(tr);
                    });
                    $('#tblLoans tbody tr[lid!=""]').on('click', function(x) {
                        window.location.href = '/loan?id='+$(this).attr('lid');
                    });
                })
            };

            findUser();
            findLoan();
        </script>

    </main>
    <footer></footer>
</body>
</html>
