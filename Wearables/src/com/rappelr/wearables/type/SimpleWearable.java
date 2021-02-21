package com.rappelr.wearables.type;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.lone.itemsadder.api.ItemsAdder;
import lombok.val;
import net.md_5.bungee.api.ChatColor;

public class SimpleWearable extends WearableType {

	private final ItemStack model;

	private SimpleWearable(String identifier, String formal, String equipSound, String unequipSound,  ItemStack model) {
		super(identifier, formal, equipSound, unequipSound, false);

		this.model = model;
	}

	public static WearableType of(ConfigurationSection section) {
		val identifier = formatIdentifier(section.getName());
		
		val modelName = section.getString("model");
		val model = ItemsAdder.getCustomItem(modelName);
		
		val equipSound = section.getString("sound-equip");
		val unequipSound = section.getString("sound-unequip");

		if(model == null) {
			Bukkit.getLogger().warning("wearable model " + modelName + " not found in " + identifier);
			return null;
		}
		
		model.getItemMeta().setDisplayName(identifier);

		val formal = section.contains("formal-name")
				? ChatColor.translateAlternateColorCodes('&', section.getString("formal-name"))
						: formatFormal(section.getName());
				
		return new SimpleWearable(identifier, formal, equipSound, unequipSound, model);
	}

	@Override
	public ItemStack getModel(Player player, boolean sneaking) {
		return model;
	}

	@Override
	public ItemStack getItem() {
		return model;
	}

	@Override
	public String getDebug() {
		return "[Simple Wearable] id: " + getIdentifier() + ", name: " + getFormal() + ChatColor.RESET + ", model: " + model.getType();
	}

}
