package com.lc.xxw.entity;

import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Comparator;
import java.util.List;

/**
 * @description: 菜单实体
 * @author: xuexiaowei
 * @create: 2019-07-22 13:58
 */

@Data
@Table(name = "SYS_MENU")
public class Menu extends BaseEntity {

    //菜单名称
    private String menuName;

    //父id
    private String parentId;

    //图标
    private String icon;

    //菜单类型 1 一级菜单 2 子菜单
    private Byte type;

    //菜单URL
    private String url;

    //菜单排序
    private Integer sort;

    //创建人
    private String createUserId;

    @Transient
    private List<Menu> subMenu;


    public static Comparator<Menu> order(){
        Comparator<Menu> comparator = new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                if(o1.getSort() != o2.getSort()){
                    return o1.getSort() - o2.getSort();
                }
                return 0;
            }
        };
        return comparator;
    }

}
