package org.cubeville.cvmenu;

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
        if(plugin.getMenuManager().menuExists(event.getView().getTitle().substring(0, event.getView().getTitle().length() - 2))) { //Substring needed to allow cvloadouts and cvmenus to have duplicate inventory titles
            plugin.saveMenuManager();
            event.getPlayer().sendMessage(ChatColor.GREEN + "Menu " + ChatColor.GOLD + event.getView().getTitle() + ChatColor.GREEN + " saved!");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getView().getTitle().length() > 4 && event.getView().getTitle().endsWith("§r§r")) {
            event.setCancelled(true);
            MenuContainer menu = plugin.getMenuManager().getMenu(event.getView().getTitle().substring(0, event.getView().getTitle().length() - 2));
            if(menu.getCommands(event.getSlot()) != null) {
                Player player = event.getWhoClicked().getKiller();
                for(String command : menu.getCommands(event.getSlot())) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            }
        }
    }
}
