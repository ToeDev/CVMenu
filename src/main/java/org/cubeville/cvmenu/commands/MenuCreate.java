package org.cubeville.cvmenu.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.*;
import org.cubeville.cvmenu.CVMenu;
import org.cubeville.cvmenu.menu.MenuManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MenuCreate extends Command {

    private final CVMenu plugin;

    public MenuCreate(CVMenu plugin) {
        super("create");
        addBaseParameter(new CommandParameterString(CommandParameterString.NO_SPECIAL_CHARACTERS));
        addParameter("size", true, new CommandParameterInteger());

        this.plugin = plugin;
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        MenuManager manager = plugin.getMenuManager();
        int size = 54;

        if(parameters.containsKey("size")) {
            if((int) parameters.get("size") > 54 || ((int) parameters.get("size") % 9) != 0) {
                throw new CommandExecutionException(ChatColor.RED + "Size: parameter must be a multiple of 9 and no greater than 54!");
            }
            size = (int) parameters.get("size");
        }

        if(!manager.menuExists((String) baseParameters.get(0))) {
            manager.createMenu(player, (String) baseParameters.get(0), size);
            return new CommandResponse(ChatColor.GREEN + "Menu " + ChatColor.GOLD + baseParameters.get(0) + ChatColor.GREEN + " created!");
        } else {
            throw new CommandExecutionException(ChatColor.RED + "Menu " + ChatColor.GOLD + baseParameters.get(0) + ChatColor.RED + " already exists!");
        }
    }
}
