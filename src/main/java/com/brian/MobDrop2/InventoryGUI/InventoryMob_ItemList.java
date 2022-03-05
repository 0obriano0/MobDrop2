package com.brian.MobDrop2.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.brian.MobDrop2.DataBase.DataBase;
import com.brian.MobDrop2.DataBase.Mob;
import com.brian.MobDrop2.DataBase.MobItem;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;

public class InventoryMob_ItemList implements InventoryProvider{
	
	Mob mob;
	
	public InventoryMob_ItemList(Mob mob) {
		this.mob = mob;
	}

	public static SmartInventory getInventory(Mob Mob) {
        return SmartInventory.builder()
                .provider(new InventoryMob_ItemList(Mob))
                .size(5, 9)
                .title(ChatColor.BLUE + DataBase.fileMessage.getString("Inventory_Title.mob_item_list").replaceAll("%mobname%", Mob.getMobName()))
                .build();
	}

    @Override
    public void init(Player player, InventoryContents contents) {
    	Pagination pagination = contents.pagination();
    	
    	ClickableItem[] items = new ClickableItem[mob.MobItemList.size()];
        
    	int index = 0;
    	
    	for(MobItem item : mob.MobItemList) {
    		items[index] = ClickableItem.empty(createbutton(item));
    		index++;
    	}
        
        pagination.setItems(items);
        pagination.setItemsPerPage(36);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
        
        contents.set(4, 0, ClickableItem.of(DataBase.fileInventory.getbutton("Back"),
                e -> InventoryMobs.getInventory(mob.isCustom()).open(player)));
        contents.set(4, 3, ClickableItem.of(DataBase.fileInventory.getbutton("Previous"),
                e -> InventoryMob_ItemList.getInventory(mob).open(player, pagination.previous().getPage())));
        contents.set(4, 4, ClickableItem.empty(InventoryTools.createPageButton(Material.PAPER,"§a - " + (pagination.getPage() + 1) + " - ")));
        contents.set(4, 5, ClickableItem.of(DataBase.fileInventory.getbutton("Next"),
                e -> InventoryMob_ItemList.getInventory(mob).open(player, pagination.next().getPage())));
        
        if (player.hasPermission("mobdrop.admin.inventory.mob.remove")) {
        	contents.set(4, 6, ClickableItem.of(InventoryTools.getbutton("Mob_Item_List", "MobRemove"),
                    e -> InventoryMobRemove.getInventory(mob).open(player) ));
        }
        if (player.hasPermission("mobdrop.admin.inventory.mob.edit")) {
        	contents.set(4, 7, ClickableItem.of(InventoryTools.getbutton("Mob_Item_List", "MobEdit"),
                e -> InventoryMobEdit.getInventory(mob).open(player)));
        }
        if (player.hasPermission("mobdrop.admin.inventory.mob.item.add")) {
        	contents.set(4, 8, ClickableItem.of(InventoryTools.getbutton("Mob_Item_List", "ItemAdd"),
                e -> InventoryMob_ItemListAdd.getInventory(mob).open(player)));
        }
        
    }

	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}
	
	private ItemStack createbutton(MobItem MobItem) {
		ItemStack item = MobItem.getResultItem();
		ItemMeta newItemMeta = item.getItemMeta();
		
        List<String> Lore = newItemMeta.getLore();
        if(Lore == null)
        	Lore = new ArrayList<String>();
        
        String Quantity = "§f " + MobItem.Quantity;
        if(MobItem.Quantity_max - MobItem.Quantity > 0)
        	Quantity = Quantity + " ~ " + MobItem.Quantity_max;
        
        for(String str : DataBase.fileMessage.getStringList("Inventory.dropitem_info_lore")) {
        	Lore.add(str.replaceAll("%Chance%", "§f " + MobItem.Chance)
        			    .replaceAll("%Quantity%", Quantity));
        }
        
        newItemMeta.setLore(Lore);
    	item.setItemMeta(newItemMeta);
    	item.setAmount(1);
		return item;
	}
}
