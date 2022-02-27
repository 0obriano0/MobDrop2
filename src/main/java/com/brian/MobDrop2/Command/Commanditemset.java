package com.brian.MobDrop2.Command;

import java.util.ArrayList;
import java.util.Arrays;

public class Commanditemset extends mainCommandSystem{
	
	public Commanditemset() {
		super(  "itemset",
				"/mobdrop itemset 設定物品",
				new ArrayList<String>(Arrays.asList("mobdrop.admin.itemset")),
				"/itemset");
	}
}
