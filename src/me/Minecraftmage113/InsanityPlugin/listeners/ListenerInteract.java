package me.Minecraftmage113.InsanityPlugin.listeners;

import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;

import me.Minecraftmage113.InsanityPlugin.InsanityMetadata;
import me.Minecraftmage113.InsanityPlugin.Main;
import me.Minecraftmage113.InsanityPlugin.helpers.enums.InsanityItems;
import me.Minecraftmage113.InsanityPlugin.helpers.enums.InsanityModifiers;
import me.Minecraftmage113.InsanityPlugin.helpers.enums.InsanityStructures;
import me.Minecraftmage113.InsanityPlugin.helpers.objects.InsanityEntity;
import me.Minecraftmage113.InsanityPlugin.items.ItemEnderPorter;

public class ListenerInteract extends InsanityListener {
	public ListenerInteract(Main plugin) { super(plugin); }

	@EventHandler
	public void onLeash(PlayerLeashEntityEvent event) {
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		ItemStack item2 = event.getPlayer().getInventory().getItemInOffHand();
		if(item!=null && InsanityItems.LASSO.instance(item)) {
			event.setCancelled(true);
			event.getPlayer().updateInventory();
		}
		if(item2!=null && InsanityItems.LASSO.instance(item2)) {
			event.setCancelled(true);
			event.getPlayer().updateInventory();
		}
	}
	
