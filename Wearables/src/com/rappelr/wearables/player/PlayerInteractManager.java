package com.rappelr.wearables.player;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import com.rappelr.wearables.WearableManager;
import com.rappelr.wearables.Wearables;
import com.rappelr.wearables.lang.Sentence;
import com.rappelr.wearables.type.WearableType;
import lombok.val;

public class PlayerInteractManager {
	
	private final WearableManager manager;
	
	{
		manager = Wearables.getInstance().getWearableManager();
	}
	
	public void handleClick(Player player, WearableType wearable) {
		val wearing = manager.set(player, wearable);
		
		if(wearable != null)
			Wearables.getInstance().getLanguageManager().sendActionBar(player, Sentence.equip_action_bar.get());
		
		if(wearing != null) {
			saveGive(player, wearing.getItem());
		}
	}
	
	public void handleInventoryClick(Player player, InventoryView inventoryView) {
		val wearing = manager.remove(player);
		
		if(wearing != null) {
			inventoryView.setCursor(wearing.getItem());
			player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 1f, 1f);
		}
	}
	
	public void saveGive(Player player, ItemStack item) {
		if(player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR) {
			player.getInventory().setItemInMainHand(item);
			return;
		}
		
		val failed = player.getInventory().addItem(item);
		
		if(failed != null && !failed.isEmpty()) {
			player.sendMessage(Sentence.item_dropped_to_floor.get());
			failed.values().forEach(i -> player.getWorld().dropItem(player.getLocation(), i));
		}
	}

}
