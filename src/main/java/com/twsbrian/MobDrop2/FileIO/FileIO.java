package com.twsbrian.MobDrop2.FileIO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.twsbrian.MobDrop2.MobDrop2;

/**
 * 檔案操作的核心檔案
 * @author Brian
 *
 */
public class FileIO implements IFileIO{
	
	private transient String FileName;
	private transient String URL = null;
	
	protected FileConfiguration data = null;
	
	public FileIO(@Nonnull String FileName){
		this.FileName = FileName;
	}
	
	public FileIO(String URL,@Nonnull String FileName){
		this.FileName = FileName;
		this.URL = URL;
	}
	
	@Override
	public String getFileName() {
		return FileName;
	}
	
	protected void setFileName(String FileName) {
		this.FileName = FileName;
		readFile();
	}
	
	@Override
	public String getPath() {
		String full_url = FileName;
		
		if(URL.equals(null))
			full_url = "./" + MobDrop2.plugin.getDataFolder().toString() + "/" + FileName;
		else
			full_url = "./" + MobDrop2.plugin.getDataFolder().toString() + "/" + URL + "/" + FileName;
		
		return full_url;
	}
	
	@Override
	public FileConfiguration getFileforYML() {
		if(data == null) reloadFile();
		return data;
	}
	
	@Override
	public String getString(String path) {
		try {
			return this.getFileforYML().getString(path).replaceAll("&", "§");
		}catch(NullPointerException e){
			
			String full_url = FileName;
			try {
				if(!URL.equals(null)) full_url = URL + "\\" + FileName;
			}catch(NullPointerException ee) {
				return "system error [file load error \" " + FileName + " \"]";
			}
			
			MobDrop2.plugin.saveResource(full_url, true);
			
			if(reloadFile())
				try {
					return this.getFileforYML().getString(path).replaceAll("&", "§");
				}catch(Exception ee){
					ee.printStackTrace();
					return "system error [file load error \" " + FileName + " \"]";
				}
			else {
				e.printStackTrace();
				return "system error [file load error \" " + FileName + " \"]";
			}
			
		}
		
	}
	
	private List<String> formateStringlist(@Nonnull List<String> data){
		List<String> finaldata = new ArrayList<String>();
		for(String Data : data) {
			finaldata.add(Data.replaceAll("&", "§"));
		}
		return finaldata;
	}
	
	@Override
	public List<String> getStringList(String path) {
		try {
			return formateStringlist(this.getFileforYML().getStringList(path));
		}catch(NullPointerException e){
			String full_url = FileName;
			
			if(!(URL == null || URL.isEmpty())) full_url = URL + "\\" + FileName;
					
			MobDrop2.plugin.saveResource(full_url, true);
			
			if(reloadFile())
				try {
					return formateStringlist(this.getFileforYML().getStringList(path));
				}catch(NullPointerException ee){
					ee.printStackTrace();
					//Arrays.asList("system error [file load error \" " + FileName + " \"]","please tell admin")
					return new ArrayList<String>();
				}
			else {
				e.printStackTrace();
				//Arrays.asList("system error [file load error \" " + FileName + " \"]","please tell admin")
				return new ArrayList<String>();
			}
			
		}
		
	}
	
	protected void readFile(){
		File File_load = null;
		String full_url = FileName;
		
		if(URL == null || URL.isEmpty())
			File_load = new File(MobDrop2.plugin.getDataFolder(), FileName);
		else {
			File_load = new File("./" + MobDrop2.plugin.getDataFolder().toString() + "/" + URL + "/" + FileName);
			full_url = URL + "\\" + FileName;
		}
		
        if (!File_load.exists()) MobDrop2.plugin.saveResource(full_url, true);
        data = YamlConfiguration.loadConfiguration(File_load);
	}
	
	@Override
	public boolean reloadFile() {
		readFile();
		if(!reloadcmd()) return false;
		return true;
	}

	@Override
	public boolean reloadcmd() {
		return true;
	}
	
}
