package org.cubeville.cvmenu.menu;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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

    //API USAGE ONLY
    public void createTempMenu(String name, int size, Player owner) {
        menus.put(owner.getName().toLowerCase() + "'s_" + name.toLowerCase(), new MenuContainer(name, size, owner));
    }

    //API USAGE ONLY
    public MenuContainer getTempMenu(String menu, Player owner) {
        if(menuExists(owner.getName() + "'s_" + menu)) {
            return menus.get(owner.getName().toLowerCase() + "'s_" + menu.toLowerCase());
        }
        return null;
    }

    //API USAGE ONLY
    public void createMenu(String name, int size) {
        menus.put(name.toLowerCase(), new MenuContainer(name, size));
    }

    //API USAGE ONLY
    public void setMenuInv(String name, Inventory newInventory) {
        menus.get(name.toLowerCase()).setInventory(newInventory);
    }

    //API USAGE ONLY
    public void setMenuItem(String name, int slot, ItemStack item) {
        menus.get(name.toLowerCase()).setItem(slot, item);
    }

    public void removeMenu(String name) {
        menus.remove(name.toLowerCase());
    }

    public void editMenu(Player player, String name) {
        menus.get(name.toLowerCase()).editInventory(player);
    }

    public boolean menuExists(String name) {
        return menus.containsKey(name.toLowerCase());
    }

    public MenuContainer getMenu(String menu) {
        if(menuExists(menu)) {
            return menus.get(menu.toLowerCase());
        }
        return null;
    }

    public List<String> getAllMenuNames() {
        List<String> menuList = new ArrayList<>();
        for(MenuContainer menu : menus.values()) {
            if(menu.getInventory().getHolder() == null) {
                menuList.add(menu.getMenuName());
            }
        }
        Collections.sort(menuList);
        return menuList;
    }

    public void setManager(CVMenu plugin) {
        this.plugin = plugin;
    }

    public void removeTempMenus() {
        for(MenuContainer menu : menus.values()) {
            if(menu.getInventory().getHolder() != null) {
                menus.values().remove(menu);
            }
        }
    }
}
