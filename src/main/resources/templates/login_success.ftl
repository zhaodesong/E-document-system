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
        .pname{font-size: 20px;font-weight: bold;}
    </style>
</head>
<body>
<#include "common/header.ftl" parse=true encoding='utf-8'>
<div class="ui inverted vertical masthead center aligned segment" style="height: 100%;padding-top: 0">
    <div class="ui container">
            <div class="projectlist ui divided items">
                <#if project?? && (project?size > 0)>
                    <#list project! as p>
                        <div class="ui card projectDiv">
                            <a class="pname" href="/toProject?pid=${p.id}">
                                ${p.name!}
                            </a>
                            <div class="content" >
                                <div class="meta">
                                    <a>成员管理</a>
                                    <a>删除</a>
                                </div>
                            </div>
                        </div>

<#--                        <div class="projectDiv">-->
<#--                            <a href="/toProject?pid=${p.id}">${p.name!}</a>-->
<#--                            &lt;#&ndash;如果有权限，显示删除，否则显示退出&ndash;&gt;-->
<#--                            <a href="/deleteProject?pid=${p.id}">删除该项目</a>-->
<#--                        </div>-->
                    </#list>

            </div>
                <#else>
                    <p class="noitem">您尚未加入任何项目</p>
                </#if>

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
    $('#showModal').on('click',function () {
        $('.small.modal').modal('show');
    });
    $('#confirm').on('click',function () {
        $.ajax({
            url: '/createProject',
            type: 'post',
            data: {projectName: $("#create").val()},
            dataType: 'json',
            success: function (data) {
                if (data.result == true) {
                    var newProjectDiv = $("<div class='ui card projectDiv'>" +
                        "<a href='/toProject?pid=" + data.project.pid + "'>" + data.project.name +
                        "</a>" +
                        "<div class='content'><div class='meta'>" +
                        "<a>删除</a><a>删除</a><a>删除</a></div></div></div>");
                    $('.noitem').remove();
                    $('.projectlist').append(newProjectDiv);
                } else {
                    alert('创建失败');
                    // $("loading").show();
                    // document.getElementById("loading").innerHTML = "操作失败，请稍后重试";
                }
            }
        });
    });
</script>
</body>
</html>