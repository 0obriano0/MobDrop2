package com.twsbrian.MobDrop2.InventoryGUI;

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
import com.twsbrian.MobDrop2.Database.Items;
import com.twsbrian.MobDrop2.Database.Mob;
import com.twsbrian.MobDrop2.Database.MobItemList;
import com.twsbrian.MobDrop2.HashMap.HashMapSortItemList;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;

public class InventoryItemsList implements InventoryProvider{
	public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("Itemlist")
            .provider(new InventoryItemsList())
            .size(5, 9)
            .title(ChatColor.BLUE + DataBase.language.Inventory.ItemList)
            .build();
	
    @Override
    public void init(Player player, InventoryContents contents) {
    	Pagination pagination = contents.pagination();
    	List<MobItemList> head_list = new ArrayList<MobItemList>();
    	
    	for(Map.Entry<String, Mob> entry:DataBase.MobsMap.entrySet()) {
    		if(entry.getValue().HeadList.size() > 0)head_list.addAll(entry.getValue().HeadList);
    	}
    	
        ClickableItem[] items = new ClickableItem[DataBase.ItemMap.size() + head_list.size()];
        int index = 0;
        HashMapSortItemList ItemList = new HashMapSortItemList((HashMap<String, Items>) DataBase.ItemMap);
        
        for (MobItemList item:head_list) {
            items[index] = ClickableItem.of(createitem(null,item.Item,player), e -> GetItem(item.Item,player));
        	index++;
        }
        
        for (Map.Entry<String, Items> entry:ItemList.list_Data) {
            items[index] = ClickableItem.of(createitem(entry.getKey(),entry.getValue(),player), e -> GetItem(entry.getValue(),player));
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
	
	private ItemStack createitem(String Keyname ,Items entry, Player player) {
		ItemStack item = entry.getResultItem();
        ItemMeta newItemMeta = item.getItemMeta();
        List<String> Lore =  newItemMeta.getLore();
        if(Lore == null)
        	Lore = new ArrayList<String>();
        Lore.add("");
        if(Keyname != null) Lore.add("§7 - " + Keyname);
        if (player.hasPermission("MobDrop.admin")) {
        	Lore.add("");
        	for(int index = 0;index < DataBase.language.Inventory.admin_lore.size();index++) {
        		Lore.add("§a"+DataBase.language.Inventory.admin_lore.get(index));
        	}
        }
        newItemMeta.setDisplayName(entry.ItemName);
        newItemMeta.setLore(Lore);
    	item.setItemMeta(newItemMeta);
    	item.setAmount(1);
		return item;
	}
	
	private void GetItem(Items entry, Player player) {
		if (player.hasPermission("MobDrop.admin")) {
			ItemStack Itemcreate = entry.getResultItem();
			Itemcreate.setAmount(1);
			if(player.getInventory().firstEmpty() == -1)
				player.sendMessage("§b" + DataBase.language.Plugin_name + " §c背包已滿，無法獲取道具");
			else {
				player.getInventory().addItem(Itemcreate);
				player.sendMessage("§b" + DataBase.language.Plugin_name + " §f獲取道具: " + entry.ItemName);
			}
		}
	}
}
