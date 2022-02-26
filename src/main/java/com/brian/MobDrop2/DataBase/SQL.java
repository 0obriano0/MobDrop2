package com.brian.MobDrop2.DataBase;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.brian.MobDrop2.DataBase.MySQL.MySQLManager;

public class SQL {
	
	private MySQLManager MySQL;
	
	public void reload() {
		if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
			MySQL_checkdb();
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
		String dropitem = (DataBase.fileDataBaseInfo.mysql.Prefix.isEmpty() ? "" : DataBase.fileDataBaseInfo.mysql.Prefix) + "dropitem";
		List<Map<String,String>> data = MySQL.select(
				  "SELECT table_name\n"
				+ "FROM information_schema.tables\n"
				+ "WHERE table_schema = '" + DataBase.fileDataBaseInfo.mysql.database + "'\n"
				+ "And table_name = '" + dropitem + "'");
		
		if(data.isEmpty()) {
			MySQL.executeUpdate(
					  "CREATE TABLE `" + dropitem + "` (\n"
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
		String mobs = (DataBase.fileDataBaseInfo.mysql.Prefix.isEmpty() ? "" : DataBase.fileDataBaseInfo.mysql.Prefix) + "mobs";
		data = MySQL.select(
				  "SELECT table_name\n"
				+ "FROM information_schema.tables\n"
				+ "WHERE table_schema = '" + DataBase.fileDataBaseInfo.mysql.database + "'\n"
				+ "And table_name = '" + mobs + "'");
		
		if(data.isEmpty()) {
			MySQL.executeUpdate(
					  "CREATE TABLE `" + mobs + "` (\n"
					+ "  `mobname` varchar(20) NOT NULL,\n"
				    + "  `custom` varchar(1) NOT NULL,\n"
				    + "  `icon` longtext,\n"
					+ "  PRIMARY KEY (`mobname`,`custom`)\n"
					+ ")");
		}
	}
	
	private void MySQL_Load() {
		
	}
	
}
