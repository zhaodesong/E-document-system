<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${Session.projectName} - 流云文档</title>
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

        #editMenu:hover {
            background-color: #80bdff !important;
        }

        #editMenu {
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
<#include "common/header.ftl" parse=true encoding='utf-8'>
<input type="hidden" value="${parentId!}" id="parentIdVal">
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
                <div class="ui container" id="noItem" style="margin-top: 20px">
                    <h2 class="ui header">文件夹为空</h2>
                </div>
            <#else>
                <div class="doclist ui five column grid">
                    <div class="row">
                        <#list documents! as doc>
                            <#if doc.type=true><!--文件夹-->
                                <div class="column" style="margin-bottom: 40px">
                                    <div class="ui card doc" id="${doc.docId}" style="width: 12.5vw;height: 35vh">
                                        <div class="content">
                                            <#if doc.isEdit! = "1">
                                                <div class="opt fright">
                                                    <span class="tip">•••</span>
                                                    <div class="operation hide menu">
                                                        <a href="#" class="showModal btn btn-success"><i
                                                                    class="pencil alternate icon"></i>重命名</a>
                                                        <a href="#" class="deleteFile"><i class="delete icon"></i>删除</a>
                                                    </div>
                                                </div>
                                            </#if>
                                            <div class="center aligned">
                                                <img src="/img/folder.png">
                                            </div>
                                            <div class="center aligned">
                                                <a href="/toSingleFolder?docId=${doc.docId}"
                                                   class="header docName">${doc.name}</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </#if>
                        </#list>
                    </div>

                    <#list documents! as doc>
                        <#if doc.type=false><!--文件-->
                            <div class="column">
                                <div class="ui card doc" id="${doc.docId}" style="width: 12.5vw;height: 35vh">
                                    <div class="content">
                                        <div class="opt fright">
                                            <span class="tip">•••</span>
                                            <div class="operation hide menu">
                                                <a href="/download?did=${doc.docId}&dver=${doc.version}"><i
                                                            class="download icon"></i>下载</a>
                                                <#if doc.isEdit! = "1">
                                                    <form method="POST"
                                                          action="/update?docId=${doc.docId}&parentId=${parentId}&flag=0"
                                                          enctype="multipart/form-data" class="updateForm">
                                                        <a><i class="edit icon"></i>更新</a>
                                                        <input type="file" name="file" class="fileUpdate"/>
                                                    </form>
                                                    <a href="/historyVersion?docId=${doc.docId}"><i
                                                                class="list ul icon"></i>历史版本</a>
                                                    <a href="#" class="showModal btn btn-success"><i
                                                                class="pencil alternate icon"></i>重命名</a>
                                                    <a href="#" class="copyFile"><i class="copy icon"></i>创建副本</a>
                                                    <a href="#" class="deleteFile"><i class="delete icon"></i>删除</a>
                                                    <div class="ui fitted divider"></div>
                                                    <div class="ui vertical menu" id="editMenu">
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
                                        <div class="center aligned">
                                            <img src="/img/file.png">
                                        </div>
                                        <div class="center aligned" style="max-height: available">
                                            <a class="center aligned header docName">${doc.name}</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </#if>
                    </#list>
                </div>
            </#if>
        </div>
        <form method="POST" action="/upload?parentId=${parentId}&flag=0" enctype="multipart/form-data"
              id="uploadForm" style="display: none">
            <button class="ui black button" style="width: 135px"><i class="upload icon"></i>上传文件
            </button>
            <input type="file" name="file" id="fileUpload"/>
        </form>
    </div>
    <div class="two wide column">
        <div class="ui secondary vertical menu" style="position: fixed;">
            <div style="margin-top: 3vh;margin-bottom: 3vh">
                <label class="ui black button" for="fileUpload" style="width: 135px"><i
                            class="upload icon"></i>上传文件</label>
            </div>
            <div style="margin-top: 3vh;margin-bottom: 3vh">
                <button id="newFolder" class="ui button showModal" style="width: 135px;"><i
                            class="folder open icon"></i>新建文件夹
                </button>
            </div>
        </div>
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

