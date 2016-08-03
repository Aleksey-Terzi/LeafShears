package com.aleksey.LeafShears.listener;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import vg.civcraft.mc.citadel.Citadel;
import vg.civcraft.mc.citadel.PlayerState;
import vg.civcraft.mc.citadel.reinforcement.PlayerReinforcement;
import vg.civcraft.mc.citadel.reinforcement.Reinforcement;

import com.aleksey.LeafShears.LeafShears;

public class EventListener implements Listener {
	
	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
	public void onBlockBreak(BlockBreakEvent event) {
		final Player player = event.getPlayer();
		
		if(player == null) {
			return;
		}
		
		final ItemStack handItem = event.getPlayer().getInventory().getItemInMainHand();
		
		if(!handItem.getType().equals(Material.SHEARS) || !Objects.equals(getLore(handItem), "Leaf Shears")) {
			return;
		}
		
		if(!isLeaf(event.getBlock())) {
			return;
		}
		
		event.setCancelled(true);
		event.getBlock().setType(Material.AIR);
		
		//Following manipulations are needed to synchronize tool durability with client after cancel block break
		handItem.setDurability((short)(handItem.getDurability() + 1));
		
		final Location center = event.getBlock().getLocation();
		
		Bukkit.getScheduler().runTask(LeafShears.getInstance(), new Runnable() {
            public void run() {
            	handItem.setDurability((short)(handItem.getDurability() - 1));
            	
            	clearCube(player, center);
            }
        });
	}
	
	private static void clearCube(Player player, Location center) {
		World world = center.getWorld();
		int cubeSize = LeafShears.getConfigManager().getCutCubeSize();
		int radius = (cubeSize - 1) / 2;
		int startX = center.getBlockX() - radius;
		int startY = center.getBlockY() - radius;
		int startZ = center.getBlockZ() - radius;
		PlayerState state = PlayerState.get(player);
		boolean isBypassMode = state.isBypassMode();
		
		for(int x = startX; x < startX + cubeSize; x++) {
			for(int y = startY; y < startY + cubeSize; y++) {
				for(int z = startY; z < startZ + cubeSize; z++) {
					if(x == center.getBlockX() && y == center.getBlockY() && z == center.getBlockZ()) {
						continue;
					}
					
					Block block = world.getBlockAt(x, y, z);
					
					if(!isLeaf(block) || !canBypass(player, isBypassMode, block.getLocation())) {
						continue;
					}
					
					block.setType(Material.AIR);
				}
			}
		}
	}
	
	private static boolean canBypass(Player player, boolean isBypassMode, Location loc) {
		Reinforcement rein = Citadel.getReinforcementManager().getReinforcement(loc);
		
		if(rein == null || !(rein instanceof PlayerReinforcement)) return true;
		
		if(!isBypassMode) return false;
		
		PlayerReinforcement playerRein = (PlayerReinforcement)rein;
		
		return playerRein.canBypass(player)
			|| player.hasPermission("citadel.admin.bypassmode");
	}
	
	private static boolean isLeaf(Block block) {
		Material blockMaterial = block.getType(); 
		
		return blockMaterial.equals(Material.LEAVES) || blockMaterial.equals(Material.LEAVES_2);
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
