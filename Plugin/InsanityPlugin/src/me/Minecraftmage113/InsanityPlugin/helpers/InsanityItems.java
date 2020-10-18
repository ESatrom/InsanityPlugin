package me.Minecraftmage113.InsanityPlugin.helpers;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Minecraftmage113.InsanityPlugin.items.ItemEnderPorter;

public enum InsanityItems {
	ENDER_PORTER, //1
	COFFEE, //2
	BLOCK_FLAGGER, //3
	DEPRESSION_WAND, //4
	BLINK_WAND, //5
	LASSO, //6
	REAPERS_SCYTHE, //7
	CHALK, //8
	CHALK_PLACED, //9
	CHALK_PLACED_TURN, //10
	SOULBINDING_ROCK; //11
	
	static final ItemStack[] items = {
		new ItemEnderPorter(), //1
		new PotionBuilder().setColor(Color.fromRGB(128, 64, 0)).
			addEffects(new PotionEffect(PotionEffectType.SPEED, 6000, 3, true, false, true), 
				new PotionEffect(PotionEffectType.FAST_DIGGING, 3600, 1, false, false, false),
				new PotionEffect(PotionEffectType.JUMP, 1200, 0, false, false, false), 
				new PotionEffect(PotionEffectType.ABSORPTION, 1200, 1, false, false, false)).
			addFlag(ItemFlag.HIDE_POTION_EFFECTS).
			setModelData(COFFEE.modelData()).
			setName(ChatColor.RESET + "Coffee").
			build(), //2
		new ItemBuilder(Material.STICK).
			setModelData(BLOCK_FLAGGER.modelData()).
			build(), //3
		new ItemBuilder(Material.STICK).
			setModelData(DEPRESSION_WAND.modelData()).
			setName(ChatColor.RESET + "" + ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "The Big Sad").
			build(), //4
		new ItemBuilder(Material.ENDER_PEARL).
			setModelData(BLINK_WAND.modelData()).
			setName("Not yet implemented").
			build(), //5
		new ItemBuilder(Material.LEAD).
			setModelData(LASSO.modelData()).
			setName(ChatColor.RESET + "" + ChatColor.GOLD + "Lasso").
			setLore("Punch a mob to collect it into your lasso.", 
				"Right click a block with a full lasso to release a mob.", 
				("Currently Contained: " + ChatColor.DARK_GRAY + "Nothing"), 
				(ChatColor.BLACK+""+ChatColor.MAGIC+"|-1")).
			build(), //6
		new ItemBuilder(Material.NETHERITE_HOE).
			setModelData(REAPERS_SCYTHE.modelData()).
			setName(ChatColor.RESET + "" + ChatColor.DARK_GRAY + "Reaper's Scythe").
			addEnch(Enchantment.VANISHING_CURSE, 1).
			setLore("Punch a player to harvest their head.").
			addFlag(ItemFlag.HIDE_ENCHANTS).
			build(), //7
		new ItemBuilder(Material.STICK).
			setModelData(CHALK.modelData()).
			setName(ChatColor.RESET + "Chalk").
			build(), //8
		new ItemBuilder(Material.STICK).
			setModelData(CHALK_PLACED.modelData()).
			build(), //9
		new ItemBuilder(Material.STICK).
			setModelData(CHALK_PLACED_TURN.modelData()).
			build(), //10
		new ItemBuilder(Material.STICK).
			setModelData(SOULBINDING_ROCK.modelData()).
			setName(ChatColor.translateAlternateColorCodes('&', "&r&k1&rSoulbinding Stone&k1")).
			build(), //11
	};
	
	public int modelData() {
		switch(this) {
		case ENDER_PORTER:
			return 1;
		case COFFEE:
			return 2;
		case BLOCK_FLAGGER:
			return 3;
		case DEPRESSION_WAND:
			return 4;
		case BLINK_WAND:
			return 5;
		case LASSO:
			return 6;
		case REAPERS_SCYTHE:
			return 7;
		case CHALK:
			return 8;
		case CHALK_PLACED:
			return 9;
		case CHALK_PLACED_TURN:
			return 10;
		case SOULBINDING_ROCK:
			return 11;
		}
		return -1;
	}
	public boolean instance(ItemStack item) {
		return item!=null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData()==modelData();
	}
	public ItemStack build() {
		return items[modelData()-1].clone();
	}
}