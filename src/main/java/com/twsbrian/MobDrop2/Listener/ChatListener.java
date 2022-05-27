package com.twsbrian.MobDrop2.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitTask;

import com.twsbrian.MobDrop2.MobDrop2;
import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.MessageSet;
import com.twsbrian.MobDrop2.DataBase.Mob;
import com.twsbrian.MobDrop2.Task.CustomMobNameTask;

public class ChatListener implements Listener{
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if(DataBase.NewCustomMobName.containsKey(player.getName())) {
			event.setCancelled(true);
			MessageSet data = DataBase.NewCustomMobName.remove(player.getName());
			if((System.currentTimeMillis() - data.getTime())/1000 < 15) {
				Mob mob = (Mob)data.getobject();
				mob.setName(event.getMessage().replaceAll("&", "§"));
				@SuppressWarnings("unused")
				BukkitTask task = new CustomMobNameTask(player,mob).runTaskLater(MobDrop2.plugin, 5);
			}
		}
		
	}
}
