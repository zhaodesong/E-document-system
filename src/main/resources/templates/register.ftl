<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录-流云文档</title>
    <link rel="stylesheet" type="text/css" href="/css/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/css/icon.min.css">
    <link rel="stylesheet" type="text/css" href="/css/icon.min.css">
    <script src="/js/jquery.min.js"></script>
    <script src="/js/semantic.min.js"></script>
    <style>
        .ui.grid > .column:only-child, .ui.grid > .row > .column:only-child {
            width: 35%;
            margin-top: 10rem;
        }
    </style>
</head>
<body>
<div class="ui middle aligned center aligned grid" id="registerForm">
    <div class="column">
        <h2 class="ui teal image header">
            <div class="content">
                流云文档
            </div>
        </h2>
        <form class="ui large form" action="/register" method="post">
            <div class="ui stacked segment">
                <div class="field">
                    <div class="ui left icon input">
                        <i class="user icon"></i>
                        <input type="text" name="mail" placeholder="邮箱"/>
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="id card icon"></i>
                        <input type="text" name="nickName" placeholder="昵称"/>
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="lock icon"></i>
                        <input type="password" name="password" placeholder="密码"/>
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <i class="lock icon"></i>
                        <input type="password" name="password2" placeholder="确认密码"/>
                    </div>
                </div>
                <p style="color:red;">${msg!}</p>
                <input class="ui fluid large teal submit button" type="submit" value="注册"/>
            </div>
        </form>

        <div class="ui message" style="height: 50px;">
            <div>
                已有帐号？去 <a href="/">登录</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>