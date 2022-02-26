package com.brian.MobDrop2.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.brian.MobDrop2.DataBase.DataBase;
import com.brian.MobDrop2.DataBase.Itemset;
import com.brian.MobDrop2.DataBase.Mob;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class InventoryMobAdd implements InventoryProvider{

	Mob mob;
	
	public InventoryMobAdd(Mob mob) {
		this.mob = mob;
	}
	
	public static SmartInventory getInventory(Mob mob) {
		return SmartInventory.builder()
				.provider(new InventoryMobAdd(mob))
				.size(1, 9)
				.title(ChatColor.BLUE + DataBase.fileInventory.getString("Inventory.MobAdd.Title"))
				.build();
	}
	
	@Override
	public void init(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		contents.set(0, 0, ClickableItem.of(DataBase.fileInventory.getbutton("Back"),
                e -> InventoryMobs.getInventory(mob.isCustom()).open(player)));
		
		if(!mob.isCustom()) {
			contents.set(0, 1, ClickableItem.of(button("getEntityType"),
	                e -> InventoryNormalMobs.getInventory(mob).open(player)));
			if(!mob.getName().isEmpty()) {
				contents.set(0, 2, ClickableItem.of(mob.getIcon(),
		                e -> {}));
			}
		} else {
			contents.set(0, 1, ClickableItem.of(button("Name"),
	                e -> {}));
		}
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}
	
	private ItemStack button(String name) {
		String Type = DataBase.fileInventory.getString("Inventory.MobAdd.Button." + name + ".Type").toUpperCase();
		String title = DataBase.fileInventory.getString("Inventory.MobAdd.Button." + name + ".Title");
		List<String> Lore = new ArrayList<String>();
		for(String str : DataBase.fileInventory.getStringList("Inventory.MobAdd.Button." + name + ".Lore")){
			Lore.add(str.replaceAll("&", "§")
					    .replaceAll("%name%", mob.getMobName() + "")
					    .replaceAll("%id%", mob.getName() + ""));
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

}
