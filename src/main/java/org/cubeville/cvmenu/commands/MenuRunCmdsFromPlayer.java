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

public class MenuRunCmdsFromPlayer extends Command {

    private final CVMenu plugin;

    public MenuRunCmdsFromPlayer(CVMenu plugin) {
        super("set playercommands");
        addBaseParameter(new CommandParameterString(CommandParameterString.NO_SPECIAL_CHARACTERS));
        addParameter("slot", true, new CommandParameterInteger());
        addBaseParameter(new CommandParameterBoolean());

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
        if(parameters.containsKey("slot")) {
            slot = (int) parameters.get("slot");
            if(slot + 1 > menu.getSize() || slot < -1) {
                throw new CommandExecutionException(ChatColor.RED + "Slot: " + ChatColor.GOLD + slot + ChatColor.RED + " doesn't exist on menu " + ChatColor.GOLD + menu.getMenuName() + ChatColor.LIGHT_PURPLE + " Remember, the first slot is slot 0! Remove the slot parameter or use slot:-1 for adding to all slots.");
            }
        }
        Boolean status = (Boolean) baseParameters.get(1);
        if(slot == -1) {
            for(int i = 0; i < menu.getSize(); i++) {
                menu.setCmdsRunFromConsole(slot, !status);
            }
            plugin.saveMenuManager();
            return new CommandResponse(ChatColor.LIGHT_PURPLE + "All menu commands will now run from the player context");
        }
        menu.setCmdsRunFromConsole(slot, !status);
        plugin.saveMenuManager();
        return new CommandResponse(ChatColor.LIGHT_PURPLE + "Menu commands running from the player context now set to: " + ChatColor.GOLD + status + ChatColor.LIGHT_PURPLE + " on slot: " + ChatColor.GOLD + slot + ChatColor.LIGHT_PURPLE + " on menu: " + ChatColor.GOLD + menu.getMenuName());
    }
}

