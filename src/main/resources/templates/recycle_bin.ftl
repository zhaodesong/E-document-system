<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>回收站 - 流云文档</title>
    <link rel="stylesheet" type="text/css" href="/css/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/css/icon.min.css">
    <link rel="stylesheet" type="text/css" href="/components/reset.css">
    <#--    <link rel="stylesheet" type="text/css" href="/components/site.css">-->

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
        .fright {
            float: right;
        }

        .hide {
            display: none;
        }

        a {
            color: black;
        }

        .operation {
            background-color: #f0f0f0;
            z-index: 2;
            position: absolute;
            width: 90px;
            border: 1px solid #b0b0b0;
            line-height: 30px;
            box-shadow: 2px 2px 2px #a0a0a0;
            left: -45px;
        }

        .operation a {
            padding: 10px 5px;
            display: block;
        }

        .opt {
            position: relative;
        }

        .tip {
            cursor: pointer;
        }

        .operation a:hover, .operation form:hover {
            background-color: #80bdff;
        }
    </style>
</head>
<body>
<#include "common/header.ftl" parse=true encoding='utf-8'>
<div class="ui grid" style="margin-top: 10px;margin-bottom: 10px">
    <div class="two wide column" style="position: relative">
        <div class="ui secondary vertical labeled icon menu" style="position: fixed;height: auto;">
            <div style="margin: 30px;margin-left: 40px">
                <a class="item" href="/toLoginSuccess"><i class="desktop icon"></i>工作台</a>
            </div>
            <div style="margin: 30px;margin-left: 40px">
                <a class="item" href="/toRecycleBin?pid=${Session.projectId}"><i class="trash icon"></i>回收站</a>
            </div>
        </div>
    </div>
    <div class="twelve wide column">
        <div class="ui container" style="margin-top: 20px">
            <#if documents?size=0>
                <div class="ui container" id="noItem" style="margin-top: 10px">
                    <h2 class="ui header">回收站为空</h2>
                </div>
            <#else>
                <div class="doclist ui five column grid">
                    <#list documents! as doc>
                        <div class="column">
                            <div class="ui card doc" id="${doc.docId}" style="width: 12.5vw;height: 35vh">
                                <div class="content">
                                    <div class="opt fright">
                                        <span class="tip">•••</span>
                                        <div class="operation hide menu">
                                            <a href="#" class="recoveryFile">恢复文件</a>
                                            <a href="#" class="deleteFile">彻底删除</a>
                                        </div>
                                    </div>
                                    <div class="center aligned">
                                        <img src="/img/file.png">
                                    </div>
                                    <div class="center aligned" style="max-height: available">
                                        <a class="center aligned header docName">${doc.name}</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </#list>
                </div>
            </#if>
        </div>
    </div>
    <div class="two wide column">
        <div class="ui secondary vertical menu" style="position: fixed;">

        </div>
    </div>
</div>

<script>
    $('.doclist').on('click', '.tip', function () {
        var id = $(this).closest('.doc').attr('id');
        $('.doc:not(#' + id + ')').find('.operation').addClass('hide');
        $(this).next('.operation').toggleClass('hide');
    });

    var selectedBtn;
    $(document).on('click', '.deleteFile', function () {
        selectedBtn = $(this);
        $.ajax({
            url: '/fileDelete',
            type: 'post',
            data: {docId: selectedBtn.closest('.doc').attr('id')},
            dataType: 'json',
            success: function (data) {
                if (data.result === 1) {
                    $('#' + data.docId).remove();
                    alert("彻底删除成功");
                    window.open("/toRecycleBin?pid=" + data.projectId, "_self");
                } else {
                    alert(data.msg);
                }
            },
            error: function (err) {
                alert("系统错误，请稍后重试");
                console.log(err);
            }
        })
    });
    $(document).on('click', '.recoveryFile', function () {
        selectedBtn = $(this);
        $.ajax({
            url: '/fileRecovery',
            type: 'post',
            data: {docId: selectedBtn.closest('.doc').attr('id')},
            dataType: 'json',
            success: function (data) {
                if (data.result === 1) {
                    $('#' + data.docId).remove();
                    alert("恢复文件成功");
                    window.open("/toRecycleBin?pid=" + data.projectId, "_self");
                } else {
                    alert(data.msg);
                }
            },
            error: function (err) {
                alert("系统错误，请稍后重试");
                console.log(err);
            }
        })
    });
</script>
</body>
</html>