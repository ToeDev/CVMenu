package org.cubeville.cvmenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.cubeville.commons.commands.CommandParser;
import org.cubeville.cvloadouts.CVLoadouts;
import org.cubeville.cvmenu.commands.*;
import org.cubeville.cvmenu.menu.MenuContainer;
import org.cubeville.cvmenu.menu.MenuManager;
import pl.betoncraft.betonquest.BetonQuest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CVMenu extends JavaPlugin {

    private CVLoadouts cvLoadouts;
    private BetonQuest betonQuest;

    private MenuManager menuManager;
    private CommandParser commandParser;
    private Logger logger;

    public void onEnable() {
        this.logger = getLogger();
        cvLoadouts = CVLoadouts.getInstance();
        betonQuest = BetonQuest.getInstance();
        menuManager = new MenuManager();

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

        commandParser = new CommandParser();
        commandParser.addCommand(new MenuCreate(this));
        commandParser.addCommand(new MenuRemove(this));
        commandParser.addCommand(new MenuEdit(this));
        //commandParser.addCommand(new MenuInfo());
        //commandParser.addCommand(new MenuList());
        //commandParser.addCommand(new MenuDisplay());

        Bukkit.getPluginManager().registerEvents(new MenuListener(this), this);
        logger.info(ChatColor.LIGHT_PURPLE + "Plugin Enabled Successfully");
    }

    public CVLoadouts getCvLoadouts() {
        return this.cvLoadouts;
    }

    public BetonQuest getBetonQuest() {
        return this.betonQuest;
    }

    public MenuManager getMenuManager() {
        return this.menuManager;
    }

    public void saveMenuManager() {
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
        logger.info(ChatColor.LIGHT_PURPLE + "Plugin Disabled Successfully");
    }
}
