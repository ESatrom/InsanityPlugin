package me.Minecraftmage113.InsanityPlugin.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.helpers.InsanityEnums;

public class ListenerPlayerDeath extends InsanityListener {
	public ListenerPlayerDeath(Main plugin) { super(plugin); }
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		dropHead(p);
		tags(p);
	}
	
	void dropHead(Player p) {
		if(p.getLastDamageCause().getCause().equals(DamageCause.ENTITY_ATTACK)){
			World world = p.getWorld();
			Location location = p.getLocation();
			ItemStack item = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta meta = (SkullMeta) item.getItemMeta();
			meta.setOwningPlayer(p);
			item.setItemMeta(meta);
			world.dropItem(location, item);
		}
	}
	
	void tags(Player p) {
		InsanityEnums.Modifiers.ROTTED.remove(p);
		if(InsanityEnums.Modifiers.ROTTING_PRESERVATION.remove(p)) {
			InsanityEnums.Modifiers.ROTTED.apply(p);
			p.sendMessage("Your flesh decays as Phthisis' preservation fades.");
			for(Entity e : p.getNearbyEntities(5, 2, 5)) {
				if(e instanceof Attributable) {
					if(e instanceof Damageable) {
						Damageable d = (Damageable) e;
						d.damage(0);
						if(InsanityEnums.Modifiers.ROTTED.apply((Attributable) d)) {
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
		InsanityEnums.Modifiers.VULCANITE.remove(p);
	}
}
