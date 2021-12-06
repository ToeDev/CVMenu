package org.cubeville.cvmenu.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.*;
import org.cubeville.cvmenu.CVMenu;
import org.cubeville.cvmenu.menu.MenuManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MenuRemove extends Command {

    private final CVMenu plugin;

    public MenuRemove(CVMenu plugin) {
        super("remove");
        addBaseParameter(new CommandParameterString(CommandParameterString.NO_SPECIAL_CHARACTERS));

        this.plugin = plugin;
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        MenuManager manager = plugin.getMenuManager();

        if(manager.menuExists((String) baseParameters.get(0))) {
            manager.removeMenu((String) baseParameters.get(0));
            plugin.saveMenuManager();
            return new CommandResponse(ChatColor.GREEN + "Menu " + ChatColor.GOLD + baseParameters.get(0) + ChatColor.GREEN + " removed!");
        } else {
            throw new CommandExecutionException(ChatColor.RED + "Menu " + ChatColor.GOLD + baseParameters.get(0) + ChatColor.RED + " doesn't exist!");
        }
    }
}
