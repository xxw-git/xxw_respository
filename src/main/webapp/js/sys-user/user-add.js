function onLoad(){
    layui.use('form',function () {
        $ = layui.jquery;
        var form = layui.form;
        var html = "";
        $.ajax({
            type:'get',
            url: _basePath +'/sys/user/role',
            dataType:'json',
            success:function (res) {
                if(res.code == 200){
                    var list = res.data;
                    $.each(list,function (index,item) {
                        html+='<input type="checkbox" title="'+ item.name +'" name="roleNames" value="'+ item.id +'"/>';
                    })
                    $('#roles').append(html);
                    form.render('checkbox');
                } else {
                    layer.msg('请求异常!',{icon:5});
                }
            },
            error:function (e) {
                console.log(e);
                layer.msg('请求异常!',{icon:5});
            }
        });
    });
}

layui.use(['form', 'layer'], function() {
    $ = layui.jquery;
    var form = layui.form,
        layer = layui.layer;

    //自定义验证规则
    form.verify({
        account: function(value) {
            if (value.length < 3) {
                return '登录账号至少3个字符';
            }
        },
        pass: [/(.+){3,20}$/, '密码必须3到20位'],
        repass: function(value) {
            if ($('#L_pass').val() != $('#L_repass').val()) {
                return '两次密码不一致';
            }
        }
    });

    //监听提交
    form.on('submit(add)', function(data) {

        if ($("input:checkbox[name='roleNames']:checked").length == 0) {
            layer.msg('请选择用户所属角色',{icon:2});
            return;
        }
        var arr = [];
        $("input:checkbox[name='roleNames']:checked").each(function(i){
            arr[i] = $(this).val();
        });
        data.field.roleIds = arr.join(",");
        console.log(data.field);
        $.ajax({
            url:_basePath +'/sys/user/save',
            type:'post',
            contentType:'application/json',
            data:JSON.stringify(data.field),
            dataType:'json',
            success:function (res) {
                if(res.code == 200){
                    layer.msg(res.msg,{icon:6},
                        function() {
                            //关闭当前frame
                            xadmin.close();
                            // 可以对父窗口进行刷新
                            xadmin.father_reload();
                        });
                } else {
                    layer.msg(res.msg,{icon:5});
                }
            },
            error:function (e) {
                console.log(e);
                layer.msg('请求异常!',{icon:5,time:1000});//时间默认是3秒
            }
        });
        return false;//阻止表单跳转
    });

});
