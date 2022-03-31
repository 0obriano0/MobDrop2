package com.twsbrian.MobDrop2.Database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.twsbrian.MobDrop2.Main;
import com.twsbrian.MobDrop2.Command.CommandsList.MainList;
import com.twsbrian.MobDrop2.FileIO.LoadConfig;
import com.twsbrian.MobDrop2.FileIO.LoadItems;
import com.twsbrian.MobDrop2.FileIO.LoadLanguage;
import com.twsbrian.MobDrop2.FileIO.LoadMobs;

public class DataBase {
	// 主插件
	public static Main main;
	
	// 設定檔
	public static LoadConfig LoadConfig;
	public static LoadItems LoadItems;
	public static LoadMobs LoadMobs;
	public static LoadLanguage LoadLanguage;
	
	// 伺服器
	public static Server server;
	
	//語言包
	public static language language = new language();
	
	// 插件目錄
	public static String pluginMainDir = "./plugins/MobDrop/";
	
	//指令目錄
	public static MainList CommandsList = new MainList();

	//掉落物品清單
	public static Map<String, Mob> MobsMap = new HashMap<String, Mob>();
	
	//物品設定
	public static Map<String, Items> ItemMap = new HashMap<String, Items>();
	
	//公開顯示訊息
	public static Config Config;
	
	// 顯示訊息
	public static void Print(String msg)
	{
		System.out.print("[MobDrop] " + msg);
	}
	
	// 取得生物名稱(中文)
	public static String GetEntityName(String entityId){
		if(DataBase.language.IDMobtoMessage.containsKey(entityId))
			return DataBase.language.IDMobtoMessage.get(entityId);
		else
			return entityId;
	}
	
	// 取得生物名稱(中文)
	public static String getEntityNameGameCode(String entityId){
		if(DataBase.language.MessagetoIDMob.containsKey(entityId))
			return DataBase.language.MessagetoIDMob.get(entityId);
		else
			return entityId;
	}
	
	//取玩家得頭顱
	public static ItemStack getplayerHead(String name) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(DataBase.Config.player_title.replaceAll("%player%", name));
        ArrayList<String> lore = new ArrayList<String>();
        for(String loredata : DataBase.Config.player_lore) {
        	lore.add(loredata.replaceAll("%player%", name).replace("&","§").replaceAll("%time%", gettime()));
        }
        skull.setLore(lore);
        skull.setOwner(name);
        item.setItemMeta(skull);
        return item;
    }
	
	public static String gettime() {
		Date dNow = new Date( );
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");

		return ft.format(dNow);
	}
}
