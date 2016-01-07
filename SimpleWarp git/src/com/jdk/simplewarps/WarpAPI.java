package com.jdk.simplewarps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WarpAPI {
	public Base plugin;
	public WarpAPI( Base plugin )
	{
		this.plugin = plugin;
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
	
	public void warpTo(String warp, Player p)
	{
		File f = new File(warpFolder(), warp.toLowerCase() + ".yml");
		if(p.hasPermission("warp." + warp.toLowerCase())
				&& f.exists())
		{
			String lP = "location.";
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			World world = plugin.getServer().getWorld(con.getString(lP + "world"));
			double X = con.getDouble(lP + "x");
			double Y = con.getDouble(lP + "y");
			double Z = con.getDouble(lP + "z");
			int yaw = con.getInt(lP + "yaw");
			int pitch = con.getInt(lP + "pitch");
			Location loc = new Location(world, X, Y, Z, yaw, pitch);
			p.teleport(loc);
			String name = con.getString("realName");
			cMessage(p, plugin.prefix + "You have warped to " + name + ".");
		}
		else
		{
			p.sendMessage(plugin.noPerm);
		}
	}
	
	public void createWarp(String name, int slot, Player p)
	{
		if(p.hasPermission("warps.create"))
		{
			File f = new File(warpFolder(), name.toLowerCase() + ".yml");
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			Location loc = p.getLocation();
			try 
			{
				f.createNewFile();
				con.set("name", name.toLowerCase());
				con.set("realName", name);
				con.set("location.world", loc.getWorld().getName());
				con.set("location.x", loc.getX());
				con.set("location.y", loc.getY());
				con.set("location.z", loc.getZ());
				con.set("location.pitch", loc.getPitch());
				con.set("location.yaw", loc.getYaw());
				con.set("GUI.item", 1);
				con.set("GUI.slot", slot);
				con.set("GUI.name", "&c" + name);
				List<String> lores = new ArrayList<String>();
				lores.add("&cWarp: &b" + name);
				lores.add("&cPermission: &b%perm%");
				con.set("GUI.lore", lores);
				con.set("GUI.glowOnReg", true);
				con.set("GUI.glowOnPerm", false);
				con.save(f);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			cMessage(p, plugin.prefix + "You have created warp: " + name);
		}
		else
		{
			p.sendMessage(plugin.noPerm);
		}
	}
	
	public void deleteWarp(String str, Player p)
	{
		if(p.hasPermission("warps.delete"))
		{
			File f = new File(warpFolder(), str.toLowerCase() + ".yml");
			f.delete();
			cMessage(p, plugin.prefix + "You have deleted warp: " + str);
		}
		else
		{
			p.sendMessage(plugin.noPerm);
		}
	}
	
	public ItemStack getItem(FileConfiguration con, Player p)
	{
		String cP = "GUI.";
		@SuppressWarnings("deprecation")
		Material mat = Material.getMaterial(con.getInt(cP + "item"));
		ItemStack item = new ItemStack(mat);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(cString(con.getString(cP + "name")));
		List<String> lores = con.getStringList(cP + "lore");
		String perm = "FALSE";
		if(p.hasPermission("warp." + con.getString("name")))
			perm = "TRUE";
		for(int i = 0; i < lores.size(); i++)
		{
			String lore = lores.get(i);
			lores.set(i, cString(lore).replace("%perm%", perm));
		}
		im.setLore(lores);
		item.setItemMeta(im);
		if(con.getBoolean(cP + "glowOnReg"))
		{
			addGlow(item);
		}
		else if(con.getBoolean(cP + "glowOnPerm"))
		{
			if(p.hasPermission("warp." + con.getString("name")))
			{
				addGlow(item);
			}
		}
		return item;
	}
	
	public ItemStack addGlow(ItemStack item)
	{
		ItemMeta im = item.getItemMeta();
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(im);
		item.addUnsafeEnchantment(Enchantment.WATER_WORKER, 70);
		return item;
	}
	
	public void makeInventory(Player p, boolean permBase)
	{
		String cP = "GUI.";
		String title = plugin.getConfig().getString("GUI.title");
		int rows = plugin.getConfig().getInt("GUI.rows");
		Inventory inv = Bukkit.createInventory(null, 9 * rows, cString(title));
		p.openInventory(inv);
		for(File f : warpFolder().listFiles()){
			FileConfiguration con = YamlConfiguration.loadConfiguration(f);
			int slot = con.getInt(cP + "slot");
			if(permBase)
			{
				if(p.hasPermission("warp." + con.getString("name")))
				{
					ItemStack item = getItem(con, p);
					inv.setItem(slot, item);
				}
			}
			else
			{
				ItemStack item = getItem(con, p);
				inv.setItem(slot, item);
			}
		}
	}
	
	public void warpList(Player p)
	{
		if(plugin.GUIBased)
		{
			if(plugin.PermBased)
			{
				makeInventory(p, true);
			}
			else
			{
				makeInventory(p, false);
			}
		}
		else
		{
			String str = plugin.noGUI;
			if(plugin.PermBased)
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
	
	public String cString(String str)
	{
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
	public void cMessage(Player p, String str)
	{
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
	}
	
}
