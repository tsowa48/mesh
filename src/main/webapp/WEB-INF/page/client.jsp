<%@ page import="static mesh.util.rb" %>
<%@ page import="mesh.db.Document" %>
<%@ page import="java.util.Set" %>
<%@ page import="mesh.db.Order" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/page/_header.jsp"%>
<body class="container">
    <main class="panel panel-primary vertical-center modal-dialog">
        <div class='panel-heading' style='text-align:center;'><b><%=rb.getString("client") %></b></div>

        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#info"><%=rb.getString("primary_info") %></a></li>
            <li><a data-toggle="tab" href="#orders"><%=rb.getString("orders") %></a></li>
        </ul>

        <div class="tab-content">
            <div id="info" class="tab-pane fade in active">
                <% mesh.db.Client client = (mesh.db.Client)request.getAttribute("client"); %>
                <div id='clientPrimaryInfo' class='list-group panel-body'>
                    <div class='input-group'>
                        <span class='input-group-addon'><%=rb.getString("first_name") %></span>
                        <input type='text' name='firstName' class='form-control input-sm' required autofocus value="<%=client.getFirstName()%>"/>
                    </div>
                    <br>
                    <div class='input-group'>
                        <span class='input-group-addon'><%=rb.getString("last_name") %></span>
                        <input type='text' name='lastName' class='form-control input-sm' required autofocus value="<%=client.getLastName()%>"/>
                    </div>
                    <br>
                    <div class='input-group'>
                        <span class='input-group-addon'><%=rb.getString("patronymic") %></span>
                        <input type='text' name='patronymic' class='form-control input-sm' required autofocus value="<%=client.getPatronymic()%>"/>
                    </div>
                    <br>
                    <div class='input-group'>
                        <span class='input-group-addon'><%=rb.getString("birth")%></span>
                        <input type='text' name='birth' class='form-control input-sm' required value="<%=client.getBirth()%>"/>
                    </div>
                    <br>
                    <div class='input-group'>
                        <span class='input-group-addon'><%=rb.getString("sex")%></span>
                        <select name='sex' class='form-control input-sm' required>
                            <option <%=client.getSex()?"selected":""%> value="1"><%=rb.getString("male")%></option>
                            <option <%=client.getSex()?"":"selected"%> value="0"><%=rb.getString("female")%></option>
                        </select>
                    </div>
                    <br>
                    <div class='input-group'>
                        <span class='input-group-addon'><%=rb.getString("address")%></span>
                        <textarea name='address' class='form-control input-sm' required rows="4"><%=client.getAddress()%></textarea>
                    </div>
                    <br>
                    <table class="table table-bordered table-condensed">
                        <tr><th><%=rb.getString("document_type")%></th><th><%=rb.getString("serial")%></th><th><%=rb.getString("number")%></th><th><%=rb.getString("document_issued")%></th></tr>
                        <% Set<Document> docs = client.getDocuments();
                            for(Document doc : docs)
                            out.write("<tr><td>" + rb.getString("document_type_" + doc.getType()) + "</td>" +
                                    "<td style='padding:0px'><input class='form-control input-sm' type='text' name='doc_s_" + doc.getType() + "' value='" + doc.getSerial() + "'/></td>" +
                                    "<td style='padding:0px'><input class='form-control input-sm' type='text' name='doc_n_" + doc.getType() + "' value='" + doc.getNumber() + "'/></td>" +
                                    "<td style='padding:0px'><input class='form-control input-sm' type='text' name='doc_i_" + doc.getType() + "' value='" + doc.getIssued() + "'/></td></tr>");
                        %>
                    </table>
                </div>
                <script type="text/javascript">
                    //TODO: onchange in id= #clientPrimaryInfo
                </script>
            </div>
            <div id="orders" class="tab-pane fade">
                <% Set<Order> orders = client.getOrders(); %>
                <div class="panel-body">
                <table id="tblOrders" class="table table-bordered table-condensed table-hover">
                    <thead><tr><th><%=rb.getString("date")%></th><th><%=rb.getString("wish_summ")%></th><th><%=rb.getString("wish_date")%></th></tr></thead>
                    <tbody><% for(Order order : orders) {
                        out.write("<tr style='cursor:pointer;' oid='" + order.getId() + "'>");
                        out.write("<td>" + order.getDate() + "</td>");
                        out.write("<td>" + order.getDesired_amount() + "</td>");
                        out.write("<td>" + order.getDesired_term() + "</td>");
                        out.write("</tr>");
                    }
                    %>
                    <tr oid='' id="newOrder">
                        <td style='padding:0px'><input type="text" class="form-control input-sm" name="order_date" value="<%=new SimpleDateFormat("dd.MM.yyyy").format(new Date())%>" disabled/></td>
                        <td style='padding:0px'><input type="number" class="form-control input-sm" name="wish_summ" min="0.00" step="100.00" value="0.00"/></td>
                        <td style='padding:0px'><input type="text" class="form-control input-sm" name="wish_date" value="" placeholder="<%=new SimpleDateFormat("dd.MM.yyyy").format(new Date())%>"/></td>
                    </tr>
                    </tbody>
                </table>
                </div>

                <script type="text/javascript">
                    $('#tblOrders tbody tr[oid!=""]').on('click', function(x) {
                        window.location.href = '/order?id='+$(this).attr('oid');
                    });
                </script>
            </div>
        </div>
        <div class='panel-footer'>
            <a href="/" class='btn btn-primary btn-block'><%=rb.getString("back") %></a>
        </div>
    </main>
</body>
</html>
