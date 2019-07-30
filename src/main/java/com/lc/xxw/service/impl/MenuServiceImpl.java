package com.lc.xxw.service.impl;

import com.lc.xxw.common.enmus.StatusEnum;
import com.lc.xxw.common.utils.StringUtils;
import com.lc.xxw.entity.Menu;
import com.lc.xxw.mapper.MenuMapper;
import com.lc.xxw.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @description: 菜单实现类
 * @author: xuexiaowei
 * @create: 2019-07-22 14:26
 */

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 加载所有菜单
     * @return
     */
    @Override
    public List<Menu> findAllMenu(){
        Example example = new Example(Menu.class);
        Example.Criteria  criteria = example.createCriteria();
        criteria.andEqualTo("status", StatusEnum.OK.getCode());
        example.orderBy("sort").asc();
        return menuMapper.selectByExample(example);
    }
}
