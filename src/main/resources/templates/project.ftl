<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>
    <link rel="stylesheet" type="text/css" href="/css/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/css/common.css">
    <link rel="stylesheet" type="text/css" href="/css/documentPage.css">
    <script src="/js/jquery.min.js"></script>
    <script src="/js/semantic.min.js"></script>
</head>
<body>
<#include "common/header.ftl" encoding="UTF-8" parse=true>
<div class="ui container">
    <#--    <p style="color:red;">${msg!}</p>-->
    <div class="doclist">
        <#if documents?size=0>
            <p class="noitem">项目中还没有文件(夹)</p>
        <#--上传新文件-->
            <form method="POST" action="/upload?parentId=0&flag=1" enctype="multipart/form-data"
                  id="uploadForm">
                <button class="ui button" style="width: 120px">上传文件</button>
                <input type="file" name="file" id="fileUpload"/>
            </form>
        <#else>
            <#list documents! as doc>
                <div class="doc" id="${doc.docId}">
                    <#if doc.type=false><!--文件-->
                    <span class="docName">${doc.name}</span>

                    <div class="opt fright" style="right: 20px;">
                        <span class="tip">•••</span>
                        <div class="operation hide menu">
                            <a href="/download?did=${doc.docId}&dver=${doc.version}">下载</a>
                            <#if doc.isEdit! = "1">
                                <form method="POST"
                                      action="/update?docId=${doc.docId}&parentId=0&flag=1"
                                      enctype="multipart/form-data" class="updateForm">
                                    <a>更新</a>
                                    <input type="file" name="file" class="fileUpdate"/>
                                </form>
                                <a href="/historyVersion?docId=${doc.docId}">历史版本</a>
                                <a href="#" class="showModal btn btn-success">重命名</a>
                                <a href="#" class="copyFile">创建副本</a>
                                <a href="#" class="deleteFile">删除</a>
                                <div class="ui vertical menu">
                                    <div class="ui dropdown item">
                                        权限
                                        <i class="dropdown icon"></i>
                                        <div class="menu">

                                            <a class="item isEditItem <#if doc.isEdit! = "1">active</#if>"
                                               data-value="1">可编辑</a>
                                            <a class="item isEditItem <#if doc.isEdit! = "0">active</#if>"
                                               data-value="0">不可编辑</a>
                                        </div>
                                    </div>
                                </div>
                            </#if>
                        </div>
                    </div>
                    <#else ><!--文件夹-->
                    <a href="/toSingleFolder?docId=${doc.docId!}"
                       class="to_single_folder docName">${doc.name}</a>
                    <#if doc.isEdit! = "1">
                        <div class="opt fright" style="right: 20px;">
                            <span class="tip">•••</span>
                            <div class="operation hide">
                                <a href="#" class="showModal btn btn-success">重命名</a>
                                <a href="#" class="deleteFile">删除</a>
                            </div>
                        </div>
                    </#if>
                    </#if>
                </div>
            </#list>
        <#--上传-->
            <form method="POST" action="/upload?parentId=${documents[0].parentId}&flag=1"
                  enctype="multipart/form-data" id="uploadForm">
                <button class="ui button" style="width: 120px;">上传文件</button>
                <input type="file" name="file" id="fileUpload"/>
            </form>
        </#if>
    </div>
    <br>
    <div class="">
        <button id="newFolder" class="ui button showModal" style="width: 120px;">新建文件夹</button>
    </div>
    <br>
    <div>
        <a href="/toRecycleBin?pid=${Session.projectId}">
            <button class="ui button" style="width: 120px">回收站</button>
        </a>
    </div>


    <div class="ui small modal">
        <div class="ui icon header">
            请输入名称
        </div>
        <div class="content">
            <div class="ui inverted segment">
                <div class="ui inverted big input" style="width: 100%;">
                    <input type="text" id="inputName">
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
<script type="text/javascript">
    $(document).on('click', '.deleteFile', function () {
        selectedBtn = $(this);
        $.ajax({
            url: '/fileDeleteRecycle',
            type: 'post',
            data: {docId: selectedBtn.closest('.doc').attr('id')},
            dataType: 'json',
            success: function (data) {
                if (data.result === 1) {
                    $('#' + data.docId).remove();
                } else {
                }
            },
            error: function (err) {
                console.log(err);
            }
        })
    });
    $('#fileUpload').change(function () {
        $("#uploadForm").submit();
    });
    $('.fileUpdate').change(function () {
        $(this).closest('.updateForm').submit();
    });
    $('.doclist').on('click', '.copyFile', function () {
        selectedBtn = $(this);
        $.ajax({
            url: '/fileCopy',
            type: 'post',
            data: {docId: selectedBtn.closest('.doc').attr('id')},
            dataType: 'json',
            success: function (data) {
                if (data.result === 1) {
                    var copyDiv = $("<div class='doc' id='" + data.document.docId + "'>" +
                        "<span class='docName'>" + data.document.name + "</span>" +
                        "<div class='opt fright' style='right: 20px;'>" +
                        "<span class='tip'>•••</span>" +
                        "<div class='operation hide'>" +
                        "<a href='/download?did=" + data.document.docId + "&dver=" + data.document.version + "'>下载</a>" +
                        "<form method='POST' action='/update?docId=" + data.document.docId + "&parentId=" + data.document.parentId + "&flag=1'" +
                        "enctype='multipart/form-data' class='updateForm'><a>更新</a>" +
                        "<input type='file' name='file' class='fileUpdate'/>" +
                        "</form>" +
                        "<a href='/historyVersion?docId=" + data.document.docId + "'>历史版本</a>" +
                        "<a href='#' class='showModal btn btn-success'>重命名</a>" +
                        "<a href='#' class='copyFile'>创建副本</a>" +
                        "<input type='hidden' value='" + data.document.docId + "'/>" +
                        "<a href='#' class='deleteFile'>删除</a>" +
                        "<input type='hidden' value='" + data.document.docId + "'/>" +
                        "</div></div>");
                    selectedBtn.closest('.doc').after(copyDiv);
                } else {

                }
            },
            error: function (err) {
                console.log(err);
            }
        })
    });
