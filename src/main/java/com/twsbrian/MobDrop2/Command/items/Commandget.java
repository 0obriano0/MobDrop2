package com.twsbrian.MobDrop2.Command.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.twsbrian.MobDrop2.Command.mainCommandSystem;
import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.Itemset;

public class Commandget  extends mainCommandSystem{
  public Commandget() {
    super(  "items.get",
        "/mobdrop items get 取得物品",
        new ArrayList<String>(Arrays.asList("mobdrop.admin.items.get")));
  }
  
  @Override
  public void run(Player player, String commandLabel, Command command, String[] args) throws Exception {
    if(args.length >= 1) {
      if(DataBase.items.get(args[0]) != null) {
        ItemStack Itemcreate = DataBase.items.get(args[0]).getItemStack();
        Itemcreate.setAmount(1);
        if(player.getInventory().firstEmpty() == -1) {
          DataBase.sendMessage(player,"§b" + DataBase.fileMessage.getString("Inventory.items_table_get_item_bag_full"));
        } else {
          player.getInventory().addItem(Itemcreate);
          DataBase.sendMessage(player,"§b" + DataBase.fileMessage.getString("Inventory.items_table_get_item_success")
                        .replaceAll("%id%", args[0])
                        .replaceAll("%name%", new Itemset(Itemcreate).getItemName()));
        }
      } else {
        DataBase.sendMessage(player,"§b" + DataBase.fileMessage.getString("Command.ItemNotFind"));
      }
    } else {
      DataBase.sendMessage(player,"§c/mobdrop items get <- ");
    }
  }
  
  @Override
  public List<String> tabComplete(Player player, String commandLabel, Command command, String[] args, final ClassLoader classLoader, final String commandPath){
    if(args.length == 1) {
      List<String> tablist = new ArrayList<String>();
          for(Entry<String, Itemset> entry : DataBase.items.entrySet()) {
            if(entry.getKey().contains(args[0])) {
              tablist.add(entry.getKey());
            }
          }
          Collections.sort(tablist); 
          return tablist;
    } else 
      return Collections.emptyList();
  }
}
