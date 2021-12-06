package org.cubeville.cvmenu.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@SerializableAs("MenuContainer")
public class MenuContainer implements ConfigurationSerializable {

    private String menuName;
    private Inventory inventory;

    private final Map<Integer, Set<String>> conditionsBQ;
    private final Map<Integer, Set<String>> eventsBQ;
    private final Map<Integer, Set<String>> commands;

    @SuppressWarnings("unchecked")
    public MenuContainer(Map<String, Object> config) {
        System.out.println(config.get("name") + " loaded from config");
        menuName = (String) config.get("name");
        conditionsBQ = new HashMap<>();
        eventsBQ = new HashMap<>();
        commands = new HashMap<>();
        inventory = Bukkit.createInventory(null, config.size() - 2, (String) config.get("name") + ChatColor.RESET);
        for(int i = 0; i < config.size() - 2; i++) {
            Map<String, Object> slot = (Map<String, Object>) config.get("Slot" + i);
            if(slot.get("conditionsBQ") != null) {
                Set<String> conditions = new HashSet<>((Collection<? extends String>) slot.get("conditionsBQ"));
                conditionsBQ.put(i, conditions);
            }
            if(slot.get("eventsBQ") != null) {
                Set<String> events = new HashSet<>((Collection<? extends String>) slot.get("eventsBQ"));
                eventsBQ.put(i, events);
            }
            if(slot.get("commands") != null) {
                Set<String> cmds = new HashSet<>((Collection<? extends String>) slot.get("commands"));
                commands.put(i, cmds);
            }
            inventory.setItem(i, (ItemStack) slot.get("item"));
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("name", menuName);
        for(int i = 0; i < inventory.getSize(); i++) {
            Map<String, Object> out = new HashMap<>();
            List<String> condList = new ArrayList<>();
            if(conditionsBQ.get(i) != null && !conditionsBQ.get(i).isEmpty()) condList.addAll(conditionsBQ.get(i));
            out.put("conditionsBQ", condList);
            List<String> eveList = new ArrayList<>();
            if(eventsBQ.get(i) != null && !eventsBQ.get(i).isEmpty()) eveList.addAll(eventsBQ.get(i));
            out.put("eventsBQ", eveList);
            List<String> cmdList = new ArrayList<>();
            if(commands.get(i) != null && !commands.get(i).isEmpty()) cmdList.addAll(commands.get(i));
            out.put("commands", cmdList);
            out.put("item", inventory.getItem(i));

            ret.put("Slot" + i, out);
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

    public int getSize() {
        return inventory.getSize();
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

    public String getSlotItem(int slot) {
        if(inventory.getItem(slot) != null) {
            return Objects.requireNonNull(inventory.getItem(slot)).getType().toString();
        }
        return "none";
    }

    public Set<String> getConditionsBQ(int slot) {
        if(conditionsBQ.get(slot) != null) {
            return conditionsBQ.get(slot);
        }
        return null;
    }

    public boolean containsConditionBQ(int slot, String condition) {
        if(conditionsBQ.get(slot) == null) {
            return false;
        }
        return conditionsBQ.get(slot).contains(condition.toLowerCase());
    }

    public void addConditionBQ(int slot, String condition) {
        if(conditionsBQ.get(slot) == null) {
            Set<String> conditions = new HashSet<>();
            conditions.add(condition);
            conditionsBQ.put(slot, conditions);
        } else {
            conditionsBQ.get(slot).add(condition.toLowerCase());
        }
    }

    public void removeConditionBQ(int slot, String condition) {
        conditionsBQ.get(slot).remove(condition.toLowerCase());
    }

    public void removeAllConditionsBQ(int slot) {
        conditionsBQ.remove(slot);
    }

    public Set<String> getEventsBQ(int slot) {
        if(eventsBQ.get(slot) != null) {
            return eventsBQ.get(slot);
        }
        return null;
    }

    public boolean containsEventBQ(int slot, String event) {
        if(eventsBQ.get(slot) == null) {
            return false;
        }
        return eventsBQ.get(slot).contains(event.toLowerCase());
    }

    public void addEventBQ(int slot, String event) {
        eventsBQ.get(slot).add(event.toLowerCase());
    }

    public void removeEventBQ(int slot, String event) {
        eventsBQ.get(slot).remove(event.toLowerCase());
    }

    public Set<String> getCommands(int slot) {
        if(commands.get(slot) != null) {
            return commands.get(slot);
        }
        return null;
    }

    public boolean containsCommand(int slot, String command) {
        if(commands.get(slot) == null) {
            return false;
        }
        return commands.get(slot).contains(command.toLowerCase());
    }

    public void addCommand(int slot, String command) {
        commands.get(slot).add(command.toLowerCase());
    }

    public void removeCommand(int slot, String command) {
        commands.get(slot).remove(command.toLowerCase());
    }



}
