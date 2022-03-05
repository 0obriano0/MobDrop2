package com.brian.MobDrop2.DataBase;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.brian.MobDrop2.DataBase.MySQL.MySQLManager;

public class SQL {
	
	private MySQLManager MySQL;
	private String table_dropitem;
	private String table_mobs;
	
	public void reload() {
		if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
			this.table_dropitem = (DataBase.fileDataBaseInfo.mysql.Prefix.isEmpty() ? "" : DataBase.fileDataBaseInfo.mysql.Prefix) + "dropitem";
			this.table_mobs = (DataBase.fileDataBaseInfo.mysql.Prefix.isEmpty() ? "" : DataBase.fileDataBaseInfo.mysql.Prefix) + "mobs";
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
					+ "  `itemname` varchar(20) NOT NULL,\n"
					+ "  `quantity` varchar(4) DEFAULT NULL,\n"
					+ "  `quantity_max` varchar(4) DEFAULT NULL,\n"
					+ "  `chance` decimal(5,3) DEFAULT NULL,\n"
					+ "  `world` longtext DEFAULT NULL,\n"
					+ "  `item` longtext DEFAULT NULL,\n"
					+ "  PRIMARY KEY (`mobname`,`custom`,`itemname`)\n"
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
	}
	
	private void MySQL_ReLoad() {
		DataBase.CustomMobsMap.clear();
		DataBase.NormalMobsMap.clear();
		if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
			List<Map<String,String>> rows = MySQL.select(""
					+ "Select * \n"
					+ "From " + this.table_mobs + "\n");
			
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
					+ "And itemname = '" + Mob.getCustom() + "'");
			
			if(data.isEmpty()) {			
				String sql = ""
				+ "Insert Into " + this.table_dropitem + "\n"
				+ "(mobname, custom	, itemname)\n"
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
	
}
