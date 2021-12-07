package org.cubeville.cvmenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.cubeville.cvmenu.menu.MenuContainer;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.exceptions.ObjectNotFoundException;
import pl.betoncraft.betonquest.id.EventID;

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
            if(event.getClickedInventory() == null) return;
            if(event.getSlot() == -999) return;
            if(event.getClickedInventory().getItem(event.getSlot()) == null) return;
            MenuContainer menu = plugin.getMenuManager().getMenu(event.getView().getTitle().substring(0, event.getView().getTitle().length() - 4));
            if(menu == null) return;
            Player player = (Player) event.getWhoClicked();
            if(menu.getCommands(event.getRawSlot()) != null) {
                for(String command : menu.getCommands(event.getSlot())) {
                    if(command.contains("%player%")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                    } else {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    }
                }
            }
            if(menu.getEventsBQ(event.getRawSlot()) != null) {
                for(String eventBQ : menu.getEventsBQ(event.getSlot())) {
                    try {
                        BetonQuest.event(player.getUniqueId().toString(), new EventID(null, eventBQ));
                    } catch (ObjectNotFoundException e) {
                        System.out.println(eventBQ + "not a valid event!");
                    }
                }
            }
        }
    }
}
