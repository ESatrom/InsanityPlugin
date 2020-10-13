package me.Minecraftmage113.InsanityPlugin.helpers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Mob;

import me.Minecraftmage113.InsanityPlugin.InsanityMetadata;
import me.Minecraftmage113.InsanityPlugin.Main;

public class Encoder {
	Main plugin;
	String[] metaKeys;
	String breakSequence, endSequence;
	
	public Encoder(Main plugin, String[] metaKeys) {
		this.plugin = plugin;
		this.metaKeys = metaKeys;
	}
	
	public Block loadBlockFromString(List<String> s, World world) {
		if(s.size()<3) {
			System.out.println("ERROR: Not enough information provided to form a block.");
			return null;
		}
		int x = Integer.parseInt(s.get(0)), 
			y = Integer.parseInt(s.get(1)), 
			z = Integer.parseInt(s.get(2));
		Block b = world.getBlockAt(x, y, z);
		if(s.size()%2!=1) {
			System.out.println("Warning! Block loaded with uneven metadata keys and texts!");
		}
		int bigness = (int) Math.ceil((s.size()-3)/2.0);
		String[] keys = new String[bigness],
				 metaTexts = new String[bigness];
		int j = 0;
		for(int i = 3; i < s.size(); i+=2) {
			keys[j] = s.get(i);
			if(i+1<s.size()) {
				metaTexts[j] = s.get(i+1);
			}
		    j++;
		}
		for(int i = 0; i < keys.length; i++) {
			String key = keys[i];
			String metaText = ((metaTexts.length<=i)?null:metaTexts[i]);
			b.setMetadata(key, new InsanityMetadata(plugin, metaText, b));
		}
		return b;
	}
	public List<String> blockToString(Block b) {
		List<String> result = new ArrayList<String>();
		result.add(b.getX()+"");
		result.add(b.getY()+"");
		result.add(b.getZ()+"");
		for(String s : metaKeys) {
			if(b.hasMetadata(s)&&b.getMetadata(s).size()>0) {
				result.add(s);
				result.add(b.getMetadata(s).get(0).value()+"");
			}
		}
		return (result.size()==3?null:result);
	}
	public String lassoFromString(String s) {
		return null;
	}
	public String lassoFromStats() {
		return null;
	}
	public List<String> lassoToString(int ID, Mob m) {
		m.getCustomName();
		return null;
	}
}
