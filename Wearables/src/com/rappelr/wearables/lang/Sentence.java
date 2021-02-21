package com.rappelr.wearables.lang;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Sentence {
	
	no_perm("&cYou don't have permission to do that"),
	item_dropped_to_floor("&cYour inventory was full, the item was dropped on the floor"),
	equip_action_bar("Right-click the air with an empty slot to unequip");
	
	private final String defaultBody;
	
	private String body;
	
	static void load(ConfigurationSection section) {
		if(section == null)
			return;
		
		for(String key : section.getKeys(false))
			for(Sentence s : values())
				if(s.name().equalsIgnoreCase(key.replace("-", "_")))
					s.body = section.getString(key);
	}
	
	public String get() {
		return ChatColor.translateAlternateColorCodes('&', body == null ? body = defaultBody : body);
	}

}
