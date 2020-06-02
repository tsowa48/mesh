<%@ page import="static mesh.util.rb" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/page/_header.jsp"%>
<body class="container">
<main class="panel panel-primary vertical-center modal-dialog">
    <div class='panel-heading' style='text-align:center;'><b><%=rb.getString("order") %></b></div>
    <% mesh.db.Order order = (mesh.db.Order)request.getAttribute("order");
        mesh.db.Client client = (mesh.db.Client)request.getAttribute("client"); %>
    <div id='orderInfo' class='panel-body' oid="<%=order.getId()%>">
        <% Set<mesh.db.ApprovedLoan> approved = order.getApproved();
            Double solvency = client.getSolvency();
            for(mesh.db.ApprovedLoan apl : approved) {
                mesh.db.Loan cl = apl.getLoan();
                String hasColor = "";
                if(cl.getMaxSolvency() > solvency && cl.getMinSolvency() < solvency)
                    hasColor = "has-success";
                else if(cl.getMinSolvency() > solvency)
                    hasColor = "has-error";
                else if(cl.getMaxSolvency() < solvency)
                    hasColor = "has-warning";
                out.print("<div class='input-group " + hasColor + "' lid='" + (cl.getMaxSolvency() < solvency ? "0" : cl.getId())
                        + "' style='cursor:" + (cl.getMaxSolvency() < solvency ? "not-allowed" : "pointer") + ";'>");
                out.print("<span class='input-group-addon'>" + cl.getName() + "</span>");
                out.print("<div class='form-control input-sm'>" + apl.getAmount().intValue() + " " + rb.getString("currency") + "</div>");
                out.print("<div class='form-control input-sm'>" + apl.getDate() + " " + rb.getString("days") + "</div>");
                out.print("<div class='form-control input-sm'>" + Math.ceil(apl.getPercent() * 10.0) / 10.0 + " %</div>");
                out.print("<div class='form-control input-sm'>" + apl.getMonthPayment().intValue() + " " + rb.getString("currency") + "/" + rb.getString("days") + "</div>");
                out.print("</div><br>");
            } %>
    </div>
    <script type="text/javascript">
        $('#orderInfo .input-group').click(function() {
            var lid = $(this).attr('lid');
            if(parseInt(lid) === 0)
                return;
            var oid = $('#orderInfo').attr('oid');
            var newData = {"oid": oid, "lid": lid};
            $.ajax({
                type: "PUT",
                async: true,
                url: "/api/order",
                data: JSON.stringify(newData),
                xhrFields: {withCredentials: true}
            }).error(function (msg) {
                console.log(msg);
            }).success(function (data) {
                window.location.href = '/client?id=<%=order.getCid()%>&<%=new java.util.Random().nextInt()%>';
            });
        });
    </script>
    <div class='panel-footer'>
        <a href="/client?id=<%=order.getCid()%>&<%=new java.util.Random().nextInt()%>" class='btn btn-primary btn-block'><%=rb.getString("back") %></a>
    </div>
</main>
</body>
</html>