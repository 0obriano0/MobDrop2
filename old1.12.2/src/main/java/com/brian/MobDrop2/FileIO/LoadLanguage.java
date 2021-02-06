package com.brian.MobDrop2.FileIO;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.brian.MobDrop2.AnsiColor;
import com.brian.MobDrop2.Database.DataBase;

public class LoadLanguage {
	// 主要讀取設定用
		private FileConfiguration data = null;

		// 開檔用
		private File filePreload = null;
		
		private String loadfiledir = "lang";
		private String loadfilename = "zh_TW.yml";
		
		public LoadLanguage(){
		}
		
		public void ReLoadLanguage(){
			loadfilename = DataBase.Config.lang + ".yml";
			// 確認檔案是否存在
		    this.filePreload = new File(DataBase.pluginMainDir + loadfiledir + "/" + loadfilename);
		    if (!this.filePreload.exists()){
		    	DataBase.main.getLogger().info(AnsiColor.RED + "[FileLoad] " + DataBase.Config.lang + ".yml 讀取失敗，使用預設值 zh_TW.yml" + AnsiColor.RESET);
		    	DataBase.Config.lang = "zh_TW";
		    	loadfilename = DataBase.Config.lang + ".yml";
		    	this.filePreload = new File(DataBase.pluginMainDir + loadfiledir + "/" + loadfilename);
		    }
		    if (this.filePreload.exists()){
		    	// 讀取設定檔內容
		    	this.data = YamlConfiguration.loadConfiguration(this.filePreload);
		    }
		    else{
		    	// 檔案不存在，建立預設檔
		    	CreateDefaultfile();
		    	// 重載檔案
		    	this.filePreload = new File(DataBase.pluginMainDir + loadfilename);
		    	// 讀取設定檔內容
		    	this.data = YamlConfiguration.loadConfiguration(this.filePreload);
		    	ReLoadLanguage();
		    	return;
		    }
		    
		    if(data.contains("Plugin")) {
		    	if(data.contains("Plugin.title")) DataBase.language.Plugin_name = data.getString("Plugin.title").replace("&","§"); else errorMessage("Plugin","title");
		    }
		    
		    if(data.contains("InventoryGUI")) {
		    	if(data.contains("InventoryGUI.info"))      					DataBase.language.Inventory.info      					= data.getString("InventoryGUI.info").replace("&","§");      					else errorMessage("InventoryGUI","info");
		    	if(data.contains("InventoryGUI.info_player_sakurahead"))      	DataBase.language.Inventory.info_player_sakurahead      = data.getString("InventoryGUI.info_player_sakurahead").replace("&","§");      	else errorMessage("InventoryGUI","info_player_sakurahead");
		    	if(data.contains("InventoryGUI.MobsList"))      				DataBase.language.Inventory.MobsList      				= data.getString("InventoryGUI.MobsList").replace("&","§");      				else errorMessage("InventoryGUI","MobsList");
		    	if(data.contains("InventoryGUI.ItemList"))      				DataBase.language.Inventory.ItemList      				= data.getString("InventoryGUI.ItemList").replace("&","§");      				else errorMessage("InventoryGUI","ItemList");
		    	if(data.contains("InventoryGUI.close"))         				DataBase.language.Inventory.close        	 			= data.getString("InventoryGUI.close").replace("&","§");         				else errorMessage("InventoryGUI","close");
		    	if(data.contains("InventoryGUI.next"))          				DataBase.language.Inventory.next          				= data.getString("InventoryGUI.next").replace("&","§");          				else errorMessage("InventoryGUI","next");
		    	if(data.contains("InventoryGUI.previous"))      				DataBase.language.Inventory.previous      				= data.getString("InventoryGUI.previous").replace("&","§");      				else errorMessage("InventoryGUI","previous");
		    	if(data.contains("InventoryGUI.menu"))          				DataBase.language.Inventory.menu          				= data.getString("InventoryGUI.menu").replace("&","§");							else errorMessage("InventoryGUI","menu");
		    	if(data.contains("InventoryGUI.back"))          				DataBase.language.Inventory.back          				= data.getString("InventoryGUI.back").replace("&","§");          				else errorMessage("InventoryGUI","back");
		    	if(data.contains("InventoryGUI.back_menu"))     				DataBase.language.Inventory.back_menu     				= data.getString("InventoryGUI.back_menu").replace("&","§");     				else errorMessage("InventoryGUI","back_menu");
		    	if(data.contains("InventoryGUI.dropList"))      				DataBase.language.Inventory.dropList      				= data.getString("InventoryGUI.dropList").replace("&","§");      				else errorMessage("InventoryGUI","dropList");
		    	if(data.contains("InventoryGUI.items"))         				DataBase.language.Inventory.items         				= data.getString("InventoryGUI.items").replace("&","§");         				else errorMessage("InventoryGUI","items");
		    	if(data.contains("InventoryGUI.mobs"))          				DataBase.language.Inventory.mobs          				= data.getString("InventoryGUI.mobs").replace("&","§");          				else errorMessage("InventoryGUI","mobs");
		    	if(data.contains("InventoryGUI.Item_Chance"))   				DataBase.language.Inventory.Item_Chance   				= data.getString("InventoryGUI.Item_Chance").replace("&","§");   				else errorMessage("InventoryGUI","Item_Chance");
		    	if(data.contains("InventoryGUI.Item_Quantity")) 				DataBase.language.Inventory.Item_Quantity 				= data.getString("InventoryGUI.Item_Quantity").replace("&","§");				else errorMessage("InventoryGUI","Item_Quantity");
		    	if(data.contains("InventoryGUI.admin_lore"))    				DataBase.language.Inventory.admin_lore    				= getListString(data.getStringList("InventoryGUI.admin_lore"));  				else errorMessage("InventoryGUI","admin_lore");
		    }
		    
		    if(data.contains("message")) {
		    	if(data.contains("message.Gobal_mobDropItem")) DataBase.language.message.Gobal_mobDropItem   = data.getString("message.Gobal_mobDropItem").replace("&","§");    else errorMessage("message","Gobal_mobDropItem");
		    	if(data.contains("message.mobDropItem"))       DataBase.language.message.mobDropItem         = data.getString("message.mobDropItem").replace("&","§");          else errorMessage("message","mobDropItem");
		    	if(data.contains("message.wall"))              DataBase.language.message.wall                = data.getString("message.wall").replace("&","§");                 else errorMessage("message","wall");
		    }
		    int MobsSuccess = 0;
		    int MobsFail = 0;
		    if(data.contains("Mobs")) {
			    for (String MobsId : data.getConfigurationSection("Mobs").getKeys(false)) {
			    	String value = null;
			    	if(data.contains("Mobs." + MobsId)) {
			    		value = data.getString("Mobs." + MobsId).replace("&","§"); 
			    		DataBase.language.IDMobtoMessage.put(MobsId, value);
			    		DataBase.language.MessagetoIDMob.put(value, MobsId);
			    		MobsSuccess++;
			    	}else{
			    		errorMessage("Mobs",MobsId);
			    		MobsFail--;
			    	}
			    }
		    }else
		    	errorMessage("Mobs","datanotfond");
		    tools.Setprint("Loadlanguage","Mobs",MobsSuccess+MobsFail,MobsSuccess,MobsFail);
		    DataBase.main.getLogger().info(AnsiColor.CYAN + "[Loadlanguage] " + AnsiColor.GREEN + loadfilename + " Load Success" + AnsiColor.RESET);
		}
		
