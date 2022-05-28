package com.twsbrian.MobDrop2.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.twsbrian.MobDrop2.MobDrop2;
import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.Itemset;
import com.twsbrian.MobDrop2.DataBase.MessageSet;
import com.twsbrian.MobDrop2.DataBase.Mob;
import com.twsbrian.MobDrop2.Timer.CustomMobNameTimer;

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
				.title(ChatColor.BLUE + DataBase.fileInventory.getInventorTitle("MobAdd"))
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
		} else {
			contents.set(0, 1, ClickableItem.of(button("Name"),
	                e -> setNewCustomMobName(player)));
		}
		
		if(!(!mob.isCustom() && mob.getName().isEmpty())) {
			contents.set(0, 2, ClickableItem.of(new Itemset(mob.getIcon()).addLore(DataBase.fileInventory.getStringList("Inventory.MobAdd.Button.Icon.Lore")).getItemStack(),
	                e -> ChangeItem(player, contents, 0, 2)));
		}
		
		LoadCreateButton(player,contents);
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

	private void ChangeItem(Player player ,InventoryContents contents, int x, int y) {
		Itemset setitem = new Itemset(player.getItemOnCursor().clone()).setAmount(1);
		if (!setitem.getItemStack().getType().toString().equals("AIR")) {
			mob.setIcon(setitem.getItemStack());
			contents.set(x,y, ClickableItem.of(setitem.addLore(DataBase.fileInventory.getStringList("Inventory.MobAdd.Button.Icon.Lore")).getItemStack(),
					e -> ChangeItem(player,contents,x,y)));
			LoadCreateButton(player, contents);
		}else {
//			DataBase.sendMessage(player,"");
		}
	}
	
	private void LoadCreateButton(Player player, InventoryContents contents) {
		ItemStack item;
		List<String> errmsg = new ArrayList<String>();
		if(mob.isCustom()) {
			if(mob.getMobName().isEmpty()) {
				errmsg.add(DataBase.fileMessage.getString("Inventory.mob_name_noset_error"));
			}
			if(DataBase.CustomMobsMap.containsKey(mob.getName())) {
				errmsg.add(DataBase.fileMessage.getString("Inventory.mob_name_same_error"));
			}
		} else {
			if(DataBase.NormalMobsMap.containsKey(mob.getName())) {
				errmsg.add(DataBase.fileMessage.getString("Inventory.mob_normal_same_select"));
			}
			if(mob.getName().isEmpty()) {
				errmsg.add(DataBase.fileMessage.getString("Inventory.mob_normal_no_select"));
			}
		}
		
		if(errmsg.isEmpty()) {
			item = DataBase.fileInventory.getbutton("Create");
			contents.set(0, 8, ClickableItem.of(item, e -> createmob(player, contents)));
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
	
	private void createmob(Player player, InventoryContents contents) {
		List<String> msg = DataBase.sql.MobAdd(mob);
		if(msg.isEmpty()) {
			if(mob.isCustom()) DataBase.CustomMobsMap.put(mob.getName(), mob);
			else DataBase.NormalMobsMap.put(mob.getName(), mob);
			InventoryMob_ItemList.getInventory(mob).open(player);
		} else {
			DataBase.sendMessage(player,msg.toString());
		}
	}
	
	private void setNewCustomMobName(Player player) {
		DataBase.NewCustomMobName.put(player.getName(),new MessageSet(mob,(Long)System.currentTimeMillis()));
		//開啟倒數計時
		new CustomMobNameTimer(player);
		int timeout = MobDrop2.plugin.getConfig().getInt("Inventory.MessageSet.CustomName");
		DataBase.sendMessage(player,DataBase.fileMessage.getString("Timer.TimeStart").replace("%time%", timeout + ""));
		InventoryMobAdd.getInventory(mob).close(player);
	}
}
