package com.brian.MobDrop2.DropItems;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.brian.MobDrop2.Database.DataBase;

public class GetItem {
	public static boolean getitem(Player player,String[] args) {
		if(DataBase.ItemMap.containsKey(args[1].toUpperCase())) {
			ItemStack Itemcreate = DataBase.ItemMap.get(args[1].toUpperCase()).getResultItem();
			Itemcreate.setAmount(1);
			if(player.getInventory().firstEmpty() == -1)
				player.sendMessage("§b" + DataBase.language.Plugin_name + " §c背包已滿，無法獲取道具");
			else {
				player.getInventory().addItem(Itemcreate);
				player.sendMessage("§b" + DataBase.language.Plugin_name + " §f獲取道具: " + DataBase.ItemMap.get(args[1].toUpperCase()).ItemName);
			}
		}else {
			player.sendMessage("§b" + DataBase.language.Plugin_name + " §c查無裝備 ID 請重新查詢");
			return false;
		}
		return true;
	}
}
