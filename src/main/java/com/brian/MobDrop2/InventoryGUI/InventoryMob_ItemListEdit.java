package com.brian.MobDrop2.InventoryGUI;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.brian.MobDrop2.DataBase.DataBase;
import com.brian.MobDrop2.DataBase.Itemset;
import com.brian.MobDrop2.DataBase.Mob;
import com.brian.MobDrop2.DataBase.MobItem;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class InventoryMob_ItemListEdit  implements InventoryProvider{

	Mob mob;
	MobItem mobitem = null;
	int quantity = 0;
	int quantity_max = 0;
	double chance = 0;
	boolean mode_quantity = true;
	
	public InventoryMob_ItemListEdit(Mob mob, MobItem mobitem) {
		this.mob = mob;
		this.mobitem = mobitem;
		
		this.quantity = mobitem.Quantity;
		this.quantity_max = mobitem.Quantity_max;
		this.chance = mobitem.Chance;
	}

	public static SmartInventory getInventory(Mob Mob, MobItem MobItem) {
        return SmartInventory.builder()
                .provider(new InventoryMob_ItemListEdit(Mob, MobItem))
                .size(5, 9)
                .title(ChatColor.BLUE + DataBase.fileMessage.getString("Inventory_Title.mob_item_list_Edit").replaceAll("%mobname%", Mob.getMobName()).replaceAll("%itemname%", MobItem.getItemName()))
                .build();
	}
	
	@Override
	public void init(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		contents.set(4, 0, ClickableItem.of(DataBase.fileInventory.getbutton("Back"),
                e -> InventoryMob_ItemList.getInventory(mob).open(player)));
		contents.set(4, 1, ClickableItem.of(DataBase.fileInventory.getbutton("Save"),
                e -> save(player,contents)));
		
		contents.set(2, 1, ClickableItem.empty(mobitem.getResultItem()));
		
		//Chance
		ChanceButton(player, contents);
		
		//Quantity
		QuantityButton(player, contents);
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}
	
	private void save(Player player, InventoryContents contents) {
		List<String> msg = DataBase.sql.MobItemUpdate(mob,new MobItem(mobitem.getItemNo(), quantity, quantity_max, chance));
		if(msg.isEmpty()) {
			mobitem.Quantity = quantity;
			mobitem.Quantity_max = quantity_max;
			mobitem.Chance = chance;
			InventoryMob_ItemList.getInventory(mob).open(player);
		} else {
			player.sendMessage(msg.toString());
		}
	}
	
	private ItemStack button(String name) {
		String Type = DataBase.fileInventory.getString("Inventory.Mob_ItemEdit.Button." + name + ".Type").toUpperCase();
		String title = DataBase.fileInventory.getString("Inventory.Mob_ItemEdit.Button." + name + ".Title");
		
		String quantity = DataBase.fileInventory.getString("Inventory.Mob_ItemEdit.Type.quantity").replaceAll("%quantity%", this.quantity + "");
		if(this.quantity_max - this.quantity > 0) {
			quantity = quantity + DataBase.fileInventory.getString("Inventory.Mob_ItemEdit.Type.quantity_max").replaceAll("%quantity_max%", this.quantity_max + "");
		}
		
		String mode_quantity = DataBase.fileInventory.getString("Inventory.Mob_ItemEdit.Type.mode_quantity");
		String mode_quantity_max = DataBase.fileInventory.getString("Inventory.Mob_ItemEdit.Type.mode_quantity_max");
		
		List<String> Lore = new ArrayList<String>();
		for(String str : DataBase.fileInventory.getStringList("Inventory.Mob_ItemEdit.Button." + name + ".Lore")){
			Lore.add(str.replaceAll("&", "§")
					    .replaceAll("%quantity%", quantity + "")
					    .replaceAll("%mode_quantity%", this.mode_quantity ? mode_quantity : mode_quantity_max)
					    .replaceAll("%chance%", chance + ""));
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
	
	private void ChanceClick(Player player, InventoryContents contents, InventoryClickEvent ClickEvent, Double type) {
		Double setchance = 999.0;
		BigDecimal b2 = new BigDecimal(Double.toString(type));
		if(ClickEvent.getClick() == ClickType.LEFT) {
			BigDecimal b1 = new BigDecimal(Double.toString(this.chance));
			setchance = b1.add(b2).doubleValue();
		    
		} else if(ClickEvent.getClick() == ClickType.RIGHT) {
			BigDecimal b1 = new BigDecimal(Double.toString(this.chance));
			setchance = b1.subtract(b2).doubleValue();
		}
		
		this.chance = setchance > 100 ? this.chance : setchance;
		ChanceButton(player, contents);
	}
	
	private void QuantityClick(Player player, InventoryContents contents, InventoryClickEvent ClickEvent, int type) {
		int setQuantity = -1;
		int setQuantity_max = -1;
		if(ClickEvent.getClick() == ClickType.LEFT) {
			if(this.mode_quantity)
				setQuantity = this.quantity + type;
			else
				setQuantity_max = this.quantity_max + type;
		} else if(ClickEvent.getClick() == ClickType.RIGHT) {
			if(this.mode_quantity)
				setQuantity = this.quantity - type;
			else
				setQuantity_max = this.quantity_max - type;
		}
		
		this.quantity = setQuantity <= 0 ? this.quantity : setQuantity;
		this.quantity_max = setQuantity_max <= 0 ? this.quantity_max : setQuantity_max;
		
		if(this.quantity_max - this.quantity <= 0) {
			this.quantity_max = 0;
		}
		QuantityButton(player, contents);
	}
	
	private void ChanceButton(Player player, InventoryContents contents) {
		contents.set(0, 5, ClickableItem.of(button("Chance_10"),
                e -> ChanceClick(player, contents, e, (double) 10)));
		contents.set(1, 5, ClickableItem.of(button("Chance_1"),
                e -> ChanceClick(player, contents, e, (double) 1)));
		contents.set(2, 5, ClickableItem.of(button("Chance"),
                e -> {}));
		contents.set(3, 5, ClickableItem.of(button("Chance_0_1"),
                e -> ChanceClick(player, contents, e, (double) 0.1)));
		contents.set(4, 5, ClickableItem.of(button("Chance_0_01"),
                e -> ChanceClick(player, contents, e, (double) 0.01)));
	}
	
	private void QuantityButton(Player player, InventoryContents contents) {
		contents.set(0, 7, ClickableItem.of(button("Quantity_max_cancel"),
                e -> {
                	this.quantity_max = 0;
                	QuantityButton(player, contents);
                }));
		contents.set(1, 7, ClickableItem.of(button("Quantity_1"),
                e -> QuantityClick(player, contents, e, 1)));
		contents.set(2, 7, ClickableItem.of(button("Quantity"),
                e -> {
                	this.quantity_max = (this.quantity_max - this.quantity) <= 0 && this.mode_quantity ? this.quantity : this.quantity_max;
                	this.mode_quantity = this.mode_quantity ? false : true;
                	QuantityButton(player, contents);
                }));
		contents.set(3, 7, ClickableItem.of(button("Quantity_10"),
                e -> QuantityClick(player, contents, e, 10)));
		contents.set(4, 7, ClickableItem.of(button("Quantity_100"),
                e -> QuantityClick(player, contents, e, 100)));
	}
}
