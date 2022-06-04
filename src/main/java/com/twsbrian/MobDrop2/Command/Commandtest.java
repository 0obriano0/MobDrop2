package com.twsbrian.MobDrop2.Command;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.twsbrian.MobDrop2.DataBase.DataBase;

public class Commandtest extends mainCommandSystem{
	public Commandtest() {
		super(  "test",
				"/mobdrop test 取得指令說明",
				new ArrayList<String>(Arrays.asList("mobdrop.admin.test")));
	}
	
	@Override
	public void run(CommandSender sender, String commandLabel, Command command, String[] args){
//		DataBase.Print("commandLabel =  " + commandLabel);
//	    if(args.length >= 1) {
//	    	try {
//	    		EntityType.valueOf(args[0].toUpperCase());
//	    		DataBase.Print(args[0] + " 存在 ");
//	    	}catch(IllegalArgumentException e) {
//	    		DataBase.Print(args[0] + " 不存在 ");
//	    	}
//	    }
//	    DataBase.Print("Colors:");
//		for(ChatColor color: ChatColor.values()) {
//			DataBase.Print(color.name());
//		}
//		for (String name : MobDrop2.plugin.getConfig().getConfigurationSection("Glow.Chance").getKeys(false)) {
//			DataBase.Print(name);
//		}
		if(args.length >= 1) {
			DataBase.Print(DataBase.getDropColorByChance(Double.valueOf(args[0])));
		}
	}
}
	
