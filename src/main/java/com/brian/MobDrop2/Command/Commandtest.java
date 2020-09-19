package com.brian.MobDrop2.Command;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.brian.MobDrop2.DataBase.DataBase;
import com.brian.MobDrop2.DataBase.Itemset;

public class Commandtest extends mainCommandSystem{
	public Commandtest() {
		super(  "test",
				"/noshove test 取得指令說明",
				new ArrayList<String>(Arrays.asList("noshove.admin.test")));
	}
	
	@Override
	public void run(CommandSender sender, String commandLabel, Command command, String[] args) throws Exception {
		
	}
	
	@Override
	public void run(Player player, String commandLabel, Command command, String[] args) throws Exception {
		DataBase.NoShoveItem.add(new Itemset(player.getInventory().getItemInMainHand()));
	}
}
