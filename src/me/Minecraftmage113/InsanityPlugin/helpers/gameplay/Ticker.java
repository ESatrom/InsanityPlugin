package me.Minecraftmage113.InsanityPlugin.helpers.gameplay;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.entities.Beholder;
import me.Minecraftmage113.InsanityPlugin.helpers.enums.InsanityItems;
/**
 * Ticks twice per second.
 */
public class Ticker {
	Main plugin;
	public Ticker(Main plugin) { this.plugin=plugin; }
	
	public void tick() {
		coffee(plugin.getServer());
		updateGamemodes();
		TimedEvents.KickList.tick();
		TimedEvents.Restarter.tick();
		plugin.runTime++;
		for(Beholder e : plugin.beholders) { e.tick(plugin.runTime%20); }
		if(plugin.runTime%(20*60*3)==0) {
			plugin.getSaver().save();
		}
	}
	
	public void plagueTick() {
		List<Player> removal = new ArrayList<>();
		for(Player p : plugin.plague.keySet()) {
			if(!plugin.getServer().getOnlinePlayers().contains(p)) {
				removal.add(p);
			} else {
				Predicate<Entity> check = (Entity e) -> { if(e instanceof Player) { if(e.equals(p)) { return false; } else { return true; } } else { return false; } };
				for(Entity e : p.getWorld().getNearbyEntities(p.getLocation().add(0, 1, 1), .75, 1.25, .75, check)) {
					plugin.plague.put((Player) e, 20*6);
					((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 45*20, 0));
					((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60*20, 0));
					((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60*20, 0));
					((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60*20, 0));
					((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6*20, 0));
				}
			}
			plugin.plague.put(p, plugin.plague.get(p)-1);
		}
	}
	
	public void updateGamemodes() {
		for(OfflinePlayer p : plugin.creativePlayers) {
			p.getPlayer().setGameMode(GameMode.SURVIVAL);
			p.setOp(false);
		}
		plugin.creativePlayers = new ArrayList<OfflinePlayer>();
	}
	
	public void coffee(Server server) {
		plugin.coffeeDelay++;
		if(plugin.coffeeDelay>=2*60*60) {
			plugin.coffeeDelay=Main.r.nextInt(2*60*30);
			for(Player p : server.getOnlinePlayers()) {
				p.getInventory().addItem(InsanityItems.COFFEE.build());
			}
		}
	}
}
