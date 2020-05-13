<html>
<head>
    <meta name='viewport' content='width=device-width, initial-scale=1.0'>
    <meta charset='utf-8' />
    <link rel='stylesheet' href='/css/bootstrap.css'/>
    <script src='/js/jquery.min.js'></script>
    <script src='/js/bootstrap.min.js'></script>
    <title><%=mesh.util.rb.getString("app_name") %></title>
</head><% mesh.db.User me = (mesh.db.User)request.getSession().getAttribute("me"); %>