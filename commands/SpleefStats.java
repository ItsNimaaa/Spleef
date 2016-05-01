package net.pixelors.spleef.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pixelors.spleef.utils.ConfigApi;
import net.pixelors.spleef.utils.MessageApi;

public final class SpleefStats extends SpleefCommand {

  public SpleefStats () {
    super ("stats", new String[]{""});
  }

  @Override
  public void executor (CommandSender sender, String[] strings) {
    if (!(sender instanceof Player)){
	 sender.sendMessage (MessageApi.prefix + "§cYou must be logged in.");
    }
    Player p = (Player) sender;
    ConfigApi.setPath ("stats." + p.getUniqueId ().toString () + ".player", p.getName ());
    sender.sendMessage ("---------§eStats§r---------");
    if (ConfigApi.getPath ("stats." + p.getUniqueId ().toString () + ".fallOuts")==null){
	 sender.sendMessage (" §e-§r FallOuts:§7 0");
    }else {
	 sender.sendMessage (" §e-§r FallOuts:§7 " + ConfigApi.getIntPath ("stats." + p.getUniqueId ().toString () + ".fallOuts"));
    }

    if (ConfigApi.getPath ("stats." + p.getUniqueId ().toString () + ".wins")==null){
	 sender.sendMessage (" §e-§r Wins:§7 0");
    }else {
	 sender.sendMessage (" §e-§r Wins:§7 " + ConfigApi.getIntPath ("stats." + p.getUniqueId ().toString () + ".wins"));
    }

    if (ConfigApi.getPath ("stats." + p.getUniqueId ().toString () + ".blocksBroken")==null){
	 sender.sendMessage (" §e-§r BlocksBroken:§7 0");
    }else {
	 sender.sendMessage (" §e-§r BlocksBroken:§7 " + ConfigApi.getIntPath ("stats." + p.getUniqueId ().toString () + ".blocksBroken"));
    }
  }
}