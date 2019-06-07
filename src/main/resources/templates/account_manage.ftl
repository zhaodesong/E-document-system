<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>成员管理 - 流云文档</title>
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

</head>
<body>
<#include "common/header.ftl" parse=true encoding='utf-8'>
<div class="ui grid" style="margin-top: 10px;margin-bottom: 10px">
    <div class="two wide column" style="position: relative">
        <div class="ui secondary vertical labeled icon menu" style="position: fixed;">
            <div style="margin: 3vw;">
                <a class="item" href="/toLoginSuccess"><i class="desktop icon"></i>返回工作台</a>
            </div>
        </div>
    </div>
    <div class="twelve wide column">
        <div class="ui container">
            <p><b>项目创建哲账户及当前登陆账户的信息不会显示</b></p>
            <p><b>权限说明：</b></p>
            <p> - 仅下载：仅可下载文件，不能进行上传、更新、删除等编辑操作</p>
            <p> - 可编辑：可下载文件，也可进行编辑操作</p>
            <p> - 管理员：可进行下载编辑等操作，也可以进行项目成员的管理</p>
            <p><b>如需要重命名项目、关闭项目，请联系项目创建者进行操作</b></p>
            </br>
            </br>
        </div>
        <div class="ui container">
            <div class="ui items memberList">
                <#list accountList! as a>
                    <div class="item">
                        <div class="content" style="width: 150px">
                            <div class="header">
                                <span>${a.nickName!}</span>
                            </div>
                            <div class="meta">
                                <span>${a.mail}</span>
                            </div>
                        </div>
                        <div class="content">
                            <button class="ui button">
                                <a href="deleteMember?deleteId=${a.id}">移除该成员</a>
                            </button>
                            <select class="ui dropdown" id="${a.id}">
                                <option value="00" <#if a.power="00">selected</#if>>仅下载</option>
                                <option value="10" <#if a.power="10">selected</#if>>可编辑</option>
                                <option value="11" <#if a.power="11">selected</#if>>管理员</option>
                            </select>
                        </div>
                    </div>
                </#list>
            </div>
        </div>
    </div>
    <div class="two wide column" style="position: relative">
        <div class="ui left" style="margin-top: 20px;text-align: left;">
            <button id="showModal" class="ui button">
                邀请新成员
            </button>
        </div>
    </div>


    <div class="ui small modal">
        <div class="ui icon header">
            请输入被邀请人的邮箱
        </div>
        <div class="content">
            <div class="ui segment">
                <div class="ui big input" style="width: 100%;">
                    <input type="text" id="create">
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
    $('.dropdown').on('change', function () {
        //获取被选中的option标签
        var selected = $(this);
        var type = $('.dropdown option:selected').val();
        $.ajax({
            url: "/changePermission",
            type: "POST",
            dataType: "json",
            //把获取到的value值传给服务器
            data: {
                permission: type,
                changeId: selected.closest('.dropdown').attr('id')
            },
            success: function (data) {
                if (data.result === 1) {
                    alert("权限修改成功");
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
    });

    $('#showModal').on('click', function () {
        $('.small.modal').modal('show');
    });
    $('#confirm').on('click', function () {
        $.ajax({
            url: '/inviteMember',
            type: 'post',
            data: {inviteMail: $("#create").val()},
            dataType: 'json',
            success: function (data) {
                if (data.result === 1) {
                    var newMemberDivStr = "<div class='item'>" +
                        "<div class='content' style='width: 150px'>" +
                        "<div class='header'>" +
                        "<span>" + data.member.nickName + "</span>" +
                        "</div>" +
                        "<div class='meta'>" +
                        "<span>" + data.member.mail + "</span>" +
                        "</div>" +
                        "</div>" +
                        "<div class='content'>" +
                        "<button class='ui button'>" +
                        "<a href='deleteMember?deleteId=" + data.member.id + "'>删除该成员</a>" +
                        "</button>" +
                        "<select class='ui dropdown' id='" + data.member.id + "'>";
                    if (data.member.power === "00") {
                        newMemberDivStr = newMemberDivStr + "<option value='00' selected>仅下载</option>";
                    } else if (data.member.power === "10") {
                        newMemberDivStr = newMemberDivStr + "<option value='10' selected>可编辑</option>";
                    } else {
                        newMemberDivStr = newMemberDivStr + "<option value='11' selected>管理员</option>";
                    }
                    newMemberDivStr = newMemberDivStr +
                        "</select>" +
                        "</div>" +
                        "</div>";
                    var newMemberDiv = $(newMemberDivStr);
                    $('.memberList').append(newMemberDiv);
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
    });
</script>
</body>
</html>