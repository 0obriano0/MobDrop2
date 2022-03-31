package com.twsbrian.MobDrop2.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.Itemset;

public class InventoryTools {
	static ItemStack createPageButton(Material MaterialType,String itemname) {
		ItemMeta newItemMeta = null;
		ItemStack Button = new ItemStack(MaterialType);
		newItemMeta = Button.getItemMeta();
		newItemMeta.setDisplayName(itemname);
		Button.setItemMeta(newItemMeta);
		return Button;
	}
	static ItemStack createPageButton(Material MaterialType,String itemname,String lore) {
		ItemStack Button = createPageButton(MaterialType,itemname);
		ItemMeta newItemMeta = Button.getItemMeta();
		List<String> lore_list = new ArrayList<String>();
		lore_list.add("");
		lore_list.add(lore);
		newItemMeta.setLore(lore_list);
		Button.setItemMeta(newItemMeta);
		return Button;
	}
	
	static ItemStack createPageButton(Material MaterialType,String itemname,List<String> lores) {
		ItemStack Button = createPageButton(MaterialType,itemname);
		ItemMeta newItemMeta = Button.getItemMeta();
		List<String> lore_list = new ArrayList<String>();
		lore_list.addAll(lores);
		newItemMeta.setLore(lore_list);
		Button.setItemMeta(newItemMeta);
		return Button;
	}
	
	static ItemStack getbutton(String Inventory,String mod) {
		String Type = DataBase.fileInventory.getString("Inventory." + Inventory + ".Button." + mod + ".Type").toUpperCase();
		String title = DataBase.fileInventory.getString("Inventory." + Inventory + ".Button." + mod + ".Title");
		List<String> Lore = new ArrayList<String>();
		for(String str : DataBase.fileInventory.getStringList("Inventory." + Inventory + ".Button." + mod + ".Lore")){
			Lore.add(str.replaceAll("&", "§"));
		}
		Material material = null;
		if(Material.getMaterial(Type) != null)
			material = Material.getMaterial(Type);
		else {
			material = Material.BARRIER;
			Lore.add(DataBase.fileMessage.getString("Inventory.Type_error"));
		}
		return new Itemset(material).setItemName(title).setLore(Lore).getItemStack();
	}
}
