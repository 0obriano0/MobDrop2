package com.twsbrian.MobDrop2.DataBase;

import org.bukkit.inventory.ItemStack;

/**
 * 怪物物品定義檔
 * @author Brian
 *
 */
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
	
	public MobItem(String Itemno, int Quantity, int Quantity_max, double Chance) {
		this.Quantity = Quantity;
		this.Quantity_max = Quantity_max;
		this.Chance = Chance;
		this.Itemno = Itemno;
	}
	
	public String getItemNo() {
		return Itemno;
	}
	
	public ItemStack getResultItem() {
		return DataBase.items.get(Itemno).getItemStack().clone();
	}
	
	public String getItemName() {
		return DataBase.items.get(Itemno).getItemName();
	}
}
