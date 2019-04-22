<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>流云文档</title>
    <link rel="stylesheet" type="text/css" href="/css/semantic.min.css">
    <script src="/js/jquery.min.js"></script>
    <script src="/js/semantic.min.js"></script>
    <style>
        .ui.grid>.column:only-child, .ui.grid>.row>.column:only-child {
            width: 35%;
            margin-top: 10rem;
        }
    </style>
</head>
<body>
<div class="ui middle aligned center aligned grid" id="loginForm">
    <div class="column">
        <h2 class="ui teal image header">
            <div class="content">
                登录
            </div>
        </h2>
        <form class="ui large form" action="/login" method="post">
            <div class="ui stacked segment">
                <div class="field">
                    <div class="ui left icon input">
                        <i class="user icon"></i>
                        <input type="text" name="mail" placeholder="账号"/>
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="lock icon"></i>
                        <input type="password" name="password" placeholder="密码"/>
                    </div>
                </div>
                <p style="color:red;">${msg!}</p>
                <input class="ui fluid large teal submit button" type="submit" value="登录"/>
            </div>

            <div class="ui error message"></div>

        </form>

        <div class="ui message" style="height: 50px;">
            <div style="float:left;">
                <a id="wjPwd" href="javascript:alert('请与管理员联系重置密码!');">忘记密码?</a>
            </div>
            <div style="float: right;">
                新用户？ <a href="/toRegister"">注册</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>