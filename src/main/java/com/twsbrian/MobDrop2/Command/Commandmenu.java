package com.twsbrian.MobDrop2.Command;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.twsbrian.MobDrop2.InventoryGUI.InventoryMenu;

public class Commandmenu extends mainCommandSystem{
  public Commandmenu() {
    super(  "menu",
        "/mobdrop menu 開啟目錄",
        new ArrayList<String>(Arrays.asList("mobdrop.user.inventory.menu")));
  }
  
  @Override
  public void run(Player player, String commandLabel, Command command, String[] args) throws Exception {
    InventoryMenu.INVENTORY.open(player);
  }
}
