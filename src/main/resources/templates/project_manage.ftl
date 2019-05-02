<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>项目管理</title>
    <link rel="stylesheet" type="text/css" href="/css/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/css/icon.min.css">
    <script src="/js/jquery.min.js"></script>
    <script src="/js/semantic.min.js"></script>

</head>
<body>
<#include "common/header.ftl" parse=true encoding='utf-8'>
<input type="hidden" value="${projectId}" id="projectId">
<div class="ui grid" style="margin-top: 40px;margin-bottom: 10px">
    <div class="two wide column" style="position: relative">
    </div>
    <div class="twelve wide column">
        <div class="ui container items">
            <div style="margin-bottom: 40px;height: 150px;">
                <h2 class="ui header">
                    转让项目所有权
                    <div class="sub header">项目创建者默认拥有项目所有权，该操作不可撤销</div>

                </h2>
                <div class="ui input">
                    <input type="text" placeholder="输入被转让人的邮箱" id="transferMail">
                </div>
                </br>
                </br>
                <button class="ui button" id="transfer">我已知晓后果，确认转让</button>
            </div>

            <div class="ui divider"></div>

            <div style="margin-top:40px;margin-bottom: 40px;height: 90px;">
                <h2 class="ui header">
                    重命名项目
                </h2>
                <input id="renameProject" type="text" value="${projectName}">
            </div>

            <div class="ui divider"></div>

            <div style="margin-top: 40px;height: 150px;">
                <h2 class="ui header">
                    删除项目
                    <div class="sub header">所有文件将会一并被删除，请谨慎操作</div>
                </h2>
                <button class="ui button" id="deleteProject">我已知晓后果，确认删除</button>
            </div>

        </div>
    </div>
    <div class="two wide column" style="position: relative">
    </div>
</body>
<script>
    $('#transfer').on('click', function () {
        $.ajax({
            url: "/transferProject",
            type: "POST",
            dataType: "json",
            data: {
                projectId: $("#projectId").val(),
                transferMail: $("#transferMail").val()
            },
            success: function (data) {
                if (data.result === 1) {
                    alert("转让成功");
                    window.open("/toLoginSuccess", "_self");
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

    var temp = $('#renameProject').val();
    $('#renameProject').on('blur', function () {
        if ($(this).val() === temp) {
            return;
        }
        $.ajax({
            url: "/renameProject",
            type: "POST",
            dataType: "json",
            data: {newName: $(this).val()},
            success: function (data) {
                if (data.result === 1) {
                    alert("项目名称修改成功！");
                } else {
                    alert(data.msg);
                }
            }
            error: function (err) {
                alert("系统错误，请稍后重试");
                console.log(err);
            }
        });
        temp = $(this).val();
    });

    $('#deleteProject').on('click', function () {
        $.ajax({
            url: "/deleteProject",
            type: "POST",
            dataType: "json",
            data: {pid: $("#projectId").val()},
            success: function (data) {
                if (data.result === 1) {
                    alert("删除成功");
                    window.open("/toLoginSuccess", "_self");
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