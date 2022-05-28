package com.twsbrian.MobDrop2.InventoryGUI;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.Itemset;
import com.twsbrian.MobDrop2.DataBase.Mob;
import com.twsbrian.MobDrop2.HashMap.HashMapSortMobList;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;

public class InventoryMobs implements InventoryProvider{
	
	Map<String, Mob> mobs;
	boolean Custom;
	
	public InventoryMobs(boolean Custom) {
		this.Custom = Custom;
		if(Custom)
			mobs = DataBase.CustomMobsMap;
		else
			mobs = DataBase.NormalMobsMap;
	}
	
	public static SmartInventory getInventory(boolean Custom) {
		String Title = "";
		if(Custom)
			Title = DataBase.fileInventory.getString("Inventory.Mob_List.Title.Custom");
		else
			Title = DataBase.fileInventory.getString("Inventory.Mob_List.Title.Normal");
		return SmartInventory.builder()
				.provider(new InventoryMobs(Custom))
				.size(6, 9)
				.title(ChatColor.BLUE + Title)
				.build();
	}
	
	@Override
    public void init(Player player, InventoryContents contents) {
		Pagination pagination = contents.pagination();
		
		ClickableItem[] items = new ClickableItem[mobs.size()];
        int index = 0;
        HashMapSortMobList ItemList = new HashMapSortMobList((HashMap<String, Mob>) mobs);
        
        for (Map.Entry<String, Mob> entry:ItemList.list_Data) {
        	Itemset icon = new Itemset(entry.getValue().getIcon());
        	for(String Lore:DataBase.fileMessage.getStringList("Inventory.mob_info_lore")) {
        		icon.addLore(Lore.replaceAll("%num%", entry.getValue().MobItems.size() + ""));
        	}
            items[index] = ClickableItem.of(icon.getItemStack(), e -> InventoryMob_ItemList.getInventory(entry.getValue()).open(player));
        	index++;
        }
        
        pagination.setItems(items);
        pagination.setItemsPerPage(45);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
		
		contents.set(5, 0, ClickableItem.of(DataBase.fileInventory.getbutton("Back"),
                e -> InventoryMenu.INVENTORY.open(player)));
		contents.set(5, 3, ClickableItem.of(DataBase.fileInventory.getbutton("Previous"),
                e -> InventoryMobs.getInventory(Custom).open(player, pagination.previous().getPage())));
        contents.set(5, 4, ClickableItem.empty(InventoryTools.createPageButton(Material.PAPER,"§a - " + (pagination.getPage() + 1) + " - ")));
        contents.set(5, 5, ClickableItem.of(DataBase.fileInventory.getbutton("Next"),
                e -> InventoryMobs.getInventory(Custom).open(player, pagination.next().getPage())));
        
        if (player.hasPermission("mobdrop.admin.inventory.mod.add")) {
        	contents.set(5, 8, ClickableItem.of(InventoryTools.getbutton("Mob_List", "MobAdd"),
                e -> InventoryMobAdd.getInventory(new Mob("", Custom ? "Y" : "N")).open(player)));
        }
	}
	
	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
	}
}
