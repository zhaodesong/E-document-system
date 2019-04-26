<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>成员管理</title>
    <link rel="stylesheet" type="text/css" href="/css/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/css/common.css">
    <script src="/js/jquery.min.js"></script>
    <script src="/js/semantic.min.js"></script>

</head>
<body>
<#include "common/header.ftl" parse=true encoding='utf-8'>
<div class="ui container">
    <p>项目创建哲账户及当前登陆账户的信息不会显示</p>
    <p>权限说明：</p>
    <p>仅下载：仅可下载文件，不能进行上传、更新、删除等编辑操作</p>
    <p>可编辑：可下载文件，也可进行编辑操作</p>
    <p>管理员：可进行下载编辑等操作，也可以进行项目成员的管理</p>
    <p>如需要重命名、删除项目，请联系项目创建者进行操作</p>
    </br>
    </br>
</div>
<div class="ui container">
    <div class="memberList">
        <#list accountList! as a>
            <div>
                <p>昵称：${a.nickName!} &nbsp;&nbsp;&nbsp;邮箱：${a.mail}</p>
                <a href="deleteMember?deleteId=${a.id}">删除该成员</a>
                <select class="dropdown" id="${a.id}">
                    <#if a.power="00">
                        <option value="00" selected>仅下载</option>
                        <option value="10">可编辑</option>
                        <option value="11">管理员</option>
                    <#elseif a.power="10">
                        <option value="00">仅下载</option>
                        <option value="10" selected>可编辑</option>
                        <option value="11">管理员</option>
                    <#else >
                        <option value="00">仅下载</option>
                        <option value="10">可编辑</option>
                        <option value="11" selected>管理员</option>
                    </#if>
                </select>
            </div>
        </#list>
    </div>

    <div class="ui left" style="margin-top: 20px;text-align: left;">
        <button id="showModal" class="ui button">
            邀请新成员
        </button>
    </div>

    <div class="ui small modal">
        <div class="ui icon header">
            请输入被邀请人的邮箱
        </div>
        <div class="content">
            <div class="ui inverted segment">
                <div class="ui inverted big input" style="width: 100%;">
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
                    alert(data.msg);
                } else {
                    alert(data.msg);
                }
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
                    var newMemberDivStr = "<div>" +
                        "<p>昵称：" + data.member.nickName + "  &nbsp;&nbsp;&nbsp;邮箱：" + data.member.mail + "</p>" +
                        "<a href='/deleteMember?deleteId=" + data.member.id + "' >删除该成员</a>" +
                        "<select class='dropdown' id='" + data.member.nickName + "'>";
                    if (data.member.power === "00") {
                        newMemberDivStr = newMemberDivStr +
                            "<option value='00' selected>仅下载</option>" +
                            "<option value='10'>可编辑</option>" +
                            "<option value='11'>管理员</option>";
                    } else if (data.member.power === "10") {
                        newMemberDivStr = newMemberDivStr +
                            "<option value='00' selected>仅下载</option>" +
                            "<option value='10'>可编辑</option>" +
                            "<option value='11'>管理员</option>";
                    } else {
                        newMemberDivStr = newMemberDivStr +
                            "<option value='00' selected>仅下载</option>" +
                            "<option value='10'>可编辑</option>" +
                            "<option value='11'>管理员</option>";
                    }
                    newMemberDivStr = newMemberDivStr + "</select></div>";
                    var newMemberDiv = $(newMemberDivStr);
                    $('.memberList').append(newMemberDiv);
                } else {
                    alert(data.msg);
                }
            }
        });
    });
</script>
</body>
</html>