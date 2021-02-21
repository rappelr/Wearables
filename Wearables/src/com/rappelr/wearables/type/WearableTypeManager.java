package com.rappelr.wearables.type;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.rappelr.wearables.Wearables;
import com.rappelr.wearables.config.ConfigUtil;

import lombok.Getter;
import lombok.NonNull;
import lombok.val;

public class WearableTypeManager {
	
	@Getter
	private final List<WearableType> wearables;
	
	private final ConfigUtil config;
	
	{
		wearables = new ArrayList<WearableType>();
		
		config = new ConfigUtil("wearables.yml", Wearables.getInstance(), true);
	}
	
	public void reload() {
		wearables.clear();
		
		config.load();
		
		if(config.get().getKeys(false).size() == 0) {
			Wearables.getInstance().getLogger().warning("no wearables set up");
			return;
		}
		
		for(String key : config.get().getKeys(false)) {
			if(!config.get().isConfigurationSection(key))
				continue;
			
			val wearable = WearableType.of(config.get().getConfigurationSection(key));
			
			if(wearable != null)
				wearables.add(wearable);
		}
	}
	
	public WearableType byId(@NonNull String id) {
		for(WearableType w : wearables)
			if(w.getIdentifier().equalsIgnoreCase(id))
				return w;
		return null;
	}
	
	public WearableType byItem(@NonNull ItemStack itemStack) {
		for(WearableType w : wearables)
			if(w.getItem().isSimilar(itemStack))
				return w;
		return null;
	}
}
