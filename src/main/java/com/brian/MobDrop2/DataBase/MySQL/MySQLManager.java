package com.brian.MobDrop2.DataBase.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.brian.MobDrop2.DataBase.DataBase;

public class MySQLManager {
	// JDBC driver name and database URL
	// MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
	protected final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	protected transient String DB_URL;
	
	//  Database credentials
	protected transient String USER;
	protected transient String PASS;
	protected transient String db;
   
	protected Connection conn = null;
	
	public boolean autoReconnect = false;
	
	protected boolean showLog = true;
	
	public Connection getconn() {
		return this.conn;
	}	
	
	/**
	 * MySQL 基本資料設定
	 * 含設定 字元編碼
	 * @param USER 帳號
	 * @param PASS 密碼
	 * @param DB_URL 連結網址
	 * @param db 數據庫
	 * @param useSSL 要不要使用SSL
	 * @param autoReconnect 是否需要 自動連接
	 * @param characterEncoding 字元編碼
	 */
	public MySQLManager(String USER,String PASS,String DB_URL,String db,boolean useSSL,boolean autoReconnect,String characterEncoding) {
		this.USER = USER;
		this.PASS = PASS;
		this.autoReconnect = autoReconnect;
		
		String data = "&useSSL=" + useSSL;
		
		if(autoReconnect) data = data + "&autoReconnect=true";
		
		if(!characterEncoding.equals("")) 
			data = data + "&useUnicode=true&characterEncoding=" + characterEncoding;
		
		if(data.equals("")) {
			this.DB_URL = DB_URL;
		} else {
			this.DB_URL = DB_URL + "?" + data.substring(1);
		}

		this.db = db;
	}
	
	/**
	 * MySQL 基本資料設定
	 * 含設定 字元編碼
	 * @param USER 帳號
	 * @param PASS 密碼
	 * @param DB_URL 連結網址
	 * @param db 數據庫
	 * @param useSSL 要不要使用SSL
	 * @param autoReconnect 是否需要 自動連接
	 */
	public MySQLManager(String USER,String PASS,String DB_URL,String db,boolean useSSL,boolean autoReconnect) {
		this(USER,PASS,DB_URL,db,useSSL,autoReconnect,"");
	}
	
    /**
     * 開啟與MySQL連結
     * @return 有沒有成功
     */
	public boolean open() {
		try{
			//Register JDBC driver
			Class.forName(JDBC_DRIVER);
			
			//Open a connection
			print("Connecting to database...");
			this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
			
			if(!this.SelectDataBase()) CreateDataBase(this.db);
		}catch(SQLException se){
			//Handle errors for JDBC
			if(se.getClass().getSimpleName().equals("CommunicationsException")) {
				conn = null;
				print("Connecting fail");
			}else
				se.printStackTrace();
			return false;
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
    * 關閉與MySQL連結
    * @return 有沒有成功
    */
	public boolean close(){
		try{
			if(conn!=null)
				conn.close();
			conn = null;
			return true;
		}catch(SQLException se){
			se.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 選擇數據庫
	 * @return 是否成
	 */
	public boolean SelectDataBase() {
		Statement stmt = null;
		if(conn==null) open();
	   
		
		boolean success = false;
		try{
			//Execute a query
			DataBase.Print("Select database[ " + db + " ]...");
			stmt = conn.createStatement();
			String sql = "use " + db;
			print("SelectDataBase = " + stmt.executeUpdate(sql));
			print("Database[ " + db + " ]Select successfully...");
			success = true;
		}catch(SQLSyntaxErrorException mse) {
			DataBase.Print("can not get DataBase [ " + db + " ]");
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null) stmt.close();
				if(!this.autoReconnect) close();
			}catch(SQLException se2){
				se2.printStackTrace();
			}
		}
		
		return success;
	}
	
	/**
     * 創建一個數據庫
     * @param DataBaseName 數據庫名稱
     */
	public void CreateDataBase(String DataBaseName) {
		Statement stmt = null;
		if(conn==null) open();
	   
		try{
			//Execute a query
			print("Creating database...");
			stmt = conn.createStatement();
			String sql = "CREATE DATABASE " + DataBaseName;
			stmt.executeUpdate(sql);
			print("Database created successfully...");
			this.SelectDataBase();
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null) stmt.close();
				if(!this.autoReconnect) close();
			}catch(SQLException se2){
				se2.printStackTrace();
			}
		}
	}
	
	/**
	 * 傳送查詢相關指令
	 * db 使用內部設定好的
	 * @param command 指令
	 * @return 回傳查詢資料(null 代表取得失敗)
	 */
	public ResultSet executeQuery(String command) {
		return executeQuery(command, "");
	}
	
