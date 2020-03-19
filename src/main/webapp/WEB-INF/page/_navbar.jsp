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
            <!--div class="btn btn-default btn-small"><i class="glyphicon glyphicon-search"></i></div-->
        </div>
    </div>

    <div id="newClient" class="modal fade" role="dialog">
        <div class="panel panel-primary modal-dialog">
            <div class="panel-heading" style="padding: 5px; text-align: center;">
                <b><%=rb.getString("new_client")%></b>
                <!--button type="button" class="close" data-dismiss="modal">&times;</button-->
            </div>

            <div class="modal-body">
                <form id="clientForm">
                    <p>Some text in the modal.</p>
                </form>
            </div>
            <div class="modal-footer" style="padding:5px;">
                <div class='btn btn-success' onclick="saveClient();"><%=rb.getString("save")%></div>
                <div class='btn btn-default' data-dismiss="modal"><%=rb.getString("cancel")%></div>
            </div>
        </div>
        <script type="text/javascript">
            function saveClient() {
                var fields = $('#clientForm').find('.form-control');
            };
        </script>
    </div>


</nav>





                <!--script type="text/javascript">
                    function saveIssue() {
                        var fields = $('#taskForm').find('.form-control');
                        var newIssue = {};
                        fields.each(function(index) {
                            newIssue[$(this).attr('name')] = $(this).val();
                        });
                        $.ajax({
                            type: "POST",
                            async: true,
                            url: "/api/task",
                            data: JSON.stringify(newIssue, (k, v) => {
                                if(v === "") return null;
                                else if(isNaN(parseInt(v))) return v;
                                else return parseInt(v);
                            }),
                            xhrFields: { withCredentials: true }
                        }).error(function(msg) {
                            console.log(msg);
                            //TODO: highlight on error fields ?
                        }).success(function(data) {
                            $('#newTask').modal('toggle');
                            $('#taskForm').trigger("reset");
                            var tr = '<tr><td><a href="/task?id=' + data.id + '">' + data.id + '</a></td>' +
                                '<td><a href="/project?id=' + data.project.id + '">' + data.project.name + '</a></td>' +
                                '<td><a href="/task?id=' + data.id + '">' + data.name + '</a></td>' +
                                '<td>' + data.status.name + '</td>' +
                                '<td>' + data.priority.name + '</td></tr>';
                            if(data.assigned.id === <=me.getId()%> && data.status.name !== 'Закрыта')
                                $('#assignedIssues tr:last').after(tr);
                        });
                    }

                    $('select[name="pid"]').on('change', function() {
                        var currentValue = $(this).val();
                        $.ajax({
                            type: "GET",
                            async: true,
                            url: "/api/user",
                            data: { pid: currentValue },
                            xhrFields: { withCredentials: true }
                        }).error(function(msg) {
                            console.log(msg);
                        }).success(function(data) {
                            data.user.forEach(p => {
                                var option = '<option value="' + p.id + '">' + p.firstName + ' ' + p.lastName + '</option>';
                                $('#newTask').find('select[name="assigned"] option:last').after(option);
                            });
                        });
                    })
                </script>

        <script type="text/javascript">
            $('#modalTask').on('click', function() {
                $.ajax({
                    type: "GET",
                    async: true,
                    url: "/api/project",
                    data: null,
                    xhrFields: { withCredentials: true }
                }).error(function(msg) {
                    console.log(msg);
                }).success(function(data) {
                    data.project.forEach(p => {
                        var option = '<option value="' + p.id + '">' + p.name + '</option>';
                        $('#newTask').find('select[name="pid"] option:last').after(option);
                    });
                });
            });
        </script-->