package com.brian.MobDrop2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.brian.MobDrop2.Command.ImainCommandSystem;
import com.brian.MobDrop2.DataBase.DataBase;

public class MobDrop2 extends JavaPlugin{
	public static Plugin plugin;
	public static Server server;
	
	@Override
    public void onEnable() {
		plugin = this;
        server = this.getServer();
        
        saveDefaultConfig();
        reloadConfig();
        
        setEvents();
        
        DataBase.fileMessage.reloadFile();

	}
	
	@Override
    public void onDisable() {

    }
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	return Command(sender,command,label,args,MobDrop2.class.getClassLoader(),"com.brian." + DataBase.pluginName + ".Command");
    }
	
	public boolean Command(CommandSender sender, Command command, String commandLabel, String[] args,final ClassLoader classLoader, final String commandPath) {
		if(args.length == 0){
        	ImainCommandSystem cmd = getCommandClass("help",classLoader,commandPath);
        	if(!cmd.hasPermission(sender)) {
        		sender.sendMessage(DataBase.fileMessage.getString("Command.NoPermission"));
				return false;
        	}
        	try {
				cmd.run(sender, commandLabel, command, args);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }else if (args.length != 0 && (commandLabel.equalsIgnoreCase(DataBase.pluginName.toLowerCase()) || commandLabel.equalsIgnoreCase("mobdrop") || commandLabel.equalsIgnoreCase("mdop"))) {
			if(!DataBase.getCommands(plugin).contains(args[0])) {
				sender.sendMessage(DataBase.fileMessage.getString("Command.CanNotFind"));
				return false;
			}
			if(args.length >= 1) {
    			ImainCommandSystem cmd = getCommandClass(args[0],classLoader,commandPath);
    			if ((sender instanceof Player)) {    
    				if(!cmd.hasPermission(sender)) {
    					sender.sendMessage(DataBase.fileMessage.getString("Command.NoPermission"));
    					return false;
    				}
            		try {
						cmd.run((Player)sender, commandLabel, command, args);
					} catch (Exception e) {
						e.printStackTrace();
					}
            	}else {
            		try {
						cmd.run(sender, commandLabel, command, args);
					} catch (Exception e) {
						e.printStackTrace();
					}
            	}
                return true;
            }
    	}
		return false;
		
	}
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    	return onTabComplete(sender,cmd,label,args,MobDrop2.class.getClassLoader(),"com.brian." + DataBase.pluginName + ".Command");
    }
    
    public List<String> onTabComplete(CommandSender sender, Command command, String commandLabel, String[] args,final ClassLoader classLoader, final String commandPath){
    	if(args.length == 1) {
    		List<String> show_commands = new ArrayList<String>();
    		for (String key : DataBase.getCommands(plugin)){
    			ImainCommandSystem cmd = getCommandClass(key,classLoader,commandPath);
    			if(key.indexOf(args[0].toLowerCase()) != -1 && cmd.hasPermission(sender))
    				show_commands.add(key);
    		}
    		return show_commands;
    	}else if(args.length > 1 && DataBase.getCommands(plugin).contains(args[0])){
    		ImainCommandSystem cmd = getCommandClass(args[0],classLoader,commandPath);
			if ((sender instanceof Player)) {    
				if(!cmd.hasPermission(sender))
					return Collections.emptyList();
        		try {
					return cmd.tabComplete((Player)sender, commandLabel, command, args);
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}else {
        		try {
        			return cmd.tabComplete(sender, commandLabel, command, args);
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
            return Collections.emptyList();
    	}
    	return Collections.emptyList();
    }
    
    /**
	 *  取得指令的類別(class)
	 * @param command 指令名稱
     * @return 該class資料
     */
    public static ImainCommandSystem getCommandClass(String command) {
    	ImainCommandSystem cmd = null;
        try {
            cmd = (ImainCommandSystem) MobDrop2.class.getClassLoader().loadClass("com.brian." + DataBase.pluginName + ".Command" + ".Command" + command).newInstance();
        }catch(InstantiationException ex) {
        	ex.printStackTrace();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
		return cmd;
    }
    
    /**
	 *  取得指令的類別(class)
	 * @param command 指令名稱
     * @param classLoader 抓取此插件讀取classLoader指令
     * @param commandPath 要抓取插件的檔案位置
     * @return 該class資料
     */
    private ImainCommandSystem getCommandClass(String command,final ClassLoader classLoader, final String commandPath) {
    	ImainCommandSystem cmd = null;
        try {
            cmd = (ImainCommandSystem) classLoader.loadClass(commandPath + ".Command" + command).newInstance();
        }catch(InstantiationException ex) {
        	ex.printStackTrace();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
		return cmd;
    }
    
    /**
     * 設定 server listener
     */
    private void setEvents(){
    	//Bukkit.getServer().getPluginManager().registerEvents(new ShopListener(), this);
    }
    
    /**
     * 重新讀取 資料
     */
    public static void reload() {
    	
    	plugin.reloadConfig();
    	DataBase.fileMessage.reloadFile();
    }
}
