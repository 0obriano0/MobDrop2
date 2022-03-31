package com.twsbrian.MobDrop2.Command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.twsbrian.MobDrop2.Command.CommandsList.GetItemCommand;
import com.twsbrian.MobDrop2.Command.CommandsList.ItemListCommand;
import com.twsbrian.MobDrop2.Command.CommandsList.Item_IOCommand;
import com.twsbrian.MobDrop2.Command.CommandsList.MobListCommand;
import com.twsbrian.MobDrop2.Command.CommandsList.ReloadCommand;
import com.twsbrian.MobDrop2.Database.DataBase;
import com.twsbrian.MobDrop2.InventoryGUI.InventoryMenu;

public class PlayerCommands implements CommandExecutor ,TabExecutor{
 
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		// 判斷是否為玩家的指令
		if (sender instanceof Player){
			// 判斷指令長度
			if (args.length == 0){
				// 顯示說明
				sender.sendMessage("§9==========§dMobDrop§9==========");
				sender.sendMessage("§a/mdop menu                          §f- §e開啟目錄");
				if(DataBase.Config.command_old_list) {
					sender.sendMessage("§a/mdop moblist <生物名(可打可不打)>  §f- §e列出所有生物的掉落資訊");
					sender.sendMessage("§a/mdop itemlist                       §f- §e列出所有道具的掉落資訊");
					
				}
				if (sender.hasPermission("MobDrop.admin")) {
					if(DataBase.Config.command_old_list)
						sender.sendMessage("§a/mdop getitem <物品名稱>            §f- §e獲取道具");
					sender.sendMessage("§a/mdop item <物品名稱>               §f- §e存取手上的物品");
					sender.sendMessage("§a/mdop remove <物品名稱>            §f- §e刪除系統裡的物品");
					sender.sendMessage("§a/mdop reload                        §f- §e重新讀取資料");
				}
				sender.sendMessage("§9===========================");
				return true;
			}
			else{
				if (sender.hasPermission("MobDrop.admin")) {
					if (args[0].equals("reload"))
						return ReloadCommand.parseCommands(sender, cmd, label, args);
					else if (args[0].equals("getitem") && DataBase.Config.command_old_list)
						return GetItemCommand.parseCommands(sender, cmd, label, args);
					else if(args[0].equals("item"))
						return Item_IOCommand.save((Player)sender,args);
					else if(args[0].equals("remove"))
						return Item_IOCommand.remove((Player)sender,args);
				}
				if (args[0].equals("moblist") && DataBase.Config.command_old_list)
					return MobListCommand.parseCommands(sender, cmd, label, args);
				//封存
				else if(args[0].equals("itemlist") && DataBase.Config.command_old_list) 
					return ItemListCommand.parseCommands(sender, cmd, label, args);
				else if(args[0].equals("menu")) {
					InventoryMenu.INVENTORY.open((Player) sender);
					return true;
				}else
					sender.sendMessage("§c/" + label + " " + args[0] + " <-- 查無此指令");
			}
	    }
		else
		{
			if (args.length == 0){
				// 顯示說明
				sender.sendMessage("§9==========§dMobDrop§9==========");
				sender.sendMessage("§a/mdop moblist <生物名(可打可不打)> §f- §e列出所有生物的掉落資訊");
				sender.sendMessage("§a/mdop itemlist §f- §e列出所有道具的掉落資訊");
				sender.sendMessage("§a/mdop reload §f- §e重新讀取資料");
				sender.sendMessage("§9===========================");
				return true;
			}else if(args.length == 1) {
				if (args[0].equals("moblist"))
					return MobListCommand.parseCommands(sender, cmd, label, args);
				else if(args[0].equals("itemlist")) 
					return ItemListCommand.parseCommands(sender, cmd, label, args);
				else if(args[0].equals("reload")) 
					return ReloadCommand.parseCommands(sender, cmd, label, args);
				else {
					sender.sendMessage("§c/" + label + " " + args[0] + " <-- 查無此指令");
					return false;
				}
			}
	    	sender.sendMessage("§c 此指令不支援控制台模式!");
	    	return false;
	    }
		return false;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) 
			return DataBase.CommandsList.secand_arg(args[0],sender);
		else if (args.length == 2) {
			if (sender.hasPermission("MobDrop.admin")) {	
				if (args[0].equals("getitem")) {
					return GetItemCommand.onTabComplete(args[1]);
				}
			}
			if (args[0].equals("moblist")) {
				return MobListCommand.onTabComplete(args[1]);
			}
			if (args[0].equals("itemlist")) {
				return ItemListCommand.onTabComplete(args);
			}
		}
		return null;
	}
}
