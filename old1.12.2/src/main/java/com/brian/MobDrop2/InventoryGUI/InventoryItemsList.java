package com.brian.MobDrop2.InventoryGUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.brian.MobDrop2.Database.DataBase;
import com.brian.MobDrop2.Database.Items;
import com.brian.MobDrop2.HashMap.HashMapSortItemList;

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
    	
        ClickableItem[] items = new ClickableItem[DataBase.ItemMap.size()];
        int index = 0;
        HashMapSortItemList ItemList = new HashMapSortItemList((HashMap<String, Items>) DataBase.ItemMap);
        
        for (Map.Entry<String, Items> entry:ItemList.list_Data) {
            items[index] = ClickableItem.of(createitem(entry,player), e -> GetItem(entry,player));
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
	
	private ItemStack createitem(Map.Entry<String, Items> entry, Player player) {
		ItemStack item = entry.getValue().getResultItem();
        ItemMeta newItemMeta = item.getItemMeta();
        List<String> Lore =  newItemMeta.getLore();
        if(Lore == null)
        	Lore = new ArrayList<String>();
        Lore.add("");
        Lore.add("§7 - " + entry.getKey());
        if (player.hasPermission("MobDrop.admin")) {
        	Lore.add("");
        	for(int index = 0;index < DataBase.language.Inventory.admin_lore.size();index++) {
        		Lore.add("§a"+DataBase.language.Inventory.admin_lore.get(index));
        	}
        }
        newItemMeta.setDisplayName(entry.getValue().ItemName);
        newItemMeta.setLore(Lore);
    	item.setItemMeta(newItemMeta);
    	item.setAmount(1);
		return item;
	}
	
	private void GetItem(Map.Entry<String, Items> entry, Player player) {
		if (player.hasPermission("MobDrop.admin")) {
			ItemStack Itemcreate = entry.getValue().getResultItem();
			Itemcreate.setAmount(1);
			if(player.getInventory().firstEmpty() == -1)
				player.sendMessage("§b" + DataBase.language.Plugin_name + " §c背包已滿，無法獲取道具");
			else {
				player.getInventory().addItem(Itemcreate);
				player.sendMessage("§b" + DataBase.language.Plugin_name + " §f獲取道具: " + entry.getValue().ItemName);
			}
		}
	}
}
