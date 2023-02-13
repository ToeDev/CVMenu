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

public class MenuRemoveCommand extends Command {

    private final CVMenu plugin;

    public MenuRemoveCommand(CVMenu plugin) {
        super("removecommand");
        addBaseParameter(new CommandParameterString(CommandParameterString.NO_SPECIAL_CHARACTERS));
        addParameter("slot", true, new CommandParameterInteger());
        addParameter("command", true, new CommandParameterString());

        this.plugin = plugin;
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        MenuManager manager = plugin.getMenuManager();
        if(manager.getMenu(((String) baseParameters.get(0)).toLowerCase()) == null) {
            throw new CommandExecutionException(ChatColor.RED + "Menu " + ChatColor.GOLD + baseParameters.get(0) + ChatColor.RED + " doesn't exist!");
        }
        MenuContainer menu = manager.getMenu(((String) baseParameters.get(0)).toLowerCase());
        int slot = -1;
        String command = "-1";
        if(parameters.containsKey("slot")) {
            slot = (int) parameters.get("slot");
            if(slot + 1 > menu.getSize() || slot < -1) {
                throw new CommandExecutionException(ChatColor.RED + "Slot: " + ChatColor.GOLD + slot + ChatColor.RED + " doesn't exist on menu " + ChatColor.GOLD + menu.getMenuName() + ChatColor.LIGHT_PURPLE + " Remember, the first slot is slot 0! Remove the slot parameter or use slot:-1 for adding to all slots.");
            }
        }
        if(parameters.containsKey("command")) {
            command = (String) parameters.get("command");
        }
        if(slot == -1) {
            for(int i = 0; i < menu.getSize(); i++) {
                if(command.equals("-1")) menu.removeAllCommands(i);
                if(menu.containsCommand(i, command)) {
                    menu.removeCommand(i, command);
                }
            }
            plugin.saveMenuManager();
            return new CommandResponse(ChatColor.LIGHT_PURPLE + (command.equals("-1") ? "All commands" : "Command: " + ChatColor.GOLD + command + ChatColor.LIGHT_PURPLE) + " removed on slots: " + ChatColor.GOLD + 0 + " - " + (menu.getSize() - 1) + ChatColor.LIGHT_PURPLE + " on menu: " + ChatColor.GOLD + menu.getMenuName());
        }
        if(command.equals("-1")) {
            menu.removeAllCommands(slot);
            plugin.saveMenuManager();
            return new CommandResponse(ChatColor.LIGHT_PURPLE + "All commands removed on slot: " + ChatColor.GOLD + slot + ChatColor.LIGHT_PURPLE + " on menu: " + ChatColor.GOLD + menu.getMenuName());
        }
        if(!menu.containsCommand(slot, command)) {
            throw new CommandExecutionException(ChatColor.RED + "Slot: " + ChatColor.GOLD + slot + ChatColor.RED + " doesn't have command: " + ChatColor.GOLD + command + ChatColor.RED + " set on menu " + ChatColor.GOLD + menu.getMenuName() + ChatColor.LIGHT_PURPLE + " Remove the condition parameter or use condition:-1 for removing all condtiions.");
        }
        menu.removeCommand(slot, command);
        plugin.saveMenuManager();
        return new CommandResponse(ChatColor.LIGHT_PURPLE + "Command: " + ChatColor.GOLD + command + ChatColor.LIGHT_PURPLE + " removed on slot: " + ChatColor.GOLD + slot + ChatColor.LIGHT_PURPLE + " on menu: " + ChatColor.GOLD + menu.getMenuName());
    }
}