<script>
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
                    alert("删除成功");
                    window.open("toSingleFolder?docId=" + data.parentId, "_self");
                } else {
                    alert(data.msg);
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
                    // var copyDiv = "<div class='ui card doc' id='" + data.doc.docId + "' style='width: 12.5vw;height: 35vh'>" +
                    //     "<div class='content'>" +
                    //     "<div class='opt fright'>" +
                    //     "<span class='tip'>•••</span>" +
                    //     "<div class='operation hide menu'>" +
                    //     "<a href='/download?did=" + data.doc.docId + "&dver=" + data.doc.version + "'><i class='download icon'></i>下载</a>";
                    // if (data.doc.isEdit === "1") {
                    //     copyDiv = copyDiv +
                    //         "<form method='POST' action='/update?docId=" + data.doc.docId + "&parentId=0&flag=0' enctype='multipart/form-data' class='updateForm'>" +
                    //         "<a><i class='edit icon'></i>更新</a>" +
                    //         "<input type='file' name='file' class='fileUpdate'/>" +
                    //         "</form>" +
                    //         "<a href='/historyVersion?docId=" + data.doc.docId + "'><i class='list ul icon'></i>历史版本</a>" +
                    //         "<a href='#' class='showModal btn btn-success'><i class='pencil alternate icon'></i>重命名</a>" +
                    //         "<a href='#' class='copyFile'><i class='copy icon'></i>创建副本</a>" +
                    //         "<a href='#' class='deleteFile'><i class='delete icon'></i>删除</a>" +
                    //         "<div class='ui fitted divider'></div>" +
                    //         "<div class='ui vertical menu' id='editMenu'>" +
                    //         "<div class='ui dropdown item'>" +
                    //         "权限" +
                    //         "<i class='dropdown icon'></i>" +
                    //         "<div class='menu'>";
                    //     if (data.doc.isEdit === "1") {
                    //         copyDiv = copyDiv + "<a class='item isEditItem active' data-value='1'>可编辑</a>";
                    //     } else if (data.doc.isEdit === "0") {
                    //         copyDiv = copyDiv + "<a class='item isEditItem active' data-value='0'>不可编辑</a>";
                    //     }
                    //     copyDiv = copyDiv +
                    //         "</div>" +
                    //         "</div>" +
                    //         "</div>";
                    // }
                    // copyDiv = copyDiv +
                    //     "</div>" +
                    //     "</div>" +
                    //     "<div class='center aligned'>" +
                    //     "<img src='/img/file.png'>" +
                    //     "</div>" +
                    //     "<div class='center aligned' style='max-height: available'>" +
                    //     "<a class='center aligned header docName'>" + data.doc.name + "</a>" +
                    //     "</div>" +
                    //     "</div>" +
                    //     "</div>";
                    // selectedBtn.closest('.doc').after(copyDiv);
                    alert("创建副本成功");
                    window.open("toSingleFolder?docId=" + data.parentId, "_self");
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
        $('.small.modal').modal('show');
    });
    $('.ui.dropdown').dropdown();

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
                        alert("重命名成功");
                    } else if (data.result === -1) {
                        alert(data.msg);
                        window.open("/", "_self");
                    } else {
                        alert(data.msg);
                    }
                },
                error: function (err) {
                    alert("系统错误，请稍后重试");
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
                    parentId: $("#parentIdVal").val(),
                },
                dataType: 'json',
                success: function (data) {
                    if (data.result === 1) {
                        // var newFolderDiv = "<div class='ui card doc' id='" + data.doc.docId + "' style='width: 12.5vw;height: 35vh'>" +
                        //     "<div class='content'>";
                        // if (data.doc.isEdit === "1") {
                        //     newFolderDiv = newFolderDiv +
                        //         "<div class='opt fright'>" +
                        //         "<span class='tip'>•••</span>" +
                        //         "<div class='operation hide menu'>" +
                        //         "<a href='#' class='showModal btn btn-success'><i class='pencil alternate icon'></i>重命名</a>" +
                        //         "<a href='#' class='deleteFile'><i class='delete icon'></i>删除</a>" +
                        //         "</div>" +
                        //         "</div>";
                        // }
                        // newFolderDiv = newFolderDiv +
                        //     "<div class='center aligned'>" +
                        //     "<img src='/img/folder.png'>" +
                        //     "</div>" +
                        //     "<div class='center aligned'>" +
                        //     "<a href='/toSingleFolder?docId=" + data.doc.docId + "' class='header docName'>" + data.doc.name + "</a>" +
                        //     "</div>" +
                        //     "</div>" +
                        //     "</div>";
                        alert("创建成功");
                        window.open("toSingleFolder?docId=" + data.parentId, "_self");
                    } else if (data.result === -1) {
                        alert(data.msg);
                        window.open("/", "_self");
                    } else {
                        alert(data.msg);
                    }
                },
                error: function (err) {
                    alert("系统错误，请稍后重试");
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
                } else if (data.result === -1) {
                    alert(data.msg);
                    window.open("/", "_self");
                } else {
                    alert(data.msg);
                }
            },
            error: function (err) {
                alert("系统错误，请稍后重试");
                console.log(err);
            }
        });
    });
</script>
</body>
</html>