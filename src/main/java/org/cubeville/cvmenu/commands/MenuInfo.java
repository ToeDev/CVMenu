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
import org.cubeville.commons.commands.*;
import org.cubeville.cvmenu.CVMenu;
import org.cubeville.cvmenu.menu.MenuContainer;
import org.cubeville.cvmenu.menu.MenuManager;

public class MenuInfo extends Command {

    private final CVMenu plugin;

    public MenuInfo(CVMenu plugin) {
        super("info");
        addBaseParameter(new CommandParameterString(CommandParameterString.NO_SPECIAL_CHARACTERS));
        addParameter("slot", true, new CommandParameterInteger());

        this.plugin = plugin;
    }

    @Override
    public CommandResponse execute(Player player, Set<String> flags, Map<String, Object> parameters, List<Object> baseParameters) throws CommandExecutionException {
        MenuManager manager = plugin.getMenuManager();
        if(manager.getMenu((String) baseParameters.get(0)) == null) {
            throw new CommandExecutionException(org.bukkit.ChatColor.RED + "Menu " + org.bukkit.ChatColor.GOLD + baseParameters.get(0) + org.bukkit.ChatColor.RED + " doesn't exist!");
        }
        MenuContainer menu = manager.getMenu((String) baseParameters.get(0));
        List<TextComponent> out = new ArrayList<>();
        if(parameters.containsKey("slot")) {
            if((int) parameters.get("slot") + 1 > menu.getSize() || (int) parameters.get("slot") < 0) {
                throw new CommandExecutionException(org.bukkit.ChatColor.RED + "Menu " + org.bukkit.ChatColor.GOLD + baseParameters.get(0) + org.bukkit.ChatColor.RED + " only contains " + ChatColor.GOLD + menu.getSize() + ChatColor.RED + " slots! Remember, the first slot is slot 0!");
            } else {
                int slot = (int) parameters.get("slot");
                out.add(new TextComponent("§2--------------------§a" + menu.getMenuName() + ":" + slot + "§2--------------------"));
                out.add(new TextComponent("§6Item: §9" + menu.getSlotItem(slot)));
                TextComponent condLbl = new TextComponent("§6BQ Conditions: ");
                TextComponent condLblClk = new TextComponent("§2[§9Add§2]");
                condLblClk.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/menu addcondition " + menu.getMenuName() + " slot:" + slot + " condition:"));
                condLblClk.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add condition to slot: " + slot).create()));
                condLbl.addExtra(condLblClk);
                out.add(condLbl);
                if(menu.getConditionsBQ(slot) != null && !menu.getConditionsBQ(slot).isEmpty()) {
                    for(String condition : menu.getConditionsBQ(slot)) {
                        TextComponent cond = new TextComponent("  - "+ condition);
                        cond.setColor(ChatColor.BLUE);
                        cond.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/menu removecondition " + condition));
                        cond.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Remove condition: " + condition).create()));
                        out.add(cond);
                    }
                }
                TextComponent eveLbl = new TextComponent("§6BQ Events: ");
                TextComponent eveLblClk = new TextComponent("§2[§9Add§2]");
                eveLblClk.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/menu addevent " + menu.getMenuName() + " slot:" + slot + " event:"));
                eveLblClk.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add event to slot: " + slot).create()));
                eveLbl.addExtra(eveLblClk);
                out.add(eveLbl);
                if(menu.getEventsBQ(slot) != null && !menu.getEventsBQ(slot).isEmpty()) {
                    for(String event : menu.getEventsBQ(slot)) {
                        TextComponent eve = new TextComponent("  - "+ event);
                        eve.setColor(ChatColor.BLUE);
                        eve.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/menu removeevent " + event));
                        eve.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Remove event: " + event).create()));
                        out.add(eve);
                    }
                }
                TextComponent cmdLbl = new TextComponent("§6Commands: ");
                TextComponent cmdLblClk = new TextComponent("§2[§9Add§2]");
                cmdLblClk.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/menu addcommand " + menu.getMenuName() + " slot:" + slot + " command:"));
                cmdLblClk.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add command to slot: " + slot).create()));
                cmdLbl.addExtra(cmdLblClk);
                out.add(cmdLbl);
                if(menu.getCommands(slot) != null && !menu.getCommands(slot).isEmpty()) {
                    for(String command : menu.getCommands(slot)) {
                        TextComponent cmd = new TextComponent("  - "+ command);
                        cmd.setColor(ChatColor.BLUE);
                        cmd.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/menu removecommand " + command));
                        cmd.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Remove command: " + command).create()));
                        out.add(cmd);
                    }
                }
            }
        } else {
            out.add(new TextComponent("§2--------------------§a" + menu.getMenuName() + "§2--------------------"));








        }
        for (TextComponent o : out) {
            player.spigot().sendMessage(o);
        }
        return new CommandResponse("");
    }
}
