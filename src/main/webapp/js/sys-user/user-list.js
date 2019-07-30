var currentPage = "";

$(document).ready(function () {
    getData();
});

/**
 * 查询
 */
function search(){
    currentPage = 1;
    getData();
}
function getData(){
    layui.use(['table','laydate','form'], function(){
        var table = layui.table;
        var laydate = layui.laydate;
        //执行一个laydate实例
        laydate.render({
            elem: '#start' //指定元素
        });

        //执行一个laydate实例
        laydate.render({
            elem: '#end' //指定元素
        });
        var startTime = $("#start").val();
        var endTime = $("#end").val();
        var userName = $("#userName").val();

        table.render({
            id:'userId',
            elem: '#userTable',
            url: _basePath +'/sys/user/list',
            toolbar: '#userToolBar',
            title: '用户数据表',
            defaultToolbar:['filter', 'print', 'exports'],
            request: {
                pageName: 'currentPage', //页码的参数名称，默认：page
                limitName: 'limit' //每页数据量的参数名，默认：limit
            },
            method:'post',//接口http请求类型，默认:get
            contentType:'application/json',//发送到服务端的内容编码类型。如设置json格式类型application/json
            where:{startTime: startTime, endTime: endTime,userName:userName},//接口的其它参数。如：where:
            cols: [[
                {type: 'checkbox'},
                {type: 'numbers',width:70,title:'序号'},
                {hide:true,field:'id',title:'主键'},
                {field:'loginAccount', title:'账号',width:120,unresize: true},
                {field:'userName', title:'用户名',width:110, sort: true},
                {field:'sfzh', title:'身份证号', width:160, sort: true},
                {field:'lxfs', title:'手机号',width:110,sort: true},
                {field:'deptId', title:'所属部门',width:120, sort: true},
                {field:'status',title:'状态',width:90, toolbar: '#barStatus', sort: true},
                {title:'操作',minWidth:240, toolbar: '#user_Operate',align:'center'}
            ]]
            ,page: true//开启分页
            ,limit:10//默认一页显示十条数据
            ,limits:[10,20,30,50]
            ,text: {
                none: '暂无相关数据' //默认：无数据。注：该属性为 layui 2.2.5 开始新增
            },
            //重新规定返回的数据格式
            response:{
                statusCode:200,//自定义成功的状态码，默认：0
                countName: 'total' //规定数据总数的字段名称，默认：count
            },
            parseData:function (result) {//result 即为原始返回的数据
                return{
                    "code":result.code,//解析接口状态
                    "msg":result.msg,//解析提示文本
                    "total": result.total, //解析数据长度
                    "data": result.data //解析数据列表
                };
            }
        });

        //监听头部工具栏事件
        table.on('toolbar(list)', function(obj){
            //获取选中的行数
            var checkStatus = table.checkStatus(obj.config.id);
            var data = checkStatus.data;
            switch (obj.event) {
                case 'delAll':
                    if(data == null || data.length == 0){
                        layer.msg('请选择要删除的行!',{icon:2,time:1500});
                        return false;
                    }
                    var ids = [];
                    for (var i  in data) {
                        ids.push(data[i].id);
                    }
                    del(ids.join(","));
                    break;
            };
        });

        //监听行工具事件
        table.on('tool(list)', function(obj){
            var data = obj.data;
            switch (obj.event) {
                case 'del':
                    del(data.id);
                    break;
                case 'edit':
                    xadmin.open('编辑用户',_basePath+'/sys/user/edit/'+data.id,700,600);
                    break;
                case 'updatePassword':
                    xadmin.open('修改密码',_basePath+'/sys/user/updatePassword/'+data.id,700,500);
                    break;
            };
        });
    });
}

/**
 * 删除
 * @param id 主键
 */
function del(id) {
    layer.confirm('确定要删除吗?', {icon: 3, title:'提示'}, function(index){
        $.ajax({
            type:'get',
            url: _basePath+'/sys/user/delete/'+id,
            data:id,
            dataType:'json',
            success:function (res) {
                if(res.status == 200){
                    layer.msg('已删除!',{icon:1});
                } else {
                    layer.msg(res.msg,{icon:5});
                }
                search();
            },
            error:function (e) {
                console.log(e);
                layer.msg('请求异常!',{icon:5});
            }
        });
        layer.close(index);
    });
}
