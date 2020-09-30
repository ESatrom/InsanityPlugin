package me.Minecraftmage113.InsanityPlugin.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.items.ItemEnderPorter;

public class CommandPurchase extends InsanityCommand {

	public CommandPurchase(Main plugin) { super(plugin); }

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Console may not purchase items.");
			return true;
		}
		Player p = (Player) sender;
		if(args.length < 1) {
			p.sendMessage("Items available for purchase: \n"
					+ "EnderPorter - 15L - Fueled by ender pearls, teleports to a bound location.\n"
					+ "Fish - 5L - Use to perform a fish-slapping dance.");
			return true;
		}
		if(p.getInventory().firstEmpty()==-1) {
			p.sendMessage("Empty inventory slot required for purchase.");
			return true;
		}
		int experienceCost;
		ItemStack item;
		String message;
		switch(args[0].toLowerCase()) {
		case "enderporter":
			experienceCost = 15;
			item = new ItemEnderPorter();
			message = "You have successfully purchased an EnderPorter™. Fill it full of ender pearls to charge.";
			break;
		case "fish":
			experienceCost = 5;
			item = new ItemStack(Material.COD);
			ItemMeta meta = item.getItemMeta();
			meta.addEnchant(Enchantment.KNOCKBACK, 5, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			item.setItemMeta(meta);
			message = "You have successfully purchased a MontyFish™.";
			break;
		default:
			p.sendMessage("Invalid item specified.");
			return true;
		}
		if(p.getLevel()>=experienceCost) {
			p.giveExpLevels(-experienceCost);
			p.getInventory().addItem(item);
			p.sendMessage(message);
			return true;
		} else {
			p.sendMessage("Not enough experience to purchase.");
			return true;
		}
	}
	
	

}
