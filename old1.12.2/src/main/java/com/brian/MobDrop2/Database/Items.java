package com.twsbrian.MobDrop2.Database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class Items{
	public String ItemName;
	//物品名稱
	public boolean UseCustomName;
	// 物品說明
	public List<String> ItemLores;
	//物品名稱(系統名稱)
	public String ItemRealname;
	// 顏色
	public int Red;
	public int Green;
	public int Blue;
	// 物品附魔
	public List<String> Enchants;
	// 隱藏數值
	public List<String> ItemFlags;
	//破壞可否
	public boolean Unbreakable;
	//耐久度
	public short durability;
	
	/**
	 * 頭顱相關設定
	 */
	public boolean head = false;
	public String value;
	
	public Items(String ItemName,List<String> ItemLores,String value) {
		this.head = true;
		this.ItemName = ItemName;
		this.ItemLores = ItemLores;
		this.value = value;
	}
	
	public Items(String ItemName, boolean UseCustomName, String ItemRealname, List<String> ItemLores, int Red, int Green, int Blue,List<String> Enchants,List<String> ItemFlags,boolean Unbreakable,short durability){
		this.ItemName = ItemName;
		this.UseCustomName = UseCustomName;
		this.ItemRealname = ItemRealname;
		this.ItemLores = ItemLores;
		this.Red = Red;
		this.Green = Green;
		this.Blue = Blue;
		this.Enchants = Enchants;
		this.ItemFlags = ItemFlags;
		this.Unbreakable = Unbreakable;
		this.durability = durability;
	}
	
	public ItemStack getResultItem() {
		if(head) {
			return customSkull(value);
		}else {
			// 產生物品用
			ItemStack ResultItem;
		    ItemMeta newItemMeta;
		    LeatherArmorMeta LeatherArmorMeta;
		    
			// 合成後得到的物品設定
		    ResultItem = new ItemStack(Material.getMaterial(ItemRealname));
		    
		    ResultItem.setDurability(durability);
		    
			// 判斷是否要設定顏色
			if(ItemRealname.split("_")[0].equals("LEATHER")) {
				LeatherArmorMeta = (LeatherArmorMeta)ResultItem.getItemMeta();
				LeatherArmorMeta.setColor(Color.fromRGB(this.Red, this.Green, this.Blue));
				ResultItem.setItemMeta(LeatherArmorMeta);
			}
			
			newItemMeta = ResultItem.getItemMeta();
			// 附魔
			for (int i = 0; i < this.Enchants.size(); i++)
			{
				String[] EnchantsParts = this.Enchants.get(i).split(":");
				int level = Integer.parseInt(EnchantsParts[1]);
				Enchantment enchantment = Enchantment.getByName(EnchantsParts[0]);
				newItemMeta.addEnchant(enchantment, level, true);
			}
			// 名稱
			if (!this.UseCustomName)
			{
				newItemMeta.setDisplayName(this.ItemName);
			}
			// 說明
			if (this.ItemLores.size() > 0)
			{
				newItemMeta.setLore(this.ItemLores);
			}
			
			for(String itemflag : ItemFlags)
				newItemMeta.addItemFlags(ItemFlag.valueOf(itemflag));
			
			if(Unbreakable) newItemMeta.setUnbreakable(Unbreakable);
			
			// 寫入資料
			ResultItem.setItemMeta(newItemMeta);
		    // 設定耐久為最高
			ResultItem.setDurability((short)0);
			// 回傳
			return ResultItem;
		}
	}
	
	public ItemStack customSkull(String url)
    {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        if (url.isEmpty()) return head;
       
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
       
        profile.getProperties().put("textures", new Property("textures", url));
       
        try
        {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
           
        }
        catch (IllegalArgumentException|NoSuchFieldException|SecurityException | IllegalAccessException error)
        {
            error.printStackTrace();
        }
        
        headMeta.setDisplayName(ItemName);
        
        if (this.ItemLores.size() > 0){
        	List<String> Lores = new ArrayList<String>();
        	for(String lore : this.ItemLores) {
        		Lores.add(lore.replaceAll("%time%", DataBase.gettime()));
        	}
        	headMeta.setLore(Lores);
		}
        
        head.setItemMeta(headMeta);
        return head;
    }
}
