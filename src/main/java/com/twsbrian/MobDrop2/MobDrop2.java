package com.twsbrian.MobDrop2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.twsbrian.MobDrop2.Command.ImainCommandSystem;
import com.twsbrian.MobDrop2.Command.ToolCommandSystem;
import com.twsbrian.MobDrop2.DataBase.DataBase;
import com.twsbrian.MobDrop2.DataBase.itemset.Glow;
import com.twsbrian.MobDrop2.Listener.ChatListener;
import com.twsbrian.MobDrop2.Listener.DeathListener;
import com.twsbrian.MobDrop2.Listener.ItemDropListener;
import com.twsbrian.MobDrop2.Listener.ItemPickupListener;

public class MobDrop2 extends JavaPlugin{
  public static Plugin plugin;
  public static Server server;
  
  @Override
    public void onEnable() {
    plugin = this;
        server = this.getServer();
        
        saveDefaultConfig();
        reloadConfig();
        DataBase.settingItemDropColorByChance();
        
        setEvents();
        DataBase.fileMessage.reloadFile();
        DataBase.fileDataBaseInfo.reloadFile();
        DataBase.fileInventory.reloadFile();

  }
  
  @Override
    public void onDisable() {
    try {
            Glow.unregister();
        } catch (NoClassDefFoundError error) {
            // this try/catch block is to prevent console spam
        }
    }
  
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      return Command(sender,command,label,args,MobDrop2.class.getClassLoader(),"com.twsbrian." + DataBase.pluginName + ".Command");
    }
  
  public boolean Command(CommandSender sender, Command command, String commandLabel, String[] args,final ClassLoader classLoader, final String commandPath) {
    if(args.length == 0){
          ImainCommandSystem cmd = ToolCommandSystem.getCommandClass("help",classLoader,commandPath);
          if(!cmd.hasPermission(sender)) {
            sender.sendMessage(DataBase.fileMessage.getString("Command.NoPermission"));
        return false;
          }
          try {
        cmd.run(sender, commandLabel + ".help", command, args, classLoader, commandPath);
      } catch (Exception e) {
        e.printStackTrace();
      }
        }else if (args.length != 0 && (commandLabel.equalsIgnoreCase(DataBase.pluginName.toLowerCase()) || commandLabel.equalsIgnoreCase("mobdrop") || commandLabel.equalsIgnoreCase("mdop"))) {
      if(!DataBase.getCommands(plugin).contains(args[0])) {
        sender.sendMessage(DataBase.fileMessage.getString("Command.CanNotFind"));
        return false;
      }
      if(args.length >= 1) {
        String[] newargs = Arrays.copyOfRange(args, 1, args.length);
          ImainCommandSystem cmd = ToolCommandSystem.getCommandClass(args[0],classLoader,commandPath);
          if ((sender instanceof Player)) {    
            if(!cmd.hasPermission(sender)) {
              sender.sendMessage(DataBase.fileMessage.getString("Command.NoPermission"));
              return false;
            }
                try {
            cmd.run((Player)sender, commandLabel + "." + args[0], command, newargs, classLoader, commandPath);
          } catch (Exception e) {
            e.printStackTrace();
          }
              }else {
                try {
            cmd.run(sender, commandLabel + "." + args[0], command, newargs, classLoader, commandPath);
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
      return onTabComplete(sender,cmd,label,args,MobDrop2.class.getClassLoader(),"com.twsbrian." + DataBase.pluginName + ".Command");
    }
    
    public List<String> onTabComplete(CommandSender sender, Command command, String commandLabel, String[] args, final ClassLoader classLoader, final String commandPath){
      if(args.length == 1) {
        List<String> show_commands = new ArrayList<String>();
        for (String key : DataBase.getCommands(plugin)){
          ImainCommandSystem cmd = ToolCommandSystem.getCommandClass(key,classLoader,commandPath);
          if(key.indexOf(args[0].toLowerCase()) != -1 && cmd.hasPermission(sender))
            show_commands.add(key);
        }
        return show_commands;
      }else if(args.length > 1 && DataBase.getCommands(plugin).contains(args[0])){
        ImainCommandSystem cmd = ToolCommandSystem.getCommandClass(args[0],classLoader,commandPath);
        String[] newargs = Arrays.copyOfRange(args, 1, args.length);
      if ((sender instanceof Player)) {    
        if(!cmd.hasPermission(sender))
          return Collections.emptyList();
            try {
          return cmd.tabComplete((Player)sender, commandLabel + "." + args[0], command, newargs, classLoader, commandPath);
        } catch (Exception e) {
          e.printStackTrace();
        }
          }else {
            try {
              return cmd.tabComplete(sender, commandLabel + "." + args[0], command, newargs, classLoader, commandPath);
        } catch (Exception e) {
          e.printStackTrace();
        }
          }
            return Collections.emptyList();
      }
      return Collections.emptyList();
    }
    
    /**
     * 設定 server listener
     */
    private void setEvents(){
      //Bukkit.getServer().getPluginManager().registerEvents(new ShopListener(), this);
      getServer().getPluginManager().registerEvents(new DeathListener(), this);
      getServer().getPluginManager().registerEvents(new ChatListener(), this);
      getServer().getPluginManager().registerEvents(new ItemDropListener(), this);
      getServer().getPluginManager().registerEvents(new ItemPickupListener(), this);
    }
    
    /**
     * 重新讀取 資料
     */
    public static void reload() {
      plugin.reloadConfig();
      DataBase.settingItemDropColorByChance();
      
      DataBase.fileMessage.reloadFile();
      DataBase.fileDataBaseInfo.reloadFile();
      DataBase.fileInventory.reloadFile();
    }
}
