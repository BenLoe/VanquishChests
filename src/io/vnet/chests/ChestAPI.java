package io.vnet.chests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class ChestAPI {
	
	public static HashMap<Location,String> chests = new HashMap<Location,String>();

	@SuppressWarnings("deprecation")
	public static void checkChest(Player p, Location block, BlockBreakEvent event){
		if (checkChance()){
			event.setCancelled(true);
			HashMap<Integer,Integer> items = new HashMap<Integer,Integer>();
			List<Integer> chosen = new ArrayList<Integer>();
			block.getBlock().setType(Material.CHEST);
			Random r = new Random();
			
			int util = 0;
			for (String s : Files.config().getStringList("Items")){
				int percent = Integer.parseInt(s.split(", ")[2]);
				items.put(util, percent);
				util++;
			}
			int slots = r.nextInt(4) + 2;
			for (int i = 1; i <= slots; i++){
				int chanceTotal = 0;
				for (int num : items.keySet()){
					chanceTotal += items.get(num);
				}
				
				int choice = r.nextInt(chanceTotal), subtotal = 0;
				for (int num1 : items.keySet()){
					subtotal += items.get(num1);
					if (choice < subtotal){
						if (!chosen.contains(num1)){
							chosen.add(num1);
							break;
						}
					}
				}
			}
			Chest chestb = (Chest) block.getBlock().getState();
			for (int chosennum : chosen){
				String s = Files.config().getStringList("Items").get(chosennum);
				int id = Integer.parseInt(s.split(", ")[0]);
				short data = Short.parseShort(s.split(", ")[1]);
				int min = Integer.parseInt(s.split(", ")[3]);
				int max = Integer.parseInt(s.split(", ")[4]);
				
				int amount = (int) (min + (max - min) * Math.random());
				ItemStack item = new ItemStack(id, amount, data);
				chestb.getInventory().addItem(item);
			}
			chestb.update();
			chests.put(chestb.getLocation(), p.getName());
		}
	}
	
	public static boolean checkChance(){
		double needed = Files.config().getDouble("Chance");
		Random r = new Random();
		double percent = 0 + (100 - 0) * r.nextDouble();
		if (percent <= needed){
			return true;
		}
		return false;
	}
	
}

