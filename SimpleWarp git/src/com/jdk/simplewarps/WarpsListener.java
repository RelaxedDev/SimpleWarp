package com.jdk.simplewarps;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class WarpsListener implements Listener{
	public Base plugin;
	public WarpsListener( Base plugin )
	{
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent ev)
	{
		Action a = ev.getAction();
		int id = plugin.getConfig().getInt("GUI.click-open");
		if(ev.getItem() != null){
			if(ev.getItem().getTypeId() ==  id
					&& a == Action.RIGHT_CLICK_BLOCK || 
					ev.getItem().getTypeId() ==  id 
					&& a == Action.RIGHT_CLICK_AIR)
			{
				new BukkitRunnable() {
					@Override
					public void run() {
						plugin.getAPI().warpList(ev.getPlayer());
					}
				}.runTaskLater(this.plugin, 1);	
			}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent ev)
	{
		if(ev.getInventory() != null && ev.getCurrentItem() != null)
		{
			String title = plugin.getConfig().getString("GUI.title");
			if(ev.getInventory().getTitle().equalsIgnoreCase(cString(title))){
				ev.setCancelled(true);
				for(File file : plugin.getAPI().warpFolder().listFiles())
				{
					FileConfiguration con = YamlConfiguration.loadConfiguration(file);
					if(ev.getCurrentItem().equals(plugin.getAPI()
							.getItem(con, 
							(Player) ev.getWhoClicked()))){
						Player p = (Player) ev.getWhoClicked();
				        new BukkitRunnable() {
				            @Override
				            public void run() {
				            	plugin.getAPI().warpTo(
				            			con.getString("name"), p);
				            }
				        }.runTaskLater(this.plugin, 1);
						break;
					}
				}
			}
		}
	}
	@EventHandler
	public void onDrag(InventoryDragEvent ev)
	{
		if(ev.getInventory() != null)
		{
			String title = plugin.getConfig().getString("GUI.title");
			if(ev.getInventory().getTitle().equalsIgnoreCase(cString(title))){
				ev.setCancelled(true);
			}
		}
	}
	public String cString(String str)
	{
		return ChatColor.translateAlternateColorCodes('&', str);
	}
}
