<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${title!}</title>
    <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
    <link rel="stylesheet" href="css/animate.min.css" type="text/css"/>
    <link rel="stylesheet" href="css/rmodal.css" type="text/css"/>
    <style>
        .doc{border:1px solid;}
    </style>
</head>
<body>
<p style="color:red;">${msg!}</p>
<#if documents?size=0>
    <p>文件夹为空</p>
<#--上传新文件-->
    <form method="POST" action="/upload?parentId=${parentId!}&level=${level!}&flag=0" enctype="multipart/form-data" id="uploadForm">
        <input type="file" name="file" id="fileUpload"/><br/><br/>
    </form>
<#else>
    <#list documents! as doc>
        <div class="doc" id="${doc.docId}">
            <#if doc.type=false>
                <p class="docName">${doc.name}</p>
            <#--下载-->
                <button><a href="/download?did=${doc.docId}&dver=${doc.version}">下载</a></button>
            <#--更新文件-->
                <form method="POST" action="/update?docId=${doc.docId}"
                      enctype="multipart/form-data" id="updateForm">
                    <input type="file" name="file" id="fileUpdate"/><br/><br/>
                </form>
            <#--查看历史版本-->
                <a href="/historyVersion?docId=${doc.docId}">查看历史版本</a>
            <#--重命名-->
                <a href="#" class="showModal btn btn-success">重命名</a>
            <#--创建副本-->
                <a href="#" class="copyFile">创建副本</a>
            <#--删除-->
                <a href="#" class="deleteFile">删除</a>
            <#else >
                <p class="docName">${doc.name}</p>
            <#--重命名-->
                <a href="#" class="showModal btn btn-success">重命名</a>
            <#--删除-->
                <a href="#" class="deleteFile">删除</a>
            </#if>
            <br/>
            <br/>
        </div>
    </#list>
<#--上传-->
    <form method="POST" action="/upload?parentId=${documents[0].parentId}&level=${documents[0].level}&flag=0"
          enctype="multipart/form-data" id="uploadForm">
        <input type="file" name="file" id="fileUpload"/><br/><br/>
    </form>


</#if>
<div class="">
    <a href="#" id="newFolder" class="showModal">新建文件夹</a>
</div>
<br>
<div id="modal" class="modal">
    <div class="modal-dialog animated">
        <div class="modal-content">
            <form class="form-horizontal" method="get">
                <div class="modal-header">
                    <strong>请输入名称</strong>
                </div>

                <div class="modal-body">
                    <input type="text" id="rename">
                </div>

                <div class="modal-footer">
                    <a class="btn btn-primary" onclick="modal.close()">
                        确定
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/js/rmodal.js"></script>
<script type="text/javascript">
    $('.deleteFile').on('click', function () {
        selectedBtn = $(this);
        $.ajax({
            url: '/fileDelete',
            type: 'post',
            data: {docId: selectedBtn.closest('.doc').attr('id')},
            dataType: 'json',
            beforeSend: function () {

            },
            success: function (data) {
                if (data.result == true) {
                    $('#' + data.docId).remove();
                } else {

                }
            },
            complete: function () {

            },
            error: function (err) {
                console.log(err);

            }
        })
    });

    $('#fileUpload').change(function () {
        $("#uploadForm").submit();
    });

    $('#fileUpdate').change(function () {
        $("#updateForm").submit();
    });

    $('.copyFile').on('click', function () {
        selectedBtn = $(this);
        $.ajax({
            url: '/fileCopy',
            type: 'post',
            data: {docId: selectedBtn.closest('.doc').attr('id')},
            dataType: 'json',
            beforeSend: function () {

            },
            success: function (data) {
                if (data.result == true) {
                    var copyDiv = $("<div class='doc' id='" + data.document.docId + "'>" +
                        "<p>" + data.document.name + "</p>" +
                        "<button><a href='/download?did=" + data.document.docId + "&dver=" + data.document.version + "'>下载</a></button>" +
                        "<form method='POST' action='/update?docId=" + data.document.docId + "' " +
                        "enctype='multipart/form-data' id='updateForm'>" +
                        "<input type='file' name='file' id='fileUpdate'/><br/><br/>" +
                        "</form>" +
                        "<a href='/historyVersion?docId="+data.document.docId+"'>查看历史版本</a>" +
                        "<a href='#' class='showModal btn btn-success'>重命名</a>" +
                        "<a href='#' class='copyFile'>创建副本</a>" +
                        "<input type='hidden' value='" + data.document.docId + "'/>" +
                        "<a href='#' class='deleteFile'>删除</a>" +
                        "<input type='hidden' value='" + data.document.docId + "'/>" +
                        "</div>");
                    $('.doc').last().after(copyDiv);
                    console.log("创建副本成功");
                } else {

                }
            },
            complete: function () {

            },
            error: function (err) {
                console.log(err);

            }
        })
    });

</script>
<script type="text/javascript">
    var selectedBtn;
    var modal = new RModal(document.getElementById('modal'), {
        beforeOpen: function (next) {
            console.log('beforeOpen');
            if(selectedBtn.attr('id')=='newFolder'){
                $('#rename').val('');
            }
            next();
        }
        , afterOpen: function () {
            console.log('opened');
        }
        , beforeClose: function (next) {
            console.log('beforeClose');
            next();
        }
        , afterClose: function () {
            //若点击重命名
            if(selectedBtn.attr('id')!='newFolder'){
                $.ajax({
                    url: '/fileRename',
                    type: 'post',
                    data: {
                        newName: $("#rename").val(),
                        docId: selectedBtn.closest('.doc').attr('id'),
                        oldName: selectedBtn.closest('.doc').find('.docName').html(),
                    },
                    dataType: 'json',
                    beforeSend: function () {

                    },
                    success: function (data) {
                        if (data.result == true) {
                            selectedBtn.closest('.doc').find('.docName').html(data.name);
                            console.log("重命名成功");
                        } else {

                        }
                    },
                    complete: function () {

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
                        folderName: $("#rename").val(),
                        docId: selectedBtn.closest('.doc').attr('id'),
                        parentId:0
                    },
                    dataType: 'json',
                    beforeSend: function () {

                    },
                    success: function (data) {
                        if (data.result == 1) {
                            var newFolderDiv = $("<div class='doc' id='" + data.document.docId + "'>" +
                                "<p>" + data.document.name + "</p>" +
                                "<input type='hidden' value='" + data.document.docId + "'/>" +
                                "<a href='#' class='to_single_folder btn btn-success'>进入文件夹</a>" +
                                "<a href='#' class='showModal btn btn-success'>重命名</a>" +
                                "<a href='#' class='deleteFile'>删除</a>" +
                                "</div>");
                            $('.doc').last().after(newFolderDiv);
                            console.log("创建成功");
                        } else {

                        }
                    },
                    complete: function () {

                    },
                    error: function (err) {
                        console.log(err);

                    }
                });
                console.log('closed');
            }
        }
    });
    document.addEventListener('keydown', function (ev) {
        modal.keydown(ev);
    }, false);
    $('.showModal').on("click", function () {
        selectedBtn = $(this);
        modal.open();
    });
    window.modal = modal;
</script>
</body>
</html>