package com.lc.xxw.service;

import com.lc.xxw.entity.Menu;

import java.util.List;

public interface MenuService {
    /**
     * 查询所有启用的菜单
     */
    List<Menu> findAllMenu();
}
