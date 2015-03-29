package io.vnet.chests;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	Files files = new Files(this);
	Events events = new Events(this);
	
	@SuppressWarnings("deprecation")
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(new Events(this), this);
		System.out.println("VanquishChests is enabled.");
		saveDefaultConfig();
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getPlugin(Main.class), new Runnable(){
			public void run(){
				if (!ChestAPI.chests.isEmpty()){
					List<Location> beingremoved = new ArrayList<Location>();
					for (Location loc : ChestAPI.chests.keySet()){
						if(loc.getBlock().getType() != Material.CHEST){
							beingremoved.add(loc);
						}
						ParticleEffect.PORTAL.display(0.1f, 0.01f, 0.1f, 0.5f, 50, loc.clone().add(0.5, 0.5, 0.5), 10);
					}
					if (!beingremoved.isEmpty()){
						for (Location loc : beingremoved){
							ChestAPI.chests.remove(loc);
						}
					}
				}
			}
		}, 20l, 10l);
	}
	
	public void onDisable(){
		for (Location loc : ChestAPI.chests.keySet()){
			Chest chest = (Chest) loc.getBlock().getState();
			chest.getInventory().clear();
			chest.update();
			loc.getBlock().setType(Material.AIR);
		}
	}
}
