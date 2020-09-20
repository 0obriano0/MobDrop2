package com.chengbrian.MobDrop2.Command;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chengbrian.MobDrop2.DataBase.DataBase;

public class mainCommandSystem implements ImainCommandSystem {
	private final transient String name;
	private final transient List<String> permissions;
	private final transient String help;
	
	protected mainCommandSystem(final String name,final String help,final List<String> permissions) {
        this.name = name;
        this.help = help;
        this.permissions = permissions;
    }
	
	@Override
	public String getName() {
        return name;
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
	public void run(CommandSender sender, String commandLabel, Command command, String[] args) throws Exception {
		sender.sendMessage(DataBase.fileMessage.getString("Command.CmdCanNotUse"));
	}

	@Override
	public void run(Player player, String commandLabel, Command command, String[] args) throws Exception {
		
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String commandLabel, Command command, String[] args) {
		return Collections.emptyList();
	}

	@Override
	public List<String> tabComplete(Player player, String commandLabel, Command command, String[] args) {
		return Collections.emptyList();
	}
}
