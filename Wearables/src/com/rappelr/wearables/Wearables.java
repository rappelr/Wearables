package com.rappelr.wearables;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.rappelr.wearables.config.ConfigUtil;
import com.rappelr.wearables.lang.LanguageManager;
import com.rappelr.wearables.listener.ItemsAdderListener;
import com.rappelr.wearables.listener.WearablesListener;
import com.rappelr.wearables.player.PlayerInteractManager;
import com.rappelr.wearables.protocol.ProtocolListener;
import com.rappelr.wearables.type.WearableTypeManager;
import lombok.Getter;

public class Wearables extends JavaPlugin {
	
	@Getter
	private static Wearables instance;
	
	@Getter
	private WearableManager wearableManager;
	
	@Getter
	private WearableTypeManager typeManager;
	
	@Getter
	private PlayerInteractManager interactManager;
	
	@Getter
	private LanguageManager languageManager;
	
	@Getter
	private ConfigUtil configuration;
	
	private WearablesListener listener;
	
	{
		instance = this;
	}
	
	@Override
    public void onEnable() {
		configuration = new ConfigUtil(this);
		configuration.load();
		
		typeManager = new WearableTypeManager();
		wearableManager = new WearableManager();
		languageManager = new LanguageManager();
		interactManager = new PlayerInteractManager();
		listener = new WearablesListener();
		
		new ProtocolListener();
		
		Bukkit.getPluginManager().registerEvents(new ItemsAdderListener(), this);
	}

	@Override
    public void onDisable() {
		wearableManager.close();
	}
	
	public void onPostLoad() {
		typeManager.reload();
		wearableManager.open();

		getCommand("wearables").setExecutor(new WearablesCommand(this));
		Bukkit.getPluginManager().registerEvents(listener, this);
	}

	public void reload() {
		getLogger().info("reloading...");
		configuration.load();
		typeManager.reload();
		wearableManager.reload();
		languageManager.reload();
		listener.reload();
		getLogger().info("reload complete!");
	}
}
