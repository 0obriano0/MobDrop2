package com.brian.MobDrop2.InventoryGUI;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.brian.MobDrop2.DataBase.DataBase;
import com.brian.MobDrop2.DataBase.Mob;
import com.brian.MobDrop2.HashMap.HashMapSortMobList;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;

public class InventoryMobs implements InventoryProvider{
	
	Map<String, Mob> mobs;
	String Title = "";
	boolean Custom;
	
	public InventoryMobs(boolean Custom, String Title) {
		this.Custom = Custom;
		this.Title = Title;
		if(Custom)
			mobs = DataBase.CustomMobsMap;
		else
			mobs = DataBase.NormalMobsMap;
	}
	
	public static SmartInventory getInventory(boolean Custom) {
		String Title = "";
		if(Custom)
			Title = DataBase.fileInventory.getString("Inventory.Menu.Button.Custom.Title");
		else
			Title = DataBase.fileInventory.getString("Inventory.Menu.Button.Normal.Title");
		return SmartInventory.builder()
				.provider(new InventoryMobs(Custom,Title))
				.size(5, 9)
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
            items[index] = ClickableItem.of(entry.getValue().getIcon(), e -> InventoryMob_ItemList.getInventory(entry.getValue()).open(player));
        	index++;
        }
        
        pagination.setItems(items);
        pagination.setItemsPerPage(36);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
		
		contents.set(4, 0, ClickableItem.of(DataBase.fileInventory.getbutton("Back"),
                e -> InventoryMenu.INVENTORY.open(player)));
		contents.set(4, 3, ClickableItem.of(DataBase.fileInventory.getbutton("Previous"),
                e -> InventoryMobs.getInventory(Custom).open(player, pagination.previous().getPage())));
        contents.set(4, 4, ClickableItem.empty(InventoryTools.createPageButton(Material.PAPER,"§a - " + (pagination.getPage() + 1) + " - ")));
        contents.set(4, 5, ClickableItem.of(DataBase.fileInventory.getbutton("Next"),
                e -> InventoryMobs.getInventory(Custom).open(player, pagination.next().getPage())));
        
        if (player.hasPermission("mobdrop.admin.inventory.mod.add")) {
        	contents.set(4, 8, ClickableItem.of(InventoryTools.createPageButton(Material.CRAFTING_TABLE,"§a" + DataBase.fileMessage.getString("Inventory.mob_add")),
                e -> InventoryMobAdd.getInventory(new Mob("", Custom ? "Y" : "N")).open(player)));
        }
	}
	
	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
	}
}
