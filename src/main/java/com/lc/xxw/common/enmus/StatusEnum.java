package com.lc.xxw.common.enmus;

import com.lc.xxw.constants.StatusConstants;
import lombok.Getter;

/**
 * 状态枚举
 */
@Getter
public enum StatusEnum {

    OK(StatusConstants.OK,"启用"),
    FREEZED(StatusConstants.FREEZED,"冻结"),
    DELETE(StatusConstants.DELETE,"删除");

    private Byte code;

    private String message;

    StatusEnum(Byte code,String message){
        this.code = code;
        this.message = message;
    }

}
