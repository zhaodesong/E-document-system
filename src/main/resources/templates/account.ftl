<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>账户管理</title>
    <link rel="stylesheet" type="text/css" href="/css/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/css/common.css">
    <script src="/js/jquery.min.js"></script>
    <script src="/js/semantic.min.js"></script>

</head>
<body>
<#include "common/header.ftl" parse=true encoding='utf-8'>
<div class="ui container">
    <span>昵称：</span>
    <input id="input" type="text" value="${Session.nickName!}">

    <div class="ui left" style="margin-top: 20px;text-align: left;">
        <button id="showModal" class="ui button">
            修改密码
        </button>
    </div>

    <div class="ui small modal">
        <div class="ui icon header">
            修改密码
        </div>
        <div class="content">
            <div class="ui inverted segment">
                <div class="ui inverted big input" style="width: 100%;">
                    <div class="ui input">
                        <input type="password" placeholder="输入原密码" id="oldPwd">
                    </div>
                    <div class="ui input">
                        <input type="password" placeholder="输入新密码" id="newPwd">
                    </div>
                    <div class="ui input">
                        <input type="password" placeholder="再次输入新密码" id="newPwd2">
                    </div>
<#--                    <div><span>输入原密码</span><input type="password" id="oldPwd"></div>-->
<#--                    <div><span>输入新密码</span><input type="password" id="newPwd"></div>-->
<#--                    <div><span>再次输入新密码</span><input type="password" id="newPwd2"></div>-->
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
            data: {nickName: $(this).val() },
            success: function (data) {
                if (data.result == 1) {
                    alert("昵称修改成功！");
                } else {
                    alert(data.msg);
                    console.log(data.msg);
                }
            }
        });
        temp = $(this).val();
    })

    $('#showModal').on('click',function () {
        $('.small.modal').modal('show');
    });
    $('#confirm').on('click',function () {
        var oldPwd = $('#oldPwd').val();
        var newPwd = $('#newPwd').val();
        var newPwd2 = $('#newPwd2').val();
        if (newPwd != newPwd2) {
            alert("新密码两次输入不一致");
            return;
        }
        $.ajax({
            url: '/changePwd',
            type: 'post',
            data: {oldPwd: oldPwd,
                newPwd: newPwd},
            dataType: 'json',
            success: function (data) {
                if (data.result == 1) {
                    alert("密码修改成功,请重新登录");
                    window.open("/","/self");
                } else if (data.result == 2) {
                    alert("原密码不正确");
                }
                else{
                    alert("修改失败");
                }

            }
        });
    });
</script>
</body>
</html>