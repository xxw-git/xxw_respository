package com.lc.xxw.controller;

import com.google.common.collect.Lists;
import com.lc.xxw.entity.Menu;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @description: 菜单管理
 * @author: xuexiaowei
 * @create: 2019-07-22 13:55
 */

@Controller
@RequestMapping(value = "sys/menu")
@Api(value = "菜单管理",description = "菜单管理")
public class MenuController extends BaseController {



}
