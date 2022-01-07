package org.cubeville.cvmenu.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.cubeville.commons.commands.Command;
import org.cubeville.commons.commands.CommandResponse;
import org.cubeville.cvmenu.CVMenu;

public class MenuList extends Command {

    private final CVMenu plugin;

    public MenuList(CVMenu plugin) {
        super("list");

        this.plugin = plugin;
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) {
        List<String> menus = plugin.getMenuManager().getAllMenuNames();
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
            if (i >= 1) m.addExtra(ChatColor.BLUE + "||");
            menuList.addExtra(m);
        }
        out.add(menuList);
        for (TextComponent o : out) {
            player.spigot().sendMessage(o);
        }
        return new CommandResponse("");
    }
}
