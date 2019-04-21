<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>回收站</title>
    <link rel="stylesheet" type="text/css" href="/css/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/css/common.css">
    <script src="/js/jquery.min.js"></script>
    <script src="/js/semantic.min.js"></script>
    <style>
        .doc {
            border: 1px solid #888888;
            max-width: 500px;
            height: 50px;
            box-shadow: 4px 4px 5px #888888;
            line-height: 50px;
            padding-left: 1em;
            margin-bottom: 30px;
        }

        #fileUpload {
            opacity: 0;
            width: 120px;
            height: 36px;
            position: relative;
            right: 128px;
            bottom: 6px;
            cursor: pointer !important;
        }

        .fileUpdate {
            position: absolute;
            opacity: 0;
            width: 90px;
            height: 30px;
            left: 0;
            top: 64px;
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
            padding: 10px 15px;
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

        .ui.vertical.menu:hover {
            background-color: #80bdff !important;
        }

        .ui.vertical.menu {
            border-radius: 0;
            width: 88px;
            background-color: #f0f0f0;
            border: none;
            margin: 0;
            padding: 0;
            border-color: #f0f0f0;
        }
    </style>
</head>
<body>
<#include "common/header.ftl" encoding="UTF-8" parse=true>
<div class="ui container">
    <#--    <p style="color:red;">${msg!}</p>-->
    <div class="doclist">
        <#if documents?size=0>
            <p class="noitem">回收站为空</p>
        <#else>
            <#list documents! as doc>
                <div class="doc" id="${doc.docId}">
                    <span class="docName">${doc.name}</span>
                    <div class="opt fright" style="right: 20px;">
                        <span class="tip">•••</span>
                        <div class="operation hide menu">
                            <a href="#" class="recoveryFile">恢复文件</a>
                            <a href="#" class="deleteFile">彻底删除</a>
                        </div>
                    </div>
                </div>
            </#list>
        </#if>
    </div>
</div>
<script type="text/javascript">
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
                if (data.result === true) {
                    $('#' + data.docId).remove();
                } else {
                    console.log(err);
                    alert(data.msg);
                }
            },
            error: function (err) {
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
                if (data.result === true) {
                    $('#' + data.docId).remove();
                } else {
                    console.log(err);
                    alert(data.msg);
                }
            },
            error: function (err) {
                console.log(err);
            }
        })
    });
</script>
</body>
</html>