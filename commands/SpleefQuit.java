package net.pixelors.spleef.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pixelors.spleef.Core;
import net.pixelors.spleef.utils.Arena;
import net.pixelors.spleef.utils.MessageApi;

public final class SpleefQuit extends SpleefCommand {

  public SpleefQuit () {
    super ("quit", new String[]{""});
  }

  @Override
  public void executor (CommandSender sender, String[] strings) {
    if (!(sender instanceof Player)){
	 sender.sendMessage (MessageApi.prefix + "§cYou must be logged in to execute this command.");
	 return;
    }

    Player p = (Player) sender;
    Arena a = Core.getOurInstance ().getArenaManager ().getArena (p);

    if (a==null){
	 sender.sendMessage (MessageApi.prefix + "§cYou must be in an arena to leave one...");
	 return;
    }

    Core.getOurInstance ().getArenaManager ().quitArena (p);
    p.sendMessage (MessageApi.prefix + "You have left the arena §l" + a.getName () + "§r!");
  }
}