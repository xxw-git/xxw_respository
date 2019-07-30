package com.lc.xxw.entity;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @description: 分页实体
 * @author: xuexiaowei
 * @create: 2019-07-12 22:05
 */

@Data
public class PageValid {

    private static final Integer PAGE_Size = 10;

    /** 当前页 */
    private Integer currentPage = 1;

    /** 每页记录数 */
    private Integer pageSize = PAGE_Size;

    /** 总页数 *//*
    private Integer totalPage;

    *//** 开始行数 *//*
    private Integer startRecord = 0;

    *//** 结束行数 *//*
    private Integer endRecord = 0;

    *//** 总记录数 *//*
    private Long totalRecord = 0L;

    private String remark;*/

    private Map<String,Object> filterMap = Maps.newHashMap();
}
