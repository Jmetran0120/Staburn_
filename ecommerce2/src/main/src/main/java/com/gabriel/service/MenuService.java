package com.gabriel.service;

import com.gabriel.model.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> getMenus();
    Menu create(Menu menu);
}
