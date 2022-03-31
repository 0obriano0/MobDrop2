package com.twsbrian.MobDrop2.Command;

import com.twsbrian.MobDrop2.MobDrop2;
import com.twsbrian.MobDrop2.DataBase.DataBase;

public class ToolCommandSystem {
	
	/**
	 *  取得指令的類別(class)
	 * @param command 指令名稱
     * @return 該class資料
     */
    @SuppressWarnings("deprecation")
	public static ImainCommandSystem getCommandClass(String command) {
    	ImainCommandSystem cmd = null;
        try {
            cmd = (ImainCommandSystem) MobDrop2.class.getClassLoader().loadClass("com.brian." + DataBase.pluginName + ".Command" + ".Command" + command).newInstance();
        }catch(InstantiationException ex) {
        	ex.printStackTrace();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
		return cmd;
    }
    
    /**
	 *  取得指令的類別(class)
	 * @param command 指令名稱
     * @param classLoader 抓取此插件讀取classLoader指令
     * @param commandPath 要抓取插件的檔案位置
     * @return 該class資料
     */
    @SuppressWarnings("deprecation")
    public static ImainCommandSystem getCommandClass(String command,final ClassLoader classLoader, final String commandPath) {
    	ImainCommandSystem cmd = null;
        try {
            cmd = (ImainCommandSystem) classLoader.loadClass(commandPath + ".Command" + command).newInstance();
        }catch(InstantiationException ex) {
        	ex.printStackTrace();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
		return cmd;
    }
    
    /**
	 * 取得是否有此指令
	 * @param command 指令名稱
     * @param classLoader 抓取此插件讀取classLoader指令
     * @param commandPath 要抓取插件的檔案位置
     * @return true or false
     */
    @SuppressWarnings("deprecation")
    public static boolean hasCommandClass(String command,final ClassLoader classLoader, final String commandPath) {
        try {
            classLoader.loadClass(commandPath + ".Command" + command).newInstance();
        }catch(InstantiationException ex) {
        	ex.printStackTrace();
        	return false;
        }catch (ClassNotFoundException cnfe) {
        	return false;
		}catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
		return true;
    }
}
