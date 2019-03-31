<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<div id="loginForm">
    <form action="/createProject?id={}" method="post">
        <p style="color:red;" >${msg!}</p>
        <input class="form-control inputText" type="text" name="mail" placeholder="项目名称"/><br/>
        <input class="button" type="submit" value="注册"/>
        <input class="button" type="reset" value="重置"/>
        <a href=”#” onClick=”javascript :history.back(-1);”>返回</a>
    </form>
</div>
</body>
</html>