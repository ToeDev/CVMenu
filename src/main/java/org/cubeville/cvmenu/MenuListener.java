package org.cubeville.cvmenu;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

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
}
