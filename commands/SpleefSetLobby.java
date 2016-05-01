package net.pixelors.spleef.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pixelors.spleef.utils.ConfigApi;
import net.pixelors.spleef.utils.MessageApi;

public final class SpleefSetLobby extends SpleefCommand {

  public SpleefSetLobby () {
    super ("setLobby", "spleef.create", new String[]{""});
  }

  @Override
  public void executor (CommandSender sender, String[] strings) {
    if (!(sender instanceof Player)){
	 sender.sendMessage (MessageApi.prefix + "?cYou must be logged in to execute this command.");
	 return;
    }
    Player pl = (Player) sender;
    ConfigApi.setPath ("lobbyLoc", pl.getLocation ());
    pl.sendMessage (MessageApi.prefix + "?aYou have defined spleef lobby at "
    + "?7(?f?l" + pl.getWorld ().getName () + "?7,?f?l" + Math.round (pl.getLocation ().getX ()) +
            "?7,?f?l" + Math.round (pl.getLocation ().getY ()) +
            "?7,?f?l" + Math.round (pl.getLocation ().getZ ()) +
            "?7)?a.");
  }
}
