package com.rappelr.wearables.entity;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.rappelr.wearables.type.WearableType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WearableEntity {
	
	@Getter
	private final Player player;

	@Getter
	private final ArmorStand host;

	@Getter
	private WearableType wearable;
	
	private boolean sneaking;
	
	public static WearableEntity create(@NonNull Player player, @NonNull WearableType wearable) {
		val host = createHost(player);
		
		val entity = new WearableEntity(player, host, wearable, player.isSneaking());
		
		entity.sync(entity.sneaking, true);
		
		wearable.playSound(player, true);
		
		return entity;
	}
	
	private static ArmorStand createHost(Player player) {
		val host = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
		host.setVisible(false);
		host.setMarker(true);
		host.setBasePlate(false);
		
		return host;
	}
	
	public void setWearable(@NonNull WearableType wearable) {
		if(this.wearable.equals(wearable))
			return;
		
		this.wearable = wearable;
		sync(player.isSneaking(), true);
		
		wearable.playSound(player, true);
	}
	
	public void sync(boolean sneaking, boolean force) {
		if(!player.getPassengers().contains(host))
			player.addPassenger(host);
		
		if(force || sneaking != this.sneaking) {
			host.getEquipment().setHelmet(wearable.getModel(player, sneaking));
			this.sneaking = sneaking;
		}
	}
	
	public void remove() {
		host.remove();
		
		wearable.playSound(player, false);
	}

}
