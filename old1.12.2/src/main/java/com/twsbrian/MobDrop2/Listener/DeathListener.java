package com.twsbrian.MobDrop2.Listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.twsbrian.MobDrop2.Database.DataBase;
import com.twsbrian.MobDrop2.Database.MobItemList;

public class DeathListener implements Listener{
	public DeathListener()
    {
    	
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onEntityDeathEvents(EntityDeathEvent event)
    {
//		if(entity instanceof Player){
//		    Player p = (Player) entity;
//		}
		LivingEntity entityDeth = event.getEntity();
    	// 判斷是否為玩家殺死的
    	if (entityDeth.getKiller() != null && entityDeth.getKiller() instanceof Player){
    		Player killBy = entityDeth.getKiller();
    		String sEntitlyName = "";
    		if(entityDeth instanceof Player && DataBase.Config.player_sakurahead){
    		    Player dethplayer = (Player) entityDeth;
    		    
    		    // 取得基數(從1~10000中抽一個號碼)
				int iChance = (int)(Math.random() * 10000 + 1);
				// 判斷物品掉落機率(乘以100後)是否小於基數
				if (iChance <= (DataBase.Config.player_Chance * 100)) {
					// 判定掉落
					if(killBy.getInventory().firstEmpty() == -1)
						entityDeth.getWorld().dropItemNaturally(entityDeth.getLocation(), DataBase.getplayerHead(dethplayer.getName()));
					else
						killBy.getInventory().addItem(DataBase.getplayerHead(dethplayer.getName()));
					// 顯示掉落訊息
					
					if(DataBase.Config.IsOpen && DataBase.Config.Chance >= DataBase.Config.player_Chance) 
						DataBase.server.broadcastMessage("§b" + DataBase.language.Plugin_name + " " + formatmessage(DataBase.language.message.Gobal_mobDropItem, killBy, dethplayer.getName(), dethplayer.getName() + " head", 1));
					
					killBy.sendMessage("§b" + DataBase.language.Plugin_name + "§f " + " " + formatmessage(DataBase.language.message.mobDropItem, killBy, dethplayer.getName(), dethplayer.getName() + " head", 1));
				}
    		}else {
    			try {
    	    		if (entityDeth.getCustomName() != null) {
    	    			if(DataBase.MobsMap.containsKey(entityDeth.getCustomName().toUpperCase()))
    	    				sEntitlyName = entityDeth.getCustomName().toUpperCase();
    	    		}else
    	    			sEntitlyName = entityDeth.getType().getName().toUpperCase();
        		} catch (NullPointerException e) {
        			return;
        		}
        		
        		// 判斷是否有掉落物清單
        		if (DataBase.MobsMap.containsKey(sEntitlyName)){
        			// 取得掉落物清單
        			List<MobItemList> dropItems = DataBase.MobsMap.get(sEntitlyName).ItemList;
        			List<MobItemList> HeadItems = DataBase.MobsMap.get(sEntitlyName).HeadList;
        			
        			if(HeadItems.size() > 0) {
        				List<MobItemList> gethead = new ArrayList<MobItemList>();
        				// 迴圈判斷是否掉落物品
            			for (int i = 0; i < HeadItems.size(); i++){
            				MobItemList MobDropItem = HeadItems.get(i);
        					// 取得基數(從1~10000中抽一個號碼)
            				int iChance = (int)(Math.random() * 10000 + 1);
            				// 判斷物品掉落機率(乘以100後)是否小於基數
            				if(DataBase.Config.command_cmd_show && DataBase.Config.command_debug) 
            					DataBase.main.getLogger().info("物品:" + MobDropItem.Item.ItemName + " | 機率:" + MobDropItem.Chance * 100 + " | 算出來的機率:" + iChance);
            				if (iChance <= (MobDropItem.Chance * 100)){	
            					gethead.add(MobDropItem);
            				}
            			}
            			MobItemList MobDropItem_ = null;
            			if(gethead.size() > 0) {
            				MobDropItem_ = gethead.get((int)(Math.random() * gethead.size()));
            			}
            			if(MobDropItem_ != null) {
            				// 判定掉落
        					if(killBy.getInventory().firstEmpty() == -1 || (DataBase.Config.dropHead && DataBase.Config.dropItem))
        						entityDeth.getWorld().dropItemNaturally(entityDeth.getLocation(), MobDropItem_.Item.getResultItem());
        					else
        						killBy.getInventory().addItem(MobDropItem_.Item.getResultItem());
        					// 顯示掉落訊息
        					
        					if(DataBase.Config.IsOpen && DataBase.Config.Chance >= MobDropItem_.Chance) 
        						DataBase.server.broadcastMessage("§b" + DataBase.language.Plugin_name + " " + formatmessage(DataBase.language.message.Gobal_mobDropItem, killBy, sEntitlyName, MobDropItem_.Item.ItemName, MobDropItem_.Item.getResultItem().getAmount()));
    						
        					killBy.sendMessage("§b" + DataBase.language.Plugin_name + "§f " + " " + formatmessage(DataBase.language.message.mobDropItem, killBy, sEntitlyName, MobDropItem_.Item.ItemName, MobDropItem_.Item.getResultItem().getAmount()));
            			}
            			
        			}
        			if(dropItems.size() > 0) {
        				if(DataBase.Config.command_cmd_show && DataBase.Config.command_debug)
        					DataBase.main.getLogger().info(sEntitlyName + " items = " + dropItems.size());
        				// 迴圈判斷是否掉落物品
            			for (int i = 0; i < dropItems.size(); i++){
            				MobItemList MobDropItem = dropItems.get(i);
            				if(DataBase.Config.command_cmd_show && DataBase.Config.command_debug)
            					DataBase.main.getLogger().info("item = " + MobDropItem.Item.ItemName);
            				// 判斷世界是否正確
            				//if (customItem.OnlyWorld.equals("") || customItem.OnlyWorld.toUpperCase().equals(entityDeth.getWorld().getName().toUpperCase()))
            				//{
            					// 取得基數(從1~10000中抽一個號碼)
                				int iChance = (int)(Math.random() * 10000 + 1);
                				// 判斷物品掉落機率(乘以100後)是否小於基數
                				if(DataBase.Config.command_cmd_show && DataBase.Config.command_debug) 
                					DataBase.main.getLogger().info("物品:" + MobDropItem.Item.ItemName + " | 機率:" + MobDropItem.Chance * 100 + " | 算出來的機率:" + iChance);
                				if (iChance <= (MobDropItem.Chance * 100)){	
                					
                					ItemStack MobDropItem_ = MobDropItem.getResultItem();
                    				int items_num = 1;
                    				// 判定掉落數量
                					if(MobDropItem.Quantity < MobDropItem.Quantity_max) {
                						items_num = (int)(Math.random() * (MobDropItem.Quantity_max-MobDropItem.Quantity+1) + MobDropItem.Quantity);
                    					MobDropItem_.setAmount(items_num);
                					}
                					// 判定掉落
                					if(killBy.getInventory().firstEmpty() == -1 || DataBase.Config.dropItem)
                						entityDeth.getWorld().dropItemNaturally(entityDeth.getLocation(), MobDropItem_);
                					else
                						killBy.getInventory().addItem(MobDropItem_);
                					// 顯示掉落訊息
                					
                					if(DataBase.Config.IsOpen && DataBase.Config.Chance >= MobDropItem.Chance) 
                						DataBase.server.broadcastMessage("§b" + DataBase.language.Plugin_name + " " + formatmessage(DataBase.language.message.Gobal_mobDropItem, killBy, sEntitlyName, MobDropItem.Item.ItemName, MobDropItem_.getAmount()));
            						
                					killBy.sendMessage("§b" + DataBase.language.Plugin_name + "§f " + " " + formatmessage(DataBase.language.message.mobDropItem, killBy, sEntitlyName, MobDropItem.Item.ItemName, MobDropItem_.getAmount()));
                				}
            				//}
            			}
        			}
        			
        		}
    		}
    	}
    }
	
	private String formatmessage(String message, Player player,String MobName,String item_Name,int item_Amount) {
		return message.replaceAll("%player%", player.getName()).replaceAll("%mob%",DataBase.GetEntityName(MobName)).replaceAll("%item%",item_Name).replaceAll("%item_num%","" + item_Amount);
	}
}
