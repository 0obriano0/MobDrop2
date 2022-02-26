package com.brian.MobDrop2.Command;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import com.brian.MobDrop2.DataBase.DataBase;

public class Commandtest extends mainCommandSystem{
	public Commandtest() {
		super(  "test",
				"/mobdrop test 取得指令說明",
				new ArrayList<String>(Arrays.asList("mobdrop.admin.test")));
	}
	
	@Override
	public void run(CommandSender sender, String commandLabel, Command command, String[] args){
	    if(args.length >= 2) {
	    	try {
	    		EntityType.valueOf(args[1].toUpperCase());
	    		DataBase.Print(args[1] + " 存在 ");
	    	}catch(IllegalArgumentException e) {
	    		DataBase.Print(args[1] + " 不存在 ");
	    	}
	    }
	}
}
	
