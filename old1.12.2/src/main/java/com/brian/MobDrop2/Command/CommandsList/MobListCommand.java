package com.brian.MobDrop2.Command.CommandsList;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.brian.MobDrop2.Database.DataBase;
import com.brian.MobDrop2.Database.MobItemList;

public class MobListCommand {
	private static boolean list_base(CommandSender sender,List<MobItemList> MobDropItemList ,String keyName){
		try {
			sender.sendMessage("§a「" + DataBase.GetEntityName(keyName) + "」");
			int itemnum = 0;
			for (MobItemList MobDropItems : MobDropItemList)
			{
				if(MobDropItems.Quantity == MobDropItems.Quantity_max) {
					sender.sendMessage("§f  "+ itemnum +". " + MobDropItems.Item.ItemName + " §a(§f" + MobDropItems.Chance + "%§a掉落§f " + MobDropItems.Quantity + " §a個)");
				}else {
					sender.sendMessage("§f  " + itemnum +". " + MobDropItems.Item.ItemName + " §a(§f" + MobDropItems.Chance + "%§a掉落§f " + MobDropItems.Quantity + "-" + MobDropItems.Quantity_max + " §a個)");
				}
				itemnum++;
			}
		}
		catch (NullPointerException e)
		{
			sender.sendMessage("§b" + DataBase.language.Plugin_name + "§c查無「§n " + DataBase.GetEntityName(keyName) + " §r§c」生物，請重新查詢");
		}
		return true;
	}
	
	public static boolean parseCommands(CommandSender sender, Command cmd, String label, String[] args) {
		// 迴圈顯示
		List<MobItemList> MobDropItemList = new ArrayList<MobItemList>();
		sender.sendMessage("§9============§dMobDrop 怪物掉落資訊§9===========");
		if(args.length == 2) {
			//把讀取到的怪物名稱轉換成中文
			MobDropItemList = DataBase.MobItemMap.get(DataBase.getEntityNameGameCode(args[1].toUpperCase()));
			list_base(sender,MobDropItemList,args[1].toUpperCase());
		}else {
			for (String key : DataBase.MobItemMap.keySet())
			{
				MobDropItemList = DataBase.MobItemMap.get(key);
				list_base(sender,MobDropItemList,key);
			}
		}
		sender.sendMessage("§9========================================");
		return true;
	}
	
	public static List<String> onTabComplete(String command){
		List<String> show_commands = new ArrayList<String>();
		for (String key : DataBase.MobItemMap.keySet())
		{
			if(DataBase.Config.list_Chinese) {
				if(DataBase.GetEntityName(key.toUpperCase()).indexOf(command) != -1)
					show_commands.add(DataBase.GetEntityName(key));
			}else
				if(key.indexOf(command.toUpperCase()) != -1)
					show_commands.add(key);
					
		}
		return show_commands;
	}
}
