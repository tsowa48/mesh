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
                <div id='clientPrimaryInfo' class='list-group panel-body' data-id="<%=client.getId()%>">
                    <div class='input-group'>
                        <span class='input-group-addon'><%=rb.getString("first_name") %></span>
                        <div class='form-control input-sm disabled'><%=client.getFirstName()%></div>
                    </div>
                    <br>
                    <div class='input-group'>
                        <span class='input-group-addon'><%=rb.getString("last_name") %></span>
                        <div class='form-control input-sm disabled'><%=client.getLastName()%></div>
                    </div>
                    <br>
                    <div class='input-group'>
                        <span class='input-group-addon'><%=rb.getString("patronymic") %></span>
                        <div class='form-control input-sm disabled'><%=client.getPatronymic()%></div>
                    </div>
                    <br>
                    <div class='input-group'>
                        <span class='input-group-addon'><%=rb.getString("birth")%></span>
                        <div class='form-control input-sm disabled'><%=client.getBirth()%></div>
                    </div>
                    <br>
                    <div class='input-group'>
                        <span class='input-group-addon'><%=rb.getString("sex")%></span>
                        <div class='form-control input-sm disabled'><%=client.getSex()?rb.getString("male"):rb.getString("female")%></div>
                    </div>
                    <br>
                    <div class='input-group'>
                        <span class='input-group-addon'><%=rb.getString("address")%></span>
                        <textarea name='address' class='form-control input-sm' required rows="4"><%=client.getAddress()%></textarea>
                    </div>
                    <br>
                    <div class='input-group'>
                        <span class='input-group-addon'><%=rb.getString("salary")%></span>
                        <input type='number' name='salary' class='form-control input-sm' min="1.0" required value="<%=client.getSalary()%>"/>
                    </div>
                    <br>
                    <table class="table table-bordered table-condensed">
                        <tr><th><%=rb.getString("document_type")%></th><th><%=rb.getString("serial")%></th><th><%=rb.getString("number")%></th><th><%=rb.getString("document_issued")%></th></tr>
                        <% Set<Document> docs = client.getDocuments();
                            for(Document doc : docs)
                            out.write("<tr><td>" + rb.getString("document_type_" + doc.getType()) + "</td>" +
                                    "<td>" + doc.getSerial() + "</td>" +//"<td style='padding:0px'><input disabled class='form-control input-sm' type='text' name='doc_s_" + doc.getType() + "' value='" + doc.getSerial() + "'/></td>" +
                                    "<td>" + doc.getNumber() + "</td>" +//"<td style='padding:0px'><input disabled class='form-control input-sm' type='text' name='doc_n_" + doc.getType() + "' value='" + doc.getNumber() + "'/></td>" +
                                    "<td>" + doc.getIssued() + "</td></tr>");//"<td style='padding:0px'><input disabled class='form-control input-sm' type='text' name='doc_i_" + doc.getType() + "' value='" + doc.getIssued() + "'/></td></tr>");
                        %>
                    </table>
                </div>
                <script type="text/javascript">
                    $('#clientPrimaryInfo .form-control').change(function() {
                        var control = $(this);
                        control.parent().removeClass('has-error');
                        var id = $('#clientPrimaryInfo').data('id');
                        var field = $(this).attr('name');
                        var value = $(this).val();
                        var newData = {"id": id, "field": field, "value": value};
                        $.ajax({
                            type: "PUT",
                            async: true,
                            url: "/api/client",
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
            <div id="orders" class="tab-pane fade">
                <% Set<Order> orders = client.getOrders(); %>
                <div class="panel-body">
                 <table id="tblOrders" class="table table-bordered table-condensed table-hover">
                        <thead><tr><th><%=rb.getString("date")%></th><th><%=rb.getString("wish_summ") + " (" + rb.getString("currency") + ")"%></th><th><%=rb.getString("wish_date") + " (" + rb.getString("days") + ")"%></th></tr></thead>
                        <tbody><% for(Order order : orders) {
                            String date = String.valueOf(order.getDate());
                            out.write("<tr style='cursor:pointer;' " + (order.getLid() != null ? "class='success'" : "") + "' oid='" + (order.getLid() == null || client.getSolvency() == 0.0 ? order.getId() : "") + "'>");
                            out.write("<td>" + date.substring(6, 8) + "." + date.substring(4, 6) + "." + date.substring(0, 4) + "</td>");
                            out.write("<td>" + order.getDesired_amount() + "</td>");
                            out.write("<td>" + order.getDesired_term() + "</td>");
                            out.write("</tr>");
                        }
                        %>
                        </tbody>
                    </table>
                    <a href="" id='modalOrder' data-toggle="modal" data-target="#newOrder" class='btn btn-success btn-block'><%=rb.getString("create") %></a>
                </div>
                <div id="newOrder" class="modal fade" role="dialog">
                    <div class="panel panel-primary modal-dialog">
                        <div class="panel-heading" style="padding: 5px; text-align: center;">
                            <b><%=rb.getString("order")%></b>
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>

                        <form class="modal-body" id="orderForm">
                            <input type="hidden" name="cid" class="form-control" value="<%=request.getParameter("id") %>" />
                            <div class='input-group'>
                                <span class='input-group-addon'><%=rb.getString("date")%></span>
                                <input type='text' name='date' class='form-control input-sm' required value="<%=new SimpleDateFormat("dd.MM.yyyy").format(new Date())%>" disabled/>
                            </div>
                            <br>
                            <div class='input-group'>
                                <span class='input-group-addon'><%=rb.getString("wish_summ") + " (" + rb.getString("currency") + ")"%></span>
                                <input type='number' name='desired_amount' class='form-control input-sm' required min="0.00" step="<%=rb.getString("max_amount_value")%>" value="0.00" autofocus/>
                            </div>
                            <br>
                            <div class='input-group'>
                                <span class='input-group-addon'><%=rb.getString("wish_date") + " (" + rb.getString("days") + ")"%></span>
                                <input type='numeric' name='desired_term' class='form-control input-sm' required min="1" value="" placeholder="1"/>
                            </div>
                        </form>
                        <div class="modal-footer" style="padding:5px;">
                            <div class='btn btn-success' onclick="saveOrder();"><%=rb.getString("save")%></div>
                            <div class='btn btn-default' data-dismiss="modal"><%=rb.getString("cancel")%></div>
                        </div>
                    </div>
                </div>
                <script type="text/javascript">
                    $('#tblOrders tbody tr[oid!=""]').on('click', function(x) {
                        window.location.href = '/order?id='+$(this).attr('oid');
                    });
                    function saveOrder() {
                        var fields = $('#orderForm').find('.form-control');
                        var newOrder = {};
                        fields.each(function(index) {
                            $(this).parent().removeClass('has-error');
                            newOrder[$(this).attr('name')] = $(this).val();
                        });
                        $.ajax({
                            type: "POST",
                            async: true,
                            url: "/api/order",
                            data: JSON.stringify(newOrder),
                            xhrFields: { withCredentials: true }
                        }).error(function(jq,s,m) {
                            fields.each(function(index) {
                                if (jq.responseText.includes($(this).attr('name'))) {
                                    $(this).parent().addClass('has-error');
                                }
                            });
                        }).success(function(data) {
                            var tr = '<tr style="cursor:pointer;" oid="' + data.data.id + '"><td>'
                                + data.data.date.toString().substring(6,8)+'.'+data.data.date.toString().substring(4,6)+'.'+data.data.date.toString().substring(0,4)
                                + '</td><td>' + data.data.desired_amount + '</td><td>' + data.data.desired_term + '</td></tr>';
                            $('#tblOrders tbody').append(tr);
                            $('#tblOrders tbody tr[oid!=""]').on('click', function(x) {
                                window.location.href = '/order?id='+$(this).attr('oid');
                            });
                            $('#orderForm').trigger("reset");
                            $('#newOrder').modal('toggle');
                        });
                    };
                </script>
            </div>
        </div>
        <div class='panel-footer'>
            <a href="/" class='btn btn-primary btn-block'><%=rb.getString("back") %></a>
        </div>
    </main>
</body>
</html>
