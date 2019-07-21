package com.lc.xxw.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 响应数据对象
 * @author: xuexiaowei
 * @create: 2019-07-21 15:07
 */
@Data
public class ResultVo implements Serializable {

    private static final long serialVersionUID = -3948389268046368059L;

    private Integer code;

    private String msg;

    private Object data;


}
