<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>工作台 - 流云文档</title>
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
        .pname {
            font-size: 20px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<#include "common/header.ftl" parse=true encoding='utf-8'>
<div class="ui grid" style="margin-top: 10px;margin-bottom: 10px">
    <div class="two wide column">
        <div class="ui secondary vertical labeled icon menu" style="position: fixed;">
            <div style="margin: 3vw;">
                <a class="item" href="/toLoginSuccess"><i class="desktop icon"></i>工作台</a>
            </div>
            <div style="margin: 3vw;">
                <a id="showModal" class="item"><i class="sticky note icon"></i>创建新项目</a>
            </div>
        </div>
    </div>
    <div class="twelve wide column">
        <div class="ui container">
            <div class="projectlist ui grid">
                <#if project?? && (project?size > 0)>
                    <#list project! as p>
                        <div class="four wide column">
                            <div class="ui card segment projectDiv" id="${p.id}" style="margin: 1vw;">
                                <div class="center aligned content">
                                    <a class="pname" href="/toProject?pid=${p.id}">
                                        ${p.name!}
                                    </a>
                                </div>

                                <div class="center aligned content">
                                    <div class="meta">
                                        <#if p.power! = "111">
                                            <a href="/toAccountManage?pid=${p.id}"
                                               class="center aligned accountManage">成员管理</a>
                                            <a href="/toProjectManage?pid=${p.id}" class="accountManage">项目管理</a>
                                        <#elseif p.power! = "11">
                                            <a href="/toAccountManage?pid=${p.id}" class="accountManage">成员管理</a>
                                            <a href="/quitProject?pid=${p.id}" class="quitProject">退出该项目</a>
                                        <#else>
                                            <a href="/quitProject?pid=${p.id}" class="quitProject">退出该项目</a>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </#list>

                <#else>
                    <div class="ui container" id="noItem">
                        <h2 class="ui header">您尚未加入任何项目</h2>
                        <span>点击左侧的“创建新项目”来创建项目</span></br>
                        <span>或联系项目管理员帮助您加入项目</span>
                    </div>

                </#if>
            </div>
        </div>
    </div>
    <div class="two wide column">
        <#--        <div class="ui vertical menu" style="position: fixed">-->
        <#--            <div class="ui vertical menu" style="position: fixed;height: auto">-->
        <#--                <div class="ui item" style="margin: 20px;">-->
        <#--                    <button id="showModal" class="ui button">-->
        <#--                        创建新项目-->
        <#--                    </button>-->
        <#--                </div>-->
        <#--            </div>-->
        <#--        </div>-->
    </div>
</div>

<#--<div class="ui bottom fixed inverted vertical footer segment" style="background-color: #f1f1f1">-->
<#--    <div class="ui center aligned container">-->
<#--        <h4 class="ui inverted header" style="color: black">流云文档</h4>-->
<#--        <p style="color: black">专注于团队协作</p>-->
<#--    </div>-->
<#--</div>-->

<div class="ui small modal">
    <div class="ui icon header">
        请输入名称
    </div>
    <div class="content">
        <div class="ui inverted segment" style="background-color: #f1f1f1">
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
                    var newProjectDiv = "<div class='ui card grey segment projectDiv' id='" + data.project.id + "' style='margin: 10px;'>" +
                        "<div class='center aligned content'>" +
                        "<a class='pname' href='/toProject?pid=" + data.project.id + "'>" +
                        data.project.name +
                        "</a>" +
                        "</div>" +
                        "<div class='center aligned content content'>" +
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
                    $('#noItem').remove();
                    $('.projectlist').append(newProjectDiv);
                } else if (data.result === -1) {
                    alert(data.msg);
                    window.open("/", "_self");
                } else {
                    alert(data.msg);
                }
            }
            error: function (err) {
                alert("系统错误，请稍后重试");
                console.log(err);
            }
        });
    });
</script>
</body>
</html>