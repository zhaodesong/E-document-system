<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="/css/semantic.min.css">
    <link rel="stylesheet" type="text/css" href="/css/icon.min.css">
    <script src="/js/jquery.min.js"></script>
    <script src="/js/semantic.min.js"></script>
</head>
<body>
<#include "common/header.ftl" parse=true encoding='utf-8'>
<div class="ui grid" style="margin-top: 10px;margin-bottom: 10px">
    <div class="two wide column" style="position: relative">
    </div>
    <div class="twelve wide column">
        <div class="ui container">
            <#list documentList! as doc>
                <div class="ui items">
                    <div class="content">
                        <button class="ui button" style="float: right">
                            <a href="/download?did=${doc.docId}&dver=${doc.version}">下载</a>
                        </button>
                        <div class="header">
                            <span>${doc.name!}</span>
                        </div>
                        <div class="meta">
                            <span>时间：
                            <#list doc.createTime?split("T") as name>
                                ${name} &nbsp;
                            </#list>
                            </span>
                        </div>
                    </div>
                </div>
                <div class="ui divider"></div>
            </#list>
        </div>
    </div>
    <div class="two wide column" style="position: relative">
    </div>
</body>
</html>