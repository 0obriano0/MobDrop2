package com.twsbrian.MobDrop2.DataBase;

import org.bukkit.entity.Player;

public class DropItem extends Itemset {
  
  private org.bukkit.entity.Item dropdata;
  private Player player;
  private Long time;
  
  public DropItem(org.bukkit.entity.Item dropdata, Player player) {
    // TODO Auto-generated constructor stub
    super(dropdata.getItemStack());
    this.dropdata = dropdata;
    this.player = player;
    this.time = (Long)System.currentTimeMillis();
  }
  
  public org.bukkit.entity.Item getItem(){
    return dropdata;
  }
  
  public Player getPlayer() {
    return player;
  }
  
  public Long getTime() {
    return time;
  }
  
  public Long getLeftTime() {
    return ((System.currentTimeMillis() - time)/1000);
  }
}
