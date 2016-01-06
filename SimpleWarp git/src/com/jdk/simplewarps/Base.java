package com.jdk.simplewarps;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Base extends JavaPlugin{
	Base plugin;
	@Override
	public void onEnable(){
		
		// Registering base methods
		
		getAPI().setupConfig();
		
		// Registering events / commands
		
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new WarpsListener( this ), this);
		
	}
	
	public WarpAPI getAPI()
	{
		return new WarpAPI( this );
	}
}
