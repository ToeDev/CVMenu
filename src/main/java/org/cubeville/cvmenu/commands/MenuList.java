package org.cubeville.cvmenu.commands;

import java.util.*;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.BaseCommand;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.cvmenu.CVMenu;

public class MenuList extends BaseCommand {

    private final CVMenu plugin;

    public MenuList(CVMenu plugin) {
        super("list");

        this.plugin = plugin;
    }

    @Override
    public CommandResponse execute(CommandSender sender, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) {
        List<String> menus = plugin.getMenuManager().getAllMenuNames();
        Collections.sort(menus);
        if(sender instanceof Player) {
            List<TextComponent> out = new ArrayList<>();
            out.add(new TextComponent(ChatColor.DARK_GREEN + "--------------------" + ChatColor.GREEN + "Menus" + ChatColor.DARK_GREEN + "--------------------"));
            TextComponent menuList = new TextComponent("");
            int i = menus.size();
            for (String menu : menus) {
                TextComponent m = new TextComponent(menu);
                m.setColor(ChatColor.GOLD);
                m.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/menu edit " + menu));
                m.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Edit " + menu).create()));
                i--;
                if(i >= 1) m.addExtra(ChatColor.BLUE + " || ");
                menuList.addExtra(m);
            }
            out.add(menuList);
            for(TextComponent o : out) {
                sender.spigot().sendMessage(o);
            }
        } else {
            List<String> out = new ArrayList<>();
            out.add(ChatColor.DARK_GREEN + "--------------------" + ChatColor.GREEN + "Menus" + ChatColor.DARK_GREEN + "--------------------");
            String list = "";
            int i = menus.size();
            for(String menu : menus) {
                i--;
                if(i >= 1) menu = menu.concat(ChatColor.BLUE + " || ");
                list = list.concat(ChatColor.GOLD + menu);
            }
            out.add(list);
            for(String o : out) {
                sender.sendMessage(o);
            }
        }
        return new CommandResponse("");
    }
}
