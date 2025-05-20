package com.twsbrian.MobDrop2.Command;

import java.util.ArrayList;
import java.util.Arrays;

public class Commanditems extends mainCommandSystem{
  public Commanditems() {
    super(  "items",
        "/mobdrop items 物品列表設定",
        new ArrayList<String>(Arrays.asList("mobdrop.admin.items")),
        "/items");
  }
}
