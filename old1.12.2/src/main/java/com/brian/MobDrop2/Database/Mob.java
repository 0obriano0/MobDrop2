package com.brian.MobDrop2.Database;

import java.util.List;

public class Mob {
	public String name;
	public List<MobItemList> ItemList;
	public List<MobItemList> HeadList;
	
	public Mob(String name, List<MobItemList> ItemList, List<MobItemList> HeadList){
		this.name = name;
		this.ItemList = ItemList;
		this.HeadList = HeadList;
	}
}
