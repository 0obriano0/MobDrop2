package com.twsbrian.MobDrop2.FileIO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.Itemset;

public class FileInventory extends FileIO{
	public FileInventory() {
		super("message", tools.getLang() + "/Inventory.yml");
	}
	
	private Map<String,ItemStack> Buttons = new HashMap<String,ItemStack>();
	private List<String> EntityType_Normal_BlackList = new ArrayList<String>();
	
	@Override
	public boolean reloadcmd() {
		this.setFileName(tools.getLang() + "/Inventory.yml");
		
		if(data.contains("Other.EntityType_Normal.blacklist")) {
			EntityType_Normal_BlackList.clear();
			for(String str: getStringList("Other.EntityType_Normal.blacklist")) {
				EntityType_Normal_BlackList.add(str.toUpperCase());
			}
		}
		
		if(data.contains("init.Button")) {
			Buttons.clear();
			for (String name : this.data.getConfigurationSection("init.Button").getKeys(false)) {
				String Title = "";
				Material Material_data = null;
				List<String> Lore = new ArrayList<String>();
				
				if(data.contains("init.Button." + name +  ".Title")) {
					Title = getString("init.Button." + name + ".Title"); 
				} else {
					Title = DataBase.fileMessage.getString("Inventory.Title_error");
				}
				
				if(data.contains("init.Button." + name +  ".Lore")) {
					Lore = getStringList("init.Button." + name + ".Lore"); 
				}
				
		    	if(data.contains("init.Button." + name +  ".Type")) {
		    		String Type = getString("init.Button." + name + ".Type").toUpperCase(); 
		    		if(Material.getMaterial(Type) != null)
		    			Material_data = Material.getMaterial(Type);
		    		else {
		    			Material_data = Material.BARRIER;
		    			Lore.add(DataBase.fileMessage.getString("Inventory.Type_error"));
		    		}
		    	} else {
		    		Material_data = Material.BARRIER;
	    			Lore.add(DataBase.fileMessage.getString("Inventory.Type_error"));
		    	}
		    	
		    	Buttons.put(name.toLowerCase(), new Itemset(Material_data).setItemName(Title).setLore(Lore).getItemStack());
			}
			return true;
		}
		return false;
	}
	
    public ItemStack getbutton(String Name) {
    	return Buttons.getOrDefault(Name.toLowerCase(), new Itemset(Material.BARRIER).setItemName(DataBase.fileMessage.getString("Inventory.Button_not_found")).setLore(DataBase.fileMessage.getString("Inventory.Button_not_found_lore")).getItemStack());
    }
    
    public String getInventorTitle(String ID) {
    	return this.getString("Inventory." + ID + ".Title");
    }
    
    public List<String> getEntityType_Normal_BlackList(){
    	return EntityType_Normal_BlackList;
    }
}
