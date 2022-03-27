package com.brian.MobDrop2.Command.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.brian.MobDrop2.Command.mainCommandSystem;
import com.brian.MobDrop2.DataBase.DataBase;
import com.brian.MobDrop2.DataBase.Itemset;

public class Commandremove extends mainCommandSystem{
	public Commandremove() {
		super(  "items.remove",
				"/mobdrop items remove 增加物品",
				new ArrayList<String>(Arrays.asList("mobdrop.admin.items.remove")));
	}
	
	@Override
	public void run(Player player, String commandLabel, Command command, String[] args) throws Exception {
		
	}
}