		public void CreateDefaultfile(){
			
		    String[] fileName = {"zh_TW","en"};
			try
			{
				for(int index = 0;index < fileName.length;index++) {
					if(CopyFileAPI.createFile(DataBase.pluginMainDir + "/" + loadfiledir, "/" + fileName[index] + ".yml", "/" + loadfiledir + "/" + fileName[index] + ".yml", DataBase.main))
						DataBase.main.getLogger().info(AnsiColor.GREEN + "[FileCreate] " + AnsiColor.YELLOW + fileName[index] + ".yml" + AnsiColor.GREEN +  " 創建成功" + AnsiColor.RESET);
					else
						DataBase.main.getLogger().info(AnsiColor.RED + "[FileCreate] 資料創建出現異常，請詢問程式設計師1" + AnsiColor.RESET);
				}
			}
			catch (Exception e)
			{
				DataBase.main.getLogger().info(AnsiColor.RED + "[FileCreate] 資料創建出現嚴重的錯誤，請詢問程式設計師" + AnsiColor.RESET);
			}
		}
		
		public void errorMessage(String title,String name) {
			DataBase.main.getLogger().info(AnsiColor.RED + "[Loadlanguage] " + title + " -> " + name + " 資料讀取失敗，使用預設值..." + AnsiColor.RESET);
		}
		
		public List<String> getListString(List<String> data) {
			for (int i = 0; i < data.size(); i++)
			{
				data.set(i, data.get(i).replace("_", " ").replace("&","§"));
			}
			return data;
		}
}
