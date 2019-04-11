<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>流云文档-欢迎使用</title>
    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <link rel="stylesheet" href="css/animate.min.css" type="text/css" />
    <link rel="stylesheet" href="css/rmodal.css" type="text/css" />
<#--    <link rel="stylesheet" href="css/htmleaf-demo.css" type="text/css" />-->
    <link rel="stylesheet" type="text/css" href="/css/semantic.min.css">
    <script src="/js/jquery.min.js"></script>
    <script src="/js/semantic.min.js"></script>
    <style>
        .projectshow{
            border:1px solid #fff;
            box-shadow: 2px 2px;
            margin-right: 20px;
            height: 50px;width: 50px;
        }
    </style>
</head>
<body>
<div class="ui inverted vertical masthead center aligned segment" style="height: 100%;">

    <div class="ui container" style="margin-bottom: 40px;">
        <div class="ui large secondary inverted pointing menu">
            <a class="item" href="/">
               流云文档
            </a>

            <div class="right item" style="float: right;color:white;">
                <span>欢迎您，${nickName!}</span>
            </div>
        </div>
    </div>

    <div class="ui text container">
            <div class="projectlist ui divided items">
                <#if project?? && (project?size > 0)>
                    <#list project! as p>
                        <div class="ui card projectDiv">
                            <a class="" href="/toProject?pid=${p.id}">
                                ${p.name!}
                            </a>
                            <div class="content">
                                <div class="meta">
                                    <a>删除</a>
                                    <a>删除</a>
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
                    <p id="noitem">您尚未加入任何项目</p>
                </#if>
            </div>
            <div class="ui left" style="margin-top: 20px;text-align: left;margin-left: 30%;">
                <button  id="showModal" class="ui button">
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
        <div class="ui green ok inverted button">
            确定
        </div>
    </div>
</div>
<#--    <div id="modal" class="modal">-->
<#--        <div class="modal-dialog animated">-->
<#--            <div class="modal-content">-->
<#--                <form class="form-horizontal" method="get">-->
<#--                    <div class="modal-header">-->
<#--                        <strong>请输入新项目名称</strong>-->
<#--                    </div>-->

<#--                    <div class="modal-body">-->
<#--                        <input type="text" id="create"><script></script>-->
<#--                    </div>-->

<#--                    <div class="modal-footer">-->
<#--                        <a class="btn btn-primary" onclick="modal.close()">-->
<#--                            确定-->
<#--                        </a>-->
<#--                    </div>-->
<#--                </form>-->
<#--            </div>-->
<#--        </div>-->
<#--    </div>-->
<#--</div>-->

    
<#--    <div class="ui container">-->

<#--        <a href="fixed.php#" class="header item">-->
<#--            项目名-->
<#--        </a>-->
<#--        <a href="fixed.php#" class="item">主页</a>-->
<#--        <div class="ui simple dropdown item">-->
<#--            Dropdown <i class="dropdown icon"></i>-->
<#--            <div class="menu">-->
<#--                <a class="item" href="fixed.php#">链接选项</a>-->
<#--                <a class="item" href="fixed.php#">链接选项</a>-->
<#--                <div class="divider"></div>-->
<#--                <div class="header">标题项</div>-->
<#--                <div class="item">-->
<#--                    <i class="dropdown icon"></i>-->
<#--                    子菜单-->
<#--                    <div class="menu">-->
<#--                        <a class="item" href="fixed.php#">链接选项</a>-->
<#--                        <a class="item" href="fixed.php#">链接选项</a>-->
<#--                    </div>-->
<#--                </div>-->
<#--                <a class="item" href="fixed.php#">链接选项</a>-->
<#--            </div>-->
<#--        </div>-->
<#--    </div>-->




    <script type="text/javascript" src="js/rmodal.js"></script>
<script type="text/javascript">

    window.onload = function() {
        var modal = new RModal(document.getElementById('modal'), {
            beforeOpen: function(next) {
                console.log('beforeOpen');
                $('#create').val('');
                next();
            }
            , afterOpen: function() {
                console.log('opened');
            }
            , beforeClose: function(next) {
                console.log('beforeClose');
                next();
            }
            , afterClose: function() {
                $.ajax({
                    url: '/createProject',
                    type: 'post',
                    data: {projectName: $("#create").val()},
                    dataType: 'json',
                    beforeSend: function () {
                        // // 禁用按钮防止重复提交
                        // $("delFileBtn").attr({disabled: "disabled"});
                        // $("loading").show();
                    },
                    success: function (data) {
                        if (data.result == true) {
                            var newProjectDiv = $("<div class='projectDiv'>" +
                                "<a href='/toProject?pid=" + data.project.id+"'>"+data.project.name+"</a>" +
                                "<a href='/deleteProject?pid="+data.project.id+"'>删除该项目</a>" +
                                "</div>");
                            $('#noitem').remove();
                            $('.projectlist').append(newProjectDiv);
                            console.log("创建新项目成功");
                        } else {
                            // $("loading").show();
                            // document.getElementById("loading").innerHTML = "操作失败，请稍后重试";
                        }
                    },
                    complete: function () {
                        // $("delFileBtn").removeAttr("disabled");
                        // $("loading").hide();
                    },
                    error: function (err) {
                        console.log(err);
                        // $("loading").show();
                        // document.getElementById("loading").innerHTML = "网络故障，请稍后重试";
                    }
                });
                console.log('closed');
            }
        });

        document.addEventListener('keydown', function(ev) {
            modal.keydown(ev);
        }, false);

        document.getElementById('showModal').addEventListener("click", function(ev) {
            ev.preventDefault();
            modal.open();
        }, false);
        window.modal = modal;
    }
</script>
<script>
    $('#showModal').on('click',function () {
        $('.small.modal')
            .modal('show')
        ;
    })
</script>
</body>
</html>