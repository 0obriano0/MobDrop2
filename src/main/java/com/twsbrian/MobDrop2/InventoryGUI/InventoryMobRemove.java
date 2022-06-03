package com.twsbrian.MobDrop2.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.Itemset;
import com.twsbrian.MobDrop2.DataBase.Mob;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class InventoryMobRemove implements InventoryProvider{
	Mob mob;
	
	public InventoryMobRemove(Mob mob) {
		this.mob = mob;
	}

	public static SmartInventory getInventory(Mob Mob) {
        return SmartInventory.builder()
                .provider(new InventoryMobRemove(Mob))
                .size(1, 9)
                .title(ChatColor.BLUE + DataBase.fileInventory.getInventorTitle("MobRemove").replaceAll("%mobname%", Mob.getMobName()))
                .build();
	}
	
	@Override
	public void init(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
		Itemset success = new Itemset(DataBase.fileInventory.getbutton("Success_Remove"));
		List<String> Lore = new ArrayList<String>();
		for(String str : success.getLore()) {
			Lore.add(str.replaceAll("%msg%", DataBase.fileMessage.getString("Inventory.mob_remove_msg").replaceAll("%mobname%", mob.getMobName())));
		}
		success.setLore(Lore);
		
		contents.set(0, 2, ClickableItem.of(success.getItemStack(),
                e -> removemob(player, contents)));
		contents.set(0, 6, ClickableItem.of(DataBase.fileInventory.getbutton("Cancel"),
                e -> InventoryMob_ItemList.getInventory(mob).open(player)));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}
	
	private void removemob(Player player, InventoryContents contents) {
		List<String> msg = DataBase.sql.MobRemove(mob);
		if(msg.isEmpty()) {
			if(mob.isCustom()) DataBase.CustomMobsMap.remove(mob.getName());
			else DataBase.NormalMobsMap.remove(mob.getName());
			InventoryMobs.getInventory(mob.isCustom()).open(player);
		} else {
			DataBase.sendMessage(player,msg.toString());
		}
	}
}
