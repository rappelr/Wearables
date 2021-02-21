package com.rappelr.wearables.protocol;

import org.bukkit.Sound;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.rappelr.wearables.Wearables;

public class ProtocolListener {

	private final ProtocolManager protocolManager;

	public ProtocolListener() {
		protocolManager = ProtocolLibrary.getProtocolManager();

		setup();
	}

	private void setup() {
		protocolManager.addPacketListener(new PacketAdapter(Wearables.getInstance(), ListenerPriority.NORMAL,
				PacketType.Play.Server.NAMED_SOUND_EFFECT) {
			@Override
			public void onPacketSending(PacketEvent event) {
				for(Sound s : event.getPacket().getSoundEffects().getValues())
					if(s == Sound.ITEM_ARMOR_EQUIP_GENERIC)
						event.setCancelled(true);
			}
		});
	}

}
