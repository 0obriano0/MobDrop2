package com.twsbrian.MobDrop2.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.Itemset;
import com.twsbrian.MobDrop2.DataBase.Mob;
import com.twsbrian.MobDrop2.DataBase.MobItem;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class InventoryMob_ItemListAdd implements InventoryProvider{

	Mob mob;
	MobItem mobitem = null;
	
	public InventoryMob_ItemListAdd(Mob mob, MobItem mobitem) {
		this.mob = mob;
		this.mobitem = mobitem;
	}

	public static SmartInventory getInventory(Mob Mob, MobItem MobItem) {
        return SmartInventory.builder()
                .provider(new InventoryMob_ItemListAdd(Mob, MobItem))
                .size(1, 9)
                .title(ChatColor.BLUE + DataBase.fileInventory.getInventorTitle("Mob_ItemAdd"))
                .build();
	}
	
	@Override
	public void init(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		contents.set(0, 0, ClickableItem.of(DataBase.fileInventory.getbutton("Back"),
                e -> InventoryMob_ItemList.getInventory(mob).open(player)));
		contents.set(0, 1, ClickableItem.of(button("Item"),
                e -> InventoryItemTable.getInventory(mob, mobitem).open(player)));
		
		if(mobitem != null) {
			contents.set(0, 2, ClickableItem.of(mobitem.getResultItem(), e -> {}));
		}
		
		LoadCreateButton(player, contents);
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}
	
	private ItemStack button(String name) {
		String itemno = mobitem != null ? mobitem.getItemNo() : "";
		String Type = DataBase.fileInventory.getString("Inventory.Mob_ItemAdd.Button." + name + ".Type").toUpperCase();
		String title = DataBase.fileInventory.getString("Inventory.Mob_ItemAdd.Button." + name + ".Title");
		List<String> Lore = new ArrayList<String>();
		for(String str : DataBase.fileInventory.getStringList("Inventory.Mob_ItemAdd.Button." + name + ".Lore")){
			Lore.add(str.replaceAll("&", "§")
					    .replaceAll("%id%", itemno));
		}
		Material material = null;
		if(Material.getMaterial(Type) != null)
			material = Material.getMaterial(Type);
		else {
			material = Material.BARRIER;
			Lore.add(DataBase.fileMessage.getString("Inventory.Type_error"));
		}
		return new Itemset(material).setItemName(title).setLore(Lore).getItemStack();
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
		List<String> msg = DataBase.sql.MobItemAdd(mob,mobitem);
		if(msg.isEmpty()) {
			mob.MobItems.put(mobitem.getItemNo(),mobitem);
			InventoryMob_ItemListEdit.getInventory(mob,mobitem).open(player);
		} else {
			DataBase.sendMessage(player,msg.toString());
		}
	}
}
