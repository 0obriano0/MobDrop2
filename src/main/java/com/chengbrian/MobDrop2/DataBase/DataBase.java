package com.chengbrian.MobDrop2.DataBase;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.plugin.Plugin;

import com.chengbrian.MobDrop2.MobDrop2;
import com.chengbrian.MobDrop2.FileIO.FileMessage;


/**
 * 基本資料暫存區
 * @author brian
 *
 */
public class DataBase {
	
	/**
	 * 插件目錄 插件附屬檔案的存放路徑
	 */
	public static String pluginMainDir = "./plugins/NoShove/";
	
	/**
	 * 此插件名稱
	 */
	public static String pluginName = "MobDrop2";
	
	/**
	 * 指令列表
	 */
	private static List<String> Commands = null;
	
	/**
	 * message 設定
	 */
	public static FileMessage fileMessage = new FileMessage();
	
	/**
	 * 顯示訊息 在cmd 裡顯示 "[MobDrop2] " + msg
	 * @param msg 要顯示的文字
	 */
	public static void Print(String msg){
		MobDrop2.plugin.getLogger().info(msg);
		//System.out.print("[MobDrop2] " + msg);
	}
	
	/**
	 * 防放系統物件宣告
	 */
	public static List<Itemset> NoShoveItem = new ArrayList<Itemset>();
	
	/**
	 * Minecraft 16 種顏色
	 */
	public static List<String> Color = new ArrayList<String>(Arrays.asList(   "white",
																		      "orange",
																		      "magenta",
																		      "light_blue",
																		      "yellow",
																		      "lime",
																		      "pink",
																		      "gray",
																		      "light_gray",
																		      "cyan",
																		      "puple",
																		      "blue",
																		      "brown",
																		      "green",
																		      "red",
																		      "black"));
	
	/**
	 * 抓取指令列表(/MobDrop2 列表資料)
	 * @param plugin 系統資料
	 * @return 列表資料
	 */
	public static List<String> getCommands(Plugin plugin){
		if(Commands == null) {
			Commands = new ArrayList<String>();
			URL jarURL = plugin.getClass().getResource("/com/chengbrian/" + pluginName + "/Command");
	    	URI uri;
			try {
				FileSystem fileSystem = null;
				uri = jarURL.toURI();
				Path myPath;
		        if (uri.getScheme().equals("jar")) {
		            fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
		            myPath = fileSystem.getPath("/com/chengbrian/"+ pluginName +"/Command");
		            
		        } else {
		            myPath = Paths.get(uri);
		        }
		        for (Iterator<Path> it = Files.walk(myPath, 1).iterator(); it.hasNext();){
		        	String[] path = it.next().toString().split("/");
		        	
		        	String file = path[path.length - 1];
		        	if(file.matches("(.*)class$")) {
		        		file = file.split("\\.")[0];
		        		if(file.matches("^Command.*")) {
			        		String filename = file.split("Command")[1];
			        		Commands.add(filename);
			        	}
		        	}
		            //System.out.println(it.next());
		        	Collections.sort(Commands);
		        }
		        fileSystem.close();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Commands;
        
    }
}
