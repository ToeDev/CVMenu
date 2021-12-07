package org.cubeville.cvmenu.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandExecutionException;
import org.cubeville.commons.commands.CommandParameterString;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.cvmenu.CVMenu;
import org.cubeville.cvmenu.menu.MenuContainer;
import org.cubeville.cvmenu.menu.MenuManager;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MenuDisplay extends Command {

    private final CVMenu plugin;

    public MenuDisplay(CVMenu plugin) {
        super("display");
        addBaseParameter(new CommandParameterString(CommandParameterString.NO_SPECIAL_CHARACTERS));
        addParameter("player", true, new CommandParameterString());
        this.plugin = plugin;
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        MenuManager manager = plugin.getMenuManager();
        if(!manager.menuExists((String) baseParameters.get(0))) {
            throw new CommandExecutionException(ChatColor.RED + "Menu " + ChatColor.GOLD + baseParameters.get(0) + ChatColor.RED + " doesn't exist!");
        }
        MenuContainer menu = manager.getMenu((String) baseParameters.get(0));
        if(parameters.containsKey("player")) {
            if(Bukkit.getPlayer((String) parameters.get("player")) == null) {
                throw new CommandExecutionException(ChatColor.GOLD + (String) parameters.get("player") + ChatColor.RED + " is not online!");
            }
            Player p = Bukkit.getPlayer((String) parameters.get("player"));
            Inventory displayInv = menu.getDisplayInventory(p);
            Objects.requireNonNull(p).openInventory(displayInv);
            return new CommandResponse("");
        }
        Inventory displayInv = menu.getDisplayInventory(player);
        player.openInventory(displayInv);
        return new CommandResponse("");
    }
}
