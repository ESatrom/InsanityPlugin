package me.Minecraftmage113.InsanityPlugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Minecraftmage113.InsanityPlugin.Main;

/**
 * A command to remove all active effects from a player (modifiers, potion, and tags)
 * 
 * @author Elijah
 * @version 24/Sept/2020
 */
public class CommandCleanse extends InsanityCommand {

	public CommandCleanse(Main plugin) {
		super(plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		/*
		 * Creates player object.
		 * If sent by console, errors console player necessary
		 */
		Player p;
		if (sender instanceof Player) {
			p = (Player) sender;
			for(AttributeModifier m : p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers()) {
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(m);
			}
			for(PotionEffect e : p.getActivePotionEffects()) {
				p.removePotionEffect(e.getType());
			}
			for(String s : p.getScoreboardTags()) {
				p.removeScoreboardTag(s);
			}
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20*60, 0, false, false, false));
			p.sendMessage(ChatColor.LIGHT_PURPLE + "Your body has been purged of external influence.");
			return true;
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "This command must be run by a player.");
			return true;
		}
	}

}
