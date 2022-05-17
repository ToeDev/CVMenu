package org.cubeville.cvmenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.cubeville.commons.commands.CommandParser;
import org.cubeville.cvmenu.commands.*;
import org.cubeville.cvmenu.menu.MenuContainer;
import org.cubeville.cvmenu.menu.MenuManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CVMenu extends JavaPlugin {

    private static CVMenu cvMenu;

    private MenuManager menuManager;
    private CommandParser commandParser;
    private Logger logger;

    public void onEnable() {
        this.logger = getLogger();
        cvMenu = this;

        final File dataDir = getDataFolder();
        if(!dataDir.exists()) {
            dataDir.mkdirs();
        }
        File configFile = new File(dataDir, "config.yml");
        if(!configFile.exists()) {
            try {
                configFile.createNewFile();
                final InputStream inputStream = this.getResource(configFile.getName());
                final FileOutputStream fileOutputStream = new FileOutputStream(configFile);
                final byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = Objects.requireNonNull(inputStream).read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch(IOException e) {
                logger.log(Level.WARNING, ChatColor.LIGHT_PURPLE + "Unable to generate config file", e);
                throw new RuntimeException(ChatColor.LIGHT_PURPLE + "Unable to generate config file", e);
            }
        }

        ConfigurationSerialization.registerClass(MenuContainer.class, "MenuContainer");
        ConfigurationSerialization.registerClass(MenuManager.class, "MenuManager");

        menuManager = (MenuManager) getConfig().get("MenuManager");
        if(menuManager == null) menuManager = new MenuManager();
        menuManager.setManager(this);

        commandParser = new CommandParser();
        commandParser.addCommand(new MenuCreate(this));
        commandParser.addCommand(new MenuRemove(this));
        commandParser.addCommand(new MenuEdit(this));
        commandParser.addCommand(new MenuList(this));
        commandParser.addCommand(new MenuInfo(this));

        commandParser.addCommand(new MenuAddCondition(this));
        commandParser.addCommand(new MenuRemoveCondition(this));
        commandParser.addCommand(new MenuAddEvent(this));
        commandParser.addCommand(new MenuRemoveEvent(this));
        commandParser.addCommand(new MenuAddCommand(this));
        commandParser.addCommand(new MenuRemoveCommand(this));

        commandParser.addCommand(new MenuSetClose(this));
        commandParser.addCommand(new MenuRunCmdsFromPlayer(this));

        commandParser.addCommand(new MenuDisplay(this));

        Bukkit.getPluginManager().registerEvents(new MenuListener(this), this);
        logger.info(ChatColor.LIGHT_PURPLE + "Plugin Enabled Successfully");
    }

    public static CVMenu getCvMenu() {
        return cvMenu;
    }

    public MenuManager getMenuManager() {
        return this.menuManager;
    }

    public void saveMenuManager() {
        this.menuManager.removeTempMenus();
        getConfig().set("MenuManager", menuManager);
        saveConfig();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("menu")) {
            return commandParser.execute(sender, args);
        }
        return false;
    }

    public void onDisable() {
        cvMenu = null;
        logger.info(ChatColor.LIGHT_PURPLE + "Plugin Disabled Successfully");
    }
}
