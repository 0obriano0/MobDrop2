package com.twsbrian.MobDrop2.Command.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.twsbrian.MobDrop2.Command.mainCommandSystem;
import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.Itemset;

public class Commandremove extends mainCommandSystem{
  public Commandremove() {
    super(  "items.remove",
        "/mobdrop items remove 增加物品",
        new ArrayList<String>(Arrays.asList("mobdrop.admin.items.remove")));
  }
  
  @Override
  public void run(Player player, String commandLabel, Command command, String[] args) throws Exception {
    if(args.length >= 1) {
      if(DataBase.items.get(args[0]) != null) {
        DataBase.sendMessage(player,"§cthis command not yet");
      } else {
        DataBase.sendMessage(player,"§b" + DataBase.fileMessage.getString("Command.ItemNotFind"));
      }
    } else {
      DataBase.sendMessage(player,"§c/mobdrop items remove <- ");
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
