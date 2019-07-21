<%--
  Created by IntelliJ IDEA.
  User: xuexiaowei
  Date: 2019/7/11
  Time: 15:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE >
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="icon" href="<%=request.getContextPath()%>/images/favicon.ico" type="image/x-icon" />
    <title>登录页面</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/fonts/font-awesome-4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/util.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css">
    <%--引入js--%>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/common/jquery/jquery1.8.3.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/common/layer/layer.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jsencrypt.min.js"></script>
</head>

<body>
<div class="limiter">
    <div class="container-login100">
        <div class="wrap-login100">
            <div class="login100-form-title" style="background-image: url(<%=request.getContextPath()%>/images/login/bg-01.jpg);">
                <span class="login100-form-title-1">登 录</span>
            </div>

            <form class="login100-form validate-form" id="loginForm" action="" method="post">
                <div class="wrap-input100 validate-input m-b-26" data-validate="用户名不能为空">
                    <span class="label-input100">用户名</span>
                    <input id="loginAccount" class="input100" type="text" name="loginAccount" placeholder="请输入登录账号">
                    <span class="focus-input100"></span>
                </div>

                <div class="wrap-input100 validate-input m-b-18" data-validate="密码不能为空">
                    <span class="label-input100">密码</span>
                    <input id="password" class="input100" type="password" name="password" placeholder="请输入密码">
                    <span class="focus-input100"></span>
                </div>

                <div class="flex-sb-m w-full p-b-30">
                    <div class="contact100-form-checkbox">
                        <input class="input-checkbox100" id="rememberMe" type="checkbox" name="rememberMe">
                        <label class="label-checkbox100" for="rememberMe">记住我</label>
                    </div>

                    <%--<div>
                        <a href="javascript:" class="txt1">忘记密码？</a>
                    </div>--%>
                </div>

                <div class="container-login100-form-btn">
                    <button class="login100-form-btn" type="button" id="login">登 录</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script >
    jQuery(document).ready(function() {
        /*try{
            var _href = window.location.href+"";
            if(_href && _href.indexOf('?kickout')!=-1){
                layer.msg('您已经被踢出，请重新登录！');
            }
        }catch(e){

        }*/
    });
    //回车事件绑定
    document.onkeydown=function(event){
        var e = event || window.event || arguments.callee.caller.arguments[0];
        if(e && e.keyCode==13){
            $('#login').click();
        }
    };

    //登录操作
    $('#login').click(function(){
        var username = $('#loginAccount').val();
        var password = $('#password').val();
        if(username == null || username == '' || typeof (username) == "undefined") {
            layer.alert('登录账号不能为空！', {icon: 6});
            return false;
        }
        if(password == null || password == '' || typeof (password) == "undefined") {
            layer.alert("密码不能为空！", {icon: 6});
            return false;
        }
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbyc5J5a6HiDgWNQvSC5eJ/s/T1nTNwwkW4QzT" +
            "ywBZKdn1/uNzATu7HxTop/G+VMkln5sopa0n3bBMNwd2QkePib19A9PgQn8FHs5rcM+C3jOUunNy" +
            "VMRuqcsVV2mcDfRx9l68T/q/Xtg8pjQ5sDYed0cQ9C0pffzWtx3SAT8zcwIDAQAB");
        var enpwd = encrypt.encrypt(password);
        var pwd = encodeURI(enpwd).replace(/\+/g, '%2B');
        var data = {username:username,password:pwd,rememberMe:$("#rememberMe").is(':checked')};
        var load = layer.load();

        $.ajax({
            url:"/web/submitLogin",
            data:data,
            type:"post",
            dataType:"json",
            beforeSend:function(){
                layer.msg('开始登录...');
            },
            success:function(result){
                layer.close(load);
                if(result && result.code == 200){
                    layer.msg(result.msg);
                    setTimeout(function(){
                        window.location.href= "/web/index";
                    },1000)
                }else{
                    layer.alert(result.msg, {icon: 5});
                    $('#password').val('');
                    return;
                }
            },
            error:function(e){
                layer.close(load);
                console.log("登录失败")
                console.log(e,e.message);
                layer.msg('登录异常！',new Function());
            }
        });
    });

</script>
</body>

</html>
