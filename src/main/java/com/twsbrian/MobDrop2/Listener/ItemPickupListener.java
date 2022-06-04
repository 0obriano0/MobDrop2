package com.twsbrian.MobDrop2.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

@SuppressWarnings("deprecation")
public class ItemPickupListener implements Listener{
	@EventHandler
    public void itemPickup(PlayerPickupItemEvent e) {
		
//		Item drop = e.getItem();
//		DataBase.Print(e.getPlayer().getName() + " get item: " + drop.getCustomName() + " | " + new Itemset(drop.getItemStack()).getItemName() + " | UUID: " + drop.getUniqueId());
	}
}
