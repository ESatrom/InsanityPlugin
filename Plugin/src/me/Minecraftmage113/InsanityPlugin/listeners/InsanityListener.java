package me.Minecraftmage113.InsanityPlugin.listeners;

import org.bukkit.event.Listener;

import me.Minecraftmage113.InsanityPlugin.Main;

public class InsanityListener implements Listener {
	protected Main plugin;
//	private long temp;
	public InsanityListener(Main plugin) {
		this.plugin = plugin;
	}
//	protected void start(int id) {
//		temp = System.currentTimeMillis();
//	}
//	protected void stop(int id) {
//		plugin.listenerCalls[0]++;
//		plugin.listenerTicks[0]+=System.currentTimeMillis()-temp;
//	}
//	protected void callprint() {
//		System.out.println();
//		System.out.println();
//		for(int i = 0; i < plugin.listenerCalls.length; i++) {
//			System.out.println(plugin.listenerNames[i] + ": Called: " + plugin.listenerCalls[i] + " times, running " + plugin.listenerTicks[i] + "ms");
//		}
//		System.out.println();
//		System.out.println();
//	}
}
