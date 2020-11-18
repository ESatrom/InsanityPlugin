package me.Minecraftmage113.InsanityPlugin.helpers.gameplay;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import me.Minecraftmage113.InsanityPlugin.Main;
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
		if(plugin.runTime%(20*60*3)==0) {
			plugin.getSaver().save();
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
