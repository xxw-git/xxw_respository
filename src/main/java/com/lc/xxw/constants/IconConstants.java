package com.lc.xxw.constants;

import com.lc.xxw.common.utils.ReadProperties;
import com.lc.xxw.controller.BaseController;

/**
 * @description: 自定义图标
 * @className: IconConstants
 * @author: xuexiaowei
 * @create: 2019-08-04 10:23
 */
public class IconConstants extends BaseController {

    /** 项目名称 */
    public static final String BASE_PATH = (String) getSession().getAttribute("basePath");

    /** 图标路径 */
    public static final String ICON_PATH = ReadProperties.getProperValueBykey(StaticPathConstants.ICON_PROPERTIES_PATH,"icon.path");

    /** 部门父节点 icon */
    public static final String DEPT_PID_ICON = "/1_open.png";

    /** 部门子节点 icon */
    public static final String DEPT_CHILD_ICON = "/6.png";
}
