package com.lc.xxw.entity;

import com.lc.xxw.common.enmus.StatusEnum;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @description: 基础属性实体
 * @className: BaseEntity
 * @author: xuexiaowei
 * @create: 2019-08-02 10:20
 */
@Data
public class BaseEntity implements Serializable {

    /** 主键 */
    @Id
    private String id;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 状态 */
    private Byte status = StatusEnum.OK.getCode();

    /** 备注 */
    private String remark;
}
