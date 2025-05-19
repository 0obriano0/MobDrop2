package com.twsbrian.MobDrop2.DataBase;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.twsbrian.MobDrop2.AnsiColor;
import com.twsbrian.MobDrop2.MobDrop2;
import com.twsbrian.MobDrop2.FileIO.FileDataBaseInfo;
import com.twsbrian.MobDrop2.FileIO.FileInventory;
import com.twsbrian.MobDrop2.FileIO.FileMessage;


/**
 * 基本資料暫存區
 * @author brian
 *
 */
public class DataBase {
  
  /**
   * 插件目錄 插件附屬檔案的存放路徑
   */
  public static String pluginMainDir = "./plugins/MobDrop2/";
  
  /**
   * 此插件名稱
   */
  public static String pluginName = "MobDrop2";
  
  /**
   * 指令列表
   */
  private static List<String> Commands = null;
  
  /**
   * 掉落物品清單
   */
//  public static Map<String, List<MobItemList>> CustomMobItemMap = new HashMap<String, List<MobItemList>>();
//  public static Map<String, List<MobItemList>> NormalMobItemMap = new HashMap<String, List<MobItemList>>();
  
  public static Map<String, Mob> CustomMobsMap = new HashMap<String, Mob>();
  public static Map<String, Mob> NormalMobsMap = new HashMap<String, Mob>();
  public static Map<String, Itemset> items = new HashMap<String, Itemset>();
  
  /**
   * message 設定
   */
  public static FileMessage fileMessage = new FileMessage();
  
  /*
   * DataBaseInfo 設定
   */
  public static FileDataBaseInfo fileDataBaseInfo = new FileDataBaseInfo();
  
  public static SQL sql = new SQL();
  
  /*
   * Inventory 相關設定檔
   */
  public static FileInventory fileInventory = new FileInventory();
  public static Map<String, MessageSet> NewCustomMobName = new HashMap<String, MessageSet>();
  
  /**
   * 傳給玩家的訊息加上 Message.Title
   * @param player 玩家
   * @param msg 文字訊息
   */
  public static void sendMessage(Player player,String msg){
    player.sendMessage(DataBase.fileMessage.getString("Message.Title") + "§f" + msg);
  }
  
  /**
   * 顯示訊息 在cmd 裡顯示 "[MobDrop2] " + msg
   * @param msg 要顯示的文字
   */
  public static void Print(String msg){
      MobDrop2.plugin.getLogger().info(msg + AnsiColor.RESET);
    //System.out.print("[MobDrop2] " + msg);
  }
  
  /**
   * 顯示訊息 在cmd 裡顯示 "[MobDrop2] " + msg
   * @param msg 要顯示的文字
   */
  public static void Print(List<String> msg){
    for(String str : msg) MobDrop2.plugin.getLogger().info(str + AnsiColor.RESET);
    //System.out.print("[MobDrop2] " + msg);
  }
  
  /**
   * 抓取指令列表(/MobDrop2 列表資料)
   * @param plugin 系統資料
   * @return 列表資料
   */
  public static List<String> getCommands(Plugin plugin){
    if(Commands == null) {
      Commands = new ArrayList<String>();
      URL jarURL = plugin.getClass().getResource("/com/twsbrian/" + pluginName + "/Command");
        URI uri;
      try {
        FileSystem fileSystem = null;
        uri = jarURL.toURI();
        Path myPath;
        if (uri.getScheme().equals("jar")) {
            fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
            myPath = fileSystem.getPath("/com/twsbrian/"+ pluginName +"/Command");
        } else {
            myPath = Paths.get(uri);
        }
        if (fileSystem == null) {
          return Commands;
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
  
  /**
   * 取得是否顯示debug 專用訊息
   * @return
   */
  public static boolean getDebug() {
    return (MobDrop2.plugin.getConfig().contains("Debug") && MobDrop2.plugin.getConfig().getBoolean("Debug"));
  }
  
  /**
   * 物品掉落顏色設定
   */
  
  protected static NavigableMap<Double, String> ItemDropColorByChance = new TreeMap<Double, String>();
  
  public static String getDropColorByChance(Double Chance) {
        // To do a lookup for some value in 'Chance'
        if (Chance < 0 || Chance >= 100) {
            // out of range
          return MobDrop2.plugin.getConfig().getString("Glow.Defualt");
        } else {
          return DataBase.ItemDropColorByChance.floorEntry(Chance).getValue();
        }
  }
  
  public static void settingItemDropColorByChance() {
    if(MobDrop2.plugin.getConfig().contains("Glow.Chance")) {
      Map<Integer, String> sortmap = new TreeMap<Integer, String>();
      for (String name : MobDrop2.plugin.getConfig().getConfigurationSection("Glow.Chance").getKeys(false)) {
          sortmap.put(Integer.valueOf(name), MobDrop2.plugin.getConfig().getString("Glow.Chance." + name));
      }
      
      DataBase.ItemDropColorByChance.clear();
      Integer lastint = 0;
      for(Entry<Integer, String> entry : sortmap.entrySet()) {
        DataBase.ItemDropColorByChance.put(lastint.doubleValue(), entry.getValue());
        lastint = entry.getKey();
      }
      
      DataBase.ItemDropColorByChance.put(lastint.doubleValue(),MobDrop2.plugin.getConfig().getString("Glow.Defualt"));
    }
  }
}
