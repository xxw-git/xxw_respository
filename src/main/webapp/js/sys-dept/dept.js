$(document).ready(function () {
    onLoadZTree();
});
var setting = {
    //视图显示样式
    view : {
        dblClickExpand: true, //双击节点时，是否自动展开父节点的标识 默认true
        showLine: true,   //设置 zTree 是否显示节点之间的连线。默认true
        selectedMulti: true, //设置是否允许同时选中多个节点。默认true
        addHoverDom: addHoverDom, //当鼠标移动到节点上时，显示按钮
        removeHoverDom: removeHoverDom, //隐藏按钮
        showIcon: true,//是否显示节点图标。默认true
    },

    //编辑
    edit: {
        enable: true,   //设置 zTree 是否处于编辑状态 默认false
        editNameSelectAll: true,  //点击编辑的时候，里面的文本全部选中
        removeTitle: '删除节点', //删除按钮的名称
        renameTitle: '重命名', //编辑名称按钮
        drag: {               // 拖拽
            autoExpandTrigger: true,//拖拽时父节点自动展开是否触发onExpand 事件回调函数。
            prev: dropPrev, //拖拽到目标节点时，设置是否允许移动到目标节点前面的操作
            inner: dropInner, //拖拽到目标节点时，设置是否允许成为目标节点的子节点。
            next: dropNext //拖拽到目标节点时，设置是否允许移动到目标节点后面的操作。
        }
    },

    check : {
        enable : true,
        chkStyle : "checkbox",
        chkboxType : {
            "Y" : "ps",
            "N" : "s"
        }
    },

    //采用的数据格式
    data: {
        simpleData: {
            enable: true,  //是否采用简单数据模式 (Array)
            idKey: "id",   //指定子节点的元素名称
            pIdKey: "pId",  //指定父节点的元素名称
            rootPId: 0,  //用于修正根节点父节点数据
        }
    },

    //回调函数
    callback: {
        beforeRemove: beforeRemove, //点击删除时触发，用来提示用户是否确定删除
        beforeEditName: beforeEditName, //点击编辑时触发，用来判断该节点是否能编辑,是否进入编辑状态
        beforeRename: beforeRename, //编辑结束时触发，用来验证输入的数据是否符合要求
        onRemove : onRemove,   //删除节点后触发，用户后台操作
        onRename : onRename,   //编辑后触发，用于操作后台
        beforeDrag: beforeDrag, //用于捕获节点被拖拽之前的事件回调函数，并且根据返回值确定是否允许开启拖拽操作
        beforeDrop:beforeDrop, //用于捕获节点拖拽操作结束之前的事件回调函数，并且根据返回值确定是否允许此拖拽操作
        beforeDragOpen: beforeDragOpen,//用于捕获拖拽节点移动到折叠状态的父节点后，即将自动展开该父节点之前的事件回调函数，并且根据返回值确定是否允许自动展开操作
        onDrag: onDrag,//用于捕获节点被拖拽的事件回调函数
        onDrop: onDrop,//用于捕获节点拖拽操作结束的事件回调函数
        onClick: zTreeOnClick,//单击事件回调函数
    }
};

/* 加载数据 */
function onLoadZTree(){
    var treeNodes = "";
    $.ajax({
        async: false,  //是否异步
        cache: false,  //是否使用缓存
        type: 'get',  //请求方式
        url: _basePath +'/sys/dept/tree', //请求地址
        dataType:'json',
        success: function(res) {
            if(res && res.code == 200){
                treeNodes = res.data; //把后台封装好的简单的JSON格式赋给treeNodes
            } else {
                layer.msg(res.msg, {icon: 5,time:1000});
            }
        },
        error:function(e){
            console.log(e,e.message);
            layer.msg('服务器异常！', {icon: 5,time:1000});
        }
    });
    var t = $("#ztree");
    t = $.fn.zTree.init(t, setting, treeNodes);  //zTree 初始化方法
    t.expandAll(true);  //展开 / 折叠 全部节点
}

