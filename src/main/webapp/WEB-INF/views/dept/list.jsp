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

<form class="layui-form">
    <div id="xtree" class="eleTree" lay-filter="treeFilter" style="width:400px;border:1px solid black;padding: 10px 0 25px 5px;"></div>
</form>
<script type="text/javascript">
    layui.use(['tree'],function () {
        var tree = layui.tree;
        tree.render({
                elem:'#xtree',
               // data: [],    //直接赋值数据
                emptText: "暂无数据", // 内容为空的时候展示的文本
                method: "get",    // 接口http请求类型，默认：get
                url: "${basePath}/sys/dept/tree",      // 异步数据接口
                where:"",   //接口的其它参数。如：where: {token: 'sasasas', id: 123}
                contentType:"",  //发送到服务端的内容编码类型。如果你要发送 json 内容，可以设置：contentType: 'application/json'
                checkStrictly: false,       // 在显示复选框的情况下，是否严格的遵循父子不互相关联的做法，默认为 false
                defaultCheckedKeys: [],     // 默认勾选的节点的 key 的数组
                accordion: true,           // 是否每次只打开一个同级树节点展开（手风琴效果）
                indent: 20,                 // 相邻级节点间的水平缩进，单位为像素
                draggable: true,           // 是否开启拖拽节点功能
                contextmenuList: ["copy","add","edit","remove"],    // 启用右键菜单，支持的操作有："copy","add","edit","remove"
                expandOnClickNode: true,    // 是否在点击节点的时候展开或者收缩节点， 默认值为 true，如果为 false，则只有点箭头图标的时候才会展开或者收缩节点。
                defaultExpandAll: true,    // 是否默认展开所有节点
                renderAfterExpand: true,    // 是否在第一次展开某个树节点后才渲染其子节点
                highlightCurrent: true,    // 是否高亮当前选中节点，默认值是 false。
                showLine:true,  //是否显示连线，默认false
                request: {      // 对后台返回的数据格式重新定义
                    name: "label",
                    key: "id",
                    children: "children",
                    checked: "checked",
                    disabled: "disabled",
                    isLeaf: "isLeaf"
                },
                response: {   // 对于后台数据重新定义名字
                    statusName: "code",
                    statusCode: 200,
                    dataName: "data"
                },
                done: function(res){
                    return{
                        "statusCode": res.code, //解析接口状态
                        "data": res.data //解析数据
                    };
                },
                searchNodeMethod: null    // 对树节点进行筛选时执行的方法，返回 true 表示这个节点可以显示，返回 false 则表示这个节点会被隐藏
            }
        );
    })
</script>

</body>
</html>
