package com.jdk.simplewarps;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Base extends JavaPlugin{
	Base plugin;
	public boolean GUIBased;
	public boolean PermBased;
	public String noGUI;
	public String prefix;
	public String noPerm;
	@Override
	public void onEnable(){
		
		// Registering base methods
		
		getAPI().setupConfig();
		
		// Registering events / commands
		
		getCommand("warps").setExecutor(new WarpsCommands( this ));
		getCommand("setwarp").setExecutor(new WarpsCommands( this ));
		getCommand("deletewarp").setExecutor(new WarpsCommands( this ));
		
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new WarpsListener( this ), this);
		
		GUIBased = getConfig().getBoolean("GUI-Based");
		PermBased = getConfig().getBoolean("Perm-Based");
		noGUI = getConfig().getString("non-GUI-list");
		prefix = getConfig().getString("prefix");
		noPerm = ChatColor.GRAY + "ERROR! "
				+ ChatColor.DARK_RED + "You don't have permission for this.";
		
	}
	
	public WarpAPI getAPI()
	{
		return new WarpAPI( this );
	}
}
