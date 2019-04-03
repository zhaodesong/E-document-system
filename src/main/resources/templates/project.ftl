<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title></title>
</head>
<body>
    <#--<#include "account.ftl" encoding="UTF-8" parse=true>-->
    <p style="color:red;" >${msg!}</p>
    <#if documents?size=0>
        <p>项目中还没有文件</p>
    <#else>
        <#list documents! as doc>
            <p>
                <p>${doc.name}</p>
                <button><a href="/download?did=${doc.docId}&dver=${doc.version}">下载</a></button>
                <#--<button><a>更新</a></button>-->
                <#--<button><a>删除</a></button>-->
                <#--<a href="/toProject?pid=${p.id}">${p.name!}</a>-->
                <#--&lt;#&ndash;如果有权限，显示删除，否则显示退出&ndash;&gt;-->
                <#--<a href="/deleteProject?pid=${p.id}">删除该项目</a>-->
            </p>
        </#list>
    </#if>
    <br>
    <form method="POST" action="/upload" enctype="multipart/form-data">
        <input type="file" name="file" /><br/><br/>
        <input type="submit" value="上传" />
    </form>
</body>
</html>