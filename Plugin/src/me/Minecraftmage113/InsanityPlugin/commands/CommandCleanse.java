package me.Minecraftmage113.InsanityPlugin.commands;

import java.util.Collection;
import java.util.Set;

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
	
	public void cleanseModifier(Player p, Attribute a) {
		if(p.getAttribute(a)!=null) {
			Collection<AttributeModifier> modifiers = p.getAttribute(a).getModifiers();
			for(AttributeModifier m : modifiers) {
				p.getAttribute(a).removeModifier(m);
			}
		}
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
			cleanseModifier(p, Attribute.GENERIC_ARMOR);
			cleanseModifier(p, Attribute.GENERIC_ARMOR_TOUGHNESS);
			cleanseModifier(p, Attribute.GENERIC_ATTACK_DAMAGE);
			cleanseModifier(p, Attribute.GENERIC_ATTACK_KNOCKBACK);
			cleanseModifier(p, Attribute.GENERIC_ATTACK_SPEED);
			cleanseModifier(p, Attribute.GENERIC_FLYING_SPEED);
			cleanseModifier(p, Attribute.GENERIC_KNOCKBACK_RESISTANCE);
			cleanseModifier(p, Attribute.GENERIC_LUCK);
			cleanseModifier(p, Attribute.GENERIC_MAX_HEALTH);
			cleanseModifier(p, Attribute.GENERIC_MOVEMENT_SPEED);
			Collection<PotionEffect> effects = p.getActivePotionEffects();
			for(PotionEffect e : effects) {
				p.removePotionEffect(e.getType());
			}
			Set<String> tags = p.getScoreboardTags();
			for(String s : tags) {
				p.removeScoreboardTag(s);
			}
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20*60, 9, false, false, false));
			p.sendMessage(ChatColor.LIGHT_PURPLE + "Your body has been purged of external influence.");
			return true;
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "This command must be run by a player.");
			return true;
		}
	}

}
