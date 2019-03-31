<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>流云文档-欢迎使用</title>
</head>
<body>
    <div>
        <div>
            <span>欢迎您，${nickName}</span>
        </div>
        <#if project?size=0>
            <p>您尚未加入任何项目</p>
        <#else>
            <#list project! as p>
                <a href="/project">${p.name}</a>
            </#list>
        </#if>
        <a href="/newProject?id=${accountId}">创建新项目</a>

    </div>
</body>
</html>