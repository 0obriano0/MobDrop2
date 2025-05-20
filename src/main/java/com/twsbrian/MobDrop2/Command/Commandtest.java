package com.twsbrian.MobDrop2.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.Itemset;
import com.twsbrian.MobDrop2.DataBase.Callback;

public class Commandtest extends mainCommandSystem{
  public Commandtest() {
    super(  "test",
        "/mobdrop test 取得指令說明",
        new ArrayList<String>(Arrays.asList("mobdrop.admin.test")));
  }
  
  @Override
  public void run(CommandSender sender, String commandLabel, Command command, String[] args){
    // DataBase.Print("commandLabel =  " + commandLabel);
    //   if(args.length >= 1) {
    //     try {
    //       EntityType.valueOf(args[0].toUpperCase());
    //       DataBase.Print(args[0] + " 存在 ");
    //     }catch(IllegalArgumentException e) {
    //       DataBase.Print(args[0] + " 不存在 ");
    //     }
    //   }
    //   DataBase.Print("Colors:");
    // for(ChatColor color: ChatColor.values()) {
    //   DataBase.Print(color.name());
    // }
    // for (String name : MobDrop2.plugin.getConfig().getConfigurationSection("Glow.Chance").getKeys(false)) {
    //   DataBase.Print(name);
    // }
    // if(args.length >= 1) {
    //   DataBase.Print(DataBase.getDropColorByChance(Double.valueOf(args[0])));
    // }

    // 同步測試
    // if (args.length >= 2) {
    //   try {
    //     int page = Integer.parseInt(args[0]);
    //     int size = Integer.parseInt(args[1]);
    //     Map<String,Itemset> items = DataBase.sql.ItemsGet(page, size);
    //     DataBase.Print("ItemNo 列表 (第 " + page + " 頁, 每頁 " + size + " 筆):");
    //     for (Map.Entry<String, Itemset> entry : items.entrySet()) {
    //       String itemNo = entry.getKey();
    //       Itemset item = entry.getValue();
    //       DataBase.Print("ItemNo: " + itemNo + ", Item: " + item);
    //     }
    //   } catch (NumberFormatException e) {
    //     DataBase.Print("請輸入正確的頁數與每頁大小 (數字)。");
    //   }
    // }

    // 異步執行測試
    DataBase.Print("查詢中...");
    if (args.length >= 2) {
      int page = Integer.parseInt(args[0]);
      int size = Integer.parseInt(args[1]);
      DataBase.sql.ItemsGetAsync(page, size, new Callback<Map<String,Itemset>>() {
        @Override
        public void onSuccess(Map<String, Itemset> result) {
          DataBase.Print("ItemNo 列表 (第 " + page + " 頁, 每頁 " + size + " 筆):");
          for (Map.Entry<String, Itemset> entry : result.entrySet()) {
            String itemNo = entry.getKey();
            Itemset item = entry.getValue();
            DataBase.Print("ItemNo: " + itemNo + ", Item: " + item);
          }
        }

        @Override
        public void onFailure(Exception e) {
          DataBase.Print("查詢失敗: " + e.getMessage());
        }
      });
    }
    
    DataBase.Print("test");
  }
}
  
