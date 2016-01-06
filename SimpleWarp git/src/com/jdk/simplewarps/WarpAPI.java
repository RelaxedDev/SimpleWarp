package com.jdk.simplewarps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class WarpAPI {
	public Base plugin;
	public WarpAPI( Base plugin )
	{
		this.plugin = plugin;
	}
	
	private boolean GUIBased = plugin.getConfig().getBoolean("GUI-Based");
	private boolean PermBased = plugin.getConfig().getBoolean("Perm-Based");
	private String noGUI = plugin.getConfig().getString("non-GUI-list");
	private String prefix = plugin.getConfig().getString("prefix");
	private String noPerm = ChatColor.GRAY + "ERROR! "
			+ ChatColor.DARK_RED + "You don't have permission for this.";
	
	public void getWarps()
	{
		
	}
	
	public File warpFolder()
	{
		return new File(plugin.getDataFolder()+File.separator+"Warps");
	}
	
	public void setupConfig()
	{
		warpFolder().mkdirs();
		plugin.getDataFolder().mkdirs();
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveDefaultConfig();
	}
	
	public void createWarp(String str, int numb, Player p)
	{
		if(p.hasPermission("warps.create"))
		{
			File f = new File(warpFolder(), str.toLowerCase() + ".yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			Location loc = p.getLocation();
			try 
			{
				f.createNewFile();
				con.set("name", str.toLowerCase());
				con.set("realName", str);
				con.set("location.world", loc.getWorld().getName());
				con.set("location.x", loc.getX());
				con.set("location.y", loc.getY());
				con.set("location.z", loc.getZ());
				con.set("location.pitch", loc.getPitch());
				con.set("location.yaw", loc.getYaw());
				con.set("GUI.item", "1");
				con.set("GUI.slot", numb);
				con.set("GUI.name", "&c" + str);
				List<String> lores = new ArrayList<String>();
				lores.add("&cWarp: &b" + str);
				lores.add("&cPermission: &b%perm%");
				con.set("GUI.lore", lores);
				con.set("GUI.glowOnReg", true);
				con.set("GUI.glowOnPerm", false);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			cMessage(p, prefix + "You have created warp: " + str);
		}
		else
		{
			p.sendMessage(noPerm);
		}
	}
	
	public void deleteWarp(String str, Player p)
	{
		if(p.hasPermission("warps.delete"))
		{
			File f = new File(warpFolder(), str.toLowerCase() + ".yml");
			f.delete();
			cMessage(p, prefix + "You have deleted warp: " + str);
		}
		else
		{
			p.sendMessage(noPerm);
		}
	}
	
	public void warpList(Player p)
	{
		if(GUIBased)
		{
			if(PermBased)
			{
				for(File f : warpFolder().listFiles()){
					
				}
			}
			else
			{
				for(File f : warpFolder().listFiles()){
					
				}
			}
		}
		else
		{
			String str = noGUI;
			if(PermBased)
			{
				for(File f : warpFolder().listFiles()){
					FileConfiguration con = YamlConfiguration.loadConfiguration(f);
					if(p.hasPermission("warp" + con.getString("name"))){
						String rn = con.getString("realName");
						str = str + " " + rn;
					}
				}
			}
			else
			{
				for(File f : warpFolder().listFiles()){
					FileConfiguration con = YamlConfiguration.loadConfiguration(f);
					String rn = con.getString("realName");
					str = str + " " + rn;
				}
			}
			cMessage(p, str);
		}
	}
	
	public void cMessage(Player p, String str)
	{
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
	}
	
}
