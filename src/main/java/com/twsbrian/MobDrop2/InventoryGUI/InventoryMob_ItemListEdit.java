package com.twsbrian.MobDrop2.InventoryGUI;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.Itemset;
import com.twsbrian.MobDrop2.DataBase.Mob;
import com.twsbrian.MobDrop2.DataBase.MobItem;

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
	
	int quantity_type = 1;
	double chance_type = 1;
	
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
                .size(6, 9)
                .title(ChatColor.BLUE + DataBase.fileInventory.getInventorTitle("Mob_ItemEdit").replaceAll("%mobname%", Mob.getMobName()).replaceAll("%itemname%", MobItem.getItemName()))
                .build();
	}
	
	@Override
	public void init(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		contents.set(5, 0, ClickableItem.of(DataBase.fileInventory.getbutton("Back"),
                e -> InventoryMob_ItemList.getInventory(mob).open(player)));
		contents.set(5, 7, ClickableItem.of(DataBase.fileInventory.getbutton("Save"),
                e -> save(player,contents)));
		
		if (player.hasPermission("mobdrop.admin.inventory.mob.item.remove")) {
        	contents.set(5, 6, ClickableItem.of(InventoryTools.getbutton("Mob_ItemEdit", "Remove"),
                    e -> InventoryMob_itemListRemove.getInventory(mob,mobitem).open(player) ));
        }
		
		//Item form
		ItemForm(player, contents);
		contents.set(2, 2, ClickableItem.empty(mobitem.getResultItem()));
		
		//Chance
		ChanceButton(player, contents);
		
		//Quantity
		QuantityButton(player, contents);
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}
	
	private void ItemForm(Player player, InventoryContents contents) {
		int locals[][] = {{1,1},{1,2},{1,3},
						  {2,1},{2,3},
						  {3,1},{3,2},{3,3}};
		for(int local[] : locals) {
			contents.set(local[0], local[1], ClickableItem.empty(button("ItemForm")));
		}
	}
	
	private void save(Player player, InventoryContents contents) {
		List<String> msg = DataBase.sql.MobItemUpdate(mob,new MobItem(mobitem.getItemNo(), quantity, quantity_max, chance));
		if(msg.isEmpty()) {
			mobitem.Quantity = quantity;
			mobitem.Quantity_max = quantity_max;
			mobitem.Chance = chance;
			InventoryMob_ItemList.getInventory(mob).open(player);
		} else {
			DataBase.sendMessage(player,msg.toString());
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
					    .replaceAll("%quantity_type%", quantity_type + "")
					    .replaceAll("%mode_quantity%", this.mode_quantity ? mode_quantity : mode_quantity_max)
					    .replaceAll("%chance%", chance + "")
					    .replaceAll("%chance_type%", chance_type + ""));
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
	
	private void TypeClick(Player player, InventoryContents contents, InventoryClickEvent ClickEvent, String Mode) {
		Double chance;
		BigDecimal b1 = new BigDecimal(Double.toString(this.chance_type));
		BigDecimal b2 = new BigDecimal("10");
		if(ClickEvent.getClick() == ClickType.LEFT) {
			if(Mode.equals("Chance")) {
				chance = b1.multiply(b2).doubleValue();
				chance_type = chance >= 10 ? 10 : chance;
			} else if(Mode.equals("Quantity")) {
				quantity_type = quantity_type >= 10 ? 100 : quantity_type * 10;
			}
		} else if(ClickEvent.getClick() == ClickType.RIGHT) {
			if(Mode.equals("Chance")) {

				chance = b1.divide(b2).doubleValue();
				chance_type = chance <= 0.01 ? 0.01 : chance;
			} else if(Mode.equals("Quantity")) {
				quantity_type = quantity_type <= 1 ? 1 : quantity_type / 10;
			}
		}
		
		if(Mode.equals("Chance")) {
			ChanceButton(player, contents);
		} else if(Mode.equals("Quantity")) {
			QuantityButton(player, contents);
		}
	}
	
	private void ChanceClick(Player player, InventoryContents contents, InventoryClickEvent ClickEvent, Double type, boolean negative) {
		Double setchance = 999.0;
		BigDecimal b2 = new BigDecimal(Double.toString(type));
		if(!negative) {
			BigDecimal b1 = new BigDecimal(Double.toString(this.chance));
			setchance = b1.add(b2).doubleValue();
		    
		} else {
			BigDecimal b1 = new BigDecimal(Double.toString(this.chance));
			setchance = b1.subtract(b2).doubleValue();
		}
		
		this.chance = setchance > 100 ? this.chance : setchance;
		this.chance = this.chance <= 0 ? 0 : this.chance;
		ChanceButton(player, contents);
	}
	
	private void QuantityMainClick(Player player, InventoryContents contents, InventoryClickEvent ClickEvent) {
		if(ClickEvent.getClick() == ClickType.LEFT) {
			this.quantity_max = (this.quantity_max - this.quantity) <= 0 && this.mode_quantity ? this.quantity : this.quantity_max;
        	this.mode_quantity = this.mode_quantity ? false : true;
		} else if(ClickEvent.getClick() == ClickType.RIGHT) {
			this.quantity_max = 0;
		}
		
		QuantityButton(player, contents);
	}
	
	private void QuantityClick(Player player, InventoryContents contents, InventoryClickEvent ClickEvent, int type, boolean negative) {
		int setQuantity = -1;
		int setQuantity_max = -1;
		if(!negative) {
			if(this.mode_quantity)
				setQuantity = this.quantity + type;
			else
				setQuantity_max = this.quantity_max + type;
		} else {
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
		
		contents.set(1, 5, ClickableItem.of(button("Chance"),
                e -> {}));
		contents.set(1, 6, ClickableItem.of(button("Chance_add"),
                e -> ChanceClick(player, contents, e, chance_type, false)));
		contents.set(1, 7, ClickableItem.of(button("Chance_subtract"),
                e -> ChanceClick(player, contents, e, chance_type, true)));
		contents.set(1, 8, ClickableItem.of(button("Chance_type"),
                e -> TypeClick(player, contents, e, "Chance")));
	}
	
	private void QuantityButton(Player player, InventoryContents contents) {
		
		contents.set(3, 5, ClickableItem.of(button("Quantity"),
                e -> QuantityMainClick(player, contents, e)));
		contents.set(3, 6, ClickableItem.of(button("Quantity_add"),
                e -> QuantityClick(player, contents, e, quantity_type, false)));
		contents.set(3, 7, ClickableItem.of(button("Quantity_subtract"),
                e -> QuantityClick(player, contents, e, quantity_type, true)));
		contents.set(3, 8, ClickableItem.of(button("Quantity_type"),
                e -> TypeClick(player, contents, e, "Quantity")));
	}
}
