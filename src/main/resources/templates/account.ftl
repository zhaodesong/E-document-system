<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>账户管理 - 流云文档</title>
    <link rel="stylesheet" type="text/css" href="/css/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/css/icon.min.css">
    <link rel="stylesheet" type="text/css" href="/components/reset.css">
    <link rel="stylesheet" type="text/css" href="/components/site.css">

    <link rel="stylesheet" type="text/css" href="/components/container.css">
    <link rel="stylesheet" type="text/css" href="/components/grid.css">
    <link rel="stylesheet" type="text/css" href="/components/header.css">
    <link rel="stylesheet" type="text/css" href="/components/image.css">
    <link rel="stylesheet" type="text/css" href="/components/menu.css">

    <link rel="stylesheet" type="text/css" href="/components/divider.css">
    <link rel="stylesheet" type="text/css" href="/components/list.css">
    <link rel="stylesheet" type="text/css" href="/components/segment.css">
    <link rel="stylesheet" type="text/css" href="/components/dropdown.css">
    <link rel="stylesheet" type="text/css" href="/components/icon.css">
    <script src="/js/jquery.min.js"></script>
    <script src="/js/semantic.min.js"></script>
    <style>
        .ui.grid > .column:only-child, .ui.grid > .row > .column:only-child {
            width: 35%;
            margin-top: 10rem;
        }

        .ui.card {
            border: 1px solid #888888;
            box-shadow: 4px 4px 5px #888888;
            width: 300px;
            height: 350px;
            margin-top: 70px;
            margin-left: 100px;
            background-color: #f3f3f3
        }
    </style>
</head>
<body>
<#include "common/header.ftl" parse=true encoding='utf-8'>
<div class="" style="background-color: white;height: 101.75vh;">
    <div class="ui middle aligned center aligned grid">
        <div class="column">
            <div class="ui card">
                <span style="margin-top: 70px;margin-bottom: 10px">昵称：</span>
                <div class="center aligned">
                    <input class="ui input" id="input" type="text" value="${Session.nickName!}" style="width: 150px;">
                </div>

                <div class="" style="margin-top: 100px;">
                    <button id="showModal" class="ui button">
                        修改密码
                    </button>
                </div>

                <div class="ui small modal" style="width: 400px;">
                    <div class="ui icon header">
                        修改密码
                    </div>
                    <div class="content">
                        <div class="ui segment">
                            <div class="ui input">
                                <input type="password" placeholder="输入原密码" id="oldPwd">
                            </div>
                            </br>
                            <div class="ui input">
                                <input type="password" placeholder="输入新密码" id="newPwd">
                            </div>
                            </br>
                            <div class="ui input">
                                <input type="password" placeholder="再次输入新密码" id="newPwd2">
                            </div>
                        </div>
                    </div>
                    <div class="actions">
                        <div class="ui red cancel inverted button">
                            取消
                        </div>
                        <div class="ui green ok inverted button" id="confirm">
                            确定
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<#--<div class="ui bottom fixed inverted vertical footer segment" style="background-color: #f1f1f1">-->
<#--    <div class="ui center aligned container">-->
<#--        <h4 class="ui inverted header" style="color: black">流云文档</h4>-->
<#--        <p style="color: black">专注于团队协作</p>-->
<#--    </div>-->
<#--</div>-->

<script>
    var temp = $('#input').val();
    $('#input').on('blur', function () {
        if ($(this).val() === temp) {
            return;
        }
        $.ajax({
            url: "/changeNickName",
            type: "POST",
            dataType: "json",
            data: {nickName: $(this).val()},
            success: function (data) {
                if (data.result === 1) {
                    alert("昵称修改成功！");
                } else if (data.result === -1) {
                    alert(data.msg);
                    window.open("/", "_self");
                } else {
                    alert(data.msg);
                }
            },
            error: function (err) {
                alert("系统错误，请稍后重试");
                console.log(err);
            }
        });
        temp = $(this).val();
    });

    $('#showModal').on('click', function () {
        $('.small.modal').modal('show');
    });
    $('#confirm').on('click', function () {
        var oldPwd = $('#oldPwd').val();
        var newPwd = $('#newPwd').val();
        var newPwd2 = $('#newPwd2').val();
        if (newPwd !== newPwd2) {
            alert("新密码两次输入不一致");
            return;
        }
        $.ajax({
            url: '/changePwd',
            type: 'post',
            data: {
                oldPwd: oldPwd,
                newPwd: newPwd
            },
            dataType: 'json',
            success: function (data) {
                if (data.result === 1) {
                    alert("密码修改成功,请重新登录");
                    window.open("/", "_self");
                } else if (data.result === -1) {
                    alert(data.msg);
                    window.open("/", "_self");
                }else {
                    alert(data.msg);
                }
            },
            error: function (err) {
                alert("系统错误，请稍后重试");
                console.log(err);
            }
        });
    });
</script>
</body>
</html>