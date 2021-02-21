package com.rappelr.wearables.lang;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.rappelr.wearables.Wearables;

import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;

public class LanguageManager {
	
	{
		reload();
	}
	
	public void reload() {
		Sentence.load(Wearables.getInstance().getConfiguration().get().getConfigurationSection("lang"));
	}

	public void sendActionBar(Player player, String message) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a((String)("{\"text\": \"" + message + "\"}")), ChatMessageType.GAME_INFO, player.getUniqueId()));
	}

}
