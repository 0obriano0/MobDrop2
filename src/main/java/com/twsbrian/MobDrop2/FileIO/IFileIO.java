package com.twsbrian.MobDrop2.FileIO;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public interface IFileIO {
	/**
	 * 取得檔案名稱(含副檔名)
	 * @return 檔案名稱(String)
	 */
	String getFileName();
	
	/**
	 * 取得檔案相對路徑
	 * @return 檔案相對路徑(String)
	 */
	String getPath();
	
	/**
	 * 取得檔案資料(用YML檔格式)
	 * @return 檔案資料(FileConfiguration)
	 */
	FileConfiguration getFileforYML();
	
	/**
	 * 直接以 FileConfiguration 讀取檔案
	 * @param path
	 * @return
	 */
	String getString(String path);
	
	/**
	 * 直接以 FileConfiguration 讀取檔案
	 * @param path
	 * @return
	 */
	List<String> getStringList(String path);
	
	/**
	 * 重新讀取檔案資料
	 * @return 是否讀取成功
	 */
	boolean reloadFile();
	
	/**
	 * 再重新讀取檔案資料後要做的事情
	 * @return 回傳事情是否成功執行
	 */
	boolean reloadcmd();
	
}
