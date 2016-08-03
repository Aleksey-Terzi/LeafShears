package com.aleksey.LeafShears;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.aleksey.LeafShears.command.LeafShearsCommand;
import com.aleksey.LeafShears.listener.EventListener;

public class LeafShears extends JavaPlugin {
    private static LeafShears instance;
    public static LeafShears getInstance() {
    	return instance;
    }
	
    public static Logger getPluginLogger() {
    	return instance.getLogger();
    }
    
    private static ConfigManager configManager;
    public static ConfigManager getConfigManager() {
    	return configManager;
    }
    
    @Override
    public void onEnable() {
    	instance = this;
    	
        // Load configuration
    	configManager = new ConfigManager();
        configManager.load(getConfig());
        saveConfig();
    	
        // register events
        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return LeafShearsCommand.onCommand(sender, command, label, args);
    }
}
