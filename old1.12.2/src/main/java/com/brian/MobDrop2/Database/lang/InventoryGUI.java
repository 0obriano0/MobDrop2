package com.brian.MobDrop2.Database.lang;

import java.util.ArrayList;
import java.util.List;

public class InventoryGUI {
	public String info = "資訊";
	public String info_player_sakurahead = "玩家會不會掉頭盧:%boolean%";
	public String MobsList = "怪物掉落列表";
	public String ItemList = "物品列表";
	public String close = "關閉";
	public String next = "下一頁";
	public String previous = "上一頁";
	public String menu = "主選單";
	public String back = "返回";
	public String back_menu = "回首頁";
	public List<String> admin_lore;
	public String dropList = "掉落列表";
	public String items = "道具:";
	public String mobs = "怪物:";
	public String Item_Chance = "Chance:";
	public String Item_Quantity = "Quantity:";
	
	public InventoryGUI() {
		admin_lore = new ArrayList<String>();
		admin_lore.add("點擊左鍵");
		admin_lore.add("拿取道具");
	}
}
