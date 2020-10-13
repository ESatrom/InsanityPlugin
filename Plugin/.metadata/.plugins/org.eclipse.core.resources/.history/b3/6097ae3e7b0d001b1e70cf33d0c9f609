package me.Minecraftmage113.InsanityPlugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.Minecraftmage113.InsanityPlugin.Main;

public class CommandLoad extends InsanityCommand {

	public CommandLoad(Main plugin) { super(plugin); }

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage("Loading...");
		plugin.getSaver().load();
		sender.sendMessage("Loaded!");
		return true;
	}

}
