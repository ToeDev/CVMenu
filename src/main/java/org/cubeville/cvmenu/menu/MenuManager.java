package org.cubeville.cvmenu.menu;

import org.bukkit.entity.Player;
import org.cubeville.cvmenu.CVMenu;

import java.util.*;

public class MenuManager {

    Map<String, MenuContainer> menus;

    CVMenu plugin;

    public MenuManager() {
        menus = new HashMap<>();
    }

    public void createMenu(Player player, String name, int size) {
        menus.put(name.toLowerCase(), new MenuContainer(player, name, size));
    }

    public void removeMenu(String name) {
        menus.remove(name);
    }

    public void editMenu(Player player, String name) {
        menus.get(name.toLowerCase()).editInventory(player);
    }

    public boolean menuExists(String name) {
        return menus.containsKey(name.toLowerCase());
    }

    public List<String> getAllMenuNames() {
        List<String> menuList = new ArrayList<>();
        for(MenuContainer menu : menus.values()) {
            menuList.add(menu.getMenuName());
        }
        Collections.sort(menuList);
        return menuList;
    }

    public void setManager(CVMenu plugin) {
        this.plugin = plugin;
    }
}
