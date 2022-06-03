package com.twsbrian.MobDrop2.FileIO;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.twsbrian.MobDrop2.AnsiColor;
import com.twsbrian.MobDrop2.DataBase.DataBase;

public class FileMessage extends FileIO{
	public FileMessage() {
		super("message", tools.getLang() + "/Base.yml");
	}
	
	public Map<String,String> IDMobtoMessage = new HashMap<String,String>();
	public Map<String,String> MessagetoIDMob = new HashMap<String,String>();
	public Map<Integer,String> TimerLeft = new HashMap<Integer,String>();
	
	@Override
	public boolean reloadcmd() {
		this.setFileName(tools.getLang() + "/Base.yml");
		int MobsSuccess = 0;
		int MobsFail = 0;
		TimerLeft.clear();
		if(this.data.contains("Timer.TimeLeft")) {
			Pattern p = Pattern.compile("%%[0-9]*%%");//java.util.regex.Pattern;
		    
			for(String str : data.getStringList("Timer.TimeLeft")) {
				Matcher matcher = p.matcher(str);//java.util.regex.Pattern;
				if(matcher.find()) {
					String getstr = matcher.group(0);
					TimerLeft.put(Integer.parseInt(getstr.replace("%", "")), str.replace("&","§").replace(getstr,"%time%"));
				}
			}
		}
		
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
