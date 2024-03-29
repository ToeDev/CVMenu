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

public class TempMenuSetClose extends BaseCommand {

    private final CVMenu plugin;

    public TempMenuSetClose(CVMenu plugin) {
        super("setclosetemp");
        addBaseParameter(new CommandParameterString(CommandParameterString.NO_SPECIAL_CHARACTERS));
        addParameter("slot", true, new CommandParameterInteger());
        addParameter("player", false, new CommandParameterOnlinePlayer());
        addBaseParameter(new CommandParameterBoolean());

        this.plugin = plugin;
    }

    @Override
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
        Boolean status = (Boolean) baseParameters.get(1);
        if(slot == -1) {
            for(int i = 0; i < menu.getSize(); i++) {
                menu.setClose(i, status);
            }
            return new CommandResponse("");
            //plugin.saveMenuManager();
            //return new CommandResponse(ChatColor.LIGHT_PURPLE + "Menu will close when clicking any slots");
        }
        menu.setClose(slot, status);
        return new CommandResponse("");
        //plugin.saveMenuManager();
        //return new CommandResponse(ChatColor.LIGHT_PURPLE + "Menu closing changed to: " + ChatColor.GOLD + status + ChatColor.LIGHT_PURPLE + " on slot: " + ChatColor.GOLD + slot + ChatColor.LIGHT_PURPLE + " on menu: " + ChatColor.GOLD + menu.getMenuName());
    }
}
