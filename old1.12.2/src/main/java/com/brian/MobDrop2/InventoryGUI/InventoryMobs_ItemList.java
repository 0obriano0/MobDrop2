package com.brian.MobDrop2.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.brian.MobDrop2.Database.DataBase;
import com.brian.MobDrop2.Database.MobItemList;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;

public class InventoryMobs_ItemList implements InventoryProvider{
	
	List<MobItemList> mobItemList;
	String MobName;
	
	public InventoryMobs_ItemList(String mobName, List<MobItemList> mobItemListTable) {
		mobItemList = mobItemListTable;
		MobName = mobName;
	}

	public static SmartInventory getInventory(String MobName , List<MobItemList> MobItemListTable) {
        return SmartInventory.builder()
                .provider(new InventoryMobs_ItemList(MobName, MobItemListTable))
                .size(5, 9)
                .title(ChatColor.GREEN + DataBase.GetEntityName(MobName) + ChatColor.BLUE + " " + DataBase.language.Inventory.dropList)
                .build();
	}

    @Override
    public void init(Player player, InventoryContents contents) {
    	Pagination pagination = contents.pagination();
    	
        ClickableItem[] items = new ClickableItem[mobItemList.size()];
        
        for (int loopnum1 = 0;loopnum1 < mobItemList.size();loopnum1++) {
            items[loopnum1] = ClickableItem.empty(createitem(mobItemList.get(loopnum1)));
        }
        
        pagination.setItems(items);
        pagination.setItemsPerPage(36);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
        
        contents.set(4, 0, ClickableItem.of(InventoryTools.createPageButton(Material.WOOD_DOOR,"§a" + DataBase.language.Inventory.back),
                e -> InventoryMobsList.INVENTORY.open(player)));
        contents.set(4, 3, ClickableItem.of(InventoryTools.createPageButton(Material.ARROW,"§a" + DataBase.language.Inventory.previous),
                e -> InventoryMobs_ItemList.getInventory(MobName,mobItemList).open(player, pagination.previous().getPage())));
        contents.set(4, 4, ClickableItem.empty(InventoryTools.createPageButton(Material.PAPER,"§a - " + (pagination.getPage() + 1) + " - ")));
        contents.set(4, 5, ClickableItem.of(InventoryTools.createPageButton(Material.ARROW,"§a" + DataBase.language.Inventory.next),
                e -> InventoryMobs_ItemList.getInventory(MobName,mobItemList).open(player, pagination.next().getPage())));
    }

	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}
	
	private ItemStack createitem(MobItemList MobItem) {
		ItemStack item = MobItem.Item.getResultItem();
		ItemMeta newItemMeta = item.getItemMeta();
	    
		newItemMeta.setDisplayName(MobItem.Item.ItemName);
		
        List<String> Lore =  newItemMeta.getLore();
        if(Lore == null)
        	Lore = new ArrayList<String>();
        Lore.add("");
        Lore.add("§a" + DataBase.language.Inventory.Item_Chance + "§f " + MobItem.Chance + "%");
        String buffer = "§a" + DataBase.language.Inventory.Item_Quantity + "§f " + MobItem.Quantity;
        if(MobItem.Quantity_max - MobItem.Quantity > 0)
        	buffer = buffer + " ~ " + MobItem.Quantity_max;
        Lore.add(buffer);
        
        newItemMeta.setLore(Lore);
    	item.setItemMeta(newItemMeta);
    	item.setAmount(1);
		return item;
	}
}
