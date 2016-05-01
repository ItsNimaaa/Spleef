package net.pixelors.spleef.utils;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import net.pixelors.spleef.Core;
import net.pixelors.spleef.events.SpleefPlayerJoinArenaEvent;
import net.pixelors.spleef.events.SpleefPlayerQuitArenaEvent;

public class ArenaManager {

	private List<Arena> arenas = new ArrayList<Arena>();
	
	public Arena arena;

	public Location getLobby() {
		return ConfigApi.getLocationPath("lobbyLoc");
	}

	public ArenaManager() {
		if (ConfigApi.getStringPath("lobbyLoc") != null) {
			if (Core.getOurInstance().getConfig().getConfigurationSection("arenas") != null) {
				for (String arenas : Core.getOurInstance().getConfig().getConfigurationSection("arenas")
						.getKeys(false)) {
					try {
						Arena arena = new Arena(ConfigApi.getLocationPath("arenas." + arenas + ".spawn"),
								ConfigApi.getStringPath("arenas." + arenas + ".name"));
						registerArena(arena);
					} catch (Exception c) {
						c.printStackTrace();
					}
				}
			}
		}
	}

	public void cleanUp(Arena arena) {
		do {
			try {
				for (BlockState blockState : arena.blocks) {
					blockState.update(true, false);
					arena.getBlocks().remove(blockState);
				}
			} catch (ConcurrentModificationException ignore) {
			}
		} while (!arena.getBlocks().isEmpty());
		try {
			for (Player p : arena.playerList) {
				p.getInventory().clear();
				quitArena(p);
			}
		} catch (ConcurrentModificationException ignore) {
		} finally {
			arena.setCurrentState(GameState.WAITING);
		}

	}

	public Arena registerArena(Arena arena) {
		ConfigApi.setPath("arenas." + arena.getName() + ".name", arena.getName());
		ConfigApi.setPath("arenas." + arena.getName() + ".spawn", arena.getSpawn());
		arenas.add(arena);
		return arena;
	}

	public List<Arena> getArenas() {
		return arenas;
	}

	public Arena getArena(String name) {
		for (Arena arena : arenas) {
			if (arena.getName().equalsIgnoreCase(name)) {
				return arena;
			}
		}
		return null;
	}

	public Arena getArena(Player player) {
		for (Arena arena : arenas) {
			if (arena.getPlayerList().contains(player)) {
				return arena;
			}
		}
		return null;
	}

	public void joinArena(Player player, Arena arena) {
		if(arena.getCurrentState().equals(GameState.INGAME)){
			player.sendMessage(MessageApi.prefix + "§cThis round is currently running!");
			return;
		}
		
		Bukkit.getPluginManager().callEvent(new SpleefPlayerJoinArenaEvent(arena, player));
		arena.getPlayerList().add(player);
		player.teleport(arena.getSpawn());
		
	}

	public void quitArena(Player player) {
		Arena arena = getArena(player);

		Bukkit.getPluginManager().callEvent(new SpleefPlayerQuitArenaEvent(arena, player));
		arena.getPlayerList().remove(player);
		player.teleport(getLobby());
	}

}