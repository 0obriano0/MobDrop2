package com.twsbrian.MobDrop2.Command.CommandsList;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.twsbrian.MobDrop2.Database.DataBase;
import com.twsbrian.MobDrop2.FileIO.WriteItems;

public class Item_IOCommand {
	public static boolean save(Player player,String[] args) {
		if(args.length != 2) {
			player.sendMessage("§c格式不對");
			return true;
		}
		if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
			player.sendMessage("§c手上是空的");
		}else {
			if(DataBase.ItemMap.containsKey(args[1].toUpperCase())) {
				player.sendMessage("§c已經有重複的 " + args[1].toUpperCase() + " 在系統裡了");
				return true;
			}
			WriteItems.addItemStack(player.getInventory().getItemInMainHand(),args[1].toUpperCase());
			player.sendMessage("§a物品增加成功");
		}
		return true;
	}
	
	public static boolean remove(Player player,String[] args) {
		if(args.length != 2) {
			player.sendMessage("§c格式不對");
			return true;
		}
		if(WriteItems.removeItemStack(args[1].toUpperCase())) {
			player.sendMessage("§a物品 " + args[1].toUpperCase() + " 刪除成功");
		}else {
			player.sendMessage("§c物品刪除失敗");
		}
		return true;
	}
}
