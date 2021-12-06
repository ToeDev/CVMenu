package org.cubeville.cvmenu.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.*;
import org.cubeville.cvmenu.CVMenu;
import org.cubeville.cvmenu.menu.MenuContainer;
import org.cubeville.cvmenu.menu.MenuManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MenuRemoveCondition extends Command {

    private final CVMenu plugin;

    public MenuRemoveCondition(CVMenu plugin) {
        super("removecondition");
        addBaseParameter(new CommandParameterString(CommandParameterString.NO_SPECIAL_CHARACTERS));
        addParameter("slot", true, new CommandParameterInteger());
        addParameter("condition", true, new CommandParameterString());

        this.plugin = plugin;
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        MenuManager manager = plugin.getMenuManager();
        if(manager.getMenu((String) baseParameters.get(0)) == null) {
            throw new CommandExecutionException(ChatColor.RED + "Menu " + ChatColor.GOLD + baseParameters.get(0) + ChatColor.RED + " doesn't exist!");
        }
        MenuContainer menu = manager.getMenu((String) baseParameters.get(0));
        int slot = -1;
        String condition = "-1";
        if(parameters.containsKey("slot")) {
            slot = (int) parameters.get("slot");
            if(slot + 1 > menu.getSize() || slot < -1) {
                throw new CommandExecutionException(ChatColor.RED + "Slot: " + ChatColor.GOLD + slot + ChatColor.RED + " doesn't exist on menu " + ChatColor.GOLD + menu.getMenuName() + ChatColor.LIGHT_PURPLE + " Remember, the first slot is slot 0! Remove the slot parameter or use slot:-1 for adding to all slots.");
            }
        }
        if(parameters.containsKey("condition")) {
            condition = (String) parameters.get("condition");
        }
        if(slot == -1) {
            for(int i = 0; i < menu.getSize(); i++) {
                if(condition.equals("-1")) menu.removeAllConditionsBQ(i);
                if(menu.containsConditionBQ(i, condition.toLowerCase())) {
                    menu.removeConditionBQ(i, condition.toLowerCase());
                }
            }
            plugin.saveMenuManager();
            return new CommandResponse(ChatColor.LIGHT_PURPLE + (condition.equals("-1") ? "All conditions" : "Condition: " + ChatColor.GOLD + condition + ChatColor.LIGHT_PURPLE) + " removed on slots: " + ChatColor.GOLD + 0 + " - " + (menu.getSize() - 1) + ChatColor.LIGHT_PURPLE + " on menu: " + ChatColor.GOLD + menu.getMenuName());
        }
        if(condition.equals("-1")) {
            menu.removeAllConditionsBQ(slot);
            plugin.saveMenuManager();
            return new CommandResponse(ChatColor.LIGHT_PURPLE + "All conditions removed on slot: " + ChatColor.GOLD + slot + ChatColor.LIGHT_PURPLE + " on menu: " + ChatColor.GOLD + menu.getMenuName());
        }
        if(!menu.containsConditionBQ(slot, condition.toLowerCase())) {
            throw new CommandExecutionException(ChatColor.RED + "Slot: " + ChatColor.GOLD + slot + ChatColor.RED + " doesn't have condition: " + ChatColor.GOLD + condition + ChatColor.RED + " set on menu " + ChatColor.GOLD + menu.getMenuName() + ChatColor.LIGHT_PURPLE + " Remove the condition parameter or use condition:-1 for removing all condtiions.");
        }
        menu.removeConditionBQ(slot, condition.toLowerCase());
        plugin.saveMenuManager();
        return new CommandResponse(ChatColor.LIGHT_PURPLE + "Condition: " + ChatColor.GOLD + condition + ChatColor.LIGHT_PURPLE + " removed on slot: " + ChatColor.GOLD + slot + ChatColor.LIGHT_PURPLE + " on menu: " + ChatColor.GOLD + menu.getMenuName());
    }
}
