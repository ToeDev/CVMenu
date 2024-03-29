package org.cubeville.cvmenu;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.exceptions.ObjectNotFoundException;
import org.betonquest.betonquest.id.EventID;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.cubeville.cvmenu.menu.MenuContainer;

public class MenuListener implements Listener {

    private final CVMenu plugin;

    public MenuListener(CVMenu plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        try {
            if (plugin.getMenuManager().menuExists(event.getView().getTitle().substring(0, event.getView().getTitle().length() - 2))) { //Substring needed to allow cvloadouts and cvmenus to have duplicate inventory titles
                plugin.saveMenuManager();
                event.getPlayer().sendMessage(ChatColor.GREEN + "Menu " + ChatColor.GOLD + event.getView().getTitle() + ChatColor.GREEN + " saved!");
            }
        } catch(IndexOutOfBoundsException ignored) {

        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getView().getTitle().length() > 4 && event.getView().getTitle().endsWith(ChatColor.RESET.toString() + ChatColor.RESET.toString())) {
            event.setCancelled(true);
            if(event.getClickedInventory() == null) return;
            if(event.getSlot() == -999) return;
            if(event.getClickedInventory().getItem(event.getSlot()) == null) return;
            MenuContainer menu = plugin.getMenuManager().getMenu(event.getView().getTitle().substring(0, event.getView().getTitle().length() - 4).toLowerCase());
            Player player = (Player) event.getWhoClicked();
            if(menu == null) {
                menu = plugin.getMenuManager().getMenu(player.getName() + "'s_" + event.getView().getTitle().substring(0, event.getView().getTitle().length() - 4).toLowerCase());
            }
            if(menu == null) return;
            if(menu.getCommands(event.getRawSlot()) != null) {
                for(String command : menu.getCommands(event.getSlot())) {
                    if(command.contains("%player%")) {
                        if(menu.doCmdsRunFromConsole(event.getSlot())) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                        } else {
                            Bukkit.dispatchCommand(player, command.replace("%player%", player.getName()));
                        }
                    } else {
                        if(menu.doCmdsRunFromConsole(event.getSlot())) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                        } else {
                            Bukkit.dispatchCommand(player, command);
                        }
                    }
                }
            }
            if(menu.doesClose(event.getSlot())) {
                player.closeInventory();
            }
        }
    }
}
