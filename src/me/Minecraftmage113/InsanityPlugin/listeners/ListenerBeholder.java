package me.Minecraftmage113.InsanityPlugin.listeners;

import java.util.Random;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.entities.Beholder;
import net.minecraft.server.v1_16_R2.EntityPlayer;

public class ListenerBeholder extends InsanityListener {
	public ListenerBeholder(Main plugin) { super(plugin); }
	
	@EventHandler
	public void onSpawn(EntitySpawnEvent event) {
		if(!(event.getEntity() instanceof ElderGuardian)) { return; }
		if(event.getEntity().getCustomName()==null) { return; }
		if(!event.getEntity().getCustomName().contains("Beholder")) { return; }
		new Beholder(plugin, (ElderGuardian) event.getEntity());
	}
	@EventHandler
	public void onRay(EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof ElderGuardian)) { return; }
		ElderGuardian beholder = (ElderGuardian) event.getDamager();
		if(beholder.getCustomName()==null) { return; }
		if(!beholder.getCustomName().contains("Beholder")) { return; }
		Entity target = event.getEntity();
		World world = target.getWorld();
		Location loc = target.getLocation();
		event.setDamage(0);
		event.setCancelled(true);
		if(event.getEntity() instanceof Player) {
			Player player = (Player) target;
			switch(1) {
			case 0: //Paralysis
				rayParalysis(player, world, loc);
				break;
			case 1: 
				rayAntimagic(player, world, loc, beholder);;
				break;
			case 2: 
				raySlow(player, world, loc);
				break;
			case 3: 
				rayPetrification(player, world, loc);
				break;
			case 4: 
				raySickness(player, world, loc);
				break;
			case 5:
				rayPush(player, world, loc, beholder);
				break;
			case 6: 
				rayNecrosis(player, world, loc, event);
				break;
			case 7: 
				rayDisintegration(player, world, loc, event);
				break;
			case 8:
				rayDeath(player, world, loc, event);
				break;
			case 9:
				rayCharm(player, world, loc);
				break;
			}
		} else {
			System.out.println("lolz");
			rayDeletion(target, world, loc);
		}
	}
	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		for(Beholder b : plugin.beholders) {
			if(b.is(event.getEntity())) {
				b.kill();
				return;
			}
		}
	}

	
	
	private void rayDeletion(Entity t, World w, Location l) {
		double wid = t.getWidth()/2.0, hig = t.getHeight()/2.0;
		w.playSound(l, Sound.ENTITY_GHAST_HURT, 10, 3);
		w.spawnParticle(Particle.ASH, l.add(0, hig, 0), (int) (hig*wid*wid*8*125), wid, hig, wid, 0);
		
		t.remove();
	}
	private void rayParalysis(Player p, World w, Location l) {
		w.playSound(l, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 10, 1);
		w.spawnParticle(Particle.VILLAGER_ANGRY, l.add(0, 1, 0), 60, .5, 1, .5, 0);
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 6*20, 100));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 6*20, 100));
		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 6*20, -100));
		p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 6*20, 100));
	}
	private void rayAntimagic(Player p, World w, Location l, ElderGuardian a) {
		w.playSound(l, Sound.BLOCK_CONDUIT_DEACTIVATE, 10, 1);
		w.spawnParticle(Particle.CRIT_MAGIC, l.add(0, 1, 0), 60, .5, 1, .5, 0);
		
		int counter = 0;
		double chance = 0;
		ItemStack[] armor = p.getInventory().getArmorContents();
		for(ItemStack item : armor) { chance+=item.getEnchantments().keySet().size(); }
		chance+=p.getInventory().getItemInMainHand().getEnchantments().keySet().size();
		if(chance<2) { chance/=2; }
		else if(chance<5) { chance = 1/chance; } //2 = 40%, 3 = 35% , 4 = 30%
		else if(chance<11) { chance = 2/chance; }
		else { chance = 3.5/chance; }
		System.out.println(chance);
		for(ItemStack item : armor) {
			if(item==null) { break; }
			for(Enchantment ench : item.getEnchantments().keySet()) {
				if(new Random().nextDouble()<=chance) {
					a.setAbsorptionAmount(a.getAbsorptionAmount()+10);
					counter++;
					if(item.getEnchantmentLevel(ench)==1) {
						item.removeEnchantment(ench);
					} else {
						item.addUnsafeEnchantment(ench, item.getEnchantmentLevel(ench)-1);
					}
				}
			}
		}
		p.getInventory().setArmorContents(armor);
		ItemStack held = p.getInventory().getItemInMainHand();
		if(held!=null) {
			for(Enchantment ench : held.getEnchantments().keySet()) {
				if(new Random().nextDouble()<=chance) {
					counter++;
					a.setAbsorptionAmount(a.getAbsorptionAmount()+10);
					if(held.getEnchantmentLevel(ench)==1) {
						held.removeEnchantment(ench);
					} else {
						held.addUnsafeEnchantment(ench, held.getEnchantmentLevel(ench)-1);
					}
				}
			}
		}
		p.getInventory().setItemInMainHand(held);
		System.out.println(counter);
	}
	private void rayPetrification(Player p, World w, Location l) {
		w.playSound(l, Sound.BLOCK_STONE_PLACE, 10, 1);
		w.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, l.add(0, 1, 0), 60, .5, 1, .5, 0);

		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10*20, 100));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10*20, 100));
		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10*20, 200));
		p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 10*20, 100));
		p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10*20, 100));
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10*20, 100));
		p.sendMessage("You have been turned to stone. You cannot be harmed while in this state.");
	}
	private void raySlow(Player p, World w, Location l) {
		w.playSound(l, Sound.BLOCK_CHAIN_PLACE, 10, 1);
		w.spawnParticle(Particle.DRIP_WATER, l.add(0, 1, 0), 60, .5, 1, .5, 0);

		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30*20, 2));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 30*20, 2));
	}
	private void raySickness(Player p, World w, Location l) {
		w.playSound(l, Sound.ENTITY_ZOMBIE_INFECT, 10, 1);
		w.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, l.add(0, 1, 0), 60, .5, 1, .5, 0);

		p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 45*20, 0));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60*20, 0));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60*20, 0));
		p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60*20, 0));
		p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6*20, 0));
		plugin.plague.put(p, 6*20);
	}
	private void rayDisintegration(Player p, World w, Location l, EntityDamageByEntityEvent e) {
		w.playSound(l, Sound.BLOCK_SAND_HIT, 10, 1);
		w.spawnParticle(Particle.CLOUD, l.add(0, 1, 0), 60, .5, 1, .5, 0);
		
		e.setCancelled(false);
		e.setDamage(20);
	}
	private void rayNecrosis(Player p, World w, Location l, EntityDamageByEntityEvent e) {
		w.playSound(l, Sound.ENTITY_ZOMBIE_HURT, 10, 1);
		w.spawnParticle(Particle.DAMAGE_INDICATOR, l.add(0, 1, 0), 30, .5, 1, .5, 0);
		
		p.damage(5);
		e.setCancelled(false);
		e.setDamage(5);
	}
	private void rayDeath(Player p, World w, Location l, EntityDamageByEntityEvent e) {
		if(new Random().nextInt(10)==0) {
			w.playSound(l, Sound.BLOCK_ANVIL_LAND, 100, 1);
			w.spawnParticle(Particle.CRIMSON_SPORE, l.add(0, 1, 0), 1000, .5, 1, .5, 0);
			
			p.damage(2*p.getHealth());
			e.setCancelled(false);
			e.setDamage(1);
		} else {
			rayNecrosis(p, w, l, e);
		}
	}
	private void rayCharm(Player p, World w, Location l) {
		w.playSound(l, Sound.ENTITY_VILLAGER_YES, 10, 1);
		w.spawnParticle(Particle.HEART, l.add(0, 1, 0), 60, .5, 1, .5, 0);
		
		Predicate<Entity> check = (Entity e) -> { if(e instanceof Player) { if(e.equals(p)) { return false; } else { return true; } } else { return false; } };
		for(Entity e : w.getNearbyEntities(l, 6, 6, 6, check)) {
			((EntityPlayer) p).resetAttackCooldown();
			p.attack(e);
		}
	}
	private void rayPush(Player p, World w, Location l, ElderGuardian a) {
		w.playSound(l, Sound.ITEM_ELYTRA_FLYING, 10, 1);
		w.spawnParticle(Particle.FLASH, l.add(0, 1, 0), 60, .5, 1, .5, 0);
		Vector smack = a.getEyeLocation().getDirection().add(new Vector(0, .5, 0));
		smack = smack.multiply(1/smack.length());
		p.setVelocity(smack.multiply(15));
	}
}
