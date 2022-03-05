package com.brian.MobDrop2.DataBase;

import org.bukkit.inventory.ItemStack;

public class MobItem {
	public Itemset Item;
	// 得到的物品數量
	public int Quantity;
	public int Quantity_max;
	// 掉落率
	public double Chance;
	
	public MobItem(Itemset Item) {
		this.Item = Item;
	}
	
	public MobItem(int Quantity, int Quantity_max, double Chance, Itemset Item) {
		this.Quantity = Quantity;
		this.Quantity_max = Quantity_max;
		this.Chance = Chance;
		this.Item = Item;
	}
	
	public ItemStack getResultItem() {
		ItemStack ResultItem = Item.getItemStack();
		ResultItem.setAmount(1);
		return ResultItem;
	}
	
	public boolean equalitem(ItemStack checkitem) {
		checkitem.setAmount(1);
		return getResultItem().equals(checkitem);
	}
	
	public String getName() {
		return Item.getItemName();
	}
}
