package com.rappelr.wearables;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rappelr.wearables.lang.Sentence;
import com.rappelr.wearables.type.WearableType;

import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class WearablesCommand implements CommandExecutor {
	
	private final Wearables plugin;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(!sender.hasPermission("wearables.admin")) {
			val text = Sentence.no_perm.get();
			
			if(!text.isEmpty())
				sender.sendMessage(text);
			
			return true;
		}
		
		if(args.length == 0) {
			sender.sendMessage(ChatColor.GREEN + "Running Wearables " + ChatColor.WHITE + "[v" + Wearables.getInstance().getDescription().getVersion() + "]" + ChatColor.GREEN + ", commands:");
			sender.sendMessage(ChatColor.GREEN + "/.. reload " + ChatColor.WHITE + "reload plugin configuration");
			sender.sendMessage(ChatColor.GREEN + "/.. list " + ChatColor.WHITE + "lists all set up wearables");
			sender.sendMessage(ChatColor.GREEN + "/.. item <id> " + ChatColor.WHITE + "get the item of a wearable");
			sender.sendMessage(ChatColor.GREEN + "/.. equip <id> " + ChatColor.WHITE + "equip a wearable");
			sender.sendMessage(ChatColor.GREEN + "/.. remove " + ChatColor.WHITE + "remove your equipped wearable");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("list")) {
			val list = plugin.getTypeManager().getWearables();

			sender.sendMessage(ChatColor.GREEN + "Wearables has (" + ChatColor.WHITE + list.size() + ChatColor.GREEN + ") wearables loaded:");
			
			for(WearableType t : list)
				sender.sendMessage(t.getDebug());
			
			return true;
		}
		
		val player = sender instanceof Player ? (Player) sender : null;
		
		if(args[0].equalsIgnoreCase("reload")) {
			if(player != null)
				player.sendMessage(ChatColor.RED + "Reloading wearables...");
			
			plugin.reload();

			if(player != null)
				player.sendMessage(ChatColor.GREEN + "Reload complete!");
			
			return true;
		}
		
		if(args[0].equalsIgnoreCase("item")) {
			
			if(player == null) {
				sender.sendMessage(ChatColor.RED + "Subcommand item is only available for players");
				return true;
			}
			
			if(args.length < 2) {
				player.sendMessage(ChatColor.RED + "Usage: /wearables item <id>");
				return true;
			}
			
			val wearable = plugin.getTypeManager().byId(args[1]);
			
			if(wearable == null)
				player.sendMessage(ChatColor.RED + "Wearable " + args[1] + " not found");
			else {
				player.sendMessage(ChatColor.GREEN + "Given you " + wearable.getFormal());
				plugin.getInteractManager().saveGive(player, wearable.getItem());
			}
			
			return true;
		}
		
		if(args[0].equalsIgnoreCase("equip")) {
			
			if(player == null) {
				sender.sendMessage(ChatColor.RED + "Subcommand item is only available for players");
				return true;
			}
			
			if(args.length < 2) {
				player.sendMessage(ChatColor.RED + "Usage: /wearables equip <id>");
				return true;
			}
			
			val wearable = plugin.getTypeManager().byId(args[1]);
			
			if(wearable == null)
				player.sendMessage(ChatColor.RED + "Wearable " + args[1] + " not found");
			else {
				player.sendMessage(ChatColor.GREEN + "Given you " + wearable.getFormal());
				plugin.getWearableManager().set(player, wearable);
			}
			
			return true;
		}
		
		if(args[0].equalsIgnoreCase("remove")) {
			
			if(player == null) {
				sender.sendMessage(ChatColor.RED + "Subcommand item is only available for players");
				return true;
			}

			val removed = plugin.getWearableManager().remove(player);
			
			if(removed != null)
				player.sendMessage(ChatColor.GREEN + "Removed your " + removed.getFormal());
			else
				player.sendMessage(ChatColor.RED + "You are not wearing a wearable");
			
			return true;
		}
		
		sender.sendMessage(ChatColor.RED + "Subcommand " + args[0] + " not found, find a list of commands using /wearables");
		return true;
	}

}
