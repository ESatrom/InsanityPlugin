package me.Minecraftmage113.InsanityPlugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.entities.Beholder;

public class CommandSpawn extends InsanityCommand {

	public CommandSpawn(Main plugin) { super(plugin); }

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Console may not spawn bosses.");
			return true;
		}
		Player p = (Player) sender;
		if(args.length < 1) {
			p.sendMessage("Boss specification required");
			return true;
		}
		switch(args[0].toLowerCase()) {
		case "beholder":
			new Beholder(plugin, (ElderGuardian) p.getWorld().spawnEntity(p.getLocation(), EntityType.ELDER_GUARDIAN));
		}
		return true;
	}
}
