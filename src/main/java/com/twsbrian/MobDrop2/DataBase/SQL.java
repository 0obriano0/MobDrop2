package com.twsbrian.MobDrop2.DataBase;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import com.twsbrian.MobDrop2.MobDrop2;
import com.twsbrian.MobDrop2.DataBase.MySQL.MySQLManager;

/**
 * 作用可以快速切換不同sql 的 實作檔
 * @author Brian
 *
 */
public class SQL {
  
  private MySQLManager MySQL;
  private String table_dropitem;
  private String table_mobs;
  private String table_items;
  
  public void reload() {
    if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
      this.table_dropitem = (DataBase.fileDataBaseInfo.mysql.Prefix.isEmpty() ? "" : DataBase.fileDataBaseInfo.mysql.Prefix) + "dropitem";
      this.table_mobs = (DataBase.fileDataBaseInfo.mysql.Prefix.isEmpty() ? "" : DataBase.fileDataBaseInfo.mysql.Prefix) + "mobs";
      this.table_items = (DataBase.fileDataBaseInfo.mysql.Prefix.isEmpty() ? "" : DataBase.fileDataBaseInfo.mysql.Prefix) + "items";
      MySql_init();
      if(MySQL.open()) {
        MySQL_checkdb();
        MySQL_ReLoad();
      }
    }
  }
  
  public ResultSet executeQuery(String command) {
    if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) 
      return MySQL.executeQuery(command, "");
    else 
      return null;
  }
  
  public ResultSet executeQuery(String command, String sql) {
    if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) 
      return MySQL.executeQuery(command, sql);
    else 
      return null;
  }
  
  public boolean executeUpdate(String command) {
    if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) 
      return MySQL.executeUpdate(command, "");
    else 
      return false;
  }
  
  public boolean executeUpdate(String command, String sql) {
    if(DataBase.fileDataBaseInfo.storage.method.equals("mysql"))
      return MySQL.executeUpdate(command, sql);
    else
      return false;
  }

  /**
   * Asynchronous update method.
   * 於 2025/05/20 新增
   * @param command 指令
   * @param callback 回調函數
   */
  public void executeUpdateAsync(String command, Callback<Boolean> callback) {
    if (DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
        MySQL.executeUpdateAsync(command, callback);
    } else {
        callback.onFailure(new UnsupportedOperationException("Only MySQL supported for async update"));
    }
}
  
  public List<Map<String,String>> select(String command) {
    if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) 
      return MySQL.select(command, "");
    else 
      return new ArrayList<Map<String,String>>();
  }
  
  public List<Map<String,String>> select(String command, String sql) {
    if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) 
      return MySQL.select(command, sql);
    else 
      return new ArrayList<Map<String,String>>();
  }

  /**
   * Asynchronous select method.
   * 於 2025/05/20 新增
   * @param command 指令
   * @param callback 回調函數
   */
  public void selectAsync(String command, Callback<List<Map<String, String>>> callback) {
    if (DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
      MySQL.selectAsync(command, callback);
    } else {
      callback.onFailure(new UnsupportedOperationException("Only MySQL supported for async query"));
    }
  }
  
  private void MySql_init() {
    // init
    MySQL = new MySQLManager(DataBase.fileDataBaseInfo.mysql.username,
          DataBase.fileDataBaseInfo.mysql.password,
          "jdbc:mysql://" + DataBase.fileDataBaseInfo.mysql.hostname + "/",
          DataBase.fileDataBaseInfo.mysql.database,
          DataBase.fileDataBaseInfo.mysql.useSSL,
          DataBase.fileDataBaseInfo.mysql.autoReconnect);
  }
  
  private void MySQL_checkdb(){
    // check dropitem
    List<Map<String,String>> data = MySQL.select(
          "SELECT table_name\n"
        + "FROM information_schema.tables\n"
        + "WHERE table_schema = '" + DataBase.fileDataBaseInfo.mysql.database + "'\n"
        + "And table_name = '" + this.table_dropitem + "'");
    
    if(data.isEmpty()) {
      print(DataBase.fileMessage.getString("SQL.Table_Createing").replaceAll("%table%", table_dropitem)
                                      .replaceAll("%db%", DataBase.fileDataBaseInfo.mysql.database));
      if(MySQL.executeUpdate(
            "CREATE TABLE `" + this.table_dropitem + "` (\n"
          + "  `mobname` varchar(20) NOT NULL,\n"
          + "  `custom` varchar(1) NOT NULL,\n"
          + "  `itemno` varchar(100) NOT NULL,\n"
          + "  `quantity` varchar(4) DEFAULT NULL,\n"
          + "  `quantity_max` varchar(4) DEFAULT NULL,\n"
          + "  `chance` decimal(6,3) DEFAULT NULL,\n"
          + "  `world` longtext DEFAULT NULL,\n"
          + "  PRIMARY KEY (`mobname`,`custom`,`itemno`)\n"
          + ")")) {
        print(DataBase.fileMessage.getString("SQL.Table_Create_Success").replaceAll("%table%", table_dropitem)
                                        .replaceAll("%db%", DataBase.fileDataBaseInfo.mysql.database));
      } else {
        print(DataBase.fileMessage.getString("SQL.Table_Create_Fail").replaceAll("%table%", table_dropitem)
                                        .replaceAll("%db%", DataBase.fileDataBaseInfo.mysql.database));
      }
    }
    
    // check mobs
    data = MySQL.select(
          "SELECT table_name\n"
        + "FROM information_schema.tables\n"
        + "WHERE table_schema = '" + DataBase.fileDataBaseInfo.mysql.database + "'\n"
        + "And table_name = '" + this.table_mobs + "'");
    
    if(data.isEmpty()) {
      print(DataBase.fileMessage.getString("SQL.Table_Createing").replaceAll("%table%", table_mobs)
                                        .replaceAll("%db%", DataBase.fileDataBaseInfo.mysql.database));
      if(MySQL.executeUpdate(
            "CREATE TABLE `" + this.table_mobs + "` (\n"
          + "  `mobname` varchar(20) NOT NULL,\n"
            + "  `custom` varchar(1) NOT NULL,\n"
            + "  `icon` longtext,\n"
          + "  PRIMARY KEY (`mobname`,`custom`)\n"
          + ")")) {
        print(DataBase.fileMessage.getString("SQL.Table_Create_Success").replaceAll("%table%", table_mobs)
                                          .replaceAll("%db%", DataBase.fileDataBaseInfo.mysql.database));
      } else {
        print(DataBase.fileMessage.getString("SQL.Table_Create_Fail").replaceAll("%table%", table_mobs)
                                          .replaceAll("%db%", DataBase.fileDataBaseInfo.mysql.database));
      }
    }
    
    //check table_items
    data = MySQL.select(
          "SELECT table_name\n"
        + "FROM information_schema.tables\n"
        + "WHERE table_schema = '" + DataBase.fileDataBaseInfo.mysql.database + "'\n"
        + "And table_name = '" + this.table_items + "'");
    
    if(data.isEmpty()) {
      print(DataBase.fileMessage.getString("SQL.Table_Createing").replaceAll("%table%", table_items)
                                        .replaceAll("%db%", DataBase.fileDataBaseInfo.mysql.database));
      if(MySQL.executeUpdate(
            "CREATE TABLE `" + this.table_items + "` (\n"
          + "  `itemno` varchar(100) NOT NULL,\n"
            + "  `item` longtext,\n"
          + "  PRIMARY KEY (`itemno`)\n"
          + ")")) {
        print(DataBase.fileMessage.getString("SQL.Table_Create_Success").replaceAll("%table%", table_items)
                                                        .replaceAll("%db%", DataBase.fileDataBaseInfo.mysql.database));
      } else {
        print(DataBase.fileMessage.getString("SQL.Table_Create_Fail").replaceAll("%table%", table_items)
                                        .replaceAll("%db%", DataBase.fileDataBaseInfo.mysql.database));
      }
    }
  }
  
  private void MySQL_ReLoad() {
    DataBase.CustomMobsMap.clear();
    DataBase.NormalMobsMap.clear();
    DataBase.items.clear();
    if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
      List<Map<String,String>> rows = MySQL.select(""
          + "Select * \n"
          + "From " + this.table_mobs + "\n");
      
      //Loading mobs
      for(Map<String,String> row : rows) {
        String id = row.get("mobname") != null ? row.get("mobname").toUpperCase() : "";
        String custom = row.get("custom") != null && row.get("custom").equals("Y")? "Y" : "N";
        String itemBase64 = row.get("icon") != null ? row.get("icon") : "";
        if(id.isEmpty()) continue;
        
        Mob mob = new Mob(id,custom);
        
        if(!itemBase64.isEmpty()) mob.setIcon(new Itemset(itemBase64).getItemStack());
        if(custom.equals("Y")) {
          DataBase.CustomMobsMap.put(id, mob);
        } else {
          DataBase.NormalMobsMap.put(id, mob);
        }
      }
      
      //Loading items
      rows = MySQL.select(""
          + "Select * \n"
          + "From " + this.table_items + "\n");
      for(Map<String,String> row : rows) {
        String itemno = row.get("itemno") != null ? row.get("itemno").toUpperCase() : "";
        String itemBase64 = row.get("item") != null ? row.get("item") : "";
        
        if(!itemBase64.isEmpty()) {
          Itemset item = new Itemset(itemBase64);
          DataBase.items.put(itemno, item);
        }
      }
      
      //Loading drop item
      rows = MySQL.select(""
          + "Select * \n"
          + "From " + this.table_dropitem + "\n");
      for(Map<String,String> row : rows) {
        String id = row.get("mobname") != null ? row.get("mobname").toUpperCase() : "";
        boolean custom = row.get("custom") != null && row.get("custom").equals("Y");
        String Itemno = row.get("itemno") != null ? row.get("itemno").toUpperCase() : "";
        int Quantity = row.get("quantity") != null ? Integer.parseInt(row.get("quantity")) : 0;
        int Quantity_max = row.get("quantity_max") != null ? Integer.parseInt(row.get("quantity_max")) : 0;
        double Chance = row.get("chance") != null ? Double.parseDouble(row.get("chance")) : 0.00;
//        DataBase.Print("Quantity: " + Quantity);
//        DataBase.Print("Quantity_max: " + Quantity_max);
//        DataBase.Print("Chance: " + Chance);
        if(custom) {
          Mob mob = DataBase.CustomMobsMap.get(id);
          mob.MobItems.put(Itemno, new MobItem(Itemno, Quantity, Quantity_max, Chance));
          DataBase.CustomMobsMap.put(id, mob);
        } else {
          Mob mob = DataBase.NormalMobsMap.get(id);
          mob.MobItems.put(Itemno, new MobItem(Itemno, Quantity, Quantity_max, Chance));
          DataBase.NormalMobsMap.put(id, mob);
        }
      }
    }
  }
  
  /**
   * 增加怪物
   * @param Mob
   * @return 
   *   當發生錯誤時會回傳錯誤代碼
   */
  public List<String> MobAdd(Mob Mob) {
    List<String> mode = new ArrayList<String>();
    
    if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
      List<Map<String,String>> data = MySQL.select(""
          + "Select * \n"
          + "From " + this.table_mobs + "\n"
          + "Where mobname = '" + Mob.getName() + "'\n"
          + "And custom = '" + Mob.getCustom() + "'");
      
      if(data.isEmpty()) {
        String sql = ""
        + "Insert Into " + this.table_mobs + "\n"
        + "(mobname, custom  , icon)\n"
        + "Values\n"
        + "('" + Mob.getName() + "',"
        + " '" + Mob.getCustom() + "'";
        
        if(Mob.hasIcon()) 
          sql = sql + ", "+ " '" + new Itemset(Mob.getIcon()).itemStackToBase64() + "')";
        else 
          sql = sql + ", "+ "null" + ")";
        
        boolean Success = MySQL.executeUpdate(sql);
        if(!Success)
          mode.add("Add_data_error");
      } else {
        mode.add("same_data_error");
      }
    } else {
      
    }
    return mode;
  }
  
  public List<String> MobRemove(Mob Mob) {
    List<String> mode = new ArrayList<String>();
    
    if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
      String sql = ""
          + "Delete From " + this.table_dropitem + "\n"
          + "Where 1=1\n"
          + "And mobname = '" + Mob.getName() + "'\n"
          + "And custom = '" + Mob.getCustom() + "'";
      
      boolean Success = MySQL.executeUpdate(sql);
      if(!Success)
        mode.add("remove_mob_item_error");
      
      sql = ""
          + "Delete From " + this.table_mobs + "\n"
          + "Where 1=1\n"
          + "And mobname = '" + Mob.getName() + "'\n"
          + "And custom = '" + Mob.getCustom() + "'";
      
      Success = MySQL.executeUpdate(sql);
      if(!Success)
        mode.add("remove_mob_error");
    } else {
      
    }
    return mode;
  }
  
  /**
   * 增加怪物
   * @param Mob
   * @return 
   *   當發生錯誤時會回傳錯誤代碼
   */
  public List<String> MobItemAdd(Mob Mob, MobItem MobItem) {
    List<String> mode = new ArrayList<String>();
    
    if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
      List<Map<String,String>> data = MySQL.select(""
          + "Select * \n"
          + "From " + this.table_dropitem + "\n"
            + "Where mobname = '" + Mob.getName() + "'\n"
            + "And custom = '" + Mob.getCustom() + "'\n"
          + "And itemno = '" + MobItem.getItemNo() + "'");
      
      if(data.isEmpty()) {      
        String sql = ""
        + "Insert Into " + this.table_dropitem + "\n"
        + "(mobname, custom , itemno)\n"
        + "Values\n"
        + "('" + Mob.getName() + "',"
        + " '" + Mob.getCustom() + "',"
        + " '" + MobItem.getItemNo() + "')";
        
        boolean Success = MySQL.executeUpdate(sql);
        if(!Success)
          mode.add("Add_data_error");
      } else {
        mode.add("same_data_error");
      }
    } else {
      
    }
    return mode;
  }
  
  public List<String> MobItemUpdate(Mob Mob, MobItem MobItem) {
    List<String> mode = new ArrayList<String>();
    
    if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
      List<Map<String,String>> data = MySQL.select(""
          + "Select * \n"
          + "From " + this.table_dropitem + "\n"
            + "Where mobname = '" + Mob.getName() + "'\n"
            + "And custom = '" + Mob.getCustom() + "'\n"
          + "And itemno = '" + MobItem.getItemNo() + "'");
      
      if(data.isEmpty()) {      
        mode.add("data_not_found_error");
      } else {
        String sql = ""
        + "Update " + this.table_dropitem + "\n"
        + "Set" + "\n"
        + "  quantity = " + MobItem.Quantity + ",\n"
        + "  quantity_max = " + ((MobItem.Quantity_max - MobItem.Quantity) <= 0 ? "null" : MobItem.Quantity_max) + ",\n"
        + "  chance = " + MobItem.Chance + "\n"
          + "Where mobname = '" + Mob.getName() + "'\n"
          + "And custom = '" + Mob.getCustom() + "'\n"
        + "And itemno = '" + MobItem.getItemNo() + "'";
          
        boolean Success = MySQL.executeUpdate(sql);
        if(!Success)
          mode.add("Update_data_error");
      }
    } else {
      
    }
    return mode;
  }
  
  public List<String> MobItemRemove(Mob Mob, MobItem MobItem) {
    List<String> mode = new ArrayList<String>();
    
    if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
      String sql = ""
          + "Delete From " + this.table_dropitem + "\n"
          + "Where 1=1\n"
          + "And mobname = '" + Mob.getName() + "'\n"
            + "And custom = '" + Mob.getCustom() + "'\n"
          + "And itemno = '" + MobItem.getItemNo() + "'";
      
      boolean Success = MySQL.executeUpdate(sql);
      if(!Success)
        mode.add("remove_mob_item_error");
    } else {
      
    }
    return mode;
  }
  
  /**
   * 增加物品
   * @param itemno
   * @param item
   * @return
   *   當發生錯誤時會回傳錯誤代碼
   */
  public List<String> ItemsAdd(String itemno, ItemStack item) {
    return ItemsAdd(itemno,new Itemset(item));
  }
  
  /**
   * 增加物品
   * @param itemno
   * @param item
   * @return
   *   當發生錯誤時會回傳錯誤代碼
   */
  public List<String> ItemsAdd(String itemno, Itemset item) {
    List<String> mode = new ArrayList<String>();
    
    if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
      List<Map<String,String>> data = MySQL.select(""
          + "Select * \n"
          + "From " + this.table_items + "\n"
            + "Where itemno = '" + itemno + "'");
      
      if(data.isEmpty()) {      
        String sql = ""
        + "Insert Into " + this.table_items + "\n"
        + "(itemno, item)\n"
        + "Values\n"
        + "('" + itemno + "',"
        + " '" + item.itemStackToBase64() + "')";
        
        boolean Success = MySQL.executeUpdate(sql);
        if(!Success)
          mode.add("Add_data_error");
      } else {
        mode.add("same_data_error");
      }
    } else {
      
    }
    return mode;
  }
  
  /**
   * 取得物品清單
   * @param page
   * @param size
   * @return
   */
  public Map<String,Itemset> ItemsGet(int page, int size) {
    Map<String, Itemset> items = new HashMap<String, Itemset>();
    if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
      List<Map<String,String>> data = MySQL.select(""
          + "Select * \n"
          + "From " + this.table_items + "\n"
          + "Limit " + (page * size) + "," + size);
      
      for(Map<String,String> row : data) {
        String itemno = row.get("itemno") != null ? row.get("itemno").toUpperCase() : "";
        String itemBase64 = row.get("item") != null ? row.get("item") : "";
        
        if(!itemBase64.isEmpty()) {
          Itemset item = new Itemset(itemBase64);
          items.put(itemno, item);
        }
      }
    } else {
      
    }
    return items;
  }

  /**
   * 取得物品清單
   * @param page
   * @param size
   * @param callback
   */
  public void ItemsGetAsync(int page, int size, Callback<Map<String,Itemset>> callback) {
    Map<String, Itemset> items = new HashMap<String, Itemset>();
    if(DataBase.fileDataBaseInfo.storage.method.equals("mysql")) {
      String sql = ""
          + "Select * \n"
          + "From " + this.table_items + "\n"
          + "Limit " + (page * size) + "," + size;
      sql = ""
          + "Select * \n"
          + "From " + this.table_items;
      DataBase.sql.selectAsync(sql, new Callback<List<Map<String, String>>>() {
        @Override
        public void onSuccess(List<Map<String, String>> data) {
          Bukkit.getScheduler().runTask(MobDrop2.plugin, () -> {
            for(Map<String,String> row : data) {
              String itemno = row.get("itemno") != null ? row.get("itemno").toUpperCase() : "";
              String itemBase64 = row.get("item") != null ? row.get("item") : "";
              
              if(!itemBase64.isEmpty()) {
                Itemset item = new Itemset(itemBase64);
                items.put(itemno, item);
              }
            }
            callback.onSuccess(items);
          });
        }

        @Override
        public void onFailure(Exception e) {
          // e.printStackTrace();
          Bukkit.getScheduler().runTask(MobDrop2.plugin, () -> {
            callback.onFailure(e);
          });
        }
      });
    } else {
      
    }
  }

  /**
   * print顯示控制器
   * @param message 訊息
   */
  private void print(String message) {
    if(MobDrop2.plugin.getConfig().getBoolean("SQL.Info")) DataBase.Print(message);
  }
  
}
