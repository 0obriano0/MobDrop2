package com.brian.MobDrop2.Command.itemset;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.brian.MobDrop2.Command.mainCommandSystem;
import com.brian.MobDrop2.DataBase.DataBase;
import com.brian.MobDrop2.DataBase.Itemset;

public class Commandaddlore extends mainCommandSystem{
	
	public Commandaddlore() {
		super(  "itemset.addlore",
				"/mobdrop itemset addlore 設定物品名稱",
				new ArrayList<String>(Arrays.asList("mobdrop.admin.itemset.addlore")));
	}
	
	@Override
	public void run(Player player, String commandLabel, Command command, String[] args) throws Exception {
		ItemStack setitem = player.getInventory().getItemInMainHand();
		if (!setitem.getType().toString().equals("AIR")) {
			if(args.length >= 1) {
				String totalstr = "";
				boolean first = true;
				for(String str : args) {
					totalstr = totalstr + (first ? "" : " ") + str;
					first = false;
				}
				Itemset item = new Itemset(setitem).addLore(totalstr.replaceAll("&", "§"));
				
				player.getInventory().setItemInMainHand(item.getItemStack());
			}
		} else {
			player.sendMessage(DataBase.fileMessage.getString("Command.HandNoItem"));
		}
	}
}
