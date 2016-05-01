package net.pixelors.spleef.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pixelors.spleef.Core;
import net.pixelors.spleef.utils.Arena;
import net.pixelors.spleef.utils.MessageApi;

public final class SpleefCreate extends SpleefCommand {

  public SpleefCreate () {
    super ("create", "spleef.create", new String[]{"§7<NAME>"});
  }

  @Override
  public void executor (CommandSender sender, String[] strings) {
    if (!(sender instanceof Player)){
	 sender.sendMessage (MessageApi.prefix + "§cYou must be logged in to execute this command.");
	 return;
    }
    Player pl = (Player) sender;
    if (strings.length<2){
	 sender.sendMessage (MessageApi.prefix + "§cYou must include the name of the arena!");
	 return;
    }
    Core.getOurInstance ().getArenaManager ().registerArena (new Arena (pl.getLocation (), strings[1]));
    pl.sendMessage (MessageApi.prefix + "§aYou have created the arena §f§l" + strings[1] + "§a at "
    + "§7(§f§l" + pl.getWorld ().getName () + "§7,§f§l" + Math.round (pl.getLocation ().getX ()) + "§7,§f§l" + Math.round (pl.getLocation ().getY ()) + "§7,§f§l" + Math.round (pl.getLocation ().getZ ()) + "§7)�§.");
  }
}