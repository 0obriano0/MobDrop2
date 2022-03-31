package com.twsbrian.MobDrop2.Command.CommandsList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.twsbrian.MobDrop2.Database.DataBase;

public class MainList {
	CommandsTools Tools = new CommandsTools();
	
	private List<String> playercommands = Arrays.asList("menu".split(" "));
	private List<String> playercommands_old_list = Arrays.asList("itemlist moblist".split(" "));
	private List<String> admincommands = Arrays.asList("reload".split(" "));
	private List<String> admincommands_old_list = Arrays.asList("getitem".split(" "));
	
	public List<String> secand_arg(String commands, CommandSender sender) {
		List<String> show_commands = new ArrayList<String>();
		if(DataBase.Config.command_old_list)
			Tools.search(playercommands_old_list, commands, show_commands);
		Tools.search(playercommands, commands, show_commands);
		if (sender.hasPermission("MobDrop.admin")) {
			if(DataBase.Config.command_old_list)
				Tools.search(admincommands_old_list, commands, show_commands);
			Tools.search(admincommands, commands, show_commands);
		}
		return show_commands;
	}
	
	public MainList(){
		
	}
}
