package com.twsbrian.MobDrop2.Listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

import com.twsbrian.MobDrop2.MobDrop2;
import com.twsbrian.MobDrop2.DataBase.Itemset;
import com.twsbrian.MobDrop2.DataBase.itemset.Glow;

public class ItemDropListener implements Listener{
	@EventHandler
    public void itemDrop(ItemSpawnEvent e) {
		Item drop = e.getEntity();
		
		if(!drop.isCustomNameVisible() && MobDrop2.plugin.getConfig().getBoolean("Holo.Show")) {
			Itemset itemset = new Itemset(drop.getItemStack());
			drop.setCustomName(itemset.getItemName());
			drop.setCustomNameVisible(true);
		}
		
		if(!drop.isGlowing() && MobDrop2.plugin.getConfig().getString("Glow.Mode").toLowerCase().contains("all")) {
			drop.setGlowing(true);
			ChatColor color = Glow.getColor();
	        Glow.setGlowColor(color, drop);
		}
	}
}
