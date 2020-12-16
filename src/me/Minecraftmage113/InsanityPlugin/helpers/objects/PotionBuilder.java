package me.Minecraftmage113.InsanityPlugin.helpers.objects;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

public class PotionBuilder extends ItemBuilder {
	PotionMeta meta;
	
	public PotionBuilder() { super(Material.POTION); }

	public PotionBuilder addEffects(PotionEffect... effects) {
		meta = (PotionMeta) stack.getItemMeta();
		for(PotionEffect effect : effects) {
			meta.addCustomEffect(effect, true);
		}
		stack.setItemMeta(meta);
		return this;
	}
	public PotionBuilder setColor(Color color) {
		meta = (PotionMeta) stack.getItemMeta();
		meta.setColor(color);
		stack.setItemMeta(meta);
		return this;
	}
}
