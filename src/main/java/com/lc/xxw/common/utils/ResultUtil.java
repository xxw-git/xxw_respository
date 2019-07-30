package com.lc.xxw.common.utils;

import com.lc.xxw.common.enmus.ResultEnum;
import com.lc.xxw.common.vo.ResultVo;

import java.util.List;

/**
 * @description:统一响应结果工具类
 * @author: xuexiaowei
 * @create: 2019-07-21 15:34
 */

public class ResultUtil {

    public static ResultVo SAVE_SUCCESS = success("保存成功");

    /**
     * 操作成功
     * @param msg 提示信息
     * @param object 对象
     */
    public static ResultVo success(String msg, Object object){
        ResultVo resultVo = new ResultVo();
        resultVo.setMsg(msg);
        resultVo.setCode(ResultEnum.SUCCESS.getCode());
        resultVo.setData(object);
        return resultVo;
    }

    /**
     * 封装 layUI 返回数据格式
     * @param msg
     * @param total
     * @param data
     * @return
     */
    public static ResultVo success(String msg, Integer total,List<?> data){
        ResultVo resultVo = new ResultVo();
        resultVo.setMsg(msg);
        resultVo.setCode(ResultEnum.SUCCESS.getCode());
        resultVo.setTotal(total);
        resultVo.setData(data);
        return resultVo;
    }

    /**
     * 操作成功，自定义提示信息，
     * @param count 总条数
     * @param data 数据集合
     */
    public static ResultVo success(Integer count,List<?> data){
        return success("查询成功",count,data);
    }


    /**
     * 操作成功，使用默认的提示信息
     * @param object 对象
     */
    public static ResultVo success(Object object){
        String msg = ResultEnum.SUCCESS.getMsg();
        return success(msg,object);
    }

    /**
     * 操作成功，返回提示信息，不返回数据
     */
    public static ResultVo success(String msg){
        return success(msg,null);
    }

    /**
     * 操作成功，不返回数据
     */
    public static ResultVo success(){
        return success(null);
    }

    /**
     * 操作有误
     * @param code 错误码
     * @param msg 提示信息
     */
    public static ResultVo error(Integer code, String msg){
        ResultVo resultVo = new ResultVo();
        resultVo.setMsg(msg);
        resultVo.setCode(code);
        return resultVo;
    }

    /**
     * 操作有误
     * @param code 错误码
     * @param data 数据
     */
    public static ResultVo error(Integer code, List<?> data){
        ResultVo resultVo = new ResultVo();
        resultVo.setData(data);
        resultVo.setCode(code);
        return resultVo;
    }

    /**
     * 操作有误，使用默认500错误码
     * @param msg 提示信息
     */
    public static ResultVo error(String msg){
        Integer code = ResultEnum.ERROR.getCode();
        return error(code,msg);
    }

    public static ResultVo error(List<?> data){
        Integer code = ResultEnum.ERROR.getCode();
        return error(code,data);
    }



}
