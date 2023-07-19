package org.cubeville.cvmenu.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.*;
import org.cubeville.cvmenu.CVMenu;
import org.cubeville.cvmenu.menu.MenuContainer;
import org.cubeville.cvmenu.menu.MenuManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TempMenuAddCommand extends BaseCommand {

    private final CVMenu plugin;

    public TempMenuAddCommand(CVMenu plugin) {
        super("addcommandtemp");
        addBaseParameter(new CommandParameterString(CommandParameterString.NO_SPECIAL_CHARACTERS));
        addParameter("slot", true, new CommandParameterInteger());
        addParameter("command", false, new CommandParameterString());
        addParameter("player", false, new CommandParameterOnlinePlayer());
        this.plugin = plugin;
    }

    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        if(sender instanceof Player) throw new CommandExecutionException(ChatColor.RED + "Only execute this command from console!");
        MenuManager manager = plugin.getMenuManager();
        if(manager.getTempMenu(((String) baseParameters.get(0)).toLowerCase(), (Player) parameters.get("player")) == null) {
            throw new CommandExecutionException(ChatColor.RED + "Menu " + ChatColor.GOLD + baseParameters.get(0) + ChatColor.RED + " doesn't exist!");
        }
        MenuContainer menu = manager.getTempMenu(((String) baseParameters.get(0)).toLowerCase(), (Player) parameters.get("player"));
        int slot = -1;
        if(parameters.containsKey("slot")) {
            slot = (int) parameters.get("slot");
            if(slot + 1 > menu.getSize() || slot < -1) {
                throw new CommandExecutionException(ChatColor.RED + "Slot: " + ChatColor.GOLD + slot + ChatColor.RED + " doesn't exist on menu " + ChatColor.GOLD + menu.getMenuName() + ChatColor.LIGHT_PURPLE + " Remember, the first slot is slot 0! Remove the slot parameter or use slot:-1 for adding to all slots.");
            }
        }
        String command = (String) parameters.get("command");
        if(slot == -1) {
            for(int i = 0; i < menu.getSize(); i++) {
                if(!menu.containsCommand(i, command)) {
                    menu.addCommand(i, command);
                }
            }
            return new CommandResponse("");
            //plugin.saveMenuManager();
            //return new CommandResponse(ChatColor.LIGHT_PURPLE + "Command: " + ChatColor.GOLD + command + ChatColor.LIGHT_PURPLE + " set on slots: " + ChatColor.GOLD + 0 + " - " + (menu.getSize() - 1) + ChatColor.LIGHT_PURPLE + " on menu: " + ChatColor.GOLD + menu.getMenuName());
        }
        if(menu.containsCommand(slot, command)) {
            throw new CommandExecutionException(ChatColor.RED + "Slot: " + ChatColor.GOLD + slot + ChatColor.RED + " already has command: " + ChatColor.GOLD + command + ChatColor.RED + " set on menu " + ChatColor.GOLD + menu.getMenuName());
        }
        menu.addCommand(slot, command);
        return new CommandResponse("");
        //plugin.saveMenuManager();
        //return new CommandResponse(ChatColor.LIGHT_PURPLE + "Command: " + ChatColor.GOLD + command + ChatColor.LIGHT_PURPLE + " set on slot: " + ChatColor.GOLD + slot + ChatColor.LIGHT_PURPLE + " on menu: " + ChatColor.GOLD + menu.getMenuName());
    }
}
