package com.twsbrian.MobDrop2.HashMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.twsbrian.MobDrop2.DataBase.Itemset;

public class HashMapSortItemset {
	// 想依照姓名或成績牌列印出所有資料，先將所有HashMap裡的entry放入List
	public List<Entry<String, Itemset>> list_Data;
	
    public HashMapSortItemset(HashMap<String, Itemset> Itemset) {

    	// 想依照姓名或成績牌列印出所有資料，先將所有HashMap裡的entry放入List
    	list_Data = new ArrayList<Map.Entry<String, Itemset>>(Itemset.entrySet());

        // 依姓名排序並列印
        Collections.sort(list_Data, new Comparator<Map.Entry<String, Itemset>>(){
            public int compare(Map.Entry<String, Itemset> entry1, Map.Entry<String, Itemset> entry2){
                return (entry1.getKey().compareTo(entry2.getKey()));
            }
        });
    }
}
