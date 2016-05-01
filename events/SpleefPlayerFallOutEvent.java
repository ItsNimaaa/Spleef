package net.pixelors.spleef.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.pixelors.spleef.utils.Arena;

public class SpleefPlayerFallOutEvent extends Event {

	private Player player;
	private Arena arena;

	public SpleefPlayerFallOutEvent(Player player, Arena arena) {
		this.player = player;
		this.arena = arena;
	}

	public Player getPlayer() {
		return player;
	}

	public Arena getArena() {
		return arena;
	}

	private static HandlerList handlerList = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlerList;
	}

	@Override
	public HandlerList getHandlers() {
		return getHandlerList();
	}
	
	
}