package me.Minecraftmage113.InsanityPlugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Minecraftmage113.InsanityPlugin.Main;

public class ListenerPlayerLog extends InsanityListener {

	public ListenerPlayerLog(Main plugin) { super(plugin); }
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
	}
	
	public void onLeave(PlayerQuitEvent event) {
		plugin.getSaver().save();
	}

}
