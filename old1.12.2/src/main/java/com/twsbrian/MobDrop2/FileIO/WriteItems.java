package com.twsbrian.MobDrop2.FileIO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.twsbrian.MobDrop2.Database.DataBase;
import com.twsbrian.MobDrop2.Database.Items;

public class WriteItems {
	
	private static String loadfilename = "Items.yml";
	
	private static List<String> ItemtoStringList() {
		List<String> data = new ArrayList<String>();
		for(Entry<String, Items> ItemData :DataBase.ItemMap.entrySet()) {
			if(ItemData.getKey().equals("WOODEN_SWORD_TEST")) continue;
			Items itemvalue = ItemData.getValue();
			data.add(ItemData.getKey() + ":");
			data.add("  ItemName: \"" + itemvalue.ItemName + "\"");
			data.add("  UseCustomName: " + itemvalue.UseCustomName);
			data.add("  ItemRealname: \"" + itemvalue.ItemRealname + "\"");
			if(itemvalue.durability > 0)  data.add("  durability: " + itemvalue.durability);
			if(itemvalue.ItemLores.size() > 0) {
				data.add("  ItemLores: ");
				for(String itemlores: itemvalue.ItemLores) data.add("  - \"" + itemlores+ "\"");
			}
			if(itemvalue.Enchants.size() > 0) {
				data.add("  Enchants: ");
				for(String enchants: itemvalue.Enchants) data.add("  - " + enchants);
			}
			if(itemvalue.ItemFlags.size() > 0) {
				data.add("  ItemFlags: ");
				for(String itemflags: itemvalue.ItemFlags) data.add("  - " + itemflags);
			}
			if(itemvalue.Unbreakable) data.add("  Unbreakable: " + true);
			
		}
		return data;
	}
	
	public static void addItemStack(ItemStack addItem,String itemid) {
		List<String> data = ItemtoStringList();
		
		data.add(itemid + ":");
		data.add("  ItemRealname: \"" + addItem.getType().name().toUpperCase() + "\"");
		if(addItem.getDurability() > 0) data.add("  durability: " + addItem.getDurability());
		if(addItem.hasItemMeta()){
			ItemMeta addMeta = addItem.getItemMeta();
			data.add("  ItemName: \"" + addMeta.getDisplayName() + "\"");
			data.add("  UseCustomName: " + false);
			if(addMeta.getLore().size() > 0) {
				data.add("  ItemLores: ");
				for(String lore : addMeta.getLore()) data.add("  - \"" + lore+ "\"");
			}
			if(addMeta.getEnchants().size() > 0) {
				data.add("  Enchants: ");
				for(Entry<Enchantment, Integer> lore : addMeta.getEnchants().entrySet())
					data.add("  - " + lore.getKey().getName() + ":" + lore.getValue());
			}
			if(addMeta.getItemFlags().size() > 0) {
				data.add("  ItemFlags: ");
				for(ItemFlag itemflags : addMeta.getItemFlags()) data.add("  - " + itemflags.name());
			}
			if(addMeta.isUnbreakable()) data.add("  Unbreakable: " + true);
		}else {
			if(addItem.getEnchantments().size() > 0) {
				data.add("  Enchants: ");
				for(Entry<Enchantment, Integer> lore : addItem.getEnchantments().entrySet())
					data.add("  - " + lore.getKey().getName() + ":" + lore.getValue());
			}
		}
		
		tools.writeFile(DataBase.pluginMainDir + loadfilename,data);
		DataBase.LoadItems.ReLoadItems();
	}
	
	public static boolean removeItemStack(String itemid) {
		if(DataBase.ItemMap.containsKey(itemid.toUpperCase())) {
			if(DataBase.ItemMap.remove(itemid.toUpperCase()) == null)
				return false;
			tools.writeFile(DataBase.pluginMainDir + loadfilename,ItemtoStringList());
			DataBase.LoadItems.ReLoadItems();
			return true;
		}
		return false;
	}
}
