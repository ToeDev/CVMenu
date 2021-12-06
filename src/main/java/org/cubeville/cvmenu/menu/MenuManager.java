package org.cubeville.cvmenu.menu;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.cubeville.cvmenu.CVMenu;

import java.util.*;

@SerializableAs("MenuManager")
public class MenuManager implements ConfigurationSerializable {

    Map<String, MenuContainer> menus;

    CVMenu plugin;

    public MenuManager() {
        menus = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public MenuManager(Map<String, Object> config) {
        menus = (Map<String, MenuContainer>) config.get("menus");
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("menus", menus);
        return ret;
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

    public MenuContainer getMenu(String menu) {
        if(menuExists(menu)) {
            return menus.get(menu);
        }
        return null;
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
