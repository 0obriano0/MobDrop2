package com.twsbrian.MobDrop2.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.Itemset;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class InventoryMenu implements InventoryProvider{
	public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("Menu")
            .provider(new InventoryMenu())
            .size(3, 9)
            .title(ChatColor.BLUE + DataBase.fileInventory.getInventorTitle("Menu"))
            .build();
	
	@Override
	public void init(Player player, InventoryContents contents) {
		
		contents.set(1, 2, ClickableItem.of(mob_button("Items"), e -> InventoryItemTable.getInventory().open(player)));
		contents.set(1, 4, ClickableItem.of(mob_button("Normal"), e -> InventoryMobs.getInventory(false).open(player)));
		contents.set(1, 6, ClickableItem.of(mob_button("Custom"), e -> InventoryMobs.getInventory(true).open(player)));
	}
	
	@Override
	public void update(Player player, InventoryContents contents) {
		
	}
	
	private ItemStack mob_button(String mod) {
		String Type = DataBase.fileInventory.getString("Inventory.Menu.Button." + mod + ".Type").toUpperCase();
		String title = DataBase.fileInventory.getString("Inventory.Menu.Button." + mod + ".Title");
		List<String> Lore = new ArrayList<String>();
		for(String str : DataBase.fileInventory.getStringList("Inventory.Menu.Button." + mod + ".Lore")){
			int size = 0;
			if(mod.equals("Normal")) {
				size = DataBase.NormalMobsMap.size();
			} else if (mod.equals("Custom")) {
				size =  DataBase.CustomMobsMap.size();
			} else if (mod.equals("Items")){
				size =  DataBase.items.size();
			}
			Lore.add(str.replaceAll("&", "§").replaceAll("%num%", size + ""));
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
