package com.brian.MobDrop2.FileIO;

import com.brian.MobDrop2.MobDrop2;
import com.brian.MobDrop2.FileIO.FileIO;

public class FileMessage extends FileIO{
	public FileMessage() {
		super("message", MobDrop2.plugin.getConfig().getString("lang") + ".yml");
	}
	
}
