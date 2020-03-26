<%@ page import="mesh.db.Document" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <ul class="nav navbar-nav">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#"><%=rb.getString("new")%>
                    <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="" data-toggle="modal" data-target="#newClient"><%=rb.getString("client")%></a></li>
                    <!--li><a href="#">Page 1-2</a></li>
                    <li><a href="#">Page 1-3</a></li-->
                </ul>
            </li>

        </ul>
        <div class="navbar-form navbar-left">
            <div class="form-group">
                <input type="text" class="form-control" name="search" placeholder="<%=rb.getString("find")%>">
            </div>
        </div>
    </div>

    <div id="newClient" class="modal fade" role="dialog">
        <div class="panel panel-primary modal-dialog">
            <div class="panel-heading" style="padding: 5px; text-align: center;">
                <b><%=rb.getString("primary_info")%></b>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <form class="modal-body" id="clientForm">
                        <div class='input-group'>
                            <span class='input-group-addon'><%=rb.getString("first_name")%></span>
                            <input type='text' name='firstName' class='form-control input-sm' required value=""/>
                        </div>
                        <br>
                        <div class='input-group'>
                            <span class='input-group-addon'><%=rb.getString("last_name")%></span>
                            <input type='text' name='lastName' class='form-control input-sm' required value=""/>
                        </div>
                        <br>
                        <div class='input-group'>
                            <span class='input-group-addon'><%=rb.getString("patronymic")%></span>
                            <input type='text' name='patronymic' class='form-control input-sm' required value=""/>
                        </div>
                        <br>
                        <div class='input-group'>
                            <span class='input-group-addon'><%=rb.getString("birth")%></span>
                            <input type='text' name='birth' class='form-control input-sm' required value="" placeholder="01.01.2000"/>
                        </div>
                        <br>
                        <div class='input-group'>
                            <span class='input-group-addon'><%=rb.getString("sex")%></span>
                            <select name='sex' class='form-control input-sm' required>
                                <option disable selected></option>
                                <option value="1"><%=rb.getString("male")%></option>
                                <option value="0"><%=rb.getString("female")%></option>
                            </select>
                        </div>
                        <br>
                        <div class='input-group'>
                            <span class='input-group-addon'><%=rb.getString("address")%></span>
                            <textarea name='address' class='form-control input-sm' required rows="4"></textarea>
                        </div>
                        <br>
                        <table class="table table-bordered table-condensed">
                            <tr><th><%=rb.getString("document_type")%></th><th><%=rb.getString("serial")%></th><th><%=rb.getString("number")%></th><th><%=rb.getString("document_issued")%></th></tr>
                            <% for(Document.Type type : Document.Type.values())
                                out.write("<tr><td>" + rb.getString("document_type_" + type.val()) + "</td>" +
                                        "<td style='padding:0px'><input class='form-control input-sm' type='text' name='doc_s_" + type.val() + "' value=''/></td>" +
                                        "<td style='padding:0px'><input class='form-control input-sm' type='text' name='doc_n_" + type.val() + "' value=''/></td>" +
                                        "<td style='padding:0px'><input class='form-control input-sm' type='text' name='doc_i_" + type.val() + "' value='' placeholder='01.01.2000'/></td></tr>");
                            %>
                        </table>
            </form>
            <div class="modal-footer" style="padding:5px;">
                <div class='btn btn-success' onclick="saveClient();"><%=rb.getString("save")%></div>
                <div class='btn btn-default' data-dismiss="modal"><%=rb.getString("cancel")%></div>
            </div>
        </div>
        <script type="text/javascript">
            function saveClient() {
                var fields = $('#clientForm').find('.form-control');
                var newClient = {};
                fields.each(function(index) {
                    newClient[$(this).attr('name')] = $(this).val();
                });
                $.ajax({
                    type: "POST",
                    async: true,
                    url: "/api/client",
                    data: JSON.stringify(newClient, (k, v) => {
                        if(v === "") return null;
                        else return v;
                    }),
                    xhrFields: { withCredentials: true }
                }).error(function(msg) {
                    console.log(msg);
                    //TODO: highlight on error fields ?
                }).success(function(data) {
                    $('#newClient').modal('toggle');
                    $('#clientForm').trigger("reset");
                    var clazz = (data.data.solvency===null?'default':(data.data.solvency>0.66?'success':(data.data.solvency<0.33?'danger':'warning')));
                    var tr = '<tr style="cursor:pointer;" cid="'+data.data.id+'" class="'+clazz+'"><td>'+data.data.firstName+' '+data.data.lastName+(data.data.patronymic!==null?(' '+data.data.patronymic):'')+'</td><td>' +
                        data.data.birth + '</td><td>'+data.data.documents[0].serial+'</td><td>'+data.data.documents[0].number+'</td><td>' + data.data.address + '</td></tr>';
                    $('#tblData tbody').append(tr);

                    $('#tblData tbody tr[cid!=""]').on('click', function(x) {
                        window.location.href = '/client?id='+$(this).attr('cid');
                    });
                });
            };
        </script>
    </div>
</nav>