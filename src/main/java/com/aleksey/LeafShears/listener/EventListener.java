package com.aleksey.LeafShears.listener;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.aleksey.LeafShears.LeafShears;

public class EventListener implements Listener {
	
	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.getPlayer() == null) {
			return;
		}
		
		final ItemStack handItem = event.getPlayer().getInventory().getItemInMainHand();
		
		if(!handItem.getType().equals(Material.SHEARS) || !Objects.equals(getLore(handItem), "Leaf Shears")) {
			return;
		}

		Material blockMaterial = event.getBlock().getType(); 
		
		if(!blockMaterial.equals(Material.LEAVES) && !blockMaterial.equals(Material.LEAVES_2)) {
			return;
		}
		
		event.setCancelled(true);
		event.getBlock().setType(Material.AIR);
		
		//Following manipulations are needed to synchronize tool durability with client after cancel block break
		handItem.setDurability((short)(handItem.getDurability() + 1));
		
		Bukkit.getScheduler().runTask(LeafShears.getInstance(), new Runnable() {
            public void run() {
            	handItem.setDurability((short)(handItem.getDurability() - 1));
            }
        });
	}
	
	private static String getLore(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		
		if(!meta.hasLore()) return null;
		
		StringBuilder lore = new StringBuilder();
		
		for (String line: meta.getLore()) {
			if(lore.length() > 0) {
				lore.append("\n");
			}
			
			lore.append(line);
		}
		
		return lore.toString();
	}
	
}
