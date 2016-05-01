package net.pixelors.spleef.timer;

import org.bukkit.scheduler.BukkitRunnable;

import net.pixelors.spleef.utils.Arena;

public abstract class GameTimer extends BukkitRunnable {

  private Arena arena;

  public GameTimer (Arena arena) {
    this.arena = arena;
  }

  public Arena getArena () {
    return arena;
  }


}
