<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        .doc{border:1px solid;}
    </style>
</head>
<body>
<#list documentList! as doc>
<div class="doc" id="${doc.docId}">
    <p class="docName">${doc.name}</p>
    <p>时间：${doc.createTime}</p>
    <#--下载-->
    <button><a href="/download?did=${doc.docId}&dver=${doc.version}">下载</a></button>
</#list>

</body>
</html>