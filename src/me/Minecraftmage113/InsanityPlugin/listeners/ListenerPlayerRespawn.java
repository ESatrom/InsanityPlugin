package me.Minecraftmage113.InsanityPlugin.listeners;

import org.bukkit.Particle;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.helpers.enums.InsanityModifiers;

public class ListenerPlayerRespawn extends InsanityListener {
	public ListenerPlayerRespawn(Main plugin) { super(plugin); }
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player p = event.getPlayer();
		p.getInventory();
	}
	
	void tags(Player p) {
		InsanityModifiers.ROTTED.remove(p);
		if(InsanityModifiers.ROTTING_PRESERVATION.remove(p)) {
			InsanityModifiers.ROTTED.apply(p);
			p.sendMessage("Your flesh decays as Phthisis' preservation fades.");
			for(Entity e : p.getNearbyEntities(5, 2, 5)) {
				if(e instanceof Attributable) {
					if(e instanceof Damageable) {
						Damageable d = (Damageable) e;
						d.damage(0);
						if(InsanityModifiers.ROTTED.apply((Attributable) d)) {
							if(e instanceof Player) {
								e.sendMessage("Your flesh has been decayed by the mighty Phthisis. Patron of the Cult of Shrooms.");
							}
							if(d.getHealth()>((Attributable) d).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
								d.setHealth(((Attributable) d).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
							}
						}
					}
				}
			}
			p.getWorld().spawnParticle(Particle.ASH, p.getLocation(), 1210+Main.r.nextInt(4236), 5, 2, 5);
		}
		InsanityModifiers.VULCANITE.remove(p);
	}
}
