<%@ page import="static mesh.util.rb" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/page/_header.jsp"%>
<body class="container">
<main class="panel panel-primary vertical-center modal-dialog">
    <div class='panel-heading' style='text-align:center;'><b><%=rb.getString("loan") %></b></div>
    <% mesh.db.Loan loan = (mesh.db.Loan)request.getAttribute("loan"); %>
    <div id='loanInfo' class='list-group panel-body' data-id="<%=loan.getId()%>">
        <div class='input-group'>
            <span class='input-group-addon'><%=rb.getString("name") %></span>
            <input type='text' name='name' class='form-control input-sm' autofocus value="<%=loan.getName()%>"/>
        </div>
        <br>
        <div class='input-group'>
            <span class='input-group-addon'><%=rb.getString("min_amount") %></span>
            <input type='number' name='min_amount' class='form-control input-sm' min="0.01" step="0.01" value="<%=loan.getMinAmount()%>"/>
        </div>
        <div class='input-group'>
            <span class='input-group-addon'><%=rb.getString("max_amount") %></span>
            <input type='number' name='max_amount' class='form-control input-sm' min="0.01" step="0.01" value="<%=loan.getMaxAmount()%>"/>
        </div>
        <br>
        <div class='input-group'>
            <span class='input-group-addon'><%=rb.getString("min_term") %></span>
            <input type='number' name='min_term' class='form-control input-sm' min="1" step="1" value="<%=loan.getMinTerm()%>"/>
        </div>
        <div class='input-group'>
            <span class='input-group-addon'><%=rb.getString("max_term") %></span>
            <input type='number' name='max_term' class='form-control input-sm' min="1" step="1" value="<%=loan.getMaxTerm()%>"/>
        </div>
        <br>
        <div class='input-group'>
            <span class='input-group-addon'><%=rb.getString("min_percent") %></span>
            <input type='number' name='min_percent' class='form-control input-sm' min="0.00" max="0.99" step="0.01" value="<%=loan.getMinPercent()%>"/>
        </div>
        <div class='input-group'>
            <span class='input-group-addon'><%=rb.getString("max_percent") %></span>
            <input type='number' name='max_percent' class='form-control input-sm' min="0.01" max="1.00" step="0.01" value="<%=loan.getMaxPercent()%>"/>
        </div>
        <br>
        <div class='input-group'>
            <span class='input-group-addon'><%=rb.getString("min_solvency") %></span>
            <input type='number' name='min_solvency' class='form-control input-sm' min="0.00" max="0.99" step="0.01" value="<%=loan.getMinSolvency()%>"/>
        </div>
        <div class='input-group'>
            <span class='input-group-addon'><%=rb.getString("max_solvency") %></span>
            <input type='number' name='max_solvency' class='form-control input-sm' min="0.01" max="1.00" step="0.01" value="<%=loan.getMaxSolvency()%>"/>
        </div>
    </div>
    <script type="text/javascript">
        $('#loanInfo .form-control').change(function() {
            var control = $(this);
            control.parent().removeClass('has-error');
            var id = $('#loanInfo').data('id');
            var field = $(this).attr('name');
            var value = $(this).val();
            var newData = {"id": id, "field": field, "value": value};
            $.ajax({
                type: "PUT",
                async: false,
                url: "/api/loan",
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
        <div id='removeLoan' class='btn btn-danger btn-block'><%=rb.getString("remove") %></div>
        <a href="/" class='btn btn-primary btn-block'><%=rb.getString("back") %></a>
        <script type="text/javascript">
            $('#removeLoan').click(function() {
                $.ajax({
                    type: "DELETE",
                    async: false,
                    url: "/api/loan",
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