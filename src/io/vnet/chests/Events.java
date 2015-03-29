package io.vnet.chests;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener {
	public static Main plugin;
	public Events(Main instance){
		plugin = instance;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void blockBreak(BlockBreakEvent event){
		if (ChestAPI.chests.containsKey(event.getBlock().getLocation())){
			event.setCancelled(true);
		}else{
		ChestAPI.checkChest(event.getPlayer(), event.getBlock().getLocation(), event);
		}
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if (event.getClickedBlock().getType().equals(Material.CHEST)){
				if (ChestAPI.chests.containsKey(event.getClickedBlock().getLocation())){
					String name = ChestAPI.chests.get(event.getClickedBlock().getLocation());
					event.setCancelled(true);
					if (p.getName().equals(name)){
						Block b = event.getClickedBlock();
						Chest chest = (Chest) event.getClickedBlock().getState();
						for (ItemStack item : chest.getInventory().getContents()){
							if (item != null){
							p.getWorld().dropItem(chest.getLocation().clone().add(0.5, 0.5, 0.5), item);
							}
						}
						chest.getInventory().clear();
						chest.update();
						b.setType(Material.AIR);
						p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
						ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.CHEST, (byte)0), 0.1f, 0.1f, 0.1f, 0.1f, 150, event.getClickedBlock().getLocation().clone().add(0.5, 0.5, 0.5), 10);
						ParticleEffect.CRIT_MAGIC.display(0.4f, 0.4f, 0.4f, 0.01f, 50, event.getClickedBlock().getLocation().clone().add(0.5, 0.5, 0.5), 10);
						ChestAPI.chests.remove(event.getClickedBlock().getLocation());
					}else{
						p.sendMessage("§e§lVanquish§c§lChests §7§l> §fThis is not your loot chest.");
					}
				}
			}
		}
	}
}
