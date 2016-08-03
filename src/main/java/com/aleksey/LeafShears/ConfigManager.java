package com.aleksey.LeafShears;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
	private FileConfiguration file;
	private int clearCubeSize;
	
	public int getCutCubeSize() {
		return this.clearCubeSize;
	}
	
	public void load(FileConfiguration file) {
		this.file = file;
		
		this.clearCubeSize = getInt("Settings.ClearCubeSize", 3);
	}

    private int getInt(String path, int defaultData) {
        if (this.file.get(path) == null)
            this.file.set(path, defaultData);
        
        return this.file.getInt(path, defaultData);
    }
}