/* 新增  */
function addHoverDom(treeId, treeNode) {
    var sObj = $("#" + treeNode.tId + "_span");//获取节点信息
    if(treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
    var addStr = "<span class='button add' id='addBtn_" + treeNode.tId +
        "' title='新增' 'this.blur();'></span>";
    sObj.after(addStr);
    var btn = $("#addBtn_" + treeNode.tId);

    layui.use(['layer', 'form'], function(){
        var layer = layui.layer,
            form = layui.form;

        if(btn) btn.bind("click", function() {
            var zTree = $.fn.zTree.getZTreeObj("ztree");
            layer.prompt({
                formType: 0,
                value: '',
                placeholder: '请输入部门名称',
                title: '请输入部门名称'
            }, function (value, index){
                if(value.trim().length === 0) {//非空验证
                    return false;
                }
                if($('#full_name').val()===""){
                    layer.tips("请填写部门全称",$('#full_name'));
                    return;
                }
                layer.close(index)
                var pid = treeNode.id;
                var name = value;
                //var grup = $('#guopp input[name="zuzu"]:checked').val();
                var deptFullName = $('#full_name').val();
                $.ajax({
                    type: "POST",
                    async: false,
                    url: _basePath +"/sys/dept/save",
                    data:{"pId": pid,"name": name,"fullName": deptFullName},
                    success: function(data) {
                        if(data.code == 200) {
                            layer.msg('添加成功', {icon: 1, time: 1000});
                            zTree.addNodes(treeNode, {pId: treeNode.id, name: value});
                            onLoadZTree(); //重新加载，不然再次添加会报错
                        } else {
                            layer.msg(data.msg, {icon: 5, time: 1000});
                            onLoadZTree();
                        }
                    }
                });
            });
            $(".layui-layer-content").append("<br/><input type=\"text\" id= \"full_name\" class=\"layui-input\" placeholder=\"输入部门全称\" />")

        });
    }); /* layui */
}

//移除鼠标隐藏按钮
function removeHoverDom(treeId, treeNode) {
    $("#addBtn_" + treeNode.tId).unbind().remove();
}

/*
 * 编辑
*/
function beforeEditName(treeId, treeNode) {
    return true;
}

/**
 * 重命名
 */
function beforeRename(treeId, treeNode, newName, isCancel) {
    if(newName.length < 3){
        layer.msg('部门名称不能少于3个字符！',{icon:5,time:2000});
        return false;
    }
    return true;
}

/* 编辑后触发，后台操作 */
function onRename(event, treeId, treeNode, isCancel){
    var typeId = treeNode.id;
    var typeName = treeNode.name;
    layer.confirm('您确认修改部门名称？', {icon: 3, title:'提示'}, function(index){
        $.ajax({
            type: "POST",
            async: false,
            url: _basePath +"/sys/dept/save",
            data:{"id": typeId,"name": typeName},
            success: function(data) {
                if(data.code == 200) {
                    layer.msg('修改成功', {icon: 1, time: 1000});
                    onLoadZTree();
                } else {
                    layer.msg(data.msg, {icon: 5, time: 1000});
                    onLoadZTree();
                    return false;
                }
            },
            error:function (e) {
                console.log(e);
                layer.msg('请求异常!',{icon:5});
            }
        });
        layer.close(index);
    });
}

/*
* 删除
*/
function beforeRemove(treeId, treeNode) {
    layer.confirm('确认要删除吗？', {icon: 3, title:'提示'}, function(index){
        if(treeNode.children.length>0){
            layer.msg('该节点下还有子节点，请从子节点开始删除！',{icon:5,time:3000});
            onLoadZTree();
            return false;
        }
        onRemove(event, treeId, treeNode);
        layer.close(index);
    });
    return false;
}

/* 删除后触发，后台操作  */
function onRemove(event, treeId, treeNode) {
    var id = treeNode.id;
    $.post(_basePath +"/sys/dept/delete/" + id,function (data){
        if(data.code == 200){
            layer.msg('删除成功',{icon:1});
            onLoadZTree();	//重新加载
        } else{
            layer.msg('删除失败',{icon:5});
        }
    },'json')
}

/** 实现节点拖拽 */
var log, className = "dark";
var curDragNodes, autoExpandNode;

function beforeDrag(treeId, treeNodes) {
    for (var i=0,l=treeNodes.length; i<l; i++) {
        var  pid = treeNodes[i].pId;
        if (treeNodes[i].drag === false) {
            curDragNodes = null;
            return false;
        } else if (pid && treeNodes[i].getParentNode().childDrag === false) {
            curDragNodes = null;
            return false;
        } else if(pid=="0"||pid==null || pid == ''){
            layer.msg('根节点无法移动',{icon:5,time:1000})
            return false;
        }
    }
    curDragNodes = treeNodes;
    return true;
};

function onDrag(event, treeId, treeNodes) {
    className = (className === "dark" ? "":"dark");
}

function dropPrev(treeId, nodes, targetNode) {
    var pNode = targetNode.getParentNode();
    if (pNode && pNode.dropInner === false) {
        return false;
    } else {
        for (var i=0,l=curDragNodes.length; i<l; i++) {
            var curPNode = curDragNodes[i].getParentNode();
            if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
                return false;
            }
        }
    }
    return true;
}
function dropInner(treeId, nodes, targetNode) {
    if (targetNode && targetNode.dropInner === false) {
        return false;
    } else {
        for (var i=0,l=curDragNodes.length; i<l; i++) {
            if (!targetNode && curDragNodes[i].dropRoot === false) {
                return false;
            } else if (curDragNodes[i].parentTId && curDragNodes[i].getParentNode() !== targetNode && curDragNodes[i].getParentNode().childOuter === false) {
                return false;
            }
        }
    }
    return true;
}
function dropNext(treeId, nodes, targetNode) {
    var pNode = targetNode.getParentNode();
    if (pNode && pNode.dropInner === false) {
        return false;
    } else {
        for (var i=0,l=curDragNodes.length; i<l; i++) {
            var curPNode = curDragNodes[i].getParentNode();
            if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
                return false;
            }
        }
    }
    return true;
}

