package com.brian.MobDrop2.InventoryGUI;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.brian.MobDrop2.DataBase.DataBase;
import com.brian.MobDrop2.DataBase.Mob;
import com.brian.MobDrop2.DataBase.MobItem;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class InventoryMob_ItemListEdit  implements InventoryProvider{

	Mob mob;
	MobItem mobitem = null;
	
	public InventoryMob_ItemListEdit(Mob mob, MobItem mobitem) {
		this.mob = mob;
		this.mobitem = mobitem;
	}

	public static SmartInventory getInventory(Mob Mob, MobItem MobItem) {
        return SmartInventory.builder()
                .provider(new InventoryMob_ItemListEdit(Mob, MobItem))
                .size(1, 9)
                .title(ChatColor.BLUE + DataBase.fileMessage.getString("Inventory_Title.mob_item_list_Edit").replaceAll("%mobname%", Mob.getMobName()).replaceAll("%itemname%", MobItem.getName()))
                .build();
	}
	
	@Override
	public void init(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		contents.set(0, 0, ClickableItem.of(DataBase.fileInventory.getbutton("Back"),
                e -> InventoryMob_ItemList.getInventory(mob).open(player)));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}

}
