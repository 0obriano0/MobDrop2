package com.brian.MobDrop2.HashMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.brian.MobDrop2.Database.MobItemList;

public class HashMapSortMobList {
	// 想依照姓名或成績牌列印出所有資料，先將所有HashMap裡的entry放入List
	public List<Map.Entry<String, List<MobItemList>>> list_Data;
	
    public HashMapSortMobList(HashMap<String, List<MobItemList>> inputdata) {

        // 想依照姓名或成績牌列印出所有資料，先將所有HashMap裡的entry放入List
    	list_Data = new ArrayList<Map.Entry<String, List<MobItemList>>>(inputdata.entrySet());

        // 依姓名排序並列印
        Collections.sort(list_Data, new Comparator<Map.Entry<String, List<MobItemList>>>(){
            public int compare(Map.Entry<String, List<MobItemList>> entry1, Map.Entry<String, List<MobItemList>> entry2){
                return (entry1.getKey().compareTo(entry2.getKey()));
            }
        });
    }
}