function beforeDrop(treeId, treeNodes, targetNode, moveType, isCopy) {
    className = (className === "dark" ? "":"dark");
    return true;
}

function onDrop(event, treeId, treeNodes, targetNode, moveType, isCopy){
    className = (className === "dark" ? "":"dark");
    //拖拽结束之后
    console.log("拖拽节点的id:"+treeNodes[0].id);
    console.log("拖拽节点的新父节点的id:"+targetNode.id);
    //更新拖拽节点的父级目录id
    $.ajax({
        type:'post',
        url: _basePath + '/sys/dept/onDrag',
        data:{"id":treeNodes[0].id,"pId":targetNode.id},
        async: false,
        success: function (data) {
            if(data.code == 200){
                layer.msg('操作成功',{icon:6,time:1000});
                onLoadZTree();
            } else {
                layer.msg(data.msg,{icon:5,time:1000});
            }
        },
        error: function (e) {
            console.log(e);
            layer.msg('请求异常!',{icon:5});
        }
    });
}

function beforeDragOpen(treeId,treeNode) {
    autoExpandNode = treeNode;
    return true;
}

function zTreeOnClick(event, treeId, treeNode) {
    var deptId = treeNode.id;
    console.log(deptId);
    getMemberData(deptId)
    /*$.ajax({
        type:'get',
        url: _basePath + '/sys/dept/getDeptMember',
        data:{"id":deptId},
        async: false,
        success: function (data) {
            if(data.code == 200){
                layer.msg('操作成功',{icon:6,time:1000});
                onLoadZTree();
            } else {
                layer.msg(data.msg,{icon:5,time:1000});
            }
        },
        error: function (e) {
            console.log(e);
            layer.msg('请求异常!',{icon:5});
        }
    });*/
}

function getMemberData(deptId){
    layui.use(['table','form'], function(){
        var table = layui.table;

        table.render({
            id:'userId',
            elem: '#deptTable',
            method:'get',//接口http请求类型，默认:get
            url: _basePath + '/sys/dept/getDeptMember',
            //contentType:'application/json',//发送到服务端的内容编码类型。如设置json格式类型application/json
            where:{deptId: deptId},//接口的其它参数。如：where:
            title: '部门成员',
            request: {
                pageName: 'currentPage', //页码的参数名称，默认：page
                limitName: 'limit' //每页数据量的参数名，默认：limit
            },
            cols: [[
                {type: 'numbers',width:70,title:'序号'},
                {hide:true,field:'id',title:'主键'},
                {field:'userName', title:'用户名', sort: true},
                {field:'sfzh', title:'身份证号',  sort: true},
                {field:'lxfs', title:'手机号',sort: true},
                {field:'deptId', title:'所属部门', sort: true},
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
    });
}
