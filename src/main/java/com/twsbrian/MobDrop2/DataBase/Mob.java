package com.twsbrian.MobDrop2.DataBase;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * 怪物 的 實作檔
 * @author Brian
 *
 */
public class Mob implements IMob{
	private String name = "";
	private String custom = "";
	public Map<String,MobItem> MobItems = new HashMap<String,MobItem>();
	private ItemStack icon = null;
	
	public Mob(@Nonnull String name, @Nonnull String custom) {
		this.name = name;
		this.custom = custom;
	}
	
	@Override
	public boolean hasIcon() {
		return icon == null ? false : true;
	}
	
	@Override
	public ItemStack getIcon() {

		if(!isCustom()) {
			if(icon == null) {
				ItemStack itemdate = null; 
				Material f_item = null;
				Material head = Material.getMaterial(name.toUpperCase() + "_HEAD");
	        	Material spawn_egg = Material.getMaterial(name.toUpperCase() + "_SPAWN_EGG");
	        	Material item = Material.getMaterial(name.toUpperCase());
	        	
	        	f_item = head;
	        	if(f_item == null) f_item = spawn_egg;
	        	if(f_item == null) f_item = item;
	        	if(f_item == null) {
	        		Itemset itemset = new Itemset(Material.BARRIER);
	        		itemset.setItemName(getMobName());
	        		itemset.setLore(DataBase.fileMessage.getString("Inventory.Material_not_found"));
	        		itemdate = itemset.getItemStack();
	        	} else itemdate = new ItemStack(f_item);
	        	
	        	return new Itemset(itemdate).setItemName(this.getMobName()).getItemStack();
			}
        	return icon;
		} else {
			if(icon == null) {
				Itemset itemset = new Itemset(Material.BARRIER);
				itemset.setItemName(getMobName());
        		itemset.setLore(DataBase.fileMessage.getString("Inventory.Material_not_found"));
				return itemset.getItemStack();
			}
			return icon;
		}

	}
	
	@Override
	public void setIcon(ItemStack icon) {
		if(icon != null) this.icon = icon;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getMobName() {
		if(isCustom()) {
			return name;
		} else {
			return DataBase.fileMessage.GetEntityName(name);
		}
	}
	
	@Override
	public String getCustom() {
		return isCustom() ? "Y" : "N";
	}
	
	@Override
	public boolean isCustom() {
		return this.custom.toUpperCase().equals("Y");
	}
	
}
