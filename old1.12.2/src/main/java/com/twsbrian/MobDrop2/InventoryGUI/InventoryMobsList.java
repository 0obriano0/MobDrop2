﻿package com.twsbrian.MobDrop2.InventoryGUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.twsbrian.MobDrop2.Database.DataBase;
import com.twsbrian.MobDrop2.Database.Mob;
import com.twsbrian.MobDrop2.Database.MobItemList;
import com.twsbrian.MobDrop2.HashMap.HashMapSortMobList;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;

public class InventoryMobsList implements InventoryProvider{
	public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("moblist")
            .provider(new InventoryMobsList())
            .size(5, 9)
            .title(ChatColor.BLUE + DataBase.language.Inventory.MobsList)
            .build();

    @Override
    public void init(Player player, InventoryContents contents) {
    	Pagination pagination = contents.pagination();
    	
        ClickableItem[] items = new ClickableItem[DataBase.MobsMap.size()];
        int index = 0;
        HashMapSortMobList ItemList = new HashMapSortMobList((HashMap<String, Mob>) DataBase.MobsMap);
        
        for (Map.Entry<String, Mob> entry:ItemList.list_Data) {
            items[index] = ClickableItem.of(createitem(entry.getKey(),entry.getValue()), e -> InventoryMobs_ItemList.getInventory(entry.getKey(),entry.getValue()).open(player));
        	index++;
        }
        
        pagination.setItems(items);
        pagination.setItemsPerPage(36);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
        
        
        contents.set(4, 0, ClickableItem.of(InventoryTools.createPageButton(Material.WOOD_DOOR,"§a" + DataBase.language.Inventory.back_menu),
                e -> InventoryMenu.INVENTORY.open(player)));
        contents.set(4, 3, ClickableItem.of(InventoryTools.createPageButton(Material.ARROW,"§a" + DataBase.language.Inventory.previous),
                e -> INVENTORY.open(player, pagination.previous().getPage())));
        contents.set(4, 4, ClickableItem.empty(InventoryTools.createPageButton(Material.PAPER,"§a - " + (pagination.getPage() + 1) + " - ")));
        contents.set(4, 5, ClickableItem.of(InventoryTools.createPageButton(Material.ARROW,"§a" + DataBase.language.Inventory.next),
                e -> INVENTORY.open(player, pagination.next().getPage())));
    }

	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}
	
	private ItemStack createitem(String mobname, Mob mob) {
		List<MobItemList> items = new ArrayList<MobItemList>();
		items.addAll(mob.HeadList);
		items.addAll(mob.ItemList);
		
		ItemStack item;
				
		if(mob.HeadList.size() > 0) {
			item = mob.HeadList.get(0).getResultItem();
		}else {
			item = new ItemStack(Material.MONSTER_EGG);
		}
		/*Material Material_data = Material.getMaterial(entry.getKey() + "_SPAWN_EGG");
		if(Material_data == null)
			Material_data = Material.getMaterial("ZOMBIE" + "_SPAWN_EGG");*/
		ItemMeta newItemMeta;
	    newItemMeta = item.getItemMeta();
	    newItemMeta.setDisplayName("§f" + DataBase.GetEntityName(mobname));
	    
        List<String> Lore =  newItemMeta.getLore();
        Lore = new ArrayList<String>();
        
        Lore.add("");
        
        Lore.add("§a - " + DataBase.language.Inventory.items + " §f" + items.size());
        Lore.add("§7 - " + mobname);
        
        newItemMeta.setLore(Lore);
    	item.setItemMeta(newItemMeta);
    	item.setAmount(1);
    	
    	return item;
	}
}
