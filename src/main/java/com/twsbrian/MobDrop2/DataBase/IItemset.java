package com.twsbrian.MobDrop2.DataBase;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * 物品創建 的 文字說明檔
 * @author Brian
 *
 */
public interface IItemset {

	ItemMeta getItemMeta();
	
	ItemStack getItemStack();
	
	/**
	 * 設定物品名稱
	 * @param Name
	 * @return Itemset
	 */
	Itemset setItemName(@Nonnull String Name);
	
	/**
	 * 讀取物品名稱
	 * @return 物品名稱
	 */
	String getItemName();
	
	/**
	 * 設定物品耐久值
	 * @param Durability 耐久值
	 * @return Itemset
	 */
	Itemset setDurability(short Durability);
	
	/**
	 * 讀取物品耐久值
	 * @return 物品耐久值
	 */
	int getDurability();
	
	/**
	 * 讀取文字敘述
	 * @return 如果是空的會回傳 []
	 */
	List<String> getLore();
	
	/**
	 * 新增文字敘述
	 * @param lore 訊息資料(type String)
	 * @return Itemset
	 */
	Itemset setLore(String lore);
	
	/**
	 * 新增文字敘述
	 * @param lore 訊息資料(type List<String>)
	 * @return Itemset
	 */
	Itemset setLore(List<String> lore) ;
	
	/**
	 * 增加文字敘述
	 * @param lore 訊息資料(type String)
	 * @return Itemset
	 */
	Itemset addLore(String lore);
	
	/**
	 * 增加文字敘述
	 * @param lore 訊息資料(type List<String>)
	 * @return Itemset
	 */
	Itemset addLore(List<String> lore);
	
	/**
	 * 抓取附魔參數
	 * @return 參數資料 如果抓取不到則回傳 null
	 */
	List<String> getEnchants();
	
	/**
	 * 設定附魔
	 * @param Enchants 附魔參數
	 * @return Itemset
	 */
	Itemset setEnchants(List<String> Enchants);
	
	/**
	 * 抓取隱藏數值
	 * @return 隱藏數值 如果抓取不到則回傳 null
	 */
	List<String> getItemFlags();
	
	/**
	 * 設定隱藏數值
	 * @param ItemFlags 數值參數
	 * @return Itemset
	 */
	Itemset setItemFlags(List<String> ItemFlags);
	
	/**
	 * 設定數量
	 * @param amount 數量
	 * @return Itemset
	 */
	Itemset setAmount(int amount) ;
	
	/**
	 * 檢查可否破壞
	 * @return true or false
	 */
	boolean isUnbreakable();
	
	/**
	 * 設定能不能破壞
	 * @param Unbreakable true or false
	 * @return true or false
	 */
	Itemset setUnbreakable(boolean Unbreakable);
	
	/**
	 * 檢查是否相同
	 * @param item 要檢查物品
	 * @return true or false
	 */
	boolean issame(ItemStack item) ;
	
	/*
	 * 物品 轉 Base64
	 */
	String itemStackToBase64();
	
	/*
	 * Base64 轉 物品 Loading
	 */
//	ItemStack itemStackFromBase64(String data);
}
