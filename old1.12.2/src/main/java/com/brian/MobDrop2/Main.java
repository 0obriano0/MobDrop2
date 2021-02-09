package com.brian.MobDrop2;

import org.bukkit.plugin.java.JavaPlugin;

import com.brian.MobDrop2.Command.PlayerCommands;
import com.brian.MobDrop2.Database.DataBase;
import com.brian.MobDrop2.FileIO.LoadConfig;
import com.brian.MobDrop2.FileIO.LoadItems;
import com.brian.MobDrop2.FileIO.LoadLanguage;
import com.brian.MobDrop2.FileIO.LoadMobs;
import com.brian.MobDrop2.Listener.DeathListener;

public class Main extends JavaPlugin {
	public void onEnable()
	{   
		// 設定主插件
		DataBase.main = this;
		// 設定伺服器
		DataBase.server = this.getServer();
		// 讀取設定檔
		DataBase.LoadItems = new LoadItems();
		DataBase.LoadMobs = new LoadMobs();
		DataBase.LoadConfig = new LoadConfig();
		DataBase.LoadLanguage = new LoadLanguage();
		DataBase.LoadConfig.ReLoadConfig();
		DataBase.LoadLanguage.ReLoadLanguage();
		DataBase.LoadItems.ReLoadItems();
		DataBase.LoadMobs.ReLoadMobs();
		// 註冊
		getServer().getPluginManager().registerEvents(new DeathListener(), this);
		getCommand("mobdrop").setExecutor(new PlayerCommands());
		// 訊息
		DataBase.Print("MobDropItems is enabled!");
	}
	
	// 載出
	public void onDisable()
	{
		// 清除合成表
		DataBase.server.resetRecipes();
		// 清除對照表
		DataBase.MobsMap.clear();
		DataBase.ItemMap.clear();
		// 訊息
		DataBase.Print("MobDropItems is disable!");
	}
}
