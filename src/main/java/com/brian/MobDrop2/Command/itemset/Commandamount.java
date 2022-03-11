package com.brian.MobDrop2.Command.itemset;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.brian.MobDrop2.Command.mainCommandSystem;
import com.brian.MobDrop2.DataBase.DataBase;
import com.brian.MobDrop2.DataBase.Itemset;

public class Commandamount extends mainCommandSystem{
	
	public Commandamount() {
		super(  "itemset.amount",
				"/mobdrop itemset amount 設定物品數量",
				new ArrayList<String>(Arrays.asList("mobdrop.admin.itemset.amount")));
	}
	
	@Override
	public void run(Player player, String commandLabel, Command command, String[] args) throws Exception {
		ItemStack setitem = player.getInventory().getItemInMainHand();
		if (!setitem.getType().toString().equals("AIR")) {
			if(args.length >= 1) {
				String str = args[0].replaceAll("&", "§");
				Itemset item = new Itemset(setitem);
				item.setAmount(Integer.parseInt(str));
				player.getInventory().setItemInMainHand(item.getItemStack());
			}
		} else {
			player.sendMessage(DataBase.fileMessage.getString("Command.HandNoItem"));
		}
	}
}
