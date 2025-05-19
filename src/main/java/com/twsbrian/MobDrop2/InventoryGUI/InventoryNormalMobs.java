package com.twsbrian.MobDrop2.InventoryGUI;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.Itemset;
import com.twsbrian.MobDrop2.DataBase.Mob;
import com.twsbrian.MobDrop2.HashMap.HashMapSortItemStackList;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;

public class InventoryNormalMobs implements InventoryProvider{
	Mob mob;
	
	public InventoryNormalMobs(Mob mob) {
		this.mob = mob;
	}
	
	public static SmartInventory getInventory(Mob mob) {
		return SmartInventory.builder()
				.provider(new InventoryNormalMobs(mob))
				.size(6, 9)
				.title(ChatColor.BLUE + DataBase.fileInventory.getInventorTitle("EntitytypeList"))
				.build();
	}
	
	@Override
	public void init(Player player, InventoryContents contents) {
		Pagination pagination = contents.pagination();
		
		Map<String, ItemStack> Mobs = getMobs();
		
		ClickableItem[] items = new ClickableItem[Mobs.size()];
		int index = 0;
		HashMapSortItemStackList ItemList = new HashMapSortItemStackList((HashMap<String, ItemStack>) Mobs);
		
        for (Map.Entry<String, ItemStack> entry:ItemList.list_Data) {
        	items[index] = ClickableItem.of(entry.getValue(), e -> InventoryMobAdd.getInventory(setmobname(entry.getKey())).open(player));
	      	index++;
	    }
        
        pagination.setItems(items);
        pagination.setItemsPerPage(45);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
        
		// TODO Auto-generated method stub
		contents.set(5, 0, ClickableItem.of(DataBase.fileInventory.getbutton("Back"),
                e -> InventoryMobAdd.getInventory(mob).open(player)));
        contents.set(5, 3, ClickableItem.of(DataBase.fileInventory.getbutton("Previous"),
                e -> InventoryNormalMobs.getInventory(mob).open(player, pagination.previous().getPage())));
        contents.set(5, 4, ClickableItem.empty(InventoryTools.createPageButton(Material.PAPER,"§a - " + (pagination.getPage() + 1) + " - ")));
        contents.set(5, 5, ClickableItem.of(DataBase.fileInventory.getbutton("Next"),
                e -> InventoryNormalMobs.getInventory(mob).open(player, pagination.next().getPage())));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		// TODO Auto-generated method stub
		
	}
	
	private Mob setmobname(String name){
		Mob mob = this.mob;
		mob.setName(name);
		return mob;
	}
	
	
	private Map<String, ItemStack> getMobs(){
		Map<String, ItemStack> mobs = new HashMap<String, ItemStack>();
		for (EntityType entry: EntityType.values()) {
			boolean findMaterial = true;
			
			if(DataBase.NormalMobsMap.containsKey(entry.name().toUpperCase())) continue;
			if(DataBase.fileInventory.getEntityType_Normal_BlackList().contains(entry.name().toUpperCase())) continue;
			if(entry.name().toUpperCase().contains("MINECART")) continue;
			if(entry.name().toUpperCase().contains("ARROW")) continue;
      if(!checkEntityType(entry)) continue;

      Material f_item = null;
      Material head = Material.getMaterial(entry.name().toUpperCase() + "_HEAD");
      Material spawn_egg = Material.getMaterial(entry.name().toUpperCase() + "_SPAWN_EGG");
      Material item = Material.getMaterial(entry.name().toUpperCase());
      f_item = head;
      if(f_item == null) f_item = spawn_egg;
      if(f_item == null) f_item = item;
      if(f_item == null) {
        f_item = Material.BARRIER;
        findMaterial = false;
      }
      
      Itemset itemset = new Itemset(f_item);
      itemset.setItemName("§a" + DataBase.fileMessage.GetEntityName(entry.name().toUpperCase()));
      if(!findMaterial) {
        itemset.setLore(DataBase.fileMessage.getString("Inventory.Material_not_found"));
      }
      
      mobs.put(entry.name().toUpperCase(), itemset.getItemStack());
    }
		
		return mobs;
	}
  
  private boolean checkEntityType(EntityType type) {
    if (type == null) return false;
    // 不用列舉、不用 SpawnCategory，也不用關鍵字，只能根據 EntityType 的屬性進行判斷
    // 這種情況下，僅能用 EntityType 的 method 判斷（但 EntityType 沒有 isMob/isLiving 這類方法）
    // 可以嘗試用 EntityType.getEntityClass() 是否為 LivingEntity 的子類來判斷
    Class<?> clazz = type.getEntityClass();
    if (clazz == null) {
      // 非實體
      return false;
    }
    if (org.bukkit.entity.LivingEntity.class.isAssignableFrom(clazz)) {
      // 是生物（包含怪物與動物）
      // 進一步判斷是否為 Monster
      if (org.bukkit.entity.Monster.class.isAssignableFrom(clazz)) {
        // 怪物
        return true;
      } else {
        // 其他生物（非怪物)
        return true;
      }
    } else {
      // 不是生物
      return false;
    }
  }
}