</script>

<script type="text/javascript">
    $('.doclist').on('click', '.tip', function () {
        var id = $(this).closest('.doc').attr('id');
        $('.doc:not(#' + id + ')').find('.operation').addClass('hide');
        $(this).next('.operation').toggleClass('hide');
    });

    $(document).on('click', '.showModal', function () {
        selectedBtn = $(this);
        if (selectedBtn.attr('id') !== 'newFolder') {
            $('#inputName').val(selectedBtn.closest('.doc').find('.docName').html().split('.')[0]);
        } else {
            $('#inputName').val('');
        }
        $('.small.modal')
            .modal('show')
        ;
    });
    $('.ui.dropdown')
        .dropdown()
    ;

    var selectedBtn;
    $('#confirm').on('click', function () {
        //若点击重命名
        if (selectedBtn.attr('id') !== 'newFolder') {
            $.ajax({
                url: '/fileRename',
                type: 'post',
                data: {
                    newName: $("#inputName").val(),
                    docId: selectedBtn.closest('.doc').attr('id'),
                    oldName: selectedBtn.closest('.doc').find('.docName').html(),
                },
                dataType: 'json',
                success: function (data) {
                    if (data.result === 1) {
                        selectedBtn.closest('.doc').find('.docName').html(data.name);
                        console.log("重命名成功");
                    } else {
                    }
                },
                error: function (err) {
                    console.log(err);
                }
            });
            console.log('closed');
        } else {//若点击新建文件夹
            $.ajax({
                url: '/newFolder',
                type: 'post',
                data: {
                    folderName: $("#inputName").val(),
                    parentId: 0,
                },
                dataType: 'json',
                success: function (data) {
                    if (data.result === 1) {
                        var newFolderDiv = $("<div class='doc' id='" + data.document.docId + "'>" +
                            "<a class='to_single_folder docName' href='/toSingleFolder?docId=" + data.document.docId + "'>" + data.document.name + "</a>" +
                            "<input type='hidden' value='" + data.document.docId + "'/>" +
                            "<div class='opt fright' style='right:20px;'>" +
                            "<span class='tip'>•••</span>" +
                            "<div class='operation hide'>" +
                            "<a href='javascript:void(0)' class='showModal btn btn-success'>重命名</a>" +
                            "<a href='javascript:void(0)' class='deleteFile'>删除</a>" +
                            "</div></div>");
                        $('.noitem').remove();
                        $('#uploadForm').before(newFolderDiv);
                        console.log("创建成功");
                    } else {
                    }
                },
                error: function (err) {
                    console.log(err);
                }
            });
        }
    })
</script>
<script>
    $('.isEditItem').on('click', function (e) {
        var clickId = $(e.target);
        var power = clickId.attr("data-value");
        var selectIsEdit = $(this);
        $.ajax({
            url: '/changeDocPower',
            type: 'post',
            data: {
                docId: selectIsEdit.closest('.doc').attr('id'),
                power: power
            },
            dataType: 'json',
            success: function (data) {
                if (data.result === 1) {
                    $(".isEditItem").removeClass("active");
                    clickId.addClass("active");
                    alert("修改权限成功");
                } else {
                    alert("修改失败");
                }
            },
            error: function (err) {
                console.log(err);
            }
        });
    });
</script>
</body>
</html>