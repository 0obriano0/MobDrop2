package com.brian.MobDrop2.DataBase;

import org.bukkit.inventory.ItemStack;

public class MobItem {
	private String Itemno;
	// 得到的物品數量
	public int Quantity;
	public int Quantity_max;
	// 掉落率
	public double Chance;
	
	public MobItem(String Itemno) {
		this.Itemno = Itemno;
	}
	
	public MobItem(int Quantity, int Quantity_max, double Chance, String Itemno) {
		this.Quantity = Quantity;
		this.Quantity_max = Quantity_max;
		this.Chance = Chance;
		this.Itemno = Itemno;
	}
	
	public String getItemNo() {
		return Itemno;
	}
	
	public ItemStack getResultItem() {
		return DataBase.mobitems.get(Itemno).getItemStack();
	}
	
	public String getItemName() {
		return DataBase.mobitems.get(Itemno).getItemName();
	}
}
