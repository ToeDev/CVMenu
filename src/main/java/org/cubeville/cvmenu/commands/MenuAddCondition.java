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

public class MenuAddCondition extends Command {

    private final CVMenu plugin;

    public MenuAddCondition(CVMenu plugin) {
        super("addcondition");
        addBaseParameter(new CommandParameterString(CommandParameterString.NO_SPECIAL_CHARACTERS));
        addParameter("slot", true, new CommandParameterInteger());
        addParameter("condition", false, new CommandParameterString());

        this.plugin = plugin;
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        MenuManager manager = plugin.getMenuManager();
        if(manager.getMenu((String) baseParameters.get(0)) == null) {
            throw new CommandExecutionException(org.bukkit.ChatColor.RED + "Menu " + org.bukkit.ChatColor.GOLD + baseParameters.get(0) + org.bukkit.ChatColor.RED + " doesn't exist!");
        }
        MenuContainer menu = manager.getMenu((String) baseParameters.get(0));
        int slot = -1;
        if(parameters.containsKey("slot")) {
            slot = (int) parameters.get("slot");
            if(slot + 1 > menu.getSize() || slot < -1) {
                throw new CommandExecutionException(ChatColor.RED + "Slot: " + ChatColor.GOLD + slot + ChatColor.RED + " doesn't exist on menu " + ChatColor.GOLD + menu.getMenuName() + ChatColor.LIGHT_PURPLE + " Remember, the first slot is slot 0! Remove the slot parameter or use slot:-1 for adding to all slots.");
            }
        }
        String condition = (String) parameters.get("condition");
        if(menu.containsConditionBQ(slot, condition.toLowerCase())) {
            throw new CommandExecutionException(ChatColor.RED + "Slot: " + ChatColor.GOLD + slot + ChatColor.RED + " already has condition: " + ChatColor.GOLD + condition + ChatColor.RED + " set on menu " + ChatColor.GOLD + menu.getMenuName());
        }
        menu.addConditionBQ(slot, condition.toLowerCase());
        plugin.saveMenuManager();
        return new CommandResponse(ChatColor.LIGHT_PURPLE + "Condition: " + ChatColor.GOLD + condition + ChatColor.LIGHT_PURPLE + " set on slot: " + ChatColor.GOLD + slot + ChatColor.LIGHT_PURPLE + " on menu: " + ChatColor.GOLD + menu.getMenuName());
    }
}
