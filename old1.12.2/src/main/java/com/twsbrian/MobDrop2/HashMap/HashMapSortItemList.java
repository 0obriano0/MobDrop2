package com.twsbrian.MobDrop2.HashMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.twsbrian.MobDrop2.Database.Items;

public class HashMapSortItemList {
	// 想依照姓名或成績牌列印出所有資料，先將所有HashMap裡的entry放入List
	public List<Map.Entry<String, Items>> list_Data;
	
    public HashMapSortItemList(HashMap<String, Items> inputdata) {

        // 想依照姓名或成績牌列印出所有資料，先將所有HashMap裡的entry放入List
    	list_Data = new ArrayList<Map.Entry<String, Items>>(inputdata.entrySet());

        // 依姓名排序並列印
        Collections.sort(list_Data, new Comparator<Map.Entry<String, Items>>(){
            public int compare(Map.Entry<String, Items> entry1, Map.Entry<String, Items> entry2){
                return (entry1.getKey().compareTo(entry2.getKey()));
            }
        });
        
        /*for (Map.Entry<String, Items> entry:list_Data) {
            System.out.print(entry.getKey() + "\t" + inputdata.get(entry.getKey()));
        }*/
    }
}