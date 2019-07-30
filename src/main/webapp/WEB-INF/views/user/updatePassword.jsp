<%--
  Created by IntelliJ IDEA.
  User: xuexiaowei
  Date: 2019/7/28
  Time: 16:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html class="x-admin-sm">
<head>
    <title>修改密码</title>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <%--引入css--%>
    <link rel="stylesheet" href="${basePath}/css/font.css">
    <link rel="stylesheet" href="${basePath}/css/xadmin.css">
    <%--引入js--%>
    <script type="text/javascript" src="${basePath}/layui/layui/layui.js"></script>
    <script type="text/javascript" src="${basePath}/js/xadmin.js"></script>
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row">
        <form class="layui-form">
            <div class="layui-form-item">
                <label for="old_pwd" class="layui-form-label">
                    <span class="x-red">*</span>原密码
                </label>
                <div class="layui-input-inline">
                    <input type="hidden" id="userId" value="${id}">
                    <input type="password" id="old_pwd" name="old_pwd" value="" placeholder="请输入原密码" lay-verify="required"
                           autocomplete="off" class="layui-input">
                </div>

            </div>
            <div class="layui-form-item">
                <label for="new_pwd" class="layui-form-label">
                    <span class="x-red">*</span>新密码
                </label>
                <div class="layui-input-inline">
                    <input type="password" id="new_pwd" name="new_pwd" placeholder="请输入新密码" lay-verify="new_pwd"
                           autocomplete="off" class="layui-input">
                </div>
                <div class="layui-form-mid layui-word-aux">
                    3到20个字符
                </div>
            </div>
            <div class="layui-form-item">
                <label for="re_new_pwd" class="layui-form-label">
                    <span class="x-red">*</span> 确认新密码
                </label>
                <div class="layui-input-inline">
                    <input type="password" id="re_new_pwd" name="re_new_pwd" placeholder="请再次输入新密码" lay-verify="re_new_pwd"
                           autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <button class="layui-btn" type="button" lay-submit lay-filter="save">保存</button>
                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
            </div>
        </form>
    </div>
</div>
<script>
    layui.use(['form', 'layer'], function() {
        $ = layui.jquery;
        var form = layui.form,
            layer = layui.layer;

        //自定义验证规则
        form.verify({
            new_pwd: [/(.+){3,20}$/, '密码必须3到20位'],
            re_new_pwd: function(value) {
                if ($('#new_pwd').val() != $('#re_new_pwd').val()) {
                    return '两次密码不一致';
                }
            }
        });

        form.on('submit(save)', function(data) {
            var userId = $('#userId').val();
            var oldPwd = $('#old_pwd').val();
            var newPwd = $('#re_new_pwd').val();
            var data = {id:userId,password:oldPwd}
            /**验证原密码是否正确*/
            $.ajax({
                url:'${basePath}/sys/user/checkOldPwd',
                type:'post',
                contentType:'application/json',
                data:JSON.stringify(data),
                dataType:'json',
                success:function (res) {
                    if(res.code == 200 && res.data == true){
                        updateNewPassWord(userId,newPwd);
                    } else {
                        layer.msg(res.msg,{icon:5});
                        $('#old_pwd').val('');
                        $('#new_pwd').val('');
                        $('#re_new_pwd').val('');
                        $('#old_pwd').focus();
                    }
                },
                error:function (e) {
                    console.log(e);
                    layer.msg('请求异常!',{icon:5});
                }
            });
            return false;//阻止表单跳转
        });

    });

    /**提交新密码修改*/
    function updateNewPassWord(userId,newPwd) {
        var data = {id:userId,newPwd:newPwd};
        $.ajax({
            url:'${basePath}/sys/user/saveNewPwd',
            type:'post',
            contentType:'application/json',
            data:JSON.stringify(data),
            dataType:'json',
            success:function (res) {
                if(res.code == 200){
                    layer.msg(res.msg,{icon:6},
                    function () {
                        //关闭当前frame
                        xadmin.close();
                        // 可以对父窗口进行刷新
                        xadmin.father_reload();
                    });
                } else {
                    layer.msg(res.msg,{icon:5});
                    $('#old_pwd').val('');
                    $('#new_pwd').val('');
                    $('#re_new_pwd').val('');
                }
            },
            error:function (e) {
                console.log(e);
                layer.msg('请求异常!',{icon:5});
            }
        });
    }

</script>

</body>
</html>