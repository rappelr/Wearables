package com.rappelr.wearables.type;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public abstract class WearableType {
	
	@Getter
	private final String identifier, formal, equipSound, unequipSound;
	
	@Getter
	private final boolean crouchSensitive;
	
	public static WearableType of(ConfigurationSection section) {
		if(section.contains("model-sneaking"))
			return CrouchSensitiveWearable.of(section);
		else
			return SimpleWearable.of(section);
	}
	
	protected ItemStack format(ItemStack item) {
		val result = item.clone();
		result.getItemMeta().setDisplayName(identifier);
		return result;
	}
	
	protected static String formatIdentifier(String identifier) {
		return identifier.toLowerCase().replace("-", "_").replace(" ", "_");
	}
	
	protected static String formatFormal(String identifier) {
		return identifier.toLowerCase().replace("_", " ");
	}
	
	public void playSound(Player player, boolean equip) {
		if(equip && equipSound != null)
			player.playSound(player.getLocation(), equipSound, 1f, 1f);
		if(!equip && unequipSound != null)
			player.playSound(player.getLocation(), unequipSound, 1f, 1f);
	}
	
	public abstract String getDebug();
	
	public abstract ItemStack getModel(Player player, boolean sneaking);
	
	public abstract ItemStack getItem();

}
