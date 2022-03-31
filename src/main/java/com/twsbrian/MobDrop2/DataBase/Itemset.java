package com.twsbrian.MobDrop2.DataBase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

/**
 * 物品創建 的 實作檔
 * @author Brian
 *
 */
public class Itemset implements IItemset {
	private ItemStack Item;
	private int mysqlNo = -1;
	
	public int getMysqlNo() {
		return mysqlNo;
	}

	public void setMysqlNo(int mysqlNo) {
		this.mysqlNo = mysqlNo;
	}

	public Itemset(@Nonnull Material MaterialType) {
		Item = new ItemStack(MaterialType);
	}

	public Itemset(@Nonnull ItemStack item) {
		Item = item.clone();
	}
	
	/**
	 * Base64 轉 物品 Loading
	 * @param Base64
	 */
	public Itemset(@Nonnull String Base64) {
		this.itemStackFromBase64(Base64);
	}
	
	@Override
	public ItemMeta getItemMeta() {
		return Item.getItemMeta();
	}

	@Override
	public ItemStack getItemStack() {
		return Item.clone();
	}

	@Override
	public Itemset setItemName(@Nonnull String Name) {
		ItemMeta newItemMeta = getItemMeta();
		newItemMeta.setDisplayName(Name);
		Item.setItemMeta(newItemMeta);
		return this;
	}

	@Override
	public String getItemName() {
		if (Item.hasItemMeta())
			return Item.getItemMeta().getDisplayName();
		else
			return Item.getType().name();
	}

	@Override
	public Itemset setDurability(short Durability) {
		Damageable meta = (Damageable) getItemMeta();
		meta.setDamage(Durability);
		Item.setItemMeta((ItemMeta) meta);
		//Item.setDurability(Durability);
		return this;
	}
	
	@Override
	public int getDurability() {
		Damageable meta = (Damageable) getItemMeta();
		return meta.getDamage();
	}

	@Override
	public List<String> getLore() {
		ItemMeta newItemMeta = getItemMeta();
		List<String> lore_list = new ArrayList<String>();
		if(newItemMeta.hasLore()) lore_list = newItemMeta.getLore();
		return lore_list;
	}
	
	@Override
	public Itemset setLore(String lore) {
		ItemMeta newItemMeta = getItemMeta();
		List<String> lore_list = new ArrayList<String>();
		lore_list.add("");
		lore_list.add(lore);
		newItemMeta.setLore(lore_list);
		Item.setItemMeta(newItemMeta);
		return this;
	}

	@Override
	public Itemset setLore(List<String> lore) {
		ItemMeta newItemMeta = getItemMeta();
		newItemMeta.setLore(lore);
		Item.setItemMeta(newItemMeta);
		return this;
	}
	
	@Override
	public Itemset addLore(String lore) {
		ItemMeta newItemMeta = getItemMeta();
		List<String> lore_list = new ArrayList<String>();
		if(newItemMeta.hasLore()) lore_list = newItemMeta.getLore();

		lore_list.add(lore);
		
		newItemMeta.setLore(lore_list);
		Item.setItemMeta(newItemMeta);
		return this;
	}
	
	@Override
	public Itemset addLore(List<String> lore) {
		ItemMeta newItemMeta = getItemMeta();
		List<String> lore_list = new ArrayList<String>();
		if(newItemMeta.hasLore()) lore_list = newItemMeta.getLore();
		
		for(String str : lore) {
			lore_list.add(str);
		}
		
		newItemMeta.setLore(lore_list);
		Item.setItemMeta(newItemMeta);
		return this;
	}

	@Override
	public List<String> getEnchants() {
		if (!Item.hasItemMeta())
			return new ArrayList<String>();
		ItemMeta newItemMeta = getItemMeta();
		if (newItemMeta.getEnchants().size() > 0) {
			List<String> data = new ArrayList<String>();
			for (Entry<Enchantment, Integer> lore : newItemMeta.getEnchants().entrySet()) {
				
				//data.add(lore.getKey().getName().replace("minecraft:", "") + ":" + lore.getValue()); //1.12.2
				data.add(lore.getKey().getKey().toString().replace("minecraft:", "") + ":" + lore.getValue()); //1.15.2
				
			}
			return data;
		}
		return new ArrayList<String>();
	}

	@Override
	public Itemset setEnchants(List<String> Enchants) {
		if(Enchants.size() == 0)
			return this;
		ItemMeta newItemMeta = getItemMeta();
		for (int i = 0; i < Enchants.size(); i++) {
			String[] EnchantsParts = Enchants.get(i).split(":");
			int level = Integer.parseInt(EnchantsParts[1]);
			
			//Enchantment enchantment = Enchantment.getByName(EnchantsParts[0]); //1.12.2
			Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(EnchantsParts[0])); //1.15.2
			
			newItemMeta.addEnchant(enchantment, level, true);
		}
		Item.setItemMeta(newItemMeta);
		return this;
	}

	@Override
	public List<String> getItemFlags() {
		if (!Item.hasItemMeta())
			return new ArrayList<String>();
		ItemMeta newItemMeta = getItemMeta();
		if (newItemMeta.getItemFlags().size() > 0) {
			List<String> data = new ArrayList<String>();
			for (ItemFlag itemflags : newItemMeta.getItemFlags())
				data.add(itemflags.name());
			return data;
		}
		return new ArrayList<String>();
	}

	@Override
	public Itemset setItemFlags(List<String> ItemFlags) {
		if(ItemFlags.size() == 0)
			return this;
		ItemMeta newItemMeta = getItemMeta();
		for (String itemflag : ItemFlags)
			newItemMeta.addItemFlags(ItemFlag.valueOf(itemflag));
		Item.setItemMeta(newItemMeta);
		return this;
	}

	@Override
	public Itemset setAmount(int amount) {
		Item.setAmount(amount);
		return this;
	}
	
	@Override
	public boolean isUnbreakable() {
		if (!Item.hasItemMeta())
			return false;
		return getItemMeta().isUnbreakable();
	}
	
	@Override
	public Itemset setUnbreakable(boolean Unbreakable) {
		if(Unbreakable) {
			ItemMeta newItemMeta = getItemMeta();
			newItemMeta.setUnbreakable(Unbreakable);
			Item.setItemMeta(newItemMeta);
		}
		
		return this;
	}
	
	@Override
	public boolean issame(ItemStack item) {
		return Item.equals(item);
	}
	
	@Override
	public String itemStackToBase64() throws IllegalStateException {
	    try {
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
	        
	        // Save every element in the list
	        dataOutput.writeObject(Item);

	        
	        // Serialize that array
	        dataOutput.close();
	        return Base64Coder.encodeLines(outputStream.toByteArray());
	    } catch (Exception e) {
	        throw new IllegalStateException("Unable to save item stacks.", e);
	    }
	}
	
	private ItemStack itemStackFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            // Read the serialized inventory
            Item = (ItemStack) dataInput.readObject();

            dataInput.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            Item = new ItemStack(Material.AIR);
        }
        return Item;
    }
}
