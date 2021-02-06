package com.brian.MobDrop2.Command.CommandsList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.brian.MobDrop2.Database.DataBase;

public class ReloadCommand {
	public static boolean parseCommands(CommandSender sender, Command cmd, String label, String[] args) {
		// 清除合成表
		DataBase.server.resetRecipes();
		// 清除對照表
		DataBase.MobItemMap.clear();
		DataBase.ItemMap.clear();
		// 重讀
		DataBase.LoadConfig.ReLoadConfig();
		DataBase.LoadLanguage.ReLoadLanguage();
		DataBase.LoadItems.ReLoadItems();
		DataBase.LoadMobs.ReLoadMobs();
		sender.sendMessage(ChatColor.YELLOW +"設定檔讀取完成");
		return true;
	}
}
