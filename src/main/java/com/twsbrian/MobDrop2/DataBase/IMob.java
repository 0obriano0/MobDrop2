package com.twsbrian.MobDrop2.DataBase;

import org.bukkit.inventory.ItemStack;

/**
 * 怪物創建 的 文字說明檔
 * @author Brian
 *
 */
public interface IMob {

	/**
	 * 檢查是否有設定圖示
	 * @return true or false
	 */
	boolean hasIcon();
	
	/**
	 * 取的圖示
	 * @return
	 */
	ItemStack getIcon();
	
	/**
	 * 設定圖示
	 * @param icon
	 */
	void setIcon(ItemStack icon);
	
	/**
	 * 取得怪物 id 名稱
	 * @return
	 */
	String getName();
	
	/**
	 * 設定 id
	 * @param name
	 */
	void setName(String name);
	
	/**
	 * 當是原版怪物時會抓取翻譯檔
	 * 如果是自定義怪物會回傳跟 id 一樣的名稱
	 * @return
	 */
	String getMobName();
	
	/**
	 * 是否為自定義怪物
	 * @return "Y" or "N"
	 */
	String getCustom();
	 
	/**
	 * 是否為自定義怪物
	 * @return true or false
	 */
	boolean isCustom();
	
}
