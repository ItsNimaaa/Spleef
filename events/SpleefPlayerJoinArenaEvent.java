package net.pixelors.spleef.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.pixelors.spleef.utils.Arena;

public class SpleefPlayerJoinArenaEvent extends Event implements Cancellable {

	private Arena arena;
	private Player player;

	public SpleefPlayerJoinArenaEvent(Arena arena, Player player) {
		this.arena = arena;
		this.player = player;
	}

	public Arena getArena() {
		return arena;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public void setCancelled(boolean b) {

	}

	private static HandlerList handlerList = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlerList;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}