package com.rappelr.wearables.storage;

import org.bukkit.entity.Player;

import com.rappelr.wearables.Wearables;
import com.rappelr.wearables.entity.WearableEntity;
import com.rappelr.wearables.type.WearableType;

import lombok.val;

public class WearableStore {
	
	private static final long DEFAULT_STORE_IV = 20 * 60;
	
	private final StoreFile file;
	
	{
		val file = StoreFile.of(Wearables.getInstance(), Wearables.getInstance().getDataFolder().getAbsolutePath() + "/store.txt", true);
		
		if(file == null)
			Wearables.getInstance().getLogger().warning("store file failed to create/load");
		
		this.file = file;
	}
	
	public void schedule() {
		file.scheduleSave(Wearables.getInstance().getConfiguration().get().getLong("store-save-interval", DEFAULT_STORE_IV));
	}
	
	public void save(boolean async) {
		file.save(async);
	}
	
	public void store(WearableEntity wearableEntity) {
		file.set(wearableEntity.getPlayer().getUniqueId().toString(), wearableEntity.getWearable().getIdentifier());
	}
	
	public void remove(Player player) {
		file.remove(player.getUniqueId().toString());
	}
	
	public WearableType get(Player player) {
		val wid = file.get(player.getUniqueId().toString());
		
		if(wid == null)
			return null;
		
		return Wearables.getInstance().getTypeManager().byId(wid);
	}

}
