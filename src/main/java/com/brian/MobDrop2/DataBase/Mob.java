package com.brian.MobDrop2.DataBase;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Mob {
	private String name = "";
	private String custom = "";
	public List<MobItem> MobItemList = new ArrayList<MobItem>();
	private ItemStack icon = null;
	
	public Mob(@Nonnull String name, @Nonnull String custom) {
		this.name = name;
		this.custom = custom;
	}
	
	public boolean hasIcon() {
		return icon == null ? false : true;
	}
	
	public ItemStack getIcon() {
		if(icon == null) {
			if(!isCustom()) {
				Material f_item = null;
				Material head = Material.getMaterial(name.toUpperCase() + "_HEAD");
	        	Material spawn_egg = Material.getMaterial(name.toUpperCase() + "_SPAWN_EGG");
	        	Material item = Material.getMaterial(name.toUpperCase());
	        	f_item = head;
	        	if(f_item == null) f_item = spawn_egg;
	        	if(f_item == null) f_item = item;
	        	if(f_item == null) f_item = Material.BARRIER;
	        	return new ItemStack(f_item);
			} else {
				return new ItemStack(Material.BARRIER);
			}
		}
		return icon;
	}
	
	public void setIcon(ItemStack icon) {
		if(icon != null) this.icon = icon;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMobName() {
		if(isCustom()) {
			return name;
		} else {
			return DataBase.fileMessage.GetEntityName(name);
		}
	}
	
	public String getCustom() {
		return custom;
	}
	
	public boolean isCustom() {
		return this.custom.toUpperCase().equals("Y");
	}
	
}
