package com.twsbrian.MobDrop2.FileIO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.twsbrian.MobDrop2.AnsiColor;
import com.twsbrian.MobDrop2.Database.DataBase;

public class tools {
	public static void Setprint(String title, String Name, int totle, int Success, int Fail) {
		DataBase.main.getLogger().info(AnsiColor.CYAN + "[" + title + "] " + AnsiColor.GREEN + Name + " load " + AnsiColor.PURPLE + "Totle: " + AnsiColor.WHITE + totle + AnsiColor.YELLOW + "  Success: " + AnsiColor.WHITE + Success + AnsiColor.RED + "  Fail:  " + AnsiColor.WHITE + Fail + AnsiColor.RESET);
	}
	public static void writeFile(String URL,List<String> lines){
		Path file = Paths.get(URL);
		lines.add(0, "\uFEFF#===========#\r\n" + 
				"#物品識別碼 #\r\n" + 
				"#===========#\r\n" + 
				"WOODEN_SWORD_TEST:\r\n" + 
				"#==============#\r\n" + 
				"#掉落的物品名稱#\r\n" + 
				"#==============#\r\n" + 
				"  ItemName: §f強力木劍§f\r\n" + 
				"#==========================#\r\n" + 
				"#掉落的物品是否套用原始名稱#\r\n" + 
				"#==========================#\r\n" + 
				"  UseCustomName: false\r\n" + 
				"#==================================================#\r\n" + 
				"#可參考: https://minecraft-ids.grahamedgecombe.com/#\r\n" + 
				"#掉落的物品原始名稱(例：木劍=WOODEN_SWORD)         #\r\n" + 
				"#==================================================#\r\n" + 
				"  ItemRealname: WOOD_SWORD\r\n" + 
				"#==============#\r\n" + 
				"#掉落的物品說明#\r\n" + 
				"#==============#\r\n" + 
				"  ItemLores:\r\n" + 
				"  - §e充滿怨靈的木劍§f\r\n" + 
				"#=======================================#\r\n" + 
				"#掉落的物品附魔                         #\r\n" + 
				"#- <附魔>:<等級>                        #\r\n" + 
				"#ARROW_INFINITE 無限弓                  #\r\n" + 
				"#ARROW_DAMAGE 強力弓                    #\r\n" + 
				"#ARROW_FIRE 火燄弓                      #\r\n" + 
				"#ARROW_KNOCKBACK 弓擊退                 #\r\n" + 
				"#BINDING_CURSE 綁定詛咒	                #\r\n" + 
				"#CHANNELING 喚雷                        #\r\n" + 
				"#DAMAGE_UNDEAD 不死剋星                 #\r\n" + 
				"#DAMAGE_ALL 鋒利                        #\r\n" + 
				"#DAMAGE_ARTHROPODS 節肢剋星             #\r\n" + 
				"#DEPTH_STRIDER 深海漫遊                 #\r\n" + 
				"#DURABILITY 耐久                        #\r\n" + 
				"#FIRE_ASPECT 火焰附加                   #\r\n" + 
				"#FROST_WALKER 冰霜行者                  #\r\n" + 
				"#IMPALING 魚叉(對海中怪物造成更大的傷害)#\r\n" + 
				"#KNOCKBACK 擊退劍                       #\r\n" + 
				"#LOOT_BONUS_BLOCKS 幸運                 #\r\n" + 
				"#LOOT_BONUS_MOBS 掠奪                   #\r\n" + 
				"#LOYALTY (拋出的三叉戟返回投擲它的玩家) #\r\n" + 
				"#LURE (增加魚咬鉤的速度)                #\r\n" + 
				"#MENDING 經驗修補                       #\r\n" + 
				"#OXYGEN 氧氣(頭)                        #\r\n" + 
				"#PROTECTION_ENVIRONMENTAL 保護          #\r\n" + 
				"#PROTECTION_EXPLOSIONS 防爆             #\r\n" + 
				"#PROTECTION_PROJECTILE 防彈             #\r\n" + 
				"#PROTECTION_FIRE 抗火                   #\r\n" + 
				"#PROTECTION_FALL 輕盈(腳)               #\r\n" + 
				"#SILK_TOUCH 絲綢之觸                    #\r\n" + 
				"#SWEEPING_EDGE 鐮刀之刃                 #\r\n" + 
				"#WATER_WORKER 親水性                    #\r\n" + 
				"#THORNS 尖刺                            #\r\n" + 
				"#VANISHING_CURSE (物品消失而不是掉落)   #\r\n" + 
				"#DIG_SPEED 效率                         #\r\n" + 
				"#=======================================#\r\n" + 
				"  Enchants:\r\n" + 
				"  - DAMAGE_ALL:1\r\n" + 
				"#=========================================================#\r\n" + 
				"#HIDE_ATTRIBUTES       隱藏 NBT                           #\r\n" + 
				"#HIDE_ENCHANTS         隱藏 附魔                          #\r\n" + 
				"#HIDE_UNBREAKABLE      隱藏 無法破壞                      #\r\n" + 
				"#HIDE_POTION_EFFECTS   隱藏 藥水效果/界符盒內容物/唱片號碼#\r\n" + 
				"#HIDE_DESTROYS         隱藏 可破壞方塊                    #\r\n" + 
				"#HIDE_PLACED_ON        隱藏 可放置於                      #\r\n" + 
				"#=========================================================#\r\n" + 
				"  ItemFlags: \r\n" + 
				"  - HIDE_ENCHANTS\r\n" + 
				"  - HIDE_UNBREAKABLE\r\n" + 
				"#======================#\r\n" + 
				"#可否破壞              #\r\n" + 
				"#======================#\r\n" + 
				"  Unbreakable: false\r\n" + 
				"#========#\r\n" + 
				"#其他範例#\r\n" + 
				"#========#");
		try {
			Files.write(file, lines, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
