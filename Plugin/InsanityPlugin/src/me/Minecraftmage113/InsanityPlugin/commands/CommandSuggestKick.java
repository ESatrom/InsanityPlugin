package me.Minecraftmage113.InsanityPlugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.helpers.gameplay.TimedEvents;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandSuggestKick extends InsanityCommand {

	public CommandSuggestKick(Main plugin) { super(plugin); }

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length<1) {
			sender.sendMessage(ChatColor.DARK_RED + "A player must be supplied!");
			return true;
		}
		Player target = plugin.getServer().getPlayer(args[0]);
		if(target==null) {
			sender.sendMessage(ChatColor.RED + "Invalid player supplied.");
			return true;
		}
		if(target.getName().equals(sender.getName())) {
			TimedEvents.KickList.clearKick(target);
			return true;
		} else {
			TextComponent message = new TextComponent("Another player believes you to be AFK, and has requested that you leave to lessen the draw on server resources. Click here to affirm that you are still active.");
			message.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/sKick " + target.getName()));
			target.spigot().sendMessage(message);
			sender.sendMessage(target.getName() + " now has 1 minute to affirm that they are online, or they will be kicked.");
			TimedEvents.KickList.initiateKick(sender, target);
			return true;
		}
	}
}
