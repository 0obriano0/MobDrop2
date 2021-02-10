package com.brian.MobDrop2.Database;

import java.util.ArrayList;
import java.util.List;

public class Config {
	//公開顯示掉落物
	public boolean IsOpen;
	//公開顯示掉落物 要幾趴以下
	public int Chance;
	public boolean list_Chinese;
	public boolean command_cmd_show;
	public boolean command_debug;
	public boolean command_old_list;
	public String lang = "zh_TW";
	
	public boolean player_sakurahead;
	public double player_Chance;
	public String player_title = "";
	public List<String> player_lore = new ArrayList<String>();
	
	public boolean dropItem = true;
	public boolean dropHead = true;
	
	public Config(boolean newIsOpen,int newChance,boolean command_cmd_show,boolean command_debug,boolean command_old_list,boolean list_Chinese,String lang,boolean player_sakurahead,double player_Chance,String player_title,List<String> player_lore,boolean dropItem,boolean dropHead) {
		this.IsOpen = newIsOpen;
		this.Chance = newChance;
		this.command_cmd_show = command_cmd_show;
		this.command_debug = command_debug;
		this.command_old_list = command_old_list;
		this.list_Chinese = list_Chinese;
		this.lang = lang;
		this.player_sakurahead = player_sakurahead;
		this.player_Chance = player_Chance;
		this.player_title = player_title;
		this.player_lore = player_lore;
		this.dropItem = dropItem;
		this.dropHead = dropHead;
	}
}
