package com.twsbrian.MobDrop2.InventoryGUI;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.Mob;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class InventoryMobEdit implements InventoryProvider{

	Mob mob;
	
	public InventoryMobEdit(Mob mob) {
		this.mob = mob;
	}

	public static SmartInventory getInventory(Mob Mob) {
        return SmartInventory.builder()
                .provider(new InventoryMobEdit(Mob))
                .size(1, 9)
                .title(ChatColor.BLUE + DataBase.fileInventory.getInventorTitle("MobEdit").replaceAll("%mobname%", Mob.getMobName()))
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
