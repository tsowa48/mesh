<%@ page import="static mesh.util.rb" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/page/_header.jsp"%>
<body class="container">
<main class="panel panel-primary vertical-center modal-dialog">
    <div class='panel-heading' style='text-align:center;'><b><%=rb.getString("order") %></b></div>
    <% mesh.db.Order order = (mesh.db.Order)request.getAttribute("order"); %>
    <div id='orderInfo' class='panel-body' data-id="<%=order.getId()%>">
        <% Set<mesh.db.ApprovedLoan> approved = order.getApproved();
            for(mesh.db.ApprovedLoan apl : approved) {
                out.print("<div class='input-group'>");
                out.print("<span class='input-group-addon'>" + apl.getLoan().getName() + "</span>");
                out.print("<div class='form-control input-sm'>" + apl.getAmount() + " " + rb.getString("currency") + "</div>");
                out.print("<div class='form-control input-sm'>" + apl.getDate() + " " + rb.getString("days") + "</div>");
                out.print("<div class='form-control input-sm'>" + apl.getPercent() / 100.0 + " %</div>");
                out.print("</div><br>");
            } %>
    </div>
    </div>
    <div class='panel-footer'>
        <a href="/client?id=<%=order.getCid()%>&<%=new java.util.Random().nextInt()%>" class='btn btn-primary btn-block'><%=rb.getString("back") %></a>
    </div>
</main>
</body>
</html>