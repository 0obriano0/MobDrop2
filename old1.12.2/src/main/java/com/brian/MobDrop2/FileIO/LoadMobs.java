package com.twsbrian.MobDrop2.FileIO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.twsbrian.MobDrop2.AnsiColor;
import com.twsbrian.MobDrop2.Database.DataBase;
import com.twsbrian.MobDrop2.Database.Items;
import com.twsbrian.MobDrop2.Database.Mob;
import com.twsbrian.MobDrop2.Database.MobItemList;

public class LoadMobs {
	// 主要讀取設定用
		private FileConfiguration data = null;

		// 開檔用
		private File filePreload = null;
		
		private String loadfilename = "Mobs.yml";
		
		public LoadMobs(){
			
		}
		
		public void ReLoadMobs(){
			// 確認檔案是否存在
		    this.filePreload = new File(DataBase.pluginMainDir + loadfilename);
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
		    }
		    
		    // 待儲存的掉落物清單
		 	List<MobItemList> ItemsList = new ArrayList<MobItemList>();
		 	List<MobItemList> HeadsList = new ArrayList<MobItemList>();
		 	int failData = 0;
		 	int success = 0;
		 	
		    for (String entity_name : data.getConfigurationSection("").getKeys(false)) {
		    	ItemsList = new ArrayList<MobItemList>();
		    	HeadsList = new ArrayList<MobItemList>();
		    	for (String Itemkey : data.getConfigurationSection(entity_name).getKeys(false)) {
		    		int Quantity = -1;
		    		int Quantity_max = 0;
		    		String ItemName = "";
		    		List<String> ItemLores = new ArrayList<String>();
		    		String value = "";
		    		
		    		if(Itemkey.toLowerCase().equals("heads")) {
		    			int head_id = 0;
		    			for (String headkey : data.getConfigurationSection(entity_name+".heads").getKeys(false)) {
		    				Quantity = 1;
				    		Quantity_max = 1;
				    		ItemName = headkey.replace("&","§");
				    		ItemLores = new ArrayList<String>();
				    		value = "";
				    		
				    		if (data.contains(entity_name + ".heads." + headkey + ".ItemName")){
			    				ItemName = data.getString(entity_name + ".heads." + headkey + ".ItemName").replace("&","§");
			    			}
				    		
				    		if (data.contains(entity_name + ".heads." + headkey + ".Lore"))
			    			{
			    				ItemLores = data.getStringList(entity_name + ".heads." + headkey +  ".Lore");
			    				for (int i = 0; i < ItemLores.size(); i++)
			    				{
			    					ItemLores.set(i, ItemLores.get(i).replace("&","§").replace("_", " "));
			    				}
			    			}
			    			if (data.contains(entity_name + ".heads." + headkey + ".value")){
			    				value = data.getString(entity_name + ".heads." + headkey + ".value");
			    			}
			    			
			    			// 掉落率
				    		double Chance = -1;
				    		
				    		if (data.contains(entity_name + ".heads." + headkey + "." + "Chance")){
				    			Chance = data.getDouble(entity_name + ".heads." + headkey + "." + "Chance");
				    		}
				    		
				    		if(Itemkey.toLowerCase().equals("heads") && Quantity >= 1 && !(Chance < 0)) {
				    			HeadsList.add(new MobItemList(Quantity, Quantity_max, Chance,new Items(ItemName, ItemLores, value)));
				    			success++;
				    			head_id++;
				    			if(DataBase.Config.command_cmd_show)
				    				DataBase.main.getLogger().info(AnsiColor.GREEN + "[LoadMobs] " + AnsiColor.WHITE + DataBase.GetEntityName(entity_name) + AnsiColor.GREEN + " 的掉落物 " + AnsiColor.WHITE + "heads_" + head_id + AnsiColor.GREEN + " 設定成功" + AnsiColor.RESET);
				    		}else {
				    			failData++;
				    			if(DataBase.ItemMap.containsKey(Itemkey))
				    				DataBase.main.getLogger().info(AnsiColor.RED + "[LoadMobs] " + AnsiColor.WHITE + DataBase.GetEntityName(entity_name) + AnsiColor.RED + " 的掉落物 " + AnsiColor.WHITE + DataBase.ItemMap.get(Itemkey).ItemName + AnsiColor.RED + " 未設定成功" + AnsiColor.RESET);
				    			else
				    				DataBase.main.getLogger().info(AnsiColor.RED + "[LoadMobs] " + AnsiColor.WHITE + DataBase.GetEntityName(entity_name) + AnsiColor.RED + " 的掉落物 " + AnsiColor.WHITE + Itemkey + AnsiColor.RED + " 未設定成功" + AnsiColor.RESET);
				    		}
		    			}
		    		}else{
		    			if(Itemkey.toLowerCase().equals("head")) {
			    			Quantity = 1;
			    			Quantity_max = 1;
			    			if (data.contains(entity_name + "." +Itemkey + ".ItemName")){
			    				ItemName = data.getString(entity_name + "." + Itemkey + ".ItemName").replace("&","§");
			    			}
			    			if (data.contains(entity_name + "." +Itemkey + ".Lore"))
			    			{
			    				ItemLores = data.getStringList(entity_name + "." +Itemkey +  ".Lore");
			    				for (int i = 0; i < ItemLores.size(); i++)
			    				{
			    					ItemLores.set(i, ItemLores.get(i).replace("&","§").replace("_", " "));
			    				}
			    			}
			    			if (data.contains(entity_name + "." +Itemkey + ".value")){
			    				value = data.getString(entity_name + "." + Itemkey + ".value");
			    			}
			    		}else {
			    			// 得到的物品數量
				    		if (data.contains(entity_name + "." + Itemkey + "." + "Quantity")){
				    			Quantity = data.getInt(entity_name + "." + Itemkey + "." + "Quantity");
				    		}
				    		
				    		if (data.contains(entity_name + "." + Itemkey + "." + "Quantity_max")){
				    			Quantity_max = data.getInt(entity_name + "." + Itemkey + "." + "Quantity_max");
				    		}else {
				    			Quantity_max = Quantity;
				    		}
			    		}
			    		
			    		// 掉落率
			    		double Chance = -1;
			    		
			    		if (data.contains(entity_name + "." + Itemkey + "." + "Chance")){
			    			Chance = data.getDouble(entity_name + "." + Itemkey + "." + "Chance");
			    		}
			    		Itemkey = Itemkey.toUpperCase();
			    		if(Itemkey.toLowerCase().equals("head") && Quantity >= 1 && !(Chance < 0)) {
			    			ItemsList.add(new MobItemList(Quantity, Quantity_max, Chance,new Items(ItemName, ItemLores, value)));
			    			success++;
			    			if(DataBase.Config.command_cmd_show) 
			    				DataBase.main.getLogger().info(AnsiColor.GREEN + "[LoadMobs] " + AnsiColor.WHITE + DataBase.GetEntityName(entity_name) + AnsiColor.GREEN + " 的掉落物 " + AnsiColor.WHITE + "head" + AnsiColor.GREEN + " 設定成功" + AnsiColor.RESET);
			    			
			    				
			    		}else if(DataBase.ItemMap.containsKey(Itemkey) && Quantity >= 1 && !(Chance < 0)) {
			    			ItemsList.add(new MobItemList(Quantity, Quantity_max, Chance, DataBase.ItemMap.get(Itemkey)));
			    			success++;
			    			if(DataBase.Config.command_cmd_show)
			    				DataBase.main.getLogger().info(AnsiColor.GREEN + "[LoadMobs] " + AnsiColor.WHITE + DataBase.GetEntityName(entity_name) + AnsiColor.GREEN + " 的掉落物 " + AnsiColor.WHITE + DataBase.ItemMap.get(Itemkey).ItemName + AnsiColor.GREEN + " 設定成功" + AnsiColor.RESET);
			    		}else {
			    			failData++;
			    			if(DataBase.ItemMap.containsKey(Itemkey))
			    				DataBase.main.getLogger().info(AnsiColor.RED + "[LoadMobs] " + AnsiColor.WHITE + DataBase.GetEntityName(entity_name) + AnsiColor.RED + " 的掉落物 " + AnsiColor.WHITE + DataBase.ItemMap.get(Itemkey).ItemName + AnsiColor.RED + " 未設定成功" + AnsiColor.RESET);
			    			else
			    				DataBase.main.getLogger().info(AnsiColor.RED + "[LoadMobs] " + AnsiColor.WHITE + DataBase.GetEntityName(entity_name) + AnsiColor.RED + " 的掉落物 " + AnsiColor.WHITE + Itemkey + AnsiColor.RED + " 未設定成功" + AnsiColor.RESET);
			    		}
		    		}
		    	}
		    	DataBase.MobsMap.put(entity_name.toUpperCase().replace("&","§"),new Mob(entity_name.toUpperCase().replace("&","§"),ItemsList,HeadsList));
		    }
		    tools.Setprint("LoadMobs","MobsLoad",success + failData,success,failData);
		}
		
		public void CreateDefaultfile(){
			try
			{
				if(CopyFileAPI.createFile(DataBase.pluginMainDir, loadfilename, "/"+loadfilename, DataBase.main))
					DataBase.main.getLogger().info(AnsiColor.GREEN + "[FileCreate] " + AnsiColor.YELLOW + loadfilename + AnsiColor.GREEN +  " 創建成功" + AnsiColor.RESET);
				else
					DataBase.main.getLogger().info(AnsiColor.RED + "[FileCreate] 資料創建出現異常，請詢問程式設計師" + AnsiColor.RESET);
			}
			catch (Exception e)
			{
				DataBase.main.getLogger().info(AnsiColor.RED + "[FileCreate] 資料創建出現嚴重的錯誤，請詢問程式設計師" + AnsiColor.RESET);
			}
		}
}
