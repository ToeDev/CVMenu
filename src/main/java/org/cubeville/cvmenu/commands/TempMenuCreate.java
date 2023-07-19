package org.cubeville.cvmenu.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.*;
import org.cubeville.cvmenu.CVMenu;
import org.cubeville.cvmenu.menu.MenuManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TempMenuCreate extends BaseCommand {

    private final CVMenu plugin;

    public TempMenuCreate(CVMenu plugin) {
        super("createtemp");
        addBaseParameter(new CommandParameterString(CommandParameterString.NO_SPECIAL_CHARACTERS));
        addParameter("player", false, new CommandParameterOnlinePlayer());
        addParameter("size", true, new CommandParameterInteger());
        this.plugin = plugin;
    }

    @Override
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        if(sender instanceof Player) throw new CommandExecutionException(ChatColor.RED + "Only execute this command from console!");
        MenuManager manager = plugin.getMenuManager();
        Player player = (Player) parameters.get("player");
        int size = 54;

        if(parameters.containsKey("size")) {
            if((int) parameters.get("size") > 54 || ((int) parameters.get("size") % 9) != 0) {
                throw new CommandExecutionException(ChatColor.RED + "Size: parameter must be a multiple of 9 and no greater than 54!");
            }
            size = (int) parameters.get("size");
        }

        //if(!manager.menuExists(player.getName() + "'s_" + ((String) baseParameters.get(0)).toLowerCase())) {
            manager.createTempMenu((String) baseParameters.get(0), size, player);
            //return new CommandResponse(ChatColor.GREEN + "Temp Menu " + ChatColor.GOLD + baseParameters.get(0) + ChatColor.GREEN + " created!");
        //} else {
            //throw new CommandExecutionException(ChatColor.RED + "Temp Menu " + ChatColor.GOLD + baseParameters.get(0) + ChatColor.RED + " already exists!");
        //}
        return new CommandResponse("");
    }
}
