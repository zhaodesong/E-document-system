<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>流云文档-欢迎使用</title>
</head>
<body>
    <div>
        <div>
            <span>欢迎您，${nickName!}</span>
        </div>
        <#include "account.ftl" encoding="UTF-8" parse=true>
        <#if project?size=0>
            <p>您尚未加入任何项目</p>
        <#else>
            <#list project! as p>
                <p>
                    <a href="/project?pid=${p.id}">${p.name!}</a>
                    <#--如果有权限，显示删除，否则显示退出-->
                    <a href="/deleteProject?pid=${p.id}">删除该项目</a>
                </p>
            </#list>
        </#if>
        <br>
        <a href="/newProject">创建新项目</a>

    </div>
</body>
</html>