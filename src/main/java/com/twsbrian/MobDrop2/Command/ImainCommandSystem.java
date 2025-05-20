package com.twsbrian.MobDrop2.Command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * safemode 基本指令系統宣告
 * @author brian
 *
 */
public interface ImainCommandSystem {
  /**
   *  負責回傳插件名稱
   * @return 插件名稱
   */
  String getName();
  
  /**
   * 拿取help指令說明
   * @return 指令說明
   */
  String getHelp();
  
  /**
   * 拿取權限列表
   * @return 權限列表
   */
  List<String> getPermissions();
  
  /**
   * 檢查是否有權限
   * @param sender 輸入者(黑盒子)
   * @return 是否有權限
   */
  boolean hasPermission(CommandSender sender);
  
  /**
   * 檢查是否有權限
   * @param player 輸入者(黑盒子 or 玩家)
   * @return 是否有權限
   */
  boolean hasPermission(Player player);
  
  /**
   * code in run(...) function
   * 是否要執行預設程式
   */
  void rundefault();
  
  /**
   *  處理從cmd傳來的指令用
   * @param sender 輸入者(玩家)
   * @param commandLabel 指令Title
   * @param command 使用指令的使用者相關資訊
   * @param args 指令後半部分
   * @param classLoader
   * @param commandPath 當前指令路徑
   * @throws Exception 執行指令錯誤時回報錯誤訊息
   */
    void run(CommandSender sender, String commandLabel, Command command, String[] args, final ClassLoader classLoader, final String commandPath) throws Exception;
    
    /**
   *  處理從cmd傳來的指令用
   * @param sender 輸入者(玩家)
   * @param commandLabel 指令Title
   * @param command 使用指令的使用者相關資訊
   * @param args 指令後半部分
   * @throws Exception 執行指令錯誤時回報錯誤訊息
   */
    void run(CommandSender sender, String commandLabel, Command command, String[] args) throws Exception;
    
    /**
   *  處理從player傳來的指令用
   * @param player 輸入者(玩家)
   * @param commandLabel 指令Title
   * @param command 使用指令的使用者相關資訊
   * @param args 指令後半部分
   * @param classLoader
   * @param commandPath 當前指令路徑
   * @throws Exception 執行指令錯誤時回報錯誤訊息
   */
    void run(Player player, String commandLabel, Command command, String[] args, final ClassLoader classLoader, final String commandPath) throws Exception;
    
    /**
   *  處理從player傳來的指令用
   * @param player 輸入者(玩家)
   * @param commandLabel 指令Title
   * @param command 使用指令的使用者相關資訊
   * @param args 指令後半部分
   * @throws Exception 執行指令錯誤時回報錯誤訊息
   */
    void run(Player player, String commandLabel, Command command, String[] args) throws Exception;

    /**
   *  回傳cmd tab相關資料
   * @param sender 輸入者(黑盒子)
   * @param commandLabel 指令Title
   * @param command 使用指令的使用者相關資訊
   * @param args 指令後半部分
   * @param classLoader
   * @param commandPath 當前指令路徑
   * @return 取得指令列表
   */
    List<String> tabComplete(CommandSender sender, String commandLabel, Command command, String[] args, final ClassLoader classLoader, final String commandPath);

    /**
   *  回傳player tab相關資料
   * @param player 輸入者(黑盒子)
   * @param commandLabel 指令Title
   * @param command 使用指令的使用者相關資訊
   * @param args 指令後半部分
   * @param classLoader
   * @param commandPath 當前指令路徑
   * @return 取得指令列表
   */
    List<String> tabComplete(Player player, String commandLabel, Command command, String[] args, final ClassLoader classLoader, final String commandPath);
    
    /**
   * 抓取指令列表
   * @return 列表資料
   */
  List<String> getsubCommands();
}
