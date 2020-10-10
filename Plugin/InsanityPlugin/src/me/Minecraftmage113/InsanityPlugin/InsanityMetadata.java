package me.Minecraftmage113.InsanityPlugin;

import org.bukkit.block.Block;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.plugin.Plugin;

public class InsanityMetadata extends MetadataValueAdapter {

	String value;
	Block block;
	Main plugin;
	
	public InsanityMetadata(Plugin owningPlugin, String value, Block block) {
		super(owningPlugin);
		plugin = (Main) owningPlugin;
		this.value = value;
		this.block = block;
		plugin.getSaver().addBlock(block);
	}

	public void setValue(String value, Block block) {
		this.value = value;
		this.block = block;
	}
	
	@Override
	public Object value() { return value; }

	@Override
	public void invalidate() {
		// Auto-generated method stub
	}

}
