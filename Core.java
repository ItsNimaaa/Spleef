package net.pixelors.spleef;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;

import net.pixelors.spleef.commands.CommandManager;
import net.pixelors.spleef.utils.Arena;
import net.pixelors.spleef.utils.ArenaManager;
import net.pixelors.spleef.utils.ConfigApi;
import net.pixelors.spleef.utils.MessageApi;

public class Core extends JavaPlugin {

	private static Core ourInstance;

	public static Core getOurInstance() {
		return ourInstance;
	}

	private ArenaManager arenaManager;
	private Villager gameNPC;

	public final int MIN_PLAYERS = 2;

	public Villager getGameNPC() {
		return gameNPC;
	}
	public ArenaManager getArenaManager() {
		return arenaManager;
	}

	@Override
	public void onLoad() {
		super.onLoad();
	}

	@Override
	public void onDisable() {
		if (gameNPC != null) {
			gameNPC.remove();
			gameNPC = null;
		}
		for (Arena a : arenaManager.getArenas()) {
			Core.getOurInstance().getArenaManager().cleanUp(a);
		}
	}

	@Override
	public void onEnable() {
		ourInstance = this;
		arenaManager = new ArenaManager();

		getCommand("spleef").setExecutor(new CommandManager());
		spawnNPC();

		Bukkit.getPluginManager().registerEvents(new GameListener(null), this);
	}

	public void spawnNPC() {
		if (gameNPC != null) {
			gameNPC.remove();
			gameNPC = null;
		}
		try {
			Villager villager = ConfigApi.getLocationPath("npc").getWorld().spawn(ConfigApi.getLocationPath("npc"),
					Villager.class);
			villager.setCustomName("" +ChatColor.WHITE + ChatColor.BOLD + "Spleef");
			villager.setCustomNameVisible(true);
			
			gameNPC = villager;
		} catch (NullPointerException ignore) {
			getLogger().log(Level.WARNING, MessageApi.prefix + "§cGameNPC is not set!");
		}

	}

}
