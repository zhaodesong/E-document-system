<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>成员管理</title>
</head>
<body>
<#include "common/header.ftl" parse=true encoding='utf-8'>
<div>
    <div>
        <#list accountList! as a>
            <div>
                <p>
                    昵称：${a.nickName!}  邮箱：${a.mail}
                </p>
                <a href="deleteMember?deleteId=${a.id}" >删除该成员</a>
            </div>
        </#list>
    </div>

</div>
</body>
</html>