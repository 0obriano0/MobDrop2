package com.twsbrian.MobDrop2.Command;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twsbrian.MobDrop2.MobDrop2;
import com.twsbrian.MobDrop2.DataBase.DataBase;

public class mainCommandSystem implements ImainCommandSystem {
  private final transient String id;
  private final transient List<String> permissions;
  private final transient String help;
  private final transient String subCommand_path;
  private List<String> subCommands = null;
  private boolean rundefault;
  
  /**
   * 定義一個指令
   * @param id 指令ID
   * @param help 說明敘述
   * @param permissions 權限(type : List<String>)
   */
  protected mainCommandSystem(final String id,final String help,final List<String> permissions) {
        this.id = id;
        this.help = help;
        this.permissions = permissions;
        this.subCommand_path = null;
    }
  
  /**
   * 定義一個指令
   * @param id 指令ID
   * @param help 說明敘述
   * @param permissions 權限(type : List<String>)
   * @param subCommand_path 指令的路徑
   */
  protected mainCommandSystem(final String id,final String help,final List<String> permissions, String subCommand_path) {
    this.id = id;
    this.help = help;
    this.permissions = permissions;
    this.subCommand_path = subCommand_path;
  }
  
  @Override
  public String getName() {
    return id;
  }
  
  @Override
  public String getHelp() {
    return help;
  }
  
  @Override
  public List<String> getPermissions() {
    return permissions;
  }
  
  @Override
  public boolean hasPermission(CommandSender sender) {
    if (!(sender instanceof Player))
      return true;
    else
      return hasPermission((Player)sender);
  }
  
  @Override
  public boolean hasPermission(Player player) {
    if(player.isOp())
      return true;
    for(String per:permissions) {
      if (!player.hasPermission(per))
        return false;
    }
    return true;
  }
  
  @Override
  public void rundefault() {
    rundefault = true;
  }
  
  @Override
  public void run(CommandSender sender, String commandLabel, Command command, String[] args, final ClassLoader classLoader, final String commandPath) throws Exception {
    run(sender,commandLabel,command, args);
    if(rundefault) {
      if(!(subCommand_path == null || getsubCommands().size() == 0)) {
        String newcommandPath = commandPath + "." + subCommand_path.split("/")[subCommand_path.split("/").length-1];
        
        if(args.length == 0 && ToolCommandSystem.hasCommandClass("help",classLoader,newcommandPath)){
          ImainCommandSystem cmd = ToolCommandSystem.getCommandClass("help",classLoader,newcommandPath);
              if(!cmd.hasPermission(sender)) {
                sender.sendMessage(DataBase.fileMessage.getString("Command.NoPermission"));
              }
              try {
            cmd.run(sender, commandLabel + ".help", command, args, classLoader, newcommandPath);
          } catch (Exception e) {
            e.printStackTrace();
          }
            }else if (args.length != 0 && (commandLabel.equalsIgnoreCase(DataBase.pluginName.toLowerCase() + "." + id) || commandLabel.equalsIgnoreCase("mobdrop." + id) || commandLabel.equalsIgnoreCase("mdop." + id))) {
          if(!getsubCommands().contains(args[0])) {
            sender.sendMessage(DataBase.fileMessage.getString("Command.CanNotFind"));
          }
          if(args.length >= 1) {
            String[] newargs = Arrays.copyOfRange(args, 1, args.length);
              ImainCommandSystem cmd = ToolCommandSystem.getCommandClass(args[0],classLoader,newcommandPath);
              try {
              cmd.run(sender, commandLabel + "." + args[0], command, newargs, classLoader, newcommandPath);
            } catch (Exception e) {
              e.printStackTrace();
            }
                }
          }
      } else {
        sender.sendMessage(DataBase.fileMessage.getString("Command.CmdCanNotUse"));
      }
    }
  }

  @Override
  public void run(CommandSender sender, String commandLabel, Command command, String[] args) throws Exception {
    rundefault();
  }

