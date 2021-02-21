package com.rappelr.wearables.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.rappelr.wearables.Wearables;

import dev.lone.itemsadder.api.Events.ItemsAdderFirstLoadEvent;

public class ItemsAdderListener implements Listener {
	
	@EventHandler
	public void onLoad(ItemsAdderFirstLoadEvent e) {
		Wearables.getInstance().getLogger().info("ItemsAdder enabled, loading wearables...");
		Wearables.getInstance().onPostLoad();
	}

}
