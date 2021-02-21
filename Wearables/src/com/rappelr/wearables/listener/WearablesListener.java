package com.rappelr.wearables.listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import com.rappelr.wearables.Wearables;

import lombok.val;

public class WearablesListener implements Listener {
	
	{
		reload();
	}
	
	private List<String> escapedCommands;

	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin(PlayerJoinEvent event) {
		Wearables.getInstance().getWearableManager().spawn(event.getPlayer());
	}	

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onDismount(EntityDismountEvent event) {
		if(event.getEntity() instanceof ArmorStand)
			Wearables.getInstance().getWearableManager().onDismount((ArmorStand) event.getEntity());
	}		
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onDeath(PlayerDeathEvent event) {
		Wearables.getInstance().getWearableManager().death(event.getEntity());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onMove(PlayerMoveEvent event) {
		val target = event.getPlayer().getLocation();
		for(Entity e : event.getPlayer().getPassengers())
			if(e instanceof ArmorStand)
				e.setRotation(target.getYaw(), target.getPitch());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onSneak(PlayerToggleSneakEvent event) {
		Wearables.getInstance().getWearableManager().sync(event.getPlayer(), event.isSneaking());
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onInteract(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_AIR)
			return;
		
		if(event.getPlayer().getInventory().getItemInMainHand() != null) {
			val wearable = Wearables.getInstance().getTypeManager().byItem(event.getPlayer().getInventory().getItemInMainHand());
			
			if(wearable != null) {
				event.getPlayer().getInventory().setItemInMainHand(null);
				Wearables.getInstance().getInteractManager().handleClick(event.getPlayer(), wearable);
				event.setCancelled(true);
			}
		}
		
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		for(String command : escapedCommands)
			if(event.getMessage().startsWith("/" + command)) {
				Wearables.getInstance().getWearableManager().escape(event.getPlayer());
				break;
			}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getClickedInventory() == null && event.getAction() == InventoryAction.NOTHING && event.getSlot() == -999 && event.getClick() == ClickType.RIGHT) {
			//casts HumanEntity to usable Player
			Player player = Bukkit.getPlayerExact(event.getWhoClicked().getName());
			
			if(player == null)
				return;
			
			if(event.getView().getTopInventory().getHolder().equals(player))
				Wearables.getInstance().getInteractManager().handleInventoryClick(player, event.getView());
			
		}
	}

	public void reload() {
		escapedCommands = Wearables.getInstance().getConfiguration().get().getStringList("escape.commands");
	}
}
