package me.Minecraftmage113.InsanityPlugin.entities;

import java.util.function.Predicate;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.helpers.enums.InsanityItems;
import me.Minecraftmage113.InsanityPlugin.helpers.objects.ItemBuilder;

public class Beholder {
	Main plugin;
	ElderGuardian entity;
	BossBar bar;
	
	public Beholder(Main plugin, ElderGuardian entity) {
		this.plugin = plugin;
		this.entity = entity;
		plugin.beholders.add(this);
		entity.getAttribute(Attribute.GENERIC_ARMOR).addModifier(new AttributeModifier("Armor", 10, Operation.ADD_NUMBER));
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(new AttributeModifier("Health", 230, Operation.ADD_NUMBER));
		entity.setHealth(5);
		entity.setCustomName("§lBeholder");
		bar = plugin.getServer().createBossBar("§lBeholder", BarColor.WHITE, BarStyle.SOLID, BarFlag.values());
		bar.setProgress(1);
		bar.setVisible(true);
	}
	public void tick(int tick){
		if(!bar.getPlayers().isEmpty()) {
			double progress = (entity.getHealth()+entity.getAbsorptionAmount())/270.0;
			bar.setProgress(progress>1?1:progress);
			if(progress>1) { bar.setColor(BarColor.YELLOW); } else { bar.setColor(BarColor.WHITE); }
		}
		if(tick==0) {
			bar.removeAll();
			Predicate<Entity> check = (Entity e) -> { if(e instanceof Player) { return true; } else { return false; } };
			for(Entity e : entity.getWorld().getNearbyEntities(entity.getLocation(), 50, 50, 50, check)) {
				bar.addPlayer((Player) e);
			}
			if(!bar.getPlayers().isEmpty()) {
				bar.addFlag(BarFlag.CREATE_FOG);
				bar.addFlag(BarFlag.DARKEN_SKY);
				bar.addFlag(BarFlag.PLAY_BOSS_MUSIC);
			} else {
				bar.removeFlag(BarFlag.CREATE_FOG);
				bar.removeFlag(BarFlag.DARKEN_SKY);
				bar.removeFlag(BarFlag.PLAY_BOSS_MUSIC);
			}
		}
	}
	public boolean is(Entity e) { return entity.equals(e); }
	public void kill() {
		bar.removeAll();
		plugin.beholders.remove(this);
		int dropTypes = -1; //0=player, 1=entity, 2=roast, 3=dumb, 4=none, 5=special
		String deathMessage = "";
		if((entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) && (((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager() instanceof Player )) {
			dropTypes = 0;
			deathMessage = ((Player) ((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager()).getName() + " has slain a beholder!";
		} else {
			switch(entity.getLastDamageCause().getCause()) {
			case CRAMMING:
			case SUICIDE:
			case SUFFOCATION:
			case FALLING_BLOCK:
				deathMessage = "A beholder has been put to death by bullying!";
				dropTypes = 3;
				break;
			case CONTACT:
			case DROWNING:
			case DRYOUT:
			case FLY_INTO_WALL:
			case HOT_FLOOR:
			case MELTING:
			case STARVATION:
				deathMessage = "A beholder died a very derpy death!";
				dropTypes = 3;
				break;
			case BLOCK_EXPLOSION:
			case ENTITY_EXPLOSION:
				deathMessage = "A beholder has been blown to bits!";
				dropTypes = 3;
				break;
			case DRAGON_BREATH:
				deathMessage = "A beholder died trying to fight a dragon!";
				dropTypes = 1;
				break;
			case ENTITY_ATTACK:
			case ENTITY_SWEEP_ATTACK:
			case PROJECTILE:
				deathMessage = "A beholder has died to an unknown assailant!";
				dropTypes = 1;
				break;
			case POISON:
			case WITHER:
				deathMessage = "A beholder has rotted away.";
				dropTypes = 4;
				//TODO beholder zombie
				break;
			case FALL:
				deathMessage = "A beholder fell to an untimely demise!";
				dropTypes = 3;
				break;
			case FIRE:
			case FIRE_TICK:
			case LAVA:
				deathMessage = "A beholder burned to death!";
				dropTypes = 2;
				break;
			case LIGHTNING:
			case VOID:
				deathMessage = "A beholder has been smote by the gods!";
				dropTypes = 5;
				break;
			case THORNS:
				deathMessage = "A beholder has died trying to slay a foe!";
				dropTypes = 5;
				break;
			case CUSTOM:
			case MAGIC:
			default:
				deathMessage = "A beholder has been slain through unknown means.";
				dropTypes = 3;
				break;
			}
		}
		ItemStack eye = new ItemBuilder(Material.GOLDEN_APPLE).setName("§rBeholder Eye").addEnch(Enchantment.LUCK, 1).addFlag(ItemFlag.HIDE_ENCHANTS).build();
		switch(dropTypes) {
		case 0:
			entity.getWorld().dropItemNaturally(entity.getLocation(), InsanityItems.LIGHTNING_STAFF.build());
			for(int i = 0; i < 4; i++) { entity.getWorld().dropItemNaturally(entity.getLocation(), eye); }
			break;
		case 1:
			entity.getWorld().dropItemNaturally(entity.getLocation(), eye);
			break;
		case 2:
			ItemStack roastEye = new ItemBuilder(Material.ENCHANTED_GOLDEN_APPLE).setName("§rRoast Beholder Eye").build();
			for(int i = 0; i < 4; i++) { entity.getWorld().dropItemNaturally(entity.getLocation(), roastEye); }
			break;
		case 3:
			entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.MUSIC_DISC_11));
			entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.MUSIC_DISC_13));
			entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.MUSIC_DISC_BLOCKS));
			entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.MUSIC_DISC_CAT));
			entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.MUSIC_DISC_CHIRP));
			entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.MUSIC_DISC_FAR));
			entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.MUSIC_DISC_MALL));
			entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.MUSIC_DISC_MELLOHI));
			entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.MUSIC_DISC_PIGSTEP));
			entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.MUSIC_DISC_STAL));
			entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.MUSIC_DISC_STRAD));
			entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.MUSIC_DISC_WAIT));
			entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.MUSIC_DISC_WARD));
			break;
		case 5:
			entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.BEDROCK, 1));
			entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.COMMAND_BLOCK, 1));
			for(int i = 0; i < 32; i++) { entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.DIAMOND)); }
			for(int i = 0; i < 16; i++) { entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.NETHERITE_INGOT)); }
		}
		plugin.getServer().broadcastMessage(deathMessage);
		entity.remove();
		entity = null;
	}
}
