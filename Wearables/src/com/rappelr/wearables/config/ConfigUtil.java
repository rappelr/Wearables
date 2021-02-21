package com.rappelr.wearables.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigUtil {
	
	/*
	 * ConfigUtil v1.0.2
	 * by Rappelr
	 */
	
	private static final String SOURCE_PATH = "res/";
	private static final String DEFAULT_CONFIG = "config.yml";
	
	private final File file;
	
	private final YamlConfiguration yamlConfiguration;
	
	private final YamlUtil util;
	
	public ConfigUtil(JavaPlugin plugin) {
		this(DEFAULT_CONFIG, plugin, true);
	}
	
	public ConfigUtil(JavaPlugin plugin, boolean copy) {
		this(DEFAULT_CONFIG, plugin, copy);
	}
	
	public ConfigUtil(JavaPlugin plugin, boolean copy, boolean load) {
		this(DEFAULT_CONFIG, plugin, copy, load);
	}
	
	public ConfigUtil(String name, JavaPlugin plugin, boolean copy) {
		this(new File(plugin.getDataFolder(), name), plugin, copy ? name : null, false);
	}
	
	public ConfigUtil(String name, JavaPlugin plugin, String source) {
		this(new File(plugin.getDataFolder(), name), plugin, source, false);
	}
	
	public ConfigUtil(File file, JavaPlugin plugin, boolean copy) {
		this(file, plugin, file.getName(), false);
	}
	
	public ConfigUtil(File file, JavaPlugin plugin, String source) {
		this(file, plugin, source, false);
	}
	
	public ConfigUtil(String name, JavaPlugin plugin, boolean copy, boolean load) {
		this(new File(plugin.getDataFolder(), name), plugin, copy ? name : null, load);
	}
	
	public ConfigUtil(String name, JavaPlugin plugin, String source, boolean load) {
		this(new File(plugin.getDataFolder(), name), plugin, source, load);
	}
	
	public ConfigUtil(File file, JavaPlugin plugin, boolean copy, boolean load) {
		this(file, plugin, file.getName(), load);
	}
	
	public ConfigUtil(File file, JavaPlugin plugin, String source, boolean load) {
		this.file = file;
		
		util = new YamlUtil(plugin);
		yamlConfiguration = new YamlConfiguration();
		
		if(!file.exists()) {
			file.getParentFile().mkdirs();
			
			if(source != null)
				util.copy(SOURCE_PATH + source, file.getAbsolutePath());
			else
				create();
		}
				
		if(load)
			load();
	}
	
	public boolean create() {
		try {
			file.createNewFile();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean load() {
		try {
			yamlConfiguration.load(file);
			return true;
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean save() {
		try {
			get().save(file);
			return true;
        } catch (IOException e) {
        	e.printStackTrace();
			return false;
        }
	}

	public boolean contains(String key) {
		return yamlConfiguration.contains(key);
	}

	public void set(String key, Object value) {
		yamlConfiguration.set(key, value);
	}
	
	public YamlConfiguration get() {
		return yamlConfiguration;
	}
}
