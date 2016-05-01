package net.pixelors.spleef.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.pixelors.spleef.Core;

public class ConfigApi {

  public static void setPath(String path, String value){
    Core.getOurInstance ().getConfig ().set (path, value);
    Core.getOurInstance ().saveConfig ();
  }

  public static void setPath(String path, int value){
    Core.getOurInstance ().getConfig ().set (path, value);
    Core.getOurInstance ().saveConfig ();
  }

  public static void setPath(String path, Location value){
    String locationValue = "";
    {
	 locationValue = locationValue + value.getWorld ().getName () + "|";
	 locationValue = locationValue + (Math.round (value.getX ())) + "|";
	 locationValue = locationValue + (Math.round (value.getY ())) + "|";
	 locationValue = locationValue + (Math.round (value.getZ ()));
    }
    Core.getOurInstance ().getConfig ().set (path, locationValue);
    Core.getOurInstance ().saveConfig ();
  }

  public static String getStringPath(String path){
    return Core.getOurInstance ().getConfig ().getString (path);
  }

  public static int getIntPath(String path){
    return Core.getOurInstance ().getConfig ().getInt (path);
  }

  public static Object getPath(String path){
    return Core.getOurInstance ().getConfig ().get (path);
  }

  public static Location getLocationPath(String path){
    String[] data = Core.getOurInstance ().getConfig ().getString (path).split ("\\|");
    return new Location (Bukkit.getWorld (data[0]), Double.parseDouble (data[ 1 ]), Double.parseDouble (data[2]), Double.parseDouble (data[3]));
  }

}