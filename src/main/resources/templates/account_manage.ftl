<!DOCTYPE html>
<html lang="en">
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
    <div class="memberList">
        <#list accountList! as a>
            <div>
                <p>昵称：${a.nickName!}  &nbsp;&nbsp;&nbsp;邮箱：${a.mail}</p>
                <a href="deleteMember?deleteId=${a.id}" >删除该成员</a>
                <select class="dropdown" id="${a.id}">
                    <#if a.power="00">
                        <option value="00" selected>仅下载文件</option>
                        <option value="10">可编辑文件</option>
                        <option value="11">可编辑文件与增减成员</option>
                    <#elseif a.power="01">
                        <option value="00">仅下载文件</option>
                        <option value="10" selected>可编辑文件</option>
                        <option value="11">可编辑文件与增减成员</option>
                    <#else >
                        <option value="00">仅下载文件</option>
                        <option value="10">可编辑文件</option>
                        <option value="11" selected>可编辑文件与增减成员</option>
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
    $('.dropdown').on('change',function () {
        //获取被选中的option标签
        var selected = $(this);
        var type = $('.dropdown option:selected').val();
        $.ajax({
            url: "/changePermission",
            type: "POST",
            dataType: "json",
            //把获取到的value值传给服务器
            data: {permission: type,
                changeId: selected.closest('.dropdown').attr('id')},
            success: function(r) {
                if (data.result == 1) {
                    var selId = selected.closest('.dropdown').attr('id');
                    var opts=selId.getElementsByTagName("option");//得到数组option
                    if (type=="00"){
                        opts[0].selected=true;
                    }else if (type=="10"){
                        opts[1].selected=true;
                    }
                    else if (type=="11") {
                        opts[2].selected=true;
                    }
                } else {
                    alert(data.msg);
                }
            }
        });
    });

    $('#showModal').on('click',function () {
        $('.small.modal').modal('show');
    });
    $('#confirm').on('click',function () {
        $.ajax({
            url: '/inviteMember',
            type: 'post',
            data: {inviteMail: $("#create").val()},
            dataType: 'json',
            success: function (data) {
                if (data.result == 1) {
                    var newMemberDivStr = "<div>" +
                        "<p>昵称："+data.member.nickName + "邮箱：" + data.member.mail + "</p>" +
                        "<a href='deleteMember?deleteId=" + data.member.id + "' >删除该成员</a>" +
                        "<select class='dropdown' id='" + data.member.nickName + "'>";
                    if(data.member.power="00"){
                        newMemberDivStr = newMemberDivStr +
                            "<option value='00' selected>仅下载文件</option>" +
                            "<option value='10'>可编辑文件</option>" +
                            "<option value='11'>可编辑文件与增减成员</option>";
                    }else if (data.member.power="10") {
                        newMemberDivStr = newMemberDivStr +
                            "<option value='00' selected>仅下载文件</option>" +
                            "<option value='10'>可编辑文件</option>" +
                            "<option value='11'>可编辑文件与增减成员</option>";
                    }else{
                        newMemberDivStr = newMemberDivStr +
                            "<option value='00' selected>仅下载文件</option>" +
                            "<option value='10'>可编辑文件</option>" +
                            "<option value='11'>可编辑文件与增减成员</option>";
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