package com.twsbrian.MobDrop2.Command.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.twsbrian.MobDrop2.Command.mainCommandSystem;
import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.Itemset;

public class Commandadd extends mainCommandSystem{
  
  public Commandadd() {
    super(  "items.add",
        "/mobdrop items add 增加物品",
        new ArrayList<String>(Arrays.asList("mobdrop.admin.items.add")));
  }
  
  @Override
  public void run(Player player, String commandLabel, Command command, String[] args) throws Exception {
    ItemStack setitem = player.getInventory().getItemInMainHand();
    if (!setitem.getType().toString().equals("AIR")) {
      if(args.length >= 1) {
        String str = args[0].replaceAll("&", "§").toUpperCase();
        List<String> msg = DataBase.sql.ItemsAdd(str, setitem);
        if(msg.isEmpty()) {
          DataBase.items.put(str, new Itemset(setitem));
          DataBase.sendMessage(player,DataBase.fileMessage.getString("Inventory.items_table_add_success"));
        } else {
          DataBase.sendMessage(player,DataBase.fileMessage.getString("Inventory.items_table_add_fail"));
          DataBase.sendMessage(player,msg.toString());
        }
      } else {
        DataBase.sendMessage(player,"請輸入物品id");
      }
    } else {
      DataBase.sendMessage(player,DataBase.fileMessage.getString("Command.HandNoItem"));
    }
  }
}
