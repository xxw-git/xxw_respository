<%--
  Created by IntelliJ IDEA.
  User: xuexiaowei
  Date: 2019/8/1
  Time: 14:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>部门管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <%--引入css--%>
    <link rel="stylesheet" href="${basePath}/css/font.css">
    <link rel="stylesheet" href="${basePath}/css/xadmin.css">
    <%--ztree css--%>
    <link rel="stylesheet" href="${basePath}/js/zTree_v3/css/demo.css">
   <%-- <link rel="stylesheet" href="${basePath}/js/zTree_v3/css/zTreeStyle/zTreeStyle.css">--%>
    <link rel="stylesheet" href="${basePath}/js/zTree_v3/css/metroStyle/metroStyle.css">

    <%--引入js--%>
    <script type="text/javascript" src="${basePath}/js/common/jquery/jquery1.8.3.min.js"></script>
    <script type="text/javascript" src="${basePath}/layui/layui/layui.js"></script>
    <script type="text/javascript" src="${basePath}/js/xadmin.js"></script>

    <%-- ztree js--%>
    <%--<script src="${basePath}/js/zTree_v3/js/jquery-1.4.4.min.js"></script>--%>
    <script src="${basePath}/js/zTree_v3/js/jquery.ztree.core.js"></script>
    <script src="${basePath}/js/zTree_v3/js/jquery.ztree.excheck.js"></script>
    <script src="${basePath}/js/zTree_v3/js/jquery.ztree.exedit.js"></script>

    <script type="text/javascript" src="${basePath}/js/sys-dept/dept.js"></script>

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
                <a><cite>部门管理</cite></a>
            </span>
    <a class="layui-btn layui-btn-small" style="line-height:1.6em;margin-top:3px;float:right" onclick="location.reload()" title="刷新">
        <i class="layui-icon layui-icon-refresh" style="line-height:30px"></i>
    </a>
</div>
<div class="layui-fluid" style="background: #fff;">
    <div class="layui-row">
        <div class="layui-col-md3">
            <div id="layer-zTree"  >
                <div>
                    <ul id="ztree" class="ztree" style="width: 75%;height: 80%;"></ul>
                </div>
            </div>
        </div>
        <div class="layui-col-md9">
            <fieldset class="layui-elem-field">
                <legend>部门属性</legend>
                <hr class="layui-bg-gray">
                <div class="layui-field-box">
                    <div class="layui-row">
                        <div class="layui-col-xs4">
                            <label class="layui-form-label" style="font-size: 16px;text-align: left;padding: 5px;">部门名称：</label>
                            <div class="layui-form-label" style="font-size: 16px;padding: 5px;width: 120px;">
                                郑州市公安局
                            </div>
                        </div>
                        <div class="layui-col-xs4">
                            <label class="layui-form-label" style="font-size: 16px;text-align: left;padding: 5px;">部门全称：</label>
                            <div class="layui-form-label" style="font-size: 16px;padding: 5px;width: 120px;">
                                郑州市公安局
                            </div>
                        </div>
                        <div class="layui-col-xs4">
                            <label class="layui-form-label" style="font-size: 16px;text-align: left;padding: 5px;">上级部门：</label>
                            <div class="layui-form-label" style="font-size: 16px;padding: 5px;width: 120px;">
                                河南省公安厅
                            </div>
                        </div>
                    </div>
                </div>
            </fieldset>

            <fieldset class="layui-elem-field" style="margin-top: 50px;">
                <legend> 部门成员</legend>
                <hr class="layui-bg-gray">
                <div class="layui-field-box">
                    <table id="deptTable" class="layui-table" lay-filter="list"></table>
                </div>
            </fieldset>

            <div class="layui-card-body">
            </div>

        </div>
    </div>
</div>
</body>
</html>
