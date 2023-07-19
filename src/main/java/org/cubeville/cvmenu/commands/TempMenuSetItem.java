package org.cubeville.cvmenu.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.cubeville.commons.commands.*;
import org.cubeville.cvmenu.CVMenu;
import org.cubeville.cvmenu.menu.MenuContainer;
import org.cubeville.cvmenu.menu.MenuManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TempMenuSetItem extends BaseCommand {

    private final CVMenu plugin;

    public TempMenuSetItem(CVMenu plugin) {
        super("setitemtemp");
        addBaseParameter(new CommandParameterString(CommandParameterString.NO_SPECIAL_CHARACTERS));
        addParameter("player", false, new CommandParameterOnlinePlayer());
        addParameter("slot", false, new CommandParameterInteger());
        addParameter("item", false, new CommandParameterString());
        addParameter("itemname", false, new CommandParameterString());
        addParameter("lore", true, new CommandParameterString());
        addParameter("itemenchantment", true, new CommandParameterEnchantment());
        addParameter("size", true, new CommandParameterInteger());
        this.plugin = plugin;
    }

    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        if(sender instanceof Player) throw new CommandExecutionException(ChatColor.RED + "Only execute this command from console!");
        MenuManager manager = plugin.getMenuManager();
        if(manager.getTempMenu(((String) baseParameters.get(0)).toLowerCase(), (Player) parameters.get("player")) == null) {
            throw new CommandExecutionException(ChatColor.RED + "Menu " + ChatColor.GOLD + baseParameters.get(0) + ChatColor.RED + " doesn't exist!");
        }
        MenuContainer menu = manager.getTempMenu(((String) baseParameters.get(0)).toLowerCase(), (Player) parameters.get("player"));
        int slot = (int) parameters.get("slot");
        if(slot + 1 > menu.getSize() || slot < -1) {
            throw new CommandExecutionException(ChatColor.RED + "Slot: " + ChatColor.GOLD + slot + ChatColor.RED + " doesn't exist on menu " + ChatColor.GOLD + menu.getMenuName() + ChatColor.LIGHT_PURPLE + " Remember, the first slot is slot 0! Remove the slot parameter or use slot:-1 for adding to all slots.");
        }
        ItemStack item;
        try {
            item = new ItemStack(Material.valueOf(((String) parameters.get("item")).toUpperCase()));
        } catch(IllegalArgumentException | NullPointerException ignored) {
            throw new CommandExecutionException(ChatColor.GOLD + (String) parameters.get("item") + ChatColor.RED + " is not a valid Bukkit Material!");
        }
        int size = 1;
        if(parameters.containsKey("size")) {
            if((int)parameters.get("size") > 0 && (int)parameters.get("size") < 65) {
                size = (int) parameters.get("size");
            } else {
                throw new CommandExecutionException(ChatColor.RED + "Size must be between 1 and 64!");
            }
        }
        item.setAmount(size);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName((String) parameters.get("itemname"));
        if(parameters.containsKey("lore")) {
            List<String> lore = new ArrayList<>();
            String full = (String) parameters.get("lore");
            while(full.contains(";")) {
                lore.add(full.substring(0, full.indexOf(";")));
                try {
                    full = full.substring(full.indexOf(";") + 1);
                } catch(IndexOutOfBoundsException ignored) {
                    break;
                }
            }
            meta.setLore(lore);
        }
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        if(parameters.containsKey("itemenchantment")) {
            item.addUnsafeEnchantment((Enchantment) parameters.get("itemenchantment"), 1);
        }

        menu.setItem(slot, item);
        return new CommandResponse("");
        //return new CommandResponse(ChatColor.LIGHT_PURPLE + "Item: " + ChatColor.GOLD + parameters.get("item") + ChatColor.LIGHT_PURPLE + " set on slot: " + ChatColor.GOLD + slot + ChatColor.LIGHT_PURPLE + " on menu: " + ChatColor.GOLD + menu.getMenuName());
    }
}