	/**
	 * 傳送查詢相關指令
	 * 預設 db 使用內部設定好的
	 * @param command 指令
	 * @param db 選擇資料庫
	 * @return 回傳查詢資料(null 代表取得失敗)
	 */
	public ResultSet executeQuery(String command, String db) {
		
		Statement stmt = null;
		ResultSet rs = null;
		if(conn==null) open();
		
		boolean success = false;
		try{
			//Execute a query
			print(DataBase.fileMessage.getString("SQL.Run_cmd_color"),DataBase.fileMessage.getString("SQL.Run_cmd") + "\n" +  command);
			stmt = conn.createStatement();
			
			String sql = "use " + (db.isEmpty() ? this.db : db);
			stmt.executeUpdate(sql);
			
			rs = stmt.executeQuery(command);
			print(DataBase.fileMessage.getString("SQL.Run_cmd_Success"));
			success = true;
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null) stmt.close();
				if(!this.autoReconnect) close();
			}catch(SQLException se2){
				se2.printStackTrace();
			}
		}
		return success ? rs : null;
	}
	
	/**
	 * 發送指令給 MySQL
	 * db 使用內部設定好的
	 * @param command 指令
	 * @return 回傳指令是否成功送出
	 */
	public boolean executeUpdate(String command) {
		return executeUpdate(command, "");
	}
	
	/**
	 * 發送指令給 MySQL
	 * 預設 db 使用內部設定好的
	 * @param command 指令
	 * @param db 選擇資料庫
	 * @return 回傳指令是否成功送出
	 */
	public boolean executeUpdate(String command, String db) {
		
		Statement stmt = null;
		if(conn==null) open();
	   
		boolean success = false;
		try{
			//Execute a query
			print(DataBase.fileMessage.getString("SQL.Run_cmd_color"),DataBase.fileMessage.getString("SQL.Run_cmd") + "\n" + command);
			stmt = conn.createStatement();
			
			String sql = "use " + (db.isEmpty() ? this.db : db);
			stmt.executeUpdate(sql);
			
			stmt.executeUpdate(command);
			print(DataBase.fileMessage.getString("SQL.Run_cmd_Success"));
			success = true;
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null) stmt.close();
				if(!this.autoReconnect) close();
			}catch(SQLException se2){
				se2.printStackTrace();
			}
		}
		return success;
	}
	
	/**
	 * 傳送查詢相關指令 並轉換成 list map 模式
	 * db 使用內部設定好的
	 * @param command 指令
	 * @return 回傳查詢資料
	 */
	public List<Map<String,String>> select(String command) {
		return select(command, "");
	}
	
	/**
	 * 傳送查詢相關指令 並轉換成 list map 模式
	 * 預設 db 使用內部設定好的
	 * 如果要更改請使用 setdb("database")
	 * @param command 指令
	 * @param db 選擇資料庫
	 * @return 回傳查詢資料
	 */
	public List<Map<String,String>> select(String command, String db) {
		Statement stmt = null;
		ResultSet rs = null;
		if(conn==null) open();
		
		List<Map<String,String>> data_list = new ArrayList<Map<String,String>>();
		
		try{
			//Execute a query
			print(DataBase.fileMessage.getString("SQL.Run_cmd_color"),DataBase.fileMessage.getString("SQL.Run_cmd") + "\n" + command);
			stmt = conn.createStatement();
			
			String sql = "use " + (db.isEmpty() ? this.db : db);
			stmt.executeUpdate(sql);
			
			rs = stmt.executeQuery(command);
			
			ResultSetMetaData metadata = rs.getMetaData();
			int columnCount = metadata.getColumnCount();
			
			while(rs.next()){
				Map<String,String> data_map = new HashMap<String,String>();
				for(int loopnum1 = 1; loopnum1 <= columnCount;loopnum1++) {
					String ColumnName = metadata.getColumnName(loopnum1);
					data_map.put(ColumnName,rs.getString(ColumnName));
				}
				data_list.add(data_map);
		    }
			rs.close();
			
			print(DataBase.fileMessage.getString("SQL.Run_cmd_Success"));
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null) stmt.close();
				if(!this.autoReconnect) close();
			}catch(SQLException se2){
				se2.printStackTrace();
			}
		}
		return data_list;
	}
	
	/**
	 * print顯示控制器
	 * @param message 訊息
	 */
	protected void print(String message) {
		if(showLog) DataBase.Print(message);
	}
	
	/**
	 * print顯示控制器
	 * @param message 訊息
	 */
	protected void print(String color,String message) {
		if(showLog) {
			List<String> lstr = new ArrayList<String>();
			for(String str : message.split("\n"))
				if(!str.isEmpty()) lstr.add(color + str);
			DataBase.Print(lstr);
		}
	}
}
