package com.rappelr.wearables.type;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.lone.itemsadder.api.ItemsAdder;
import lombok.val;
import net.md_5.bungee.api.ChatColor;

public class CrouchSensitiveWearable extends WearableType {
	
	private final ItemStack modelUncrouched, modelCrouched;

	public CrouchSensitiveWearable(String identifier, String formal, String equipSound, String unequipSound, ItemStack modelUncrouched, ItemStack modelCrouched) {
		super(identifier, formal, equipSound, unequipSound, true);
		
		this.modelUncrouched = modelUncrouched;
		this.modelCrouched = modelCrouched;
	}

	public static WearableType of(ConfigurationSection section) {
		val identifier = formatIdentifier(section.getName());
		
		val modelNameUncrouched = section.getString("model");
		val modelUncrouched =  modelNameUncrouched != null ? ItemsAdder.getCustomItem(modelNameUncrouched) : null;
		val modelNameCrouched = section.getString("model-sneaking");
		val modelCrouched = modelNameCrouched != null ? ItemsAdder.getCustomItem(modelNameCrouched) : null;
		
		val equipSound = section.getString("sound-equip");
		val unequipSound = section.getString("sound-unequip");

		if(modelUncrouched == null) {
			Bukkit.getLogger().warning("wearable model " + modelNameUncrouched + " not found in " + identifier);
			return null;
		}

		if(modelCrouched == null) {
			Bukkit.getLogger().warning("wearable model " + modelNameCrouched + " not found in " + identifier);
			return null;
		}

		val formal = section.contains("formal-name")
				? ChatColor.translateAlternateColorCodes('&', section.getString("formal-name"))
						: formatFormal(section.getName());
				
		return new CrouchSensitiveWearable(identifier, formal, equipSound, unequipSound, modelUncrouched, modelCrouched);
	}

	@Override
	public ItemStack getModel(Player player, boolean sneaking) {
		return sneaking ? modelCrouched : modelUncrouched;
	}

	@Override
	public ItemStack getItem() {
		return modelUncrouched;
	}

	@Override
	public String getDebug() {
		return "[Simple Wearable] id: " + getIdentifier() + ", name: " + getFormal() + ChatColor.RESET + ", model: " + modelUncrouched.getType();
	}
}
