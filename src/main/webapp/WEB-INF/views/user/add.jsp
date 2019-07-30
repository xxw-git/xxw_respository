<%--
  Created by IntelliJ IDEA.
  User: 84657
  Date: 2019/7/26
  Time: 16:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html class="x-admin-sm">
<head>
    <title>新增用户</title>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <%--引入css--%>
    <link rel="stylesheet" href="${basePath}/css/font.css">
    <link rel="stylesheet" href="${basePath}/css/xadmin.css">
    <%--引入js--%>
    <script type="text/javascript" src="${basePath}/layui/layui/layui.js"></script>
    <script type="text/javascript" src="${basePath}/js/xadmin.js"></script>
    <script type="text/javascript" src="${basePath}/js/sys-user/user-add.js"></script>
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <c:set var="_basePath" value="${basePath}" scope="session" />
    <script type="text/javascript">
        var _basePath = "${_basePath}";
    </script>
</head>
<body onload="onLoad()">
<div class="layui-fluid">
    <div class="layui-row">
        <form class="layui-form">
            <div class="layui-form-item">
                <label for="loginAccount" class="layui-form-label">
                    <span class="x-red">*</span>登录账号
                </label>
                <div class="layui-input-inline">
                    <input type="text" id="loginAccount" name="loginAccount" placeholder="请输入登录账号" lay-verify="account"
                         value=""  autocomplete="off" class="layui-input"/>
                </div>
                <label for="userName" class="layui-form-label">
                    <span class="x-red">*</span>真实姓名
                </label>
                <div class="layui-input-inline">
                    <input type="text" id="userName" name="userName" value="" placeholder="请输入姓名" lay-verify="required"
                           autocomplete="off" class="layui-input"/>
                </div>
            </div>
            <div class="layui-form-item">
                <label for="L_pass" class="layui-form-label">
                    <span class="x-red">*</span>密码
                </label>
                <div class="layui-input-inline">
                    <input type="password" id="L_pass" name="pass" value="" placeholder="请输入密码" lay-verify="pass"
                           autocomplete="off" class="layui-input">
                </div>
                <label for="L_repass" class="layui-form-label">
                    <span class="x-red">*</span> 确认密码
                </label>
                <div class="layui-input-inline">
                    <input type="password" id="L_repass" name="password" placeholder="请再次输入密码" lay-verify="repass"
                           autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label for="sfzh" class="layui-form-label"><span class="x-red">*</span>身份证号码</label>
                <div class="layui-input-inline">
                    <input type="text" id="sfzh" name="sfzh" value="" placeholder="请输入身份证号码" lay-verify="required|identity"
                           autocomplete="off" class="layui-input">
                </div>
                <label for="lxfs" class="layui-form-label">手机号码</label>
                <div class="layui-input-inline">
                    <input type="text" id="lxfs" name="lxfs" value="" placeholder="请输入手机号" lay-verify="phone"
                           autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label for="email" class="layui-form-label">邮箱</label>
                <div class="layui-input-inline">
                    <input type="text" id="email" name="email" value="" placeholder="请输入邮箱"
                           autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item" >
                <label class="layui-form-label"><span class="x-red">*</span>所属角色</label>
                <div class="layui-input-block" id="roles"></div>
            </div>
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">备注</label>
                <div class="layui-input-block">
                    <textarea placeholder="请输入内容" class="layui-textarea" name="remark"></textarea>
                </div>
            </div>
            <div class="layui-form-item">
                <button class="layui-btn" type="button" lay-submit lay-filter="add">保存</button>
                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
            </div>
        </form>
    </div>
</div>

</body>
</html>
