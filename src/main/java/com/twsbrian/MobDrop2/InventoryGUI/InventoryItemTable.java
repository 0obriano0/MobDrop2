package com.twsbrian.MobDrop2.InventoryGUI;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.Itemset;
import com.twsbrian.MobDrop2.DataBase.Mob;
import com.twsbrian.MobDrop2.DataBase.MobItem;
import com.twsbrian.MobDrop2.HashMap.HashMapSortItemset;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;

public class InventoryItemTable  implements InventoryProvider{
	
	Mob mob;
	MobItem mobitem;
	
	public InventoryItemTable() {
		this.mob = null;
	}
	
	public InventoryItemTable(Mob mob, MobItem mobitem) {
		this.mob = mob;
		this.mobitem = mobitem;
	}
	
	public static SmartInventory getInventory() {
        return SmartInventory.builder()
                .provider(new InventoryItemTable())
                .size(6, 9)
                .title(ChatColor.BLUE + DataBase.fileInventory.getInventorTitle("Items"))
                .build();
	}
	
	public static SmartInventory getInventory(Mob Mob, MobItem MobItem) {
        return SmartInventory.builder()
                .provider(new InventoryItemTable(Mob, MobItem))
                .size(6, 9)
                .title(ChatColor.BLUE + DataBase.fileInventory.getInventorTitle("ItemsListMob").replaceAll("%mobname%", Mob.getMobName()))
                .build();
	}
	
	@Override
	public void init(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
		Pagination pagination = contents.pagination();
		
		
		Map<String, Itemset> dbitems = new HashMap<String, Itemset>();
		dbitems.putAll(DataBase.items);
        if(mob != null) {
        	for(Entry<String, MobItem> entry : mob.MobItems.entrySet()) {
        		dbitems.remove(entry.getKey());
        	}
        }
        
        HashMapSortItemset ItemList = new HashMapSortItemset((HashMap<String, Itemset>) dbitems);

        ClickableItem[] items = new ClickableItem[dbitems.size()];
        int index = 0;
        for (Map.Entry<String, Itemset> entry:ItemList.list_Data) {
        	Itemset item = new Itemset(entry.getValue().getItemStack().clone());
        	if (player.hasPermission("mobdrop.admin.inventory.items.get") && mob == null) {
        		item.addLore(DataBase.fileMessage.getStringList("Inventory.items_table_get_item_info"));
        	}
            items[index] = ClickableItem.of(item.getItemStack(), e -> selectitem(entry.getKey(),player));
        	index++;
        }
        
        pagination.setItems(items);
        pagination.setItemsPerPage(45);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
		if(mob == null) {
			contents.set(5, 0, ClickableItem.of(DataBase.fileInventory.getbutton("Back"),
	                e -> InventoryMenu.INVENTORY.open(player)));
		} else {
			contents.set(5, 0, ClickableItem.of(DataBase.fileInventory.getbutton("Back"),
	                e -> InventoryMob_ItemListAdd.getInventory(mob, mobitem).open(player)));
		}
		
		contents.set(5, 3, ClickableItem.of(DataBase.fileInventory.getbutton("Previous"),
                e -> InventoryItemTable.getInventory().open(player, pagination.previous().getPage())));
        contents.set(5, 4, ClickableItem.empty(InventoryTools.createPageButton(Material.PAPER,"§a - " + (pagination.getPage() + 1) + " - ")));
        contents.set(5, 5, ClickableItem.of(DataBase.fileInventory.getbutton("Next"),
                e -> InventoryItemTable.getInventory().open(player, pagination.next().getPage())));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}
	
	private void selectitem(String itemno, Player player) {
		if(mob != null) {
			InventoryMob_ItemListAdd.getInventory(mob, new MobItem(itemno)).open(player);
		} else if (player.hasPermission("mobdrop.admin.inventory.items.get")) {
			ItemStack Itemcreate = DataBase.items.get(itemno).getItemStack();
			Itemcreate.setAmount(1);
			if(player.getInventory().firstEmpty() == -1) {
				DataBase.sendMessage(player,"§b" + DataBase.fileMessage.getString("Inventory.items_table_get_item_bag_full"));
			} else {
				player.getInventory().addItem(Itemcreate);
				DataBase.sendMessage(player,"§b" + DataBase.fileMessage.getString("Inventory.items_table_get_item_success")
											.replaceAll("%id%", itemno)
											.replaceAll("%name%", new Itemset(Itemcreate).getItemName()));
			}
		}
	}

}
