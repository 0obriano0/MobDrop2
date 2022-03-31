package com.twsbrian.MobDrop2.FileIO;

import com.twsbrian.MobDrop2.DataBase.DataBase;

public class FileDataBaseInfo extends FileIO{
	public FileDataBaseInfo() {
		super("dataBaseInfo.yml");
	}
	
	public class storage {
		public String method = "";
	}
	
	public class mysql {
		public String username = "";
		public String password = "";
		public String hostname = "";
		public String database = "";
		public String Prefix = "";
		public boolean autoReconnect = false;
		public boolean useSSL = false;
		
		protected boolean isEmpty() {
			if(username.isEmpty() || password.isEmpty() || hostname.isEmpty() || database.isEmpty())
				return true;
			return false;
		}
		
		public String print() {
			return "username = " + username + "\n" +
				   "password = " + password + "\n" +
				   "hostname = " + hostname + "\n" + 
				   "database = " + database + "\n" + 
				   "Prefix = " + Prefix + "\n" + 
				   "autoReconnect = " + autoReconnect + "\n" + 
				   "useSSL = " + useSSL + "\n";
		}
	}
	
	public storage storage = new storage();
	public mysql mysql = new mysql();
	
	@Override
	public boolean reloadcmd() {
		storage newstorage = new storage();
		mysql newmysql = new mysql();
		if(data.contains("storage.method") && !(data.getString("storage.method") == null || data.getString("storage.method").isEmpty())) {
			newstorage.method = data.getString("storage.method").toLowerCase();
			if(newstorage.method.equals("mysql") && this.data.contains("mysql")) {
				
				if(data.contains("mysql.username") && !(data.getString("mysql.username") == null || data.getString("mysql.username").isEmpty())) 
					newmysql.username = data.getString("mysql.username");
				
				if(data.contains("mysql.password") && !(data.getString("mysql.password") == null || data.getString("mysql.password").isEmpty())) 
					newmysql.password = data.getString("mysql.password");
				
				if(data.contains("mysql.hostname") && !(data.getString("mysql.hostname") == null || data.getString("mysql.hostname").isEmpty())) 
					newmysql.hostname = data.getString("mysql.hostname");
				
				if(data.contains("mysql.database") && !(data.getString("mysql.database") == null || data.getString("mysql.database").isEmpty())) 
					newmysql.database = data.getString("mysql.database");
				
				if(data.contains("mysql.Prefix") && !(data.getString("mysql.Prefix") == null || data.getString("mysql.Prefix").isEmpty())) 
					newmysql.Prefix = data.getString("mysql.Prefix");
				
				if(data.contains("mysql.autoReconnect")) 
					newmysql.autoReconnect = data.getBoolean("mysql.autoReconnect");
				
				if(data.contains("mysql.useSSL")) 
					newmysql.useSSL = data.getBoolean("mysql.useSSL");
				
			} else if (newstorage.method.equals("sqlite")) {
				
			} else {
				return false;
			}
			
			if(!newstorage.method.isEmpty()) {
				storage = newstorage;
			} else {
				DataBase.Print("storage loading error, use sqlite");
				storage.method = "sqlite";
				return false;
			}
			
			if(!newmysql.isEmpty()) {
				mysql = newmysql;
			} else if (storage.method.equals("mysql")) {
				DataBase.Print("mysql config loading error, use sqlite");
				storage.method = "sqlite";
				return false;
			}
		} else {
			return false;
		}
		
	    DataBase.sql.reload();
		return true;
	}
}
