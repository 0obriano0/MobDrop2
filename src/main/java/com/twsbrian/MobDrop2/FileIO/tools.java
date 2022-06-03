package com.twsbrian.MobDrop2.FileIO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.bukkit.plugin.Plugin;

import com.twsbrian.MobDrop2.AnsiColor;
import com.twsbrian.MobDrop2.MobDrop2;

/**
 * 檔案操作的額外工具
 * @author Brian
 *
 */
public class tools {
	/**
	 * 基本文字輸出 顯示讀取成功及失敗
	 * @param plugin 插件資料
	 * @param title 插件名稱
	 * @param Name 檔案名稱
	 * @param totle 總共資料
	 * @param Success 成功資料
	 * @param Fail 失敗資料
	 */
	public static void setprint(Plugin plugin,String title, String Name, int totle, int Success, int Fail) {
		plugin.getLogger().info(AnsiColor.CYAN + "[" + title + "] " + AnsiColor.GREEN + Name + " load " + AnsiColor.PURPLE + "Totle: " + AnsiColor.WHITE + totle + AnsiColor.YELLOW + "  Success: " + AnsiColor.WHITE + Success + AnsiColor.RED + "  Fail:  " + AnsiColor.WHITE + Fail + AnsiColor.RESET);
	}
	/**
	 * 檔案寫入
	 * @param URL 檔案存放路徑
	 * @param lines 資料(type ListString)
	 */
	public static void writeFile(String URL,List<String> lines){
		Path file = Paths.get(URL);
		lines.add(0, "\uFEFF");
		try {
			Files.write(file, lines, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	protected static String getLang() {
		String Name = MobDrop2.plugin.getConfig().getString("lang");
		if(Name.toLowerCase().equals("zh_tw")) Name = "zh_TW";
		return Name;
	}
}
