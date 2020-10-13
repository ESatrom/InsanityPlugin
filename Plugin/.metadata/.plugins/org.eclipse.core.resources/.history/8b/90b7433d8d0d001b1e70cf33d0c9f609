package me.Minecraftmage113.InsanityPlugin.listeners;

import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreeperPowerEvent;

import me.Minecraftmage113.InsanityPlugin.Main;

public class ListenerCharge extends InsanityListener {
	public ListenerCharge(Main plugin) { super(plugin); }
	
	@EventHandler
	public void onCharge(CreeperPowerEvent event) {
		plugin.getServer().broadcastMessage("A creeper has been charged!");
		event.getEntity().getWorld().spawn(event.getEntity().getLocation(), Firework.class);
	}
}
