package com.brian.MobDrop2.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.brian.MobDrop2.DataBase.DataBase;
import com.brian.MobDrop2.DataBase.Itemset;
import com.brian.MobDrop2.DataBase.Mob;
import com.brian.MobDrop2.DataBase.MobItem;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class InventoryMob_ItemListAdd implements InventoryProvider{

	Mob mob;
	MobItem mobitem = null;
	
	public InventoryMob_ItemListAdd(Mob mob) {
		this.mob = mob;
	}

	public static SmartInventory getInventory(Mob Mob) {
        return SmartInventory.builder()
                .provider(new InventoryMob_ItemListAdd(Mob))
                .size(1, 9)
                .title(ChatColor.BLUE + DataBase.fileMessage.getString("Inventory_Title.mob_item_list_add"))
                .build();
	}
	
	@Override
	public void init(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		contents.set(0, 0, ClickableItem.of(DataBase.fileInventory.getbutton("Back"),
                e -> InventoryMob_ItemList.getInventory(mob).open(player)));
		contents.set(0, 1, ClickableItem.of(InventoryTools.getbutton("Mob_ItemAdd", "Item"),
                e -> {}));
		
		LoadCreateButton(player, contents);
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}
	
	private void LoadCreateButton(Player player, InventoryContents contents) {
		ItemStack item;
		List<String> errmsg = new ArrayList<String>();
		
		if(mobitem != null) {
			if(mob.MobItems.containsKey(mobitem.getItemNo())) {
				errmsg.add(DataBase.fileMessage.getString("Inventory.mob_item_same_error"));
			}
		} else {
			errmsg.add(DataBase.fileMessage.getString("Inventory.mob_item_noset_error"));
		}
		
		if(errmsg.isEmpty()) {
			item = DataBase.fileInventory.getbutton("Create");
			contents.set(0, 8, ClickableItem.of(item, e -> createitem(player, contents)));
		} else {
			Itemset itemset = new Itemset(DataBase.fileInventory.getbutton("Error_Create"));
			List<String> Lore = new ArrayList<String>();
			for(String str : itemset.getLore()) {
				if(str.contains("%error%")) {
					for(String errstr : errmsg) {
						Lore.add(errstr);
					}
				} else {
					Lore.add(str);
				}
			}
			item = itemset.setLore(Lore).setAmount(1).getItemStack();
			contents.set(0, 8, ClickableItem.of(item, e -> {}));
		}
	}
	
	private void createitem(Player player, InventoryContents contents) {
		List<String> msg = DataBase.sql.MobAdd(mob);
		if(msg.isEmpty()) {
			mob.MobItems.put(mobitem.getItemNo(),mobitem);
			if(mob.isCustom()) DataBase.CustomMobsMap.put(mob.getName(), mob);
			else DataBase.NormalMobsMap.put(mob.getName(), mob);
//			InventoryMob_ItemList.getInventory(mob).open(player);
		} else {
			player.sendMessage(msg.toString());
		}
	}
}
