package com.twsbrian.MobDrop2.Listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import com.twsbrian.MobDrop2.DataBase.DataBase;

public class PlayerListener implements Listener {
	@EventHandler(priority = EventPriority.LOW)//當撿起物品
	public void onPickup (EntityPickupItemEvent event){
	    LivingEntity pickEntity = event.getEntity();
	
//	    Item item = event.getItem();
//	    item.setOwner(test);
	    DataBase.Print("撿取人:"+pickEntity.getUniqueId()+" 物品location: ");
	}
}
