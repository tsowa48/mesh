<%@ page import="static mesh.util.rb" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/page/_header.jsp"%>
<body class="container">
    <%@include file="/WEB-INF/page/_navbar.jsp"%>

    <main>
        <table id="tblData" class="table table-bordered table-condensed table-striped table-hover">
            <thead><tr><th rowspan="2" class="col-lg-4"><%=rb.getString("fio")%></th><th rowspan="2" class="col-lg-1"><%=rb.getString("birth")%></th><th colspan="2" class="col-lg-2"><%=rb.getString("passport")%></th><th rowspan="2" class="col-lg-5 hidden-xs"><%=rb.getString("address")%></th></tr>
            <tr><th class="col-lg-1"><%=rb.getString("serial")%></th><th class="col-lg-1"><%=rb.getString("number")%></th></tr></thead>
            <tbody></tbody>
        </table>
        <script type="text/javascript">
            function findClient() {
                var currentValue = null;
                try{currentValue=$(this).val();}catch(e){};
                $('#tblData tbody tr').remove();
                $.ajax({
                    type: "GET",
                    async: true,
                    url: "/api/client",
                    data: {q: currentValue},
                    xhrFields: {withCredentials: true}
                }).error(function (msg) {
                    console.log(msg);
                }).success(function(data) {
                    data.data.forEach(it => {
                        var clazz = (it.solvency===null?'default':(it.solvency>0.66?'success':(it.solvency<0.33?'danger':'warning')));
                        var tr = '<tr class="'+clazz+'"><td>'+it.firstName+' '+it.lastName+(it.patronymic!==null?(' '+it.patronymic):'')+'</td><td>' +
                            it.birth + '</td><td>'+it.documents[0].serial+'</td><td>'+it.documents[0].number+'</td><td>' + it.address + '</td></tr>';
                        $('#tblData tbody').append(tr);
                    });
                    $('input[name="search"]').val('');
                })
            };
            $('input[name="search"]').on('change', findClient);
            findClient();
        </script>
    </main>

    <footer></footer>
</body>
</html>
