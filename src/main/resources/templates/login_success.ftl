<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>流云文档-欢迎使用</title>
    <link rel="stylesheet" type="text/css" href="/css/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/css/common.css">
    <script src="/js/jquery.min.js"></script>
    <script src="/js/semantic.min.js"></script>
    <style>
        .pname {
            font-size: 20px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<#include "common/header.ftl" parse=true encoding='utf-8'>
<div>${msg!}</div>
<div class="ui inverted vertical masthead center aligned segment" style="height: 100%;padding-top: 0">
    <div class="ui container">
        <div class="projectlist ui divided items">
            <#if project?? && (project?size > 0)>
                <#list project! as p>
                    <div class="ui card projectDiv" id=${p.id}>
                        <a class="pname" href="/toProject?pid=${p.id}">
                            ${p.name!}
                        </a>
                        <div class="content">
                            <div class="meta">
                                <#if p.power! = "111">
                                    <a href="/toAccountManage?pid=${p.id}" class="accountManage">成员管理</a>
                                    <a href="/toProjectManage?pid=${p.id}" class="accountManage">项目管理</a>
                                <#elseif p.power! = "11">
                                    <a href="/toAccountManage?pid=${p.id}" class="accountManage">成员管理</a>
                                <#else>
                                    <a href="/quitProject?pid=${p.id}" class="quitProject">退出该项目</a>
                                </#if>
                            </div>
                        </div>
                    </div>
                </#list>

            <#else>
                <p class="noitem">您尚未加入任何项目</p>
            </#if>
        </div>
        <div class="ui left" style="margin-top: 20px;text-align: left;">
            <button id="showModal" class="ui button">
                创建新项目
            </button>
        </div>
    </div>

    <div class="ui small modal">
        <div class="ui icon header">
            请输入名称
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
    $('#showModal').on('click', function () {
        $('.small.modal').modal('show');
    });
    $('#confirm').on('click', function () {
        $.ajax({
            url: '/createProject',
            type: 'post',
            data: {projectName: $("#create").val()},
            dataType: 'json',
            success: function (data) {
                if (data.result === 1) {
                    var newProjectDiv = "<div class='ui card projectDiv' id=" + data.project.id + ">" +
                        "<a class='pname' href='/toProject?pid=" + data.project.id + "'>" +
                        data.project.name +
                        "</a>" +
                        "<div class='content'>" +
                        "<div class='meta'>";
                    if (data.project.power === "111") {
                        newProjectDiv = newProjectDiv +
                            "<a href='toAccountManage?pid=" + data.project.id + "' class='accountManage'>成员管理</a>" +
                            "<a href='toProjectManage?pid=" + data.project.id + "' class='accountManage'>项目管理</a>";
                    } else if (data.project.power === "11") {
                        newProjectDiv = newProjectDiv +
                            "<a href='toAccountManage?pid=" + data.project.id + "' class='accountManage'>成员管理</a>";
                    } else {
                        newProjectDiv = newProjectDiv +
                            "<a href='quitProject?pid=" + data.project.id + "' class='quitProject'>退出该项目</a>";
                    }
                    newProjectDiv = newProjectDiv +
                        "</div>" +
                        "</div>" +
                        "</div>";
                    $('.noitem').remove();
                    $('.projectlist').append(newProjectDiv);
                    console.log(data.project.power);
                } else {
                    alert('创建失败');
                }
            }
        });
    });
</script>
</body>
</html>