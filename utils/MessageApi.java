package net.pixelors.spleef.utils;

public class MessageApi {
  public static String prefix = "§b§lSPLEEF§3§ ";
  public static String noPerm = prefix + "§cYou can't use this here.";
  public static String timerTrans(int ticks){
    int rema = ticks%3600;
    int mins = rema/60;
    int secs = rema%60;

    String sec = String.valueOf (secs);

    if (secs < 10){
	 sec = "0" + secs;
    }

    if (mins!=0) {
	 if (mins >= 10) {
	   return mins + ":" + sec;
	 }else {
	   return "0" + mins + ":" + sec;
	 }
    }else {
	 return "00:" + sec;
    }
  }
}
