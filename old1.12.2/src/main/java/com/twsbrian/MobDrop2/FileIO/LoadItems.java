package com.twsbrian.MobDrop2.FileIO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.twsbrian.MobDrop2.AnsiColor;
import com.twsbrian.MobDrop2.Database.DataBase;
import com.twsbrian.MobDrop2.Database.Items;

public class LoadItems {
	// 主要讀取設定用
	private FileConfiguration data = null;

	// 開檔用
	private File filePreload = null;
	
	private String loadfilename = "Items.yml";
	
	public LoadItems()
	{
		
	}
	
	public void ReLoadItems()
	{
		// 確認檔案是否存在
	    this.filePreload = new File(DataBase.pluginMainDir + loadfilename);
	    if (this.filePreload.exists())
	    {
	    	// 讀取設定檔內容
	    	this.data = YamlConfiguration.loadConfiguration(this.filePreload);
	    }
	    else
	    {
	    	// 檔案不存在，建立預設檔
	    	CreateDefaultfile();
	    	// 重載檔案
	    	this.filePreload = new File(DataBase.pluginMainDir + loadfilename);
	    	// 讀取設定檔內容
	    	this.data = YamlConfiguration.loadConfiguration(this.filePreload);
	    }
	    
	    DataBase.ItemMap.clear();
	    
		// 掉落的物品名稱
		String ItemName = "";
		// 掉落的物品是否套用原始名稱
		boolean UseCustomName = false;
		// 掉落的物品說明
		List<String> ItemLores = new ArrayList<String>();
		// 掉落的物品名稱(原始名稱)
		String ItemRealname = "";
		// 色彩
		int Red = 0;
		int Green = 0;
		int Blue = 0;
		// 掉落的物品附魔
		List<String> Enchants = new ArrayList<String>();
		List<String> ItemFlags = new ArrayList<String>();
		boolean Unbreakable;
		
		short durability = 0;
		// 拆解物品附屬ID用
		//String strItemID = "";
		
		int failData = 0;
		// 取得生物名稱
		for (String ItemKey : data.getConfigurationSection("").getKeys(false)){
			// 迴圈讀出掉落物
			// ###########################################
			// 清空暫存區
			// ###########################################
			// 掉落的物品名稱
			ItemName = "";
			// 掉落的物品是否套用原始名稱
			UseCustomName = false;
			// 掉落的物品說明
			ItemLores = new ArrayList<String>();
			// 掉落的物品名稱(原始名稱)
			ItemRealname = "";
			// 色彩
			Red = 0;
			Green = 0;
			Blue = 0;
			// 掉落的物品附魔
			Enchants = new ArrayList<String>();
			ItemFlags = new ArrayList<String>();
			Unbreakable = false;
			// 拆解物品附屬ID用
			//strItemID = "";
			// ###########################################
			// 開始讀取內容
			// ###########################################
			// 讀取物品名稱
			if (data.contains(ItemKey + ".ItemName"))
			{
				ItemName = data.getString(ItemKey + ".ItemName");
			}
			
			if(ItemKey.toLowerCase().equals("head")) {
				failData++;
				DataBase.main.getLogger().info(AnsiColor.RED + "[LoadItems] " + AnsiColor.GREEN + "ID: " + AnsiColor.WHITE + ItemKey + AnsiColor.GREEN + " 物品: " + AnsiColor.WHITE + ItemName + AnsiColor.RED + " 未設定成功,此ID為特殊字元請換別的名詞" + AnsiColor.RESET);
    			continue;
    		}
			
			if (data.contains(ItemKey + ".UseCustomName")) 
				UseCustomName = data.getBoolean(ItemKey + ".UseCustomName");
			// 物品說明
			if (data.contains(ItemKey + ".ItemLores"))
			{
				ItemLores = data.getStringList(ItemKey +  ".ItemLores");
				for (int i = 0; i < ItemLores.size(); i++)
				{
					ItemLores.set(i, ItemLores.get(i).replace("_", " "));
				}
			}
			if (data.contains(ItemKey +  ".ItemRealname"))
			{
				ItemRealname = data.getString(ItemKey + ".ItemRealname").toUpperCase();
			}
			
			if (data.contains(ItemKey + ".ItemFlags")) ItemFlags = data.getStringList(ItemKey + ".ItemFlags");
			
			if (data.contains(ItemKey + ".Unbreakable")) Unbreakable = data.getBoolean(ItemKey + ".Unbreakable");
			
			if (data.contains(ItemKey + ".durability")) durability = (short)data.getInt(ItemKey + ".durability");
			
			// 判斷是否為皮甲(讀取染色碼)
			if(ItemRealname.split("_")[0].equals("LEATHER")) {
				if (data.contains(ItemKey + ".RGB")) {
					String RGBbuffer = this.data.getString(ItemKey + ".RGB");
					Red = Integer.parseInt(RGBbuffer.split(",")[0]);
					Green = Integer.parseInt(RGBbuffer.split(",")[1]);
					Blue = Integer.parseInt(RGBbuffer.split(",")[2]);
				}
			}
			
			// 取得附魔
			if (data.contains(ItemKey + ".Enchants"))
			{
				Enchants = this.data.getStringList(ItemKey + ".Enchants");
			}
			
			// 判斷是否有必要資訊
			if (ItemRealname.length() > 0 && (Red <=255 && Red >= 0) && (Blue <=255 && Blue >= 0) && (Green <=255 && Green >= 0) && ItemName != "" && ItemRealname != "")
			{
				// 加入
				try {
					Items item = new Items(ItemName, UseCustomName, ItemRealname, ItemLores, Red, Green, Blue, Enchants,ItemFlags,Unbreakable,durability);
					item.getResultItem();
					DataBase.ItemMap.put(ItemKey.toUpperCase(),item);
					// 加入
					if(DataBase.Config.command_cmd_show)
						DataBase.main.getLogger().info(AnsiColor.GREEN + "[LoadItems] " + AnsiColor.GREEN + "物品 " + AnsiColor.WHITE + ItemName + AnsiColor.GREEN + " 設定成功" + AnsiColor.RESET);
				}catch(Exception e) {
					DataBase.main.getLogger().info(AnsiColor.RED + "[LoadItems] " + AnsiColor.GREEN + "ID: " + AnsiColor.WHITE + ItemKey + AnsiColor.GREEN + " 物品: " + AnsiColor.WHITE + ItemName + AnsiColor.RED + " 未設定成功" + AnsiColor.RESET);
				}
			}else{
				// 警告
				failData++;
				DataBase.main.getLogger().info(AnsiColor.RED + "[LoadItems] " + AnsiColor.GREEN + "ID: " + AnsiColor.WHITE + ItemKey + AnsiColor.GREEN + " 物品: " + AnsiColor.WHITE + ItemName + AnsiColor.RED + " 未設定成功" + AnsiColor.RESET);
			}
	    }
		tools.Setprint("LoadItems","Items",DataBase.ItemMap.size()+failData,DataBase.ItemMap.size(),failData);
	}
	
	// 建立預設檔
	public void CreateDefaultfile()
	{
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
	
	public void errorMessage(String title,String name,String def) {
		DataBase.main.getLogger().info(AnsiColor.RED + "[Loadlanguage] " + title + " -> " + name + " 資料讀取失敗，使用預設值: " + def + AnsiColor.RESET);
	}
}
