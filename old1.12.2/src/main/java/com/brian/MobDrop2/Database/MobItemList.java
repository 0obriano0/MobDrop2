package com.twsbrian.MobDrop2.Database;

import org.bukkit.inventory.ItemStack;

public class MobItemList {
	public Items Item;
	// 得到的物品數量
	public int Quantity;
	public int Quantity_max;
	// 掉落率
	public double Chance;
	
	public MobItemList(int Quantity, int Quantity_max, double Chance, Items Item) {
		this.Quantity = Quantity;
		this.Quantity_max = Quantity_max;
		this.Chance = Chance;
		this.Item = Item;
	}
	
	public ItemStack getResultItem() {
		ItemStack ResultItem = Item.getResultItem();
		ResultItem.setAmount(this.Quantity);
		return ResultItem;
	}
}
