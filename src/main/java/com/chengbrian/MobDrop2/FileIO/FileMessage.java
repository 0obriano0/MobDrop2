package com.chengbrian.MobDrop2.FileIO;

import java.util.HashMap;
import java.util.Map;

import com.chengbrian.MobDrop2.AnsiColor;
import com.chengbrian.MobDrop2.MobDrop2;
import com.chengbrian.MobDrop2.DataBase.DataBase;
import com.chengbrian.MobDrop2.FileIO.FileIO;

public class FileMessage extends FileIO{
	public FileMessage() {
		super("message", MobDrop2.plugin.getConfig().getString("lang") + ".yml");
	}
	
	public Map<String,String> IDMobtoMessage = new HashMap<String,String>();
	public Map<String,String> MessagetoIDMob = new HashMap<String,String>();
	
	@Override
	public boolean reloadcmd() {
		int MobsSuccess = 0;
		int MobsFail = 0;
		if(this.data.contains("Mobs")) {
			IDMobtoMessage.clear();
			MessagetoIDMob.clear();
			for (String MobsId : this.data.getConfigurationSection("Mobs").getKeys(false)) {
		    	String value = null;
		    	if(data.contains("Mobs." + MobsId)) {
		    		value = data.getString("Mobs." + MobsId).replace("&","§"); 
		    		//DataBase.Print("Load " + MobsId + " data: " + value);
		    		IDMobtoMessage.put(MobsId, value);
		    		MessagetoIDMob.put(value, MobsId);
		    		MobsSuccess++;
		    	}else{
		    		errorMessage("Mobs",MobsId);
		    		MobsFail++;
		    	}
		    }
			DataBase.Print(AnsiColor.YELLOW + "[Loadlanguage] Mods translation success: " + AnsiColor.GREEN + MobsSuccess + AnsiColor.YELLOW +  " and Fail: " + AnsiColor.RED + MobsFail);
		}else
			return false;
		return true;
	}
	
	/**
	 * 錯誤訊息顯示
	 * @param title 標題
	 * @param name 名稱
	 */
	public void errorMessage(String title,String name) {
		DataBase.Print(AnsiColor.RED + "[Loadlanguage] " + title + " -> " + name + " data load error，use default..." + AnsiColor.RESET);
	}
	
	/**
	 * 取得生物名稱(翻譯的名稱)
	 * @param entityId
	 * @return
	 */
	public String GetEntityName(String entityId){
		if(IDMobtoMessage.containsKey(entityId))
			return IDMobtoMessage.get(entityId);
		else
			return entityId;
	}
	
	/**
	 * 取得生物名稱(系統名稱)
	 * @param entityId
	 * @return
	 */
	public String getEntityNameGameCode(String entityId){
		if(MessagetoIDMob.containsKey(entityId))
			return MessagetoIDMob.get(entityId);
		else
			return entityId;
	}
	
}
