package com.aleksey.LeafShears.command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.aleksey.LeafShears.LeafShears;

public class LeafShearsCommand {
	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player)sender;
		ItemStack item = new ItemStack(Material.SHEARS);
		
		setLore(item, "Leaf Shears");
		
		putItemToInventoryOrDrop(player, player.getLocation(), item);
		
		return true;
	}
	
	private static void setLore(ItemStack item, String lore) {
		if (lore == null || lore.length() == 0) return;
		
		ItemMeta meta = item.getItemMeta();
		List<String> array = Arrays.asList(lore.split("\n"));
		meta.setLore(array);
		item.setItemMeta(meta);
	}
	
	private static void putItemToInventoryOrDrop(Player player, Location dropLocation, ItemStack dropItem) {
        if (player != null){
            Inventory inv = player.getInventory();

            for(ItemStack leftover : inv.addItem(dropItem).values()) {
            	dropItemAtLocation(dropLocation, leftover);
            }
        }
        else {
        	dropItemAtLocation(dropLocation, dropItem);
        }
	}
	
	private static void dropItemAtLocation(final Location dropLocation, final ItemStack dropItem) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(LeafShears.getInstance(), new Runnable() {
			@Override
			public void run() {
				try {
					Location newDropLocation = dropLocation.add(0.5, 0.5, 0.5);
					dropLocation.getWorld().dropItem(newDropLocation, dropItem).setVelocity(new Vector(0, 0.05, 0));
				} catch (Exception e) {
				}
			}
		}, 1);
	}
}
