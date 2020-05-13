<%@ page import="mesh.db.Document" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <ul class="nav navbar-nav">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#"><%=rb.getString("new")%>
                    <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="" data-toggle="modal" data-target="#newClient"><%=rb.getString("client")%></a></li>
                    <% if(me.getRole().getAccess().equals("admin")) { %>
                    <li><a href="" id="modalUser" data-toggle="modal" data-target="#newUser"><%=rb.getString("user")%></a></li>
                    <li><a href="" id="modalLoan" data-toggle="modal" data-target="#newLoan"><%=rb.getString("loan")%></a></li>
                    <% } %>
                </ul>
            </li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span><span class='hidden-xs'>&nbsp;<%=rb.getString("logout")%></span></a></li>
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
    <% if(me.getRole().getAccess().equals("admin")) { %>
    <div id="newUser" class="modal fade" role="dialog">
        <div class="panel panel-primary modal-dialog">
            <div class="panel-heading" style="padding: 5px; text-align: center;">
                <b><%=rb.getString("user")%></b>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <form class="modal-body" id="userForm">
                <div class='input-group'>
                    <span class='input-group-addon'><%=rb.getString("fio")%></span>
                    <input type='text' name='fio' class='form-control input-sm' required value=""/>
                </div>
                <br>
                <div class='input-group'>
                    <span class='input-group-addon'><%=rb.getString("login")%></span>
                    <input type='text' name='login' class='form-control input-sm' required value=""/>
                </div>
                <br>
                <div class='input-group'>
                    <span class='input-group-addon'><%=rb.getString("password")%></span>
                    <input type='password' name='password' class='form-control input-sm' required value=""/>
                </div>
                <br>
                <div class='input-group'>
                    <span class='input-group-addon'><%=rb.getString("access")%></span>
                    <select name='access' class='form-control input-sm' required>
                        <option selected disabled></option>

                    </select>
                </div>
            </form>
            <div class="modal-footer" style="padding:5px;">
                <div class='btn btn-success' onclick="saveUser();"><%=rb.getString("save")%></div>
                <div class='btn btn-default' data-dismiss="modal"><%=rb.getString("cancel")%></div>
            </div>
        </div>
        <script type="text/javascript">
            $('#modalUser').on('click', function() {
                $('#newUser select[name="access"] option').remove();
                $('#newUser').find('select[name="access"]').append('<option selected disabled></option>');
                $.ajax({
                    type: "GET",
                    async: false,
                    url: "/api/role",
                    data: null,
                    xhrFields: { withCredentials: true }
                }).error(function(msg) {
                    console.log(msg);
                }).success(function(data) {
                    data.data.forEach(r => {
                        var option = '<option value="' + r.id + '">' + r.name + '</option>';
                        $('#newUser').find('select[name="access"] option:last').after(option);
                    });
                });
            });

            function saveUser() {
                var fields = $('#userForm').find('.form-control');
                var newUser = {};
                fields.each(function(index) {
                    newUser[$(this).attr('name')] = $(this).val();
                });
                $.ajax({
                    type: "POST",
                    async: true,
                    url: "/api/user",
                    data: JSON.stringify(newUser, (k, v) => {
                        if(v === "") return null;
                        else if(isNaN(parseInt(v))) return v;
                        else return parseInt(v);
                    }),
                    xhrFields: { withCredentials: true }
                }).error(function(msg) {
                    console.log(msg);
                    //TODO: highlight on error fields ?
                }).success(function(data) {
                    var tr = '<tr style="cursor:pointer;" uid="'+data.data.id+'"><td>'+data.data.login+'</td><td>'+data.data.fio+'</td><td>'+data.data.role.name+'</td></tr>';
                    $('#tblUsers tbody').append(tr);
                    $('#tblUsers tbody tr[uid!=""]').on('click', function(x) {
                        window.location.href = '/user?id='+$(this).attr('uid');
                    });
                    $('#userForm').trigger("reset");
                    $('#newUser').modal('toggle');
                });
            };
        </script>
    </div>
    <div id="newLoan" class="modal fade" role="dialog">
        <div class="panel panel-primary modal-dialog">
            <div class="panel-heading" style="padding: 5px; text-align: center;">
                <b><%=rb.getString("loan")%></b>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <form class="modal-body" id="loanForm">
                <div class='input-group'>
                    <span class='input-group-addon'><%=rb.getString("name")%></span>
                    <input type='text' name='name' class='form-control input-sm' required value=""/>
                </div>
                <br>
                <div class='input-group'>
                    <span class='input-group-addon'><%=rb.getString("min_amount")%></span>
                    <input type='number' name='min_amount' class='form-control input-sm' min="0.01" step="0.01" required value="0.01"/>
                </div>
                <br>
                <div class='input-group'>
                    <span class='input-group-addon'><%=rb.getString("max_amount")%></span>
                    <input type='number' name='max_amount' class='form-control input-sm' min="0.01" step="0.01" required value="0.01"/>
                </div>
                <br>
                <div class='input-group'>
                    <span class='input-group-addon'><%=rb.getString("min_term")%></span>
                    <input type='number' name='min_term' class='form-control input-sm' min="1" step="1" required value="1"/>
                </div>
                <br>
                <div class='input-group'>
                    <span class='input-group-addon'><%=rb.getString("max_term")%></span>
                    <input type='number' name='max_term' class='form-control input-sm' min="1" step="1" required value="1"/>
                </div>
                <br>
                <div class='input-group'>
                    <span class='input-group-addon'><%=rb.getString("min_percent")%></span>
                    <input type='number' name='min_percent' class='form-control input-sm' min="0.00" max="0.99" step="0.01" required value="0.00"/>
                </div>
                <br>
                <div class='input-group'>
                    <span class='input-group-addon'><%=rb.getString("max_percent")%></span>
                    <input type='number' name='max_percent' class='form-control input-sm' min="0.01" max="1.00" step="0.01" required value="0.01"/>
                </div>
                <br>
                <div class='input-group'>
                    <span class='input-group-addon'><%=rb.getString("min_solvency")%></span>
                    <input type='number' name='min_solvency' class='form-control input-sm' min="0.00" max="0.99" step="0.01" required value="0.00"/>
                </div>
                <br>
                <div class='input-group'>
                    <span class='input-group-addon'><%=rb.getString("max_solvency")%></span>
                    <input type='number' name='max_solvency' class='form-control input-sm' min="0.01" max="1.00" step="0.01" required value="0.01"/>
                </div>
            </form>
            <div class="modal-footer" style="padding:5px;">
                <div class='btn btn-success' onclick="saveLoan();"><%=rb.getString("save")%></div>
                <div class='btn btn-default' data-dismiss="modal"><%=rb.getString("cancel")%></div>
            </div>
        </div>
        <script type="text/javascript">
            function saveLoan() {
                var fields = $('#loanForm').find('.form-control');
                var newLoan = {};
                fields.each(function(index) {
                    newLoan[$(this).attr('name')] = $(this).val();
                });
                $.ajax({
                    type: "POST",
                    async: true,
                    url: "/api/loan",
                    data: JSON.stringify(newLoan, (k, v) => {
                        if(v === "") return null;
                        return v;
                    }),
                    xhrFields: { withCredentials: true }
                }).error(function(msg) {
                    console.log(msg);
                    //TODO: highlight on error fields ?
                }).success(function(data) {
                    var tr = '<tr style="cursor:pointer;" lid="'+data.data.id+'"><td>'+data.data.name+'</td><td>'+data.data.min_amount+' - '+data.data.max_amount+'</td><td>'+data.data.min_term+' - '+data.data.max_term+'</td><td>'+data.data.min_percent+' - '+data.data.max_percent+'</td></tr>';
                    $('#tblLoans tbody').append(tr);
                    $('#tblLoans tbody tr[lid!=""]').on('click', function(x) {
                        window.location.href = '/loan?id='+$(this).attr('lid');
                    });
                    $('#loanForm').trigger("reset");
                    $('#newLoan').modal('toggle');
                });
            };
        </script>
    </div>
    <% } %>
</nav>