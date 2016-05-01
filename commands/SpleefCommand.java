package net.pixelors.spleef.commands;

import org.bukkit.command.CommandSender;

public abstract class SpleefCommand {
  private String label, permission;
  private String[] strings;

  public SpleefCommand (String label, String permission, String[] strings) {
    this.label = label;
    this.permission = permission;
    this.strings = strings;
  }

  public SpleefCommand (String label, String[] strings) {
    this.label = label;
    this.strings = strings;
    this.permission = "spleef.default";
  }

  public String getLabel () {
    return label;
  }

  public String getPermission () {
    return permission;
  }

  public String[] getStrings () {
    return strings;
  }

  public abstract void executor(CommandSender sender, String[] strings);
}
