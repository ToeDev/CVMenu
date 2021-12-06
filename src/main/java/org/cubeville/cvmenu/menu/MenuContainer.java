package org.cubeville.cvmenu.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MenuContainer implements ConfigurationSerialization {

    private String menuName;
    private Inventory inventory;

    private Map<Integer, Set<String>> conditionsBQ;
    private Map<Integer, Set<String>> eventsBQ;
    private Map<Integer, Set<String>> commands;

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        Map<Integer, Set<String>> conditionsBQ1 = new HashMap<>();
        Map<Integer, Set<String>> eventsBQ1 = new HashMap<>();
        Map<Integer, Set<String>> commands1 = new HashMap<>();
        ret.put("name", menuName);
        ret.put("conditions", conditionsBQ);
        ret.put("events", eventsBQ);
        ret.put("commands", commands);
        {
            List<ItemStack> itemList = new ArrayList<>();
            for(int i = 0; i < inventory.getSize(); i++) {

                itemList.add(inventory.getItem(i));
            }
            ret.put("slots", itemList);
        }
        return ret;
    }

    public MenuContainer(Player player, String name, int size) {
        menuName = name;
        inventory = Bukkit.createInventory(null, size, name + ChatColor.RESET); //ChatColor.RESET Needed to allow cvloadouts and cvmenus to have duplicate inventory titles
        conditionsBQ = new HashMap<>();
        eventsBQ = new HashMap<>();
        commands = new HashMap<>();
        player.openInventory(inventory);
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String name) {
        menuName = name;
        Inventory oldInv = inventory;
        inventory = Bukkit.createInventory(null, oldInv.getSize(), name + ChatColor.RESET); //ChatColor.RESET Needed to allow cvloadouts and cvmenus to have duplicate inventory titles
        inventory.setContents(oldInv.getContents());
    }

    /*public void setMenuSize(int size) {
        Inventory oldInv = inventory;
        inventory = Bukkit.cre
    }*/

    public Inventory getInventory() {
        return inventory;
    }

    public void editInventory(Player player) {
        player.openInventory(inventory);
    }

    public Set<String> getConditionsBQ(int slot) {
        return conditionsBQ.get(slot);
    }

    public boolean containsConditionBQ(int slot, String condition) {
        return conditionsBQ.get(slot).contains(condition.toLowerCase());
    }

    public void addConditionBQ(int slot, String condition) {
        conditionsBQ.get(slot).add(condition.toLowerCase());
    }

    public void removeConditionBQ(int slot, String condition) {
        conditionsBQ.get(slot).remove(condition.toLowerCase());
    }

    public Set<String> getEventsBQ(int slot) {
        return eventsBQ.get(slot);
    }

    public boolean containsEventBQ(int slot, String event) {
        return eventsBQ.get(slot).contains(event.toLowerCase());
    }

    public void addEventBQ(int slot, String event) {
        eventsBQ.get(slot).add(event.toLowerCase());
    }

    public void removeEventBQ(int slot, String event) {
        eventsBQ.get(slot).remove(event.toLowerCase());
    }

    public Set<String> getCommands(int slot) {
        return commands.get(slot);
    }

    public boolean containsCommand(int slot, String command) {
        return commands.get(slot).contains(command.toLowerCase());
    }

    public void addCommand(int slot, String command) {
        commands.get(slot).add(command.toLowerCase());
    }

    public void removeCommand(int slot, String command) {
        commands.get(slot).remove(command.toLowerCase());
    }



}
