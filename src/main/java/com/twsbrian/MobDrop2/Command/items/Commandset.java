package com.twsbrian.MobDrop2.Command.items;

import java.util.ArrayList;
import java.util.Arrays;

import com.twsbrian.MobDrop2.Command.mainCommandSystem;

public class Commandset extends mainCommandSystem{
  public Commandset() {
    super(  "items.set",
        "/mobdrop items set 設定物品",
        new ArrayList<String>(Arrays.asList("mobdrop.admin.items.set")),
        "/items/set");
  }
}
