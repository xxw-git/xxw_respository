package com.lc.xxw.entity;

import com.lc.xxw.common.enmus.StatusEnum;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @description: 角色表
 * @author: xuexiaowei
 * @create: 2019-07-27 08:46
 */
@Table(name = "SYS_ROLE")
@Data
public class Role implements Serializable {

    @Id
    private String id;

    private String name;

    private String description;

    private Byte status = StatusEnum.OK.getCode();
}
