package me.Minecraftmage113.InsanityPlugin.commands;

import org.bukkit.command.CommandExecutor;

import me.Minecraftmage113.InsanityPlugin.Main;


/**
 * A Command superclass containing a reference to the plugin.
 * @author Minecraftmage113
 * @version 24/Sept/2020
 */
public abstract class InsanityCommand implements CommandExecutor {
	protected Main plugin;
	public InsanityCommand(Main plugin) {
		this.plugin = plugin;
	}
	
}
