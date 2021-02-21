package com.rappelr.wearables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import com.rappelr.wearables.entity.WearableEntity;
import com.rappelr.wearables.storage.WearableStore;
import com.rappelr.wearables.type.WearableType;

import lombok.NonNull;
import lombok.val;

public class WearableManager {

	private final WearableStore store;

	private final List<WearableEntity> entities;
	
	private final List<Player> escaped;
	
	private long escapeTime;

	{
		entities = new ArrayList<WearableEntity>();
		escaped = new ArrayList<Player>();
		store = new WearableStore();
		escapeTime = Wearables.getInstance().getConfiguration().get().getLong("escape.delay", 4l);
	}
	
	public void open() {
		store.schedule();
		Bukkit.getOnlinePlayers().forEach(this::spawn);
	}
	
	public void reload() {
		escapeTime = Wearables.getInstance().getConfiguration().get().getLong("escape.delay", 4l);
		store.save(true);
		store.schedule();
	}

	public void close() {
		store.save(false);
		entities.forEach(e -> e.remove());
		entities.clear();
	}

	public void onDismount(@NonNull ArmorStand host) {
		val entity = get(host);
		
		if(entity!= null) {
			entity.remove();
			entities.remove(entity);
		}
	}

	public WearableType remove(@NonNull Player player) {
		if(escaped.contains(player))
			return null;
		
		val entity = get(player);

		store.remove(player);

		if(entity != null) {
			entity.remove();
			entities.remove(entity);
			return entity.getWearable();
		}
		
		return null;
	}

	public void sync(@NonNull Player player, boolean sneaking) {
		val entity = get(player);

		if(entity != null)
			entity.sync(sneaking, false);
	}

	public WearableType set(@NonNull Player player, WearableType wearable) {
		if(escaped.contains(player))
			return null;
		
		if(wearable == null)
			return remove(player);

		WearableEntity entity = get(player);
		WearableType replaced = null;

		if(entity == null) {
			entity = WearableEntity.create(player, wearable);
			entities.add(entity);
		} else {
			replaced = entity.getWearable();
			entity.setWearable(wearable);
		}

		store.store(entity);
		return replaced;
	}

	public void spawn(@NonNull Player player) {
		escaped.remove(player);
		
		val wearable = store.get(player);

		if(wearable != null)
			set(player, wearable);
	}

	public void death(@NonNull Player player) {
		val wearable = remove(player);
		
		if(wearable != null)
			player.getWorld().dropItemNaturally(player.getLocation(), wearable.getItem());
	}
	
	public void escape(@NonNull Player player) {
		val entity = get(player);

		if(entity == null)
			return;
		
		escaped.add(player);
		store.store(entity);
		
		Bukkit.getScheduler().runTaskLater(Wearables.getInstance(), () -> unescape(player), escapeTime);
		
		entity.remove();
	}
	
	private void unescape(@NonNull Player player) {
		escaped.remove(player);
		
		val wearable = store.get(player);
		
		if(wearable == null)
			return;
		
		val replaced = set(player, wearable);
			
		if(replaced != null)
			Wearables.getInstance().getInteractManager().saveGive(player, replaced.getItem());
	}

	private WearableEntity get(Player player) {
		if(player.getPassengers().size() > 0)
			for(WearableEntity e : entities)
				if(e.getPlayer().equals(player))
					return e;
		return null;
	}

	private WearableEntity get(ArmorStand host) {
		for(WearableEntity e : entities)
			if(e.getHost().equals(host))
				return e;
		return null;
	}

}
