package com.chengbrian.MobDrop2.DataBase;

import org.bukkit.inventory.ItemStack;

public class MobItemList {
	public Itemset Item;
	// 得到的物品數量
	public int Quantity;
	public int Quantity_max;
	// 掉落率
	public double Chance;
	
	public MobItemList(int Quantity, int Quantity_max, double Chance, Itemset Item) {
		this.Quantity = Quantity;
		this.Quantity_max = Quantity_max;
		this.Chance = Chance;
		this.Item = Item;
	}
	
	public ItemStack getResultItem() {
		ItemStack ResultItem = Item.getItemStack();
		ResultItem.setAmount(this.Quantity);
		return ResultItem;
	}
}
