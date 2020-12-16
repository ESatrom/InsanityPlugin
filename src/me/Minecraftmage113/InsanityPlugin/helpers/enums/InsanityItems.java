package me.Minecraftmage113.InsanityPlugin.helpers.enums;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.ImmutableMap;

import me.Minecraftmage113.InsanityPlugin.helpers.objects.ItemBuilder;
import me.Minecraftmage113.InsanityPlugin.helpers.objects.PotionBuilder;
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
	SOULBINDING_ROCK, //11
	ENDER_CORE,
	LIGHTNING_STAFF; //12
	
	static final Map<InsanityItems, ItemStack> items = ImmutableMap.<InsanityItems, ItemStack>builder().
		put(ENDER_PORTER, (ItemStack) new ItemEnderPorter()).
		put(COFFEE, new PotionBuilder().setColor(Color.fromRGB(128, 64, 0)).
			addEffects(new PotionEffect(PotionEffectType.SPEED, 6000, 3, true, false, true), 
				new PotionEffect(PotionEffectType.FAST_DIGGING, 3600, 1, false, false, false),
				new PotionEffect(PotionEffectType.JUMP, 1200, 0, false, false, false), 
				new PotionEffect(PotionEffectType.ABSORPTION, 1200, 1, false, false, false)).
			addFlag(ItemFlag.HIDE_POTION_EFFECTS).
			setModelData(COFFEE.modelData()).
			setName(ChatColor.RESET + "Coffee").
			build()).
		put(BLOCK_FLAGGER, new ItemBuilder(Material.STICK).
			setModelData(BLOCK_FLAGGER.modelData()).
			build()).
		put(DEPRESSION_WAND, new ItemBuilder(Material.STICK).
			setModelData(DEPRESSION_WAND.modelData()).
			setName(ChatColor.RESET + "" + ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "The Big Sad").
			build()).
		put(BLINK_WAND, new ItemBuilder(Material.ENDER_PEARL).
			setModelData(BLINK_WAND.modelData()).
			setName("Not yet implemented").
			build()).
		put(LASSO, new ItemBuilder(Material.LEAD).
			setModelData(LASSO.modelData()).
			setName(ChatColor.RESET + "" + ChatColor.GOLD + "Lasso").
			setLore("Punch a mob to collect it into your lasso.", 
				"Right click a block with a full lasso to release a mob.", 
				("Currently Contained: " + ChatColor.DARK_GRAY + "Nothing"), 
				(ChatColor.BLACK+""+ChatColor.MAGIC+"|-1")).
			build()).
		put(REAPERS_SCYTHE, new ItemBuilder(Material.NETHERITE_HOE).
			setModelData(REAPERS_SCYTHE.modelData()).
			setName(ChatColor.RESET + "" + ChatColor.DARK_GRAY + "Reaper's Scythe").
			addEnch(Enchantment.VANISHING_CURSE, 1).
			setLore("Punch a player to harvest their head.").
			addFlag(ItemFlag.HIDE_ENCHANTS).
			build()).
		put(CHALK, new ItemBuilder(Material.STICK).
			setModelData(CHALK.modelData()).
			setName(ChatColor.RESET + "Chalk").
			build()).
		put(CHALK_PLACED, new ItemBuilder(Material.STICK).
			setModelData(CHALK_PLACED.modelData()).
			build()).
		put(CHALK_PLACED_TURN, new ItemBuilder(Material.STICK).
			setModelData(CHALK_PLACED_TURN.modelData()).
			build()).
		put(SOULBINDING_ROCK, new ItemBuilder(Material.BRICK).
			setModelData(SOULBINDING_ROCK.modelData()).
			setName(ChatColor.translateAlternateColorCodes('&', "&r&k1&rSoulbinding Stone&k1")).
			build()).
		put(ENDER_CORE, new ItemBuilder(Material.ENDER_EYE).
			setModelData(ENDER_CORE.modelData()).
			setName(ChatColor.translateAlternateColorCodes('&', "&r&1&lEnder Core")).
			build()).
		put(LIGHTNING_STAFF, new ItemBuilder(Material.STICK).
			setModelData(LIGHTNING_STAFF.modelData()).
			setName(ChatColor.translateAlternateColorCodes('&', "&r&e&lLightning &b&lStaff")).
			setLore("§7Sneak+RClick: Summon lightning upon yourself, charging the staff", "§7RClick: Fire a lance of lightning").
			addFlag(ItemFlag.HIDE_ENCHANTS).
			build()).
	build();
	
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
		case ENDER_CORE:
			return 12;
		case LIGHTNING_STAFF:
			return 13;
		}
		return -1;
	}
	public boolean instance(ItemStack item) {
		return item!=null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData()==modelData();
	}
	public ItemStack build() {
		ItemStack result = items.get(this).clone();
		if(this.equals(BLINK_WAND)||this.equals(LIGHTNING_STAFF)) {
			ItemMeta meta = result.getItemMeta();
			List<String> lore = meta.getLore();
			lore.add("§0§k" + UUID.randomUUID());
			meta.setLore(lore);
			result.setItemMeta(meta);
		}
		return result;
	}
}