	@EventHandler
	public void onInteraction(PlayerInteractEvent event) {
		Player p = (Player) event.getPlayer();
		ItemStack item = event.getItem();
		Block b = event.getClickedBlock();
		//TO DO probably breaks coffee lol
		if(item!=null) {
			for(InsanityItems enu : InsanityItems.values()) {
				if(enu.instance(item)) {
					event.setCancelled(true);
				}
			}
			if(InsanityItems.COFFEE.instance(item)) { event.setCancelled(false); }
		}
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if(item!=null) {
					if(InsanityItems.ENDER_PORTER.instance(item)){
						enderPorter(event, p);
					} else if(InsanityItems.LIGHTNING_STAFF.instance(item)) {
						lightning(p, item);
					}
			}
		}
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			altar(event, p, b);
			if(item!=null) {
				if(InsanityItems.ENDER_PORTER.instance(item)) {
					enderPorter(event, p);
				} else if(InsanityItems.BLOCK_FLAGGER.instance(item)) {
					flagBlock(event, p, b);
				} else if(InsanityItems.LASSO.instance(item)) {
					lassoRelease(event, item, b);
				} else if(InsanityItems.ENDER_CORE.instance(item)) {
					enderCreate(b, p, item);
				} else if(InsanityItems.LIGHTNING_STAFF.instance(item)) {
					lightning(p, item);
				}
			}
		}
	}
	
	public void enderPorter(PlayerInteractEvent event, Player p) {
		event.setCancelled(true);
		ItemEnderPorter porter = new ItemEnderPorter(event.getItem(), p);
		if(p.isSneaking()) {
			porter.setTarget(p.getLocation());
			p.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Bound!");
		} else {
			if(p.getInventory().getItemInOffHand().getType().equals(Material.ENDER_PEARL)) {
				while(p.getInventory().getItemInOffHand().getAmount()>0&&porter.getCharge()<991) {
					p.getInventory().getItemInOffHand().setAmount(p.getInventory().getItemInOffHand().getAmount()-1);
					porter.addCharge(10);
				}
				p.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Charged!");
			} else {
				int distanceCostCap = 160;
				int cost = (int) Math.floor(Math.sqrt(porter.getTarget().distance(p.getLocation())));
				cost = (cost>distanceCostCap?distanceCostCap:cost);
				if(porter.getCharge()>=cost) {
					porter.addCharge(0-cost);
					p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, .5F);
					p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 40, .5, 1, .5);
					p.teleport(porter.getTarget());
					p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 20, .5, 1, .5);
					p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, .5F);
				} else {
					p.sendMessage(ChatColor.RED + "Not enough charge. Right click while holding ender pearls in your off-hand to charge the porter.");
				}
			}
		}
		event.getItem().setItemMeta(porter.getItemMeta());
	}
	
	private interface SacrificeCondition { public boolean execute(); }
	private interface SacrificeBoon { public void execute(); }
	public void altar(PlayerInteractEvent event, Player p, Block b) {
		List<MetadataValue> meta = b.getMetadata("Altar");
		Collection<Entity> sacrifices = b.getWorld().getNearbyEntities(b.getLocation(), 3, 2, 3);
		SacrificeCondition cond = () -> { return false; };
		SacrificeBoon boon = () -> {};
		long day = b.getWorld().getFullTime()/24000;
		long phase = day%8; // 0=full, 4=new
		if(meta!=null && meta.size()>0) {
			switch(meta.get(0).value()+"") {
			case "Phthisis":
				cond = () -> {
					if(phase==4&&b.getWorld().getTime()>17000&&b.getWorld().getTime()<19000) {
						for(Entity e : sacrifices) {
							if(e instanceof MushroomCow) {
								if(!((MushroomCow) e).isAdult()) {
									e.remove();
									return true;		
								}
							}
						}
						p.sendMessage("Phthisis desires the slaughter of an infected calf. (RClick altar, do not kill calf)");
					} else {
						if(phase!=4) {
							p.sendMessage("Phthisis desires a moon of darkness.");
						} else {
							p.sendMessage("Phthisis requires the height of the moon to touch the land.");
						}
					}
					return false; 
				};
				boon = () -> {
					p.getWorld().spawnParticle(Particle.ASH, p.getLocation(), 1024+Main.r.nextInt(3073), 1, 2, 1);
					p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 0, false, false, false));
					if(InsanityModifiers.ROTTING_PRESERVATION.apply(p)) {
						p.sendMessage("You have been preserved by Phthisis.");
					}
				};
				break;
			case "Vulcan":
				cond = () -> {
					for(Entity e : sacrifices) {
						if(e instanceof Item) {
							Item i = (Item) e;
							if(i.getItemStack().getType().equals(Material.NETHERITE_SCRAP)) {
								if(i.getItemStack().getAmount()==1) {
									i.remove();
								} else {
									i.getItemStack().setAmount(i.getItemStack().getAmount()-1);
								}
								p.sendMessage("Vulcan blessed you with skin of steel.");
								return true;		
							}
						}
					}
					p.sendMessage("Vulcan desires a scrap of netherite to work into your flesh.");
					return false; 
				};
				boon = () -> {
					p.getWorld().spawnParticle(Particle.LAVA, p.getLocation(), 1024+Main.r.nextInt(3073), 1, 2, 1);
					if(InsanityModifiers.VULCANITE.apply(p)) {
						p.sendMessage("Your flesh has been hardened by Vulcan.");
					}
				};
				break;
			}
			if(cond.execute()) {
				boon.execute();
			}
		}
	}
	
	public void flagBlock(PlayerInteractEvent event, Player p, Block b) {
		if(p.isSneaking()){
			b.setMetadata("Altar", new InsanityMetadata(plugin, event.getItem().getItemMeta().getDisplayName(), b));
		} else {
			if(b.hasMetadata("Altar")&&b.getMetadata("Altar").size()>0) {
				p.sendMessage(""+b.getMetadata("Altar").get(0).value());
			}
		}
	}
	
	public void lassoRelease(PlayerInteractEvent event, ItemStack item, Block b){
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		if(Integer.parseInt(lore.get(lore.size()-1).substring(lore.get(lore.size()-1).indexOf('|')+1))==-1) {return;}
		Location l = b.getLocation();
		BlockFace face = event.getBlockFace();
		Entity releasee = plugin.releaseLasso(Integer.parseInt(lore.get(lore.size()-1).substring(lore.get(lore.size()-1).indexOf('|')+1)));
		if(releasee!=null) {
			if(face.equals(BlockFace.UP)) {
				l.setY(l.getY()+1);
			} else if(face.equals(BlockFace.DOWN)) {
				l.setY(l.getY()-releasee.getHeight());
			} else if(face.equals(BlockFace.EAST)) {
				l.setX(l.getX()+1);
			} else if(face.equals(BlockFace.WEST)) {
				l.setX(l.getX()-1);
			} else if(face.equals(BlockFace.NORTH)) {
				l.setZ(l.getZ()-1);
			} else if(face.equals(BlockFace.SOUTH)) {
				l.setZ(l.getZ()+1);
			}
			l.setZ(l.getZ()+.5);
			l.setX(l.getX()+.5);
			Entity spawned = l.getWorld().spawn(l, releasee.getClass());
			InsanityEntity.clone(releasee, spawned);
			spawned.teleport(l);
		} else { System.out.println("Lasso entity glitched"); }
		lore.set(lore.size()-1, lore.get(lore.size()-1).substring(0, lore.get(lore.size()-1).indexOf('|')+1)+"-1");
		lore.set(lore.size()-2, "Currently Contained: " + ChatColor.DARK_GRAY + "Nothing");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public void enderCreate(Block b, Player p, ItemStack item) {
		Material[] materials = InsanityStructures.ENDER_RIFT.check(b, p.getFacing());
		InsanityStructures type = InsanityStructures.ENDER_RIFT;
		if(materials==null) {
			type = InsanityStructures.ENDER_GATE;
			materials = InsanityStructures.ENDER_GATE.check(b, BlockFace.EAST);
			if(materials==null) {
				materials = InsanityStructures.ENDER_GATE.check(b, BlockFace.NORTH);
			}
		}
		if(materials==null) {
			p.sendMessage("You didn't build anything!");
			return;
		}
		if(type==InsanityStructures.ENDER_RIFT) {
			p.sendMessage("You built a rift of " + materials[0] + ", " + materials[1] + ", and " + materials[2]);
		} else {
			p.sendMessage("You built a gate of " + materials[0] + " and " + materials[1]);
		}
		b.setType(Material.END_PORTAL_FRAME);
		EndPortalFrame dat = (EndPortalFrame) b.getBlockData();
		dat.setEye(true);
		b.setBlockData(dat);
		if(item.getAmount()==1) {
			item = null;
		} else {
			item.setAmount(item.getAmount()-1);
			p.dropItem(true);
		}
	}
	
	public void lightning(Player p, ItemStack i) {
		if(p.isSneaking()) {
			i.addUnsafeEnchantment(Enchantment.DURABILITY, i.getEnchantmentLevel(Enchantment.DURABILITY)+1);
			p.getWorld().strikeLightning(p.getLocation());
		} else {
			if(i.getEnchantmentLevel(Enchantment.DURABILITY)>0) {
				//shoot
				if(i.getEnchantmentLevel(Enchantment.DURABILITY)==1) {
					i.removeEnchantment(Enchantment.DURABILITY);
				} else {
					i.addUnsafeEnchantment(Enchantment.DURABILITY, i.getEnchantmentLevel(Enchantment.DURABILITY)-1);
				}
				boolean done = false;
				int damageTotal = 30;
				int damageMax = 5;
				int blockDamage = 10;
				Location src = p.getEyeLocation();
				while(!done) {
					src.add(p.getEyeLocation().getDirection().multiply(.5));
					RayTraceResult target = p.getWorld().rayTrace(src, p.getEyeLocation().getDirection(), 300.0, FluidCollisionMode.ALWAYS, false, 0, null);
					Location src1 = src.clone();
					if(target!=null&&target.getHitPosition()!=null) {
						src = target.getHitPosition().toLocation(p.getWorld());
					}
					if(target==null||(target.getHitBlock()==null&&target.getHitEntity()==null)) {
						src.add(p.getEyeLocation().multiply(300));
						done = true;
					} else {
						if(target.getHitBlock()!=null) {
							Block b = (Block) target.getHitBlock();
							switch(b.getType()) {
							case ACACIA_LOG: case BIRCH_LOG: case DARK_OAK_LOG: case JUNGLE_LOG: case OAK_LOG: case SPRUCE_LOG:
							case STRIPPED_ACACIA_LOG: case STRIPPED_BIRCH_LOG: case STRIPPED_DARK_OAK_LOG: case STRIPPED_JUNGLE_LOG: case STRIPPED_OAK_LOG: case STRIPPED_SPRUCE_LOG:
								b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.CHARCOAL));
								b.getWorld().spawnParticle(Particle.SMOKE_LARGE, b.getLocation(), 50, .5, .5, .5);
								b.setType(Material.AIR);
								if(damageTotal<=blockDamage) {
									done = true;
								} else {
									damageTotal-=blockDamage;
								}
								break;
							case SAND: case RED_SAND:
								b.getWorld().spawnParticle(Particle.CLOUD, b.getLocation(), 150, .75, 1, .75);
								b.setType(Material.GLASS);
								done=true;
								break;
							case ACACIA_LEAVES: case BIRCH_LEAVES: case DARK_OAK_LEAVES: case JUNGLE_LEAVES: case OAK_LEAVES: case SPRUCE_LEAVES: 
								b.getWorld().spawnParticle(Particle.SMOKE_LARGE, b.getLocation(), 50, .5, .5, .5);
								b.setType(Material.AIR);
								break;
							case NETHER_BRICKS:
								b.setType(Material.CRACKED_NETHER_BRICKS);
								BlockFace f = target.getHitBlockFace();
								b.getWorld().getBlockAt(b.getX()+f.getModX(), b.getY()+f.getModY(), b.getZ()+f.getModZ()).setType(Material.FIRE);
								done=true;
								break;
							case POLISHED_BLACKSTONE_BRICKS:
								b.setType(Material.CRACKED_POLISHED_BLACKSTONE_BRICKS);
								f = target.getHitBlockFace();
								b.getWorld().getBlockAt(b.getX()+f.getModX(), b.getY()+f.getModY(), b.getZ()+f.getModZ()).setType(Material.FIRE);
								done=true;
								break;
							case STONE_BRICKS:
								b.setType(Material.CRACKED_STONE_BRICKS);
								f = target.getHitBlockFace();
								b.getWorld().getBlockAt(b.getX()+f.getModX(), b.getY()+f.getModY(), b.getZ()+f.getModZ()).setType(Material.FIRE);
								done=true;
								break;
							case INFESTED_STONE_BRICKS:
								b.setType(Material.INFESTED_CRACKED_STONE_BRICKS);
								f = target.getHitBlockFace();
								b.getWorld().getBlockAt(b.getX()+f.getModX(), b.getY()+f.getModY(), b.getZ()+f.getModZ()).setType(Material.FIRE);
								done=true;
								break;
							default:
								
								if(b.getType().isBurnable()) {
									b.setType(Material.FIRE);
									if(!b.isPassable()) {
										if(damageTotal<=blockDamage) {
											done = true;
										} else {
											damageTotal-=blockDamage;
										}
									}
								} else {
									f = target.getHitBlockFace();
									if(b.getWorld().getBlockAt(b.getX()+f.getModX(), b.getY()+f.getModY(), b.getZ()+f.getModZ()).getType().equals(Material.AIR)) {
										b.getWorld().getBlockAt(b.getX()+f.getModX(), b.getY()+f.getModY(), b.getZ()+f.getModZ()).setType(Material.FIRE);
									}
									if(!b.isPassable()) { done=true; }
								}
								break;
							}
						}
						if(target.getHitEntity()!=null && target.getHitEntity() instanceof LivingEntity) {
							LivingEntity l = (LivingEntity) target.getHitEntity();
							if(l instanceof IronGolem) {
								l.getWorld().playSound(l.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1000, 1);
								done = true;
								l.damage(damageTotal, p);
							} else {
								l.damage(damageTotal<damageMax?damageTotal:damageMax, p);
								damageTotal-=damageMax;
								if(damageTotal<=0) { done = true; }
							}
						}
					}
					if(!src.equals(src1)) {
						int iterator = 0;
						while(src.distance(src1)>1) {
							src1.add(p.getEyeLocation().getDirection().multiply(.1));
							p.getWorld().spawnParticle(Particle.CRIT_MAGIC, src1.clone(), 1, 0, 0, 0, 0);
							iterator++;
							if(iterator==30000) { break; }
						}
					}
				}
			} else {
				p.sendMessage("Your staff must be recharged. Shift+RClick to call lightning down upon you, charging the staff.");
			}
		}
	}
}