  @Override
  public void run(Player player, String commandLabel, Command command, String[] args, final ClassLoader classLoader, final String commandPath) throws Exception {
    run(player,commandLabel,command, args);
    if(rundefault) {
      if(!(subCommand_path == null || getsubCommands().size() == 0)) {
        String newcommandPath = commandPath + "." + subCommand_path.split("/")[subCommand_path.split("/").length-1];
        
        if(args.length == 0 && ToolCommandSystem.hasCommandClass("help",classLoader,newcommandPath)){
          ImainCommandSystem cmd = ToolCommandSystem.getCommandClass("help",classLoader,newcommandPath);
              if(!cmd.hasPermission(player)) {
                DataBase.sendMessage(player,DataBase.fileMessage.getString("Command.NoPermission"));
              }
          try {
            cmd.run(player, commandLabel + ".help", command, args, classLoader, newcommandPath);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }else if (args.length != 0 && (commandLabel.equalsIgnoreCase(DataBase.pluginName.toLowerCase() + "." + id) || commandLabel.equalsIgnoreCase("mobdrop." + id) || commandLabel.equalsIgnoreCase("mdop." + id))) {
          if(!getsubCommands().contains(args[0])) {
            DataBase.sendMessage(player,DataBase.fileMessage.getString("Command.CanNotFind"));
          }
          if(args.length >= 1) {
            String[] newargs = Arrays.copyOfRange(args, 1, args.length);
              ImainCommandSystem cmd = ToolCommandSystem.getCommandClass(args[0],classLoader,newcommandPath);
              if(!cmd.hasPermission(player)) {
                DataBase.sendMessage(player,DataBase.fileMessage.getString("Command.NoPermission"));
              }
                  try {
              cmd.run(player, commandLabel + "." + args[0], command, newargs, classLoader, newcommandPath);
            } catch (Exception e) {
              if(DataBase.getDebug())  e.printStackTrace();
            }
                }
          } 
      } else {
          //預留
        }
    }
  }
  
  @Override
  public void run(Player player, String commandLabel, Command command, String[] args) throws Exception {
    rundefault();
  }

  @Override
  public List<String> tabComplete(CommandSender sender, String commandLabel, Command command, String[] args, final ClassLoader classLoader, final String commandPath) {
    if(subCommand_path == null || getsubCommands().size() == 0) return Collections.emptyList();
    String newcommandPath = commandPath + "." + subCommand_path.split("/")[subCommand_path.split("/").length-1];
    
    if(args.length == 1) {
        List<String> show_commands = new ArrayList<String>();
        for (String key : getsubCommands()){
          ImainCommandSystem cmd = ToolCommandSystem.getCommandClass(key,classLoader,newcommandPath);
          if(key.indexOf(args[0].toLowerCase()) != -1 && cmd.hasPermission(sender))
            show_commands.add(key);
        }
        return show_commands;
      }else if(args.length > 1 && getsubCommands().contains(args[0])){
        ImainCommandSystem cmd = ToolCommandSystem.getCommandClass(args[0],classLoader,newcommandPath);
        String[] newargs = Arrays.copyOfRange(args, 1, args.length);
        try {
          return cmd.tabComplete(sender, commandLabel + "." + args[0], command, newargs, classLoader, newcommandPath);
      } catch (Exception e) {
        e.printStackTrace();
      }
            return Collections.emptyList();
      }
      return Collections.emptyList();
  }

  @Override
  public List<String> tabComplete(Player player, String commandLabel, Command command, String[] args, final ClassLoader classLoader, final String commandPath) {
    if(subCommand_path == null || getsubCommands().size() == 0) return Collections.emptyList();
    String newcommandPath = commandPath + "." + subCommand_path.split("/")[subCommand_path.split("/").length-1];
    
    if(args.length == 1) {
        List<String> show_commands = new ArrayList<String>();
        for (String key : getsubCommands()){
          ImainCommandSystem cmd = ToolCommandSystem.getCommandClass(key,classLoader,newcommandPath);
          if(key.indexOf(args[0].toLowerCase()) != -1 && cmd.hasPermission(player))
            show_commands.add(key);
        }
        return show_commands;
      }else if(args.length > 1 && getsubCommands().contains(args[0])){
        ImainCommandSystem cmd = ToolCommandSystem.getCommandClass(args[0],classLoader,newcommandPath);
        String[] newargs = Arrays.copyOfRange(args, 1, args.length);
        if(!cmd.hasPermission(player))
        return Collections.emptyList();
        try {
        return cmd.tabComplete(player, commandLabel + "." + args[0], command, newargs, classLoader, newcommandPath);
      } catch (Exception e) {
        e.printStackTrace();
      }
            return Collections.emptyList();
      }
      return Collections.emptyList();
  }
  
  @Override
  public List<String> getsubCommands(){
    if(subCommands == null && subCommand_path != null) {
      subCommands = new ArrayList<String>();
      URL jarURL = MobDrop2.plugin.getClass().getResource("/com/twsbrian/" + DataBase.pluginName + "/Command" + subCommand_path);
        URI uri;
      try {
        FileSystem fileSystem = null;
        uri = jarURL.toURI();
        Path myPath;
        if (uri.getScheme().equals("jar")) {
            fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
            myPath = fileSystem.getPath("/com/twsbrian/"+ DataBase.pluginName +"/Command" + subCommand_path);
        } else {
            myPath = Paths.get(uri);
        }
        if (fileSystem == null) {
          return subCommands;
        }
        for (Iterator<Path> it = Files.walk(myPath, 1).iterator(); it.hasNext();){
          String[] path = it.next().toString().split("/");
          
          String file = path[path.length - 1];
          if(file.matches("(.*)class$")) {
            file = file.split("\\.")[0];
            if(file.matches("^Command.*")) {
              String filename = file.split("Command")[1];
              subCommands.add(filename);
            }
          }
          // System.out.println(it.next());
          Collections.sort(subCommands);
        }
        fileSystem.close();
      } catch (URISyntaxException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return subCommands == null? new ArrayList<String>() : subCommands;
    }
}
