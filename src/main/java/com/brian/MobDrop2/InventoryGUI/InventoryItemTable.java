package com.brian.MobDrop2.InventoryGUI;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.brian.MobDrop2.DataBase.DataBase;
import com.brian.MobDrop2.DataBase.Itemset;
import com.brian.MobDrop2.HashMap.HashMapSortItemset;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;

public class InventoryItemTable  implements InventoryProvider{
	
	public InventoryItemTable() {
		
	}

	public static SmartInventory getInventory() {
        return SmartInventory.builder()
                .provider(new InventoryItemTable())
                .size(5, 9)
                .title(ChatColor.BLUE + "")
                .build();
	}
	
	@Override
	public void init(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
		Pagination pagination = contents.pagination();
		
		ClickableItem[] items = new ClickableItem[DataBase.items.size()];
        int index = 0;
        HashMapSortItemset ItemList = new HashMapSortItemset((HashMap<String, Itemset>) DataBase.items);
        
        for (Map.Entry<String, Itemset> entry:ItemList.list_Data) {
        	Itemset item = entry.getValue();
            items[index] = ClickableItem.of(item.getItemStack(), e -> {});
        	index++;
        }
        
        pagination.setItems(items);
        pagination.setItemsPerPage(36);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
		
		contents.set(4, 0, ClickableItem.of(DataBase.fileInventory.getbutton("Back"),
                e -> InventoryMenu.INVENTORY.open(player)));
		contents.set(4, 3, ClickableItem.of(DataBase.fileInventory.getbutton("Previous"),
                e -> InventoryItemTable.getInventory().open(player, pagination.previous().getPage())));
        contents.set(4, 4, ClickableItem.empty(InventoryTools.createPageButton(Material.PAPER,"§a - " + (pagination.getPage() + 1) + " - ")));
        contents.set(4, 5, ClickableItem.of(DataBase.fileInventory.getbutton("Next"),
                e -> InventoryItemTable.getInventory().open(player, pagination.next().getPage())));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}

}
