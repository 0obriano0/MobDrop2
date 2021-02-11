package com.brian.MobDrop2.InventoryGUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.brian.MobDrop2.Database.DataBase;
import com.brian.MobDrop2.Database.Mob;
import com.brian.MobDrop2.Database.MobItemList;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class InventoryMenu implements InventoryProvider{
	public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("Menu")
            .provider(new InventoryMenu())
            .size(3, 9)
            .title(ChatColor.BLUE + DataBase.language.Inventory.menu)
            .build();
	
	@Override
	public void init(Player player, InventoryContents contents) {
		contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE)));
		
		List<MobItemList> head_list = new ArrayList<MobItemList>();
    	
    	for(Map.Entry<String, Mob> entry:DataBase.MobsMap.entrySet()) {
    		if(entry.getValue().HeadList.size() > 0)head_list.addAll(entry.getValue().HeadList);
    	}
		
		contents.set(1, 2, ClickableItem.of(InventoryTools.createPageButton(Material.ITEM_FRAME,"§a" + DataBase.language.Inventory.ItemList,"§a - " + DataBase.language.Inventory.items + " §f" + (DataBase.ItemMap.size() + head_list.size())),
                e -> InventoryItemsList.INVENTORY.open(player)));
		contents.set(1, 4, ClickableItem.empty(InventoryTools.createPageButton(Material.PAPER,"§a" + DataBase.language.Inventory.info,info_format(DataBase.language.Inventory.info_lore))));
		contents.set(1, 6, ClickableItem.of(InventoryTools.createPageButton(Material.MONSTER_EGG,"§a" + DataBase.language.Inventory.MobsList,"§a - " + DataBase.language.Inventory.mobs + " §f" + DataBase.MobsMap.size()),
                e -> InventoryMobsList.INVENTORY.open(player)));
		contents.set(2, 8, ClickableItem.of(InventoryTools.createPageButton(Material.BARRIER,"§a" + DataBase.language.Inventory.close),
                e -> InventoryMobsList.INVENTORY.close(player)));
	}
	
	private final Random random = new Random();
	@Override
	public void update(Player player, InventoryContents contents) {
        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        if(state % 5 != 0)
            return;
        
        short durability = (short)random.nextInt(15);

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, durability);
        ItemMeta glass_im = glass.getItemMeta();
        glass_im.setDisplayName(DataBase.language.message.wall);
        glass.setItemMeta(glass_im);
        contents.fillBorders(ClickableItem.empty(glass));
    }
	public List<String> info_format(List<String> lores) {
		List<String> final_lores = new ArrayList<String>();
		for(String lore : lores) {
			final_lores.add(lore.replaceAll("%GobalMessageIsOpen%", DataBase.Config.IsOpen ? DataBase.language.Inventory.info_true : DataBase.language.Inventory.info_false)
								.replaceAll("%GobalMessageChance%", DataBase.Config.Chance + "")
								.replaceAll("%cmdShow%", DataBase.Config.command_cmd_show ? DataBase.language.Inventory.info_true : DataBase.language.Inventory.info_false)
								.replaceAll("%debug%", DataBase.Config.command_debug ? DataBase.language.Inventory.info_true : DataBase.language.Inventory.info_false)
								.replaceAll("%oldList%", DataBase.Config.command_old_list ? DataBase.language.Inventory.info_true : DataBase.language.Inventory.info_false)
								.replaceAll("%lang%", DataBase.Config.lang + "")
								.replaceAll("%sakurahead%", DataBase.Config.player_sakurahead ? DataBase.language.Inventory.info_true : DataBase.language.Inventory.info_false)
								.replaceAll("%dropItem%", DataBase.Config.dropItem ? DataBase.language.Inventory.info_true : DataBase.language.Inventory.info_false)
								.replaceAll("%dropHead%", DataBase.Config.dropHead ? DataBase.language.Inventory.info_true : DataBase.language.Inventory.info_false));
		}
		return final_lores;
	}
}
