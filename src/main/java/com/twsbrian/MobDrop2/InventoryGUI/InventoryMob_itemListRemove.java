package com.twsbrian.MobDrop2.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.Itemset;
import com.twsbrian.MobDrop2.DataBase.Mob;
import com.twsbrian.MobDrop2.DataBase.MobItem;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class InventoryMob_itemListRemove implements InventoryProvider{
	Mob mob;
	MobItem mobitem;
	
	public InventoryMob_itemListRemove(Mob mob, MobItem mobitem) {
		this.mob = mob;
		this.mobitem = mobitem;
	}

	public static SmartInventory getInventory(Mob Mob, MobItem MobItem) {
        return SmartInventory.builder()
                .provider(new InventoryMob_itemListRemove(Mob, MobItem))
                .size(1, 9)
                .title(ChatColor.BLUE + DataBase.fileInventory.getInventorTitle("Mob_ItemRemove")
											                  .replaceAll("%mobname%", Mob.getMobName())
														      .replaceAll("%itemname%", MobItem.getItemName())
														      .replaceAll("%itemno%", MobItem.getItemNo()))
                .build();
	}
	
	@Override
	public void init(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
		Itemset success = new Itemset(DataBase.fileInventory.getbutton("Success_Remove"));
		List<String> Lore = new ArrayList<String>();
		for(String str : success.getLore()) {
			Lore.add(str.replaceAll("%msg%", DataBase.fileMessage.getString("Inventory.mob_item_remove_msg")
																 .replaceAll("%mobname%", mob.getMobName())
																 .replaceAll("%itemname%", mobitem.getItemName())
																 .replaceAll("%itemno%", mobitem.getItemNo())
																 ));
		}
		success.setLore(Lore);
		
		contents.set(0, 2, ClickableItem.of(success.getItemStack(),
                e -> removemob(player, contents)));
		contents.set(0, 6, ClickableItem.of(DataBase.fileInventory.getbutton("Cancel"),
                e -> InventoryMob_ItemListEdit.getInventory(mob,mobitem).open(player)));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}
	
	private void removemob(Player player, InventoryContents contents) {
		List<String> msg = DataBase.sql.MobItemRemove(mob, mobitem);
		if(msg.isEmpty()) {
			if(mob.isCustom()) DataBase.CustomMobsMap.get(mob.getName()).MobItems.remove(mobitem.getItemNo());
			else DataBase.NormalMobsMap.get(mob.getName()).MobItems.remove(mobitem.getItemNo());
			InventoryMob_ItemList.getInventory(mob).open(player);
		} else {
			DataBase.sendMessage(player,msg.toString());
		}
	}
}
