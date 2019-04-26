<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="/css/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/css/common.css">
    <script src="/js/jquery.min.js"></script>
    <script src="/js/semantic.min.js"></script>
</head>
<body>
<#include "common/header.ftl" parse=true encoding='utf-8'>
<div class="ui container">
    <#list documentList! as doc>
        <div class="doc content" id="${doc.docId}">
            <p class="docName">${doc.name!}</p>
            <p>时间：
                <#list doc.createTime?split("T") as name>
                    ${name} &nbsp;
                </#list>
            </p>
            <#--下载-->
            <button class="ui button"><a href="/download?did=${doc.docId}&dver=${doc.version}">下载</a></button>
        </div>
        </br>
    </#list>
</div>
</body>
</html>