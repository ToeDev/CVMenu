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

public class TempMenuDisplay extends BaseCommand {

    private final CVMenu plugin;

    public TempMenuDisplay(CVMenu plugin) {
        super("displaytemp");
        addBaseParameter(new CommandParameterString(CommandParameterString.NO_SPECIAL_CHARACTERS));
        addParameter("player", false, new CommandParameterOnlinePlayer());
        this.plugin = plugin;
    }

    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        if(sender instanceof Player) throw new CommandExecutionException(ChatColor.RED + "Only execute this command from console!");
        MenuManager manager = plugin.getMenuManager();
        manager.showTempMenu((String) baseParameters.get(0), (Player) parameters.get("player"));
        return new CommandResponse("");
    }
}
