<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: xuexiaowei
  Date: 2019/7/20
  Time: 9:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>用户管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <%--引入css--%>
    <link rel="stylesheet" href="${basePath}/css/font.css">
    <link rel="stylesheet" href="${basePath}/css/xadmin.css">
    <%--引入js--%>
    <script type="text/javascript" src="${basePath}/js/common/jquery/jquery1.8.3.min.js"></script>
    <script type="text/javascript" src="${basePath}/layui/layui/layui.js"></script>
    <script type="text/javascript" src="${basePath}/js/xadmin.js"></script>
    <script type="text/javascript" src="${basePath}/js/sys-user/user-list.js"></script>
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <c:set var="_basePath" value="${basePath}" scope="session" />
    <script type="text/javascript">
        var _basePath = "${_basePath}";
    </script>
</head>
<body>
<div class="x-nav">
            <span class="layui-breadcrumb">
                <a href="">首页</a>
                <a href="">系统管理</a>
                <a><cite>用户管理</cite></a>
            </span>
    <a class="layui-btn layui-btn-small" style="line-height:1.6em;margin-top:3px;float:right" onclick="location.reload()" title="刷新">
        <i class="layui-icon layui-icon-refresh" style="line-height:30px"></i>
    </a>
</div>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body ">
                    <form class="layui-form layui-col-space5">
                        <div class="layui-inline layui-show-xs-block">
                            <input class="layui-input" autocomplete="off" placeholder="开始日" name="start" id="start"></div>
                        <div class="layui-inline layui-show-xs-block">
                            <input class="layui-input" autocomplete="off" placeholder="截止日" name="end" id="end"></div>
                        <div class="layui-inline layui-show-xs-block">
                            <input type="text" id="userName" name="userName" placeholder="请输入用户名" autocomplete="off" class="layui-input"></div>
                        <div class="layui-inline layui-show-xs-block">
                            <button class="layui-btn" lay-submit="" type="button" onclick="search()" lay-filter="sreach">
                                <i class="layui-icon">&#xe615;</i></button>
                        </div>
                    </form>
                </div>

                <div class="layui-card-body">
                    <table id="userTable" class="layui-table" lay-filter="list"></table>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/html" id="userToolBar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-danger" lay-event="delAll" ><i class="layui-icon"></i>批量删除</button>
        <button class="layui-btn" type="button" onclick="xadmin.open('添加用户','${basePath}/sys/user/add',700,600)"><i class="layui-icon"></i>添加</button>
    </div>
</script>
<script type="text/html" id="user_Operate">
    <a class="layui-btn layui-btn-normal" lay-event="updatePassword">重置密码</a>
    <a class="layui-btn layui-btn-normal" lay-event="view">查看</a>
    <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
</script>
<script type="text/html" id="barStatus">
    {{#  if(d.status == 1){ }}
    <div><span class="layui-btn layui-btn-normal layui-btn-mini">正常</span></div>
    {{#  } }}
    {{#  if(d.status == 2){ }}
    <div><span class="layui-btn layui-btn-disabled">冻结</span></div>
    {{#  } }}
    {{#  if(d.status == 3){ }}
    <div><span class="layui-btn layui-btn-danger layui-btn-mini">删除</span></div>
    {{#  } }}
</script>
<script>

</script>
</body>
</html>
