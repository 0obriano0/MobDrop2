package com.brian.MobDrop2.Command.CommandsList;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.brian.MobDrop2.Database.DataBase;

public class ItemListCommand {
	public static boolean parseCommands(CommandSender sender, Command cmd, String label, String[] args) {
		// 迴圈顯示
		sender.sendMessage("§9=============§dMobDrop 裝備資訊§9============");
		if(args.length == 1) {
			for (String key : DataBase.ItemMap.keySet())
			{
				sender.sendMessage("§f" + key + "§a  物品名稱 - §f" +  DataBase.ItemMap.get(key).ItemName);
			}
		}else {
			
		}
		sender.sendMessage("§9========================================");
		return true;
	}
	
	public static List<String> onTabComplete(String[] args){
		List<String> show_commands = new ArrayList<String>();
		if(args.length == 2) {
			for (String key : DataBase.ItemMap.keySet())
			{
				if(key.indexOf(args[1].toUpperCase()) != -1)
					show_commands.add(key);	
			}
		}
		return show_commands;
	}
}
