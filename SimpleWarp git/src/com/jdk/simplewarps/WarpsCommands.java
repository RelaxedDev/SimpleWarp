package com.jdk.simplewarps;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpsCommands implements CommandExecutor{
	public Base plugin;
	public WarpsCommands( Base plugin )
	{
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) 
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			if(cmd.getLabel().equalsIgnoreCase("warps"))
			{
				if(args.length > 0)
				{
					if(args[0].equalsIgnoreCase("help")){
						cMessage(p, "&4===== &cWarps &4=====");
						cMessage(p, "&b\"/warp help\" &3Display this menu.");
						cMessage(p, "&b\"/warp(s)\" &3Display warp list.");
						cMessage(p, "&b\"/warp <warp>\" &3Warp to a place.");
						cMessage(p, "&b\"/setwarp <warp> <GUI-Slot>\" &3Set a warp.");
						cMessage(p, "&b\"/deletewarp <warp>\" &3Delete a warp.");
						cMessage(p, "&4================");
					}else{
						plugin.getAPI().warpTo(args[0], p);
					}
				}
				else
				{
					plugin.getAPI().warpList(p);
				}
			}
			if(cmd.getLabel().equalsIgnoreCase("setwarp"))
			{
				if(args.length > 1 )
				{
					String warp = args[0];
					try{
						int slot = Integer.parseInt(args[1]);
						plugin.getAPI().createWarp(warp, slot, p);
					}catch(NumberFormatException ex){
						cMessage(p, "&7ERROR! &c\"" + args[1] + "\" is not a number.");
					}
				}
				else
				{
					cMessage(p, "&7ERROR! &cCorrect format is /setwarp <warp> <GUI-Slot>");
				}
			}
			if(cmd.getLabel().equalsIgnoreCase("deletewarp"))
			{
				if(args.length > 0)
				{
					plugin.getAPI().deleteWarp(args[0], p);
				}
				else
				{
					cMessage(p, "&7ERROR! &cCorrect format is /deletewarp <warp>");
					
				}
			}
		}else{
			sender.sendMessage(cString("&4===== &cWarps &4====="));
			sender.sendMessage(cString("&b\"/warp help\" &3Display this menu."));
			sender.sendMessage(cString("&b\"/warp(s)\" &3Display warp list."));
			sender.sendMessage(cString("&b\"/warp <warp>\" &3Warp to a place."));
			sender.sendMessage(cString("&b\"/setwarp <warp> <GUI-Slot>\" &3Set a warp."));
			sender.sendMessage(cString("&b\"/deletewarp <warp>\" &3Delete a warp."));
			sender.sendMessage(cString("&4================"));
		}
		return true;
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
