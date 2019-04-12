<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>
    <link rel="stylesheet" type="text/css" href="/css/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/css/common.css">
    <script src="/js/jquery.min.js"></script>
    <script src="/js/semantic.min.js"></script>
    <style>
        .doc{border:1px solid #888888;max-width: 300px;height: 50px;box-shadow: 4px 4px 5px #888888;line-height: 50px;padding-left: 1em;margin-bottom: 30px;}
        #fileUpload{opacity: 0;width:120px;height: 36px;position: relative;right:128px;bottom:6px;cursor: pointer!important;}
        .fileUpdate{position: absolute;opacity: 0;width:90px;height: 30px;left: 0;top:64px;}
        .operation{background-color: #f0f0f0;z-index:2;position: absolute;width: 90px;border:1px solid #b0b0b0;line-height: 30px;box-shadow: 2px 2px 2px #a0a0a0;
            left: -45px;}
        .operation a{padding: 10px 15px;display: block;}
        .opt{position:relative;}
        .tip{cursor: pointer;}
        .operation a:hover,.operation form:hover{background-color: #80bdff;}
    </style>
</head>
<body>
<#include "common/header.ftl" encoding="UTF-8" parse=true>
<div class="ui container">
    <p style="color:red;">${msg!}</p>
    <div class="doclist">
        <#if documents?size=0>
            <p class="noitem">项目中还没有文件(夹)</p>
        <#--上传新文件-->
            <form method="POST" action="/upload?parentId=0&level=0&flag=1" enctype="multipart/form-data" id="uploadForm">
                <button class="ui basic button" style="width: 120px">上传文件</button>
                <input type="file" name="file" id="fileUpload"/>
            </form>
        <#else>
            <#list documents! as doc>
                <div class="doc" id="${doc.docId}">
                    <#if doc.type=false><!--文件-->
                        <span class="docName">${doc.name}</span>

                        <div class="opt fright"  style="right: 20px;">
                            <span class="tip">•••</span>
                            <div class="operation hide">
                                <a href="/download?did=${doc.docId}&dver=${doc.version}">下载</a>
                                <form method="POST" action="/update?docId=${doc.docId}"
                                      enctype="multipart/form-data" class="updateForm">
                                    <a>更新</a>
                                    <input type="file" name="file" class="fileUpdate"/>
                                </form>
                                <a href="/historyVersion?docId=${doc.docId}">历史版本</a>
                                <a href="#" class="showModal btn btn-success">重命名</a>
                                <a href="#" class="copyFile">创建副本</a>
                                <a href="#" class="deleteFile">删除</a>
                            </div>
                        </div>
                    <#else ><!--文件夹-->
                        <a href="/toSingleFolder?docId=${doc.docId!}&level=${doc.level!}" class="to_single_folder docName">${doc.name}</a>
                        <div class="opt fright" style="right: 20px;">
                            <span class="tip">•••</span>
                            <div class="operation hide">
                                <a href="#" class="showModal btn btn-success">重命名</a>
                                <a href="#" class="deleteFile">删除</a>
                            </div>
                        </div>
                    </#if>
                </div>
            </#list>
        <#--上传-->
            <form method="POST" action="/upload?parentId=${documents[0].parentId}&level=${documents[0].level}&flag=1"
                  enctype="multipart/form-data" id="uploadForm">
                <button class="ui button" style="width: 120px;">上传文件</button>
                <input type="file" name="file" id="fileUpload"/>
            </form>


        </#if>
    </div>
    <div class="">
        <button id="newFolder" class="ui button showModal" style="width: 120px;">新建文件夹</button>
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
    $(document).on('click','.deleteFile', function () {
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
    $('.doclist').on('click', '.copyFile',function () {
        selectedBtn = $(this);
        $.ajax({
            url: '/fileCopy',
            type: 'post',
            data: {docId: selectedBtn.closest('.doc').attr('id')},
            dataType: 'json',
            success: function (data) {
                if (data.result == true) {
                    var copyDiv = $("<div class='doc' id='" + data.document.docId + "'>" +
                        "<span class='docName'>" + data.document.name + "</span>" +
                        "<div class='opt fright' style='right: 20px;'>" +
                        "<span class='tip'>•••</span>" +
                        "<div class='operation hide'>" +
                        "<a href='/download?did=" + data.document.docId + "&dver=" + data.document.version + "'>下载</a>" +
                        "<form method='POST' action='/update?docId=" + data.document.docId + "' " +
                        "enctype='multipart/form-data' class='updateForm'><a>更新</a>" +
                        "<input type='file' name='file' class='fileUpdate'/>" +
                        "</form>" +
                        "<a href='/historyVersion?docId="+data.document.docId+"'>历史版本</a>" +
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
    $('.doclist').on('click','.tip',function () {
        var id = $(this).closest('.doc').attr('id');
        $('.doc:not(#'+id+')').find('.operation').addClass('hide');
        $(this).next('.operation').toggleClass('hide');
    });
    $(document).on('click',function (e) {
        if(e.target.className.search('tip') !== -1) return;
        $('.operation').addClass('hide');
    });

    $('.doclist').on('click','.showModal',function () {
        selectedBtn = $(this);
        if(selectedBtn.attr('id') !== 'newFolder'){
            $('#inputName').val(selectedBtn.closest('.doc').find('.docName').html().split('.')[0]);
        }else{
            $('#inputName').val('');
        }
        $('.small.modal')
            .modal('show')
        ;
    });

    var selectedBtn;
    $('#confirm').on('click',function () {
        //若点击重命名
        if(selectedBtn.attr('id') !== 'newFolder'){
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
                    if (data.result === true) {
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
        }else {//若点击新建文件夹
            $.ajax({
                url: '/newFolder',
                type: 'post',
                data: {
                    folderName: $("#inputName").val(),
                    parentId:0,
                    level:0
                },
                dataType: 'json',
                success: function (data) {
                    if (data.result == 1) {
                        var newFolderDiv = $("<div class='doc' id='" + data.document.docId + "'>" +
                            "<a class='to_single_folder docName' href='/toSingleFolder?docId="+ data.document.docId+"&level="+ data.document.level+"'>" + data.document.name + "</a>" +
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
</body>
</html>