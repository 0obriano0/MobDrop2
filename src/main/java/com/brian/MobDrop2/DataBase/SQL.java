package com.brian.MobDrop2.DataBase;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.brian.MobDrop2.DataBase.MySQL.MySQLManager;

public class SQL {
	
	private MySQLManager MySQL;
	private String table_dropitem;
	private String table_mobs;
	private String table_items;
	
	public void reload() {
		if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
			this.table_dropitem = (DataBase.fileDataBaseInfo.mysql.Prefix.isEmpty() ? "" : DataBase.fileDataBaseInfo.mysql.Prefix) + "dropitem";
			this.table_mobs = (DataBase.fileDataBaseInfo.mysql.Prefix.isEmpty() ? "" : DataBase.fileDataBaseInfo.mysql.Prefix) + "mobs";
			this.table_items = (DataBase.fileDataBaseInfo.mysql.Prefix.isEmpty() ? "" : DataBase.fileDataBaseInfo.mysql.Prefix) + "items";
			MySQL_checkdb();
			MySQL_ReLoad();
		}
	}
	
	public ResultSet executeQuery(String command) {
		if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) 
			return MySQL.executeQuery(command, "");
		else 
			return null;
	}
	
	public ResultSet executeQuery(String command, String sql) {
		if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) 
			return MySQL.executeQuery(command, sql);
		else 
			return null;
	}
	
	public boolean executeUpdate(String command) {
		if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) 
			return MySQL.executeUpdate(command, "");
		else 
			return false;
	}
	
	public boolean executeUpdate(String command, String sql) {
		if(DataBase.fileDataBaseInfo.storage.method.equals("mysql"))
			return MySQL.executeUpdate(command, sql);
		else
			return false;
	}
	
	public List<Map<String,String>> select(String command) {
		if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) 
			return MySQL.select(command, "");
		else 
			return new ArrayList<Map<String,String>>();
	}
	
	public List<Map<String,String>> select(String command, String sql) {
		if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) 
			return MySQL.select(command, sql);
		else 
			return new ArrayList<Map<String,String>>();
	}
	
	private void MySQL_checkdb(){
		// init
		MySQL = new MySQLManager(DataBase.fileDataBaseInfo.mysql.username,
				   DataBase.fileDataBaseInfo.mysql.password,
				   "jdbc:mysql://" + DataBase.fileDataBaseInfo.mysql.hostname + "/",
				   DataBase.fileDataBaseInfo.mysql.database,
				   DataBase.fileDataBaseInfo.mysql.useSSL,
				   DataBase.fileDataBaseInfo.mysql.autoReconnect);
		
		// check dropitem
		List<Map<String,String>> data = MySQL.select(
				  "SELECT table_name\n"
				+ "FROM information_schema.tables\n"
				+ "WHERE table_schema = '" + DataBase.fileDataBaseInfo.mysql.database + "'\n"
				+ "And table_name = '" + this.table_dropitem + "'");
		
		if(data.isEmpty()) {
			MySQL.executeUpdate(
					  "CREATE TABLE `" + this.table_dropitem + "` (\n"
					+ "  `mobname` varchar(20) NOT NULL,\n"
					+ "  `custom` varchar(1) NOT NULL,\n"
					+ "  `itemno` varchar(100) NOT NULL,\n"
					+ "  `quantity` varchar(4) DEFAULT NULL,\n"
					+ "  `quantity_max` varchar(4) DEFAULT NULL,\n"
					+ "  `chance` decimal(6,3) DEFAULT NULL,\n"
					+ "  `world` longtext DEFAULT NULL,\n"
					+ "  PRIMARY KEY (`mobname`,`custom`,`itemno`)\n"
					+ ")");
		}
		
		// check mobs
		data = MySQL.select(
				  "SELECT table_name\n"
				+ "FROM information_schema.tables\n"
				+ "WHERE table_schema = '" + DataBase.fileDataBaseInfo.mysql.database + "'\n"
				+ "And table_name = '" + this.table_mobs + "'");
		
		if(data.isEmpty()) {
			MySQL.executeUpdate(
					  "CREATE TABLE `" + this.table_mobs + "` (\n"
					+ "  `mobname` varchar(20) NOT NULL,\n"
				    + "  `custom` varchar(1) NOT NULL,\n"
				    + "  `icon` longtext,\n"
					+ "  PRIMARY KEY (`mobname`,`custom`)\n"
					+ ")");
		}
		
		//check table_items
		data = MySQL.select(
				  "SELECT table_name\n"
				+ "FROM information_schema.tables\n"
				+ "WHERE table_schema = '" + DataBase.fileDataBaseInfo.mysql.database + "'\n"
				+ "And table_name = '" + this.table_items + "'");
		
		if(data.isEmpty()) {
			MySQL.executeUpdate(
					  "CREATE TABLE `" + this.table_items + "` (\n"
					+ "  `itemno` varchar(100) NOT NULL,\n"
				    + "  `item` longtext,\n"
					+ "  PRIMARY KEY (`itemno`)\n"
					+ ")");
		}
	}
	
	private void MySQL_ReLoad() {
		DataBase.CustomMobsMap.clear();
		DataBase.NormalMobsMap.clear();
		DataBase.items.clear();
		if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
			List<Map<String,String>> rows = MySQL.select(""
					+ "Select * \n"
					+ "From " + this.table_mobs + "\n");
			
			//Loading mobs
			for(Map<String,String> row : rows) {
				String id = row.get("mobname") != null ? row.get("mobname").toUpperCase() : "";
				String custom = row.get("custom") != null && row.get("custom").equals("Y")? "Y" : "N";
				String itemBase64 = row.get("icon") != null ? row.get("icon") : "";
				if(id.isEmpty()) continue;
				
				Mob mob = new Mob(id,custom);
				
				if(!itemBase64.isEmpty()) mob.setIcon(new Itemset(itemBase64).getItemStack());
				if(custom.equals("Y")) {
					DataBase.CustomMobsMap.put(id, mob);
				} else {
					DataBase.NormalMobsMap.put(id, mob);
				}
			}
			
			//Loading items
			rows = MySQL.select(""
					+ "Select * \n"
					+ "From " + this.table_items + "\n");
			for(Map<String,String> row : rows) {
				String itemno = row.get("itemno") != null ? row.get("itemno").toUpperCase() : "";
				String itemBase64 = row.get("item") != null ? row.get("item") : "";
				
				if(!itemBase64.isEmpty()) {
					Itemset item = new Itemset(itemBase64);
					DataBase.items.put(itemno, item);
				}
			}
			
			//Loading drop item
			rows = MySQL.select(""
					+ "Select * \n"
					+ "From " + this.table_dropitem + "\n");
			for(Map<String,String> row : rows) {
				String id = row.get("mobname") != null ? row.get("mobname").toUpperCase() : "";
				boolean custom = row.get("custom") != null && row.get("custom").equals("Y");
				String Itemno = row.get("itemno") != null ? row.get("itemno").toUpperCase() : "";
				int Quantity = row.get("quantity") != null ? Integer.parseInt(row.get("quantity")) : 0;
				int Quantity_max = row.get("quantity_max") != null ? Integer.parseInt(row.get("quantity_max")) : 0;
				double Chance = row.get("chance") != null ? Double.parseDouble(row.get("chance")) : 0.00;
//				DataBase.Print("Quantity: " + Quantity);
//				DataBase.Print("Quantity_max: " + Quantity_max);
//				DataBase.Print("Chance: " + Chance);
				if(custom) {
					Mob mob = DataBase.CustomMobsMap.get(id);
					mob.MobItems.put(Itemno, new MobItem(Itemno, Quantity, Quantity_max, Chance));
					DataBase.CustomMobsMap.put(id, mob);
				} else {
					Mob mob = DataBase.NormalMobsMap.get(id);
					mob.MobItems.put(Itemno, new MobItem(Itemno, Quantity, Quantity_max, Chance));
					DataBase.NormalMobsMap.put(id, mob);
				}
			}
		}
	}
	
	/**
	 * 增加怪物
	 * @param Mob
	 * @return 
	 *   當發生錯誤時會回傳錯誤代碼
	 */
	public List<String> MobAdd(Mob Mob) {
		List<String> mode = new ArrayList<String>();
		
		if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
			List<Map<String,String>> data = MySQL.select(""
					+ "Select * \n"
					+ "From " + this.table_mobs + "\n"
				    + "Where mobname = '" + Mob.getName() + "'\n"
				    + "And custom = '" + Mob.getCustom() + "'");
			
			if(data.isEmpty()) {			
				String sql = ""
				+ "Insert Into " + this.table_mobs + "\n"
				+ "(mobname, custom	, icon)\n"
				+ "Values\n"
				+ "('" + Mob.getName() + "',"
				+ " '" + Mob.getCustom() + "'";
				
				if(Mob.hasIcon()) 
					sql = sql + ", "+ " '" + new Itemset(Mob.getIcon()).itemStackToBase64() + "')";
				else 
					sql = sql + ", "+ "null" + ")";
				
				boolean Success = MySQL.executeUpdate(sql);
				if(!Success)
					mode.add("Add_data_error");
			} else {
				mode.add("same_data_error");
			}
		} else {
			
		}
		return mode;
	}
	
	public List<String> MobRemove(Mob Mob) {
		List<String> mode = new ArrayList<String>();
		
		if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
			String sql = ""
					+ "Delete From " + this.table_dropitem + "\n"
					+ "Where 1=1\n"
					+ "And mobname = '" + Mob.getName() + "'\n"
					+ "And custom = '" + Mob.getCustom() + "'";
			
			boolean Success = MySQL.executeUpdate(sql);
			if(!Success)
				mode.add("remove_mob_item_error");
			
			sql = ""
					+ "Delete From " + this.table_mobs + "\n"
					+ "Where 1=1\n"
					+ "And mobname = '" + Mob.getName() + "'\n"
					+ "And custom = '" + Mob.getCustom() + "'";
			
			Success = MySQL.executeUpdate(sql);
			if(!Success)
				mode.add("remove_mob_error");
		} else {
			
		}
		return mode;
	}
	
	/**
	 * 增加怪物
	 * @param Mob
	 * @return 
	 *   當發生錯誤時會回傳錯誤代碼
	 */
	public List<String> MobItemAdd(Mob Mob, MobItem MobItem) {
		List<String> mode = new ArrayList<String>();
		
		if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
			List<Map<String,String>> data = MySQL.select(""
					+ "Select * \n"
					+ "From " + this.table_dropitem + "\n"
				    + "Where mobname = '" + Mob.getName() + "'\n"
				    + "And custom = '" + Mob.getCustom() + "'\n"
					+ "And itemno = '" + MobItem.getItemNo() + "'");
			
			if(data.isEmpty()) {			
				String sql = ""
				+ "Insert Into " + this.table_dropitem + "\n"
				+ "(mobname, custom , itemno)\n"
				+ "Values\n"
				+ "('" + Mob.getName() + "',"
				+ " '" + Mob.getCustom() + "',"
				+ " '" + MobItem.getItemNo() + "')";
				
				boolean Success = MySQL.executeUpdate(sql);
				if(!Success)
					mode.add("Add_data_error");
			} else {
				mode.add("same_data_error");
			}
		} else {
			
		}
		return mode;
	}
	
	/**
	 * 增加物品
	 * @param itemno
	 * @param item
	 * @return
	 *   當發生錯誤時會回傳錯誤代碼
	 */
	public List<String> ItemsAdd(String itemno, ItemStack item) {
		return ItemsAdd(itemno,new Itemset(item));
	}
	
	/**
	 * 增加物品
	 * @param itemno
	 * @param item
	 * @return
	 *   當發生錯誤時會回傳錯誤代碼
	 */
	public List<String> ItemsAdd(String itemno, Itemset item) {
		List<String> mode = new ArrayList<String>();
		
		if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
			List<Map<String,String>> data = MySQL.select(""
					+ "Select * \n"
					+ "From " + this.table_items + "\n"
				    + "Where itemno = '" + itemno + "'");
			
			if(data.isEmpty()) {			
				String sql = ""
				+ "Insert Into " + this.table_items + "\n"
				+ "(itemno, item)\n"
				+ "Values\n"
				+ "('" + itemno + "',"
				+ " '" + item.itemStackToBase64() + "')";
				
				boolean Success = MySQL.executeUpdate(sql);
				if(!Success)
					mode.add("Add_data_error");
			} else {
				mode.add("same_data_error");
			}
		} else {
			
		}
		return mode;
	}
	
}
