package me.Minecraftmage113.InsanityPlugin.helpers.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
	ItemStack stack;
	ItemMeta meta;
	public ItemBuilder(Material material) { stack = new ItemStack(material); }
	public ItemBuilder(ItemStack stack) { this.stack = stack; }

	public ItemBuilder setName(String name) {
		meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		return this;
	}
	public ItemBuilder setLore(String... lore) {
		List<String> lores = new ArrayList<String>();
		for(String s : lore) { lores.add(s); 
		System.out.println(s);}
		System.out.println(lore);
		meta = stack.getItemMeta();
		meta.setLore(lores);
		stack.setItemMeta(meta);
		return this;
	}
	public ItemBuilder setLore(List<String> lore) {
		meta = stack.getItemMeta();
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return this;
	}
	public ItemBuilder setModelData(int id) {
		meta = stack.getItemMeta();
		meta.setCustomModelData(id);
		stack.setItemMeta(meta);
		return this;
	}
	public ItemBuilder makeUnbreakable() {
		meta = stack.getItemMeta();
		meta.setUnbreakable(true);
		stack.setItemMeta(meta);
		return this;
	}
	public ItemBuilder addEnch(Enchantment ench, int level) {
		meta = stack.getItemMeta();
		meta.addEnchant(ench, level, true);
		stack.setItemMeta(meta);
		return this;
	}
	public ItemBuilder addFlag(ItemFlag... flags) {
		meta = stack.getItemMeta();
		meta.addItemFlags(flags);
		stack.setItemMeta(meta);
		return this;
	}
	public ItemBuilder addMod(Attribute attribute, AttributeModifier mod) {
		meta = stack.getItemMeta();
		meta.addAttributeModifier(attribute, mod);
		stack.setItemMeta(meta);
		return this;
	}
	public ItemStack build() { return stack.clone(); }
}
