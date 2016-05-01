package net.pixelors.spleef;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.pixelors.spleef.events.SpleefArenaGameStateChangeEvent;
import net.pixelors.spleef.events.SpleefPlayerFallOutEvent;
import net.pixelors.spleef.events.SpleefPlayerJoinArenaEvent;
import net.pixelors.spleef.events.SpleefPlayerWinEvent;
import net.pixelors.spleef.timer.CleanupTimer;
import net.pixelors.spleef.timer.IngameTimer;
import net.pixelors.spleef.timer.WaitingTimer;
import net.pixelors.spleef.utils.Arena;
import net.pixelors.spleef.utils.ArenaManager;
import net.pixelors.spleef.utils.ConfigApi;
import net.pixelors.spleef.utils.GameState;
import net.pixelors.spleef.utils.MessageApi;

public class GameListener implements Listener {

	private Arena arena;

	public GameListener(Arena arena) {
		this.arena = arena;
	}

	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e) {
		final Player p = e.getPlayer();
		Entity entity = e.getRightClicked();

		if (entity instanceof Villager) {
			final Villager v = (Villager) entity;
			if (Core.getOurInstance().getGameNPC().getUniqueId().equals(v.getUniqueId())) {
				Inventory inv = Bukkit.createInventory(null, 27, v.getCustomName());
				p.openInventory(inv);
				new BukkitRunnable() {
					@Override
					public void run() {
						inv.clear();
						for (Arena a : Core.getOurInstance().getArenaManager().getArenas()) {
							ItemStack item = new ItemStack(Material.WOOL);
							ItemMeta meta = item.getItemMeta();
							meta.setDisplayName("§e§l" + a.getName());
							{
								List<String> lore = new ArrayList<String>();
								lore.add("§7§m----------------");
								lore.add("  ");
								lore.add("§7§lPLAYERS INGAME:§e " + a.getPlayerList().size());
								lore.add("§7§lPLAYERS NEEDED:§e " + Core.getOurInstance().MIN_PLAYERS);
								lore.add("   ");
								lore.add("§7§lGAMESTATE:§e " + a.getCurrentState().name());
								lore.add("§7§lTIME:§e " + MessageApi.timerTrans(a.getTicks()));
								lore.add(" ");
								lore.add("§7§m---------------- ");
								meta.setLore(lore);
							}
							item.setItemMeta(meta);
							switch (a.getCurrentState()) {
							case WAITING:
								item.setDurability((short) 5);
								break;
							case INGAME:
								item.setDurability((short) 4);
								break;
							case CLEANUP:
								item.setDurability((short) 14);
								break;
							}
							inv.addItem(item);
							p.updateInventory();
						}
						if (p.getOpenInventory() == null
								|| p.getOpenInventory().getTitle().equalsIgnoreCase(inv.getTitle())) {
							this.cancel();
						}
					}
				}.runTaskTimer(Core.getOurInstance(), 0L, 10L);
				
			} else {
				e.setCancelled(true);
			
			}
			}
			else {
				return;
			}
			
		

	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Inventory inv = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();

		if (inv == null)
			return;
		if (inv.getName().equalsIgnoreCase(Core.getOurInstance().getGameNPC().getCustomName())) {
			if (item == null)
				return;
			if (item.getType().equals(Material.WOOL)) {
				String name = item.getItemMeta().getDisplayName();
				Arena arena = Core.getOurInstance().getArenaManager().getArena(ChatColor.stripColor(name));
				Core.getOurInstance().getArenaManager().joinArena(p, arena);
				p.closeInventory();
			}
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerJoin(SpleefPlayerJoinArenaEvent e) {
		Player p = e.getPlayer();
		Arena a = e.getArena();

		p.setGameMode(GameMode.SURVIVAL);
		p.getInventory().clear();
		p.setFoodLevel(20);
		p.setSaturation(18);
		p.getInventory().setArmorContents(null);

		if (a.getPlayerList().size() + 1 == Core.getOurInstance().MIN_PLAYERS) {
			WaitingTimer waitingTimer = new WaitingTimer(a);
			waitingTimer.runTaskTimer(Core.getOurInstance(), 0L, 20L);
			a.broadcastMessage(
					MessageApi.prefix + "Enough players have joined the arena, the game will start shortly!");
		}
	}

	@SuppressWarnings("incomplete-switch")
	@EventHandler
	public void onGameStateChange(SpleefArenaGameStateChangeEvent e) {
		Arena a = e.getArena();
		GameState state = e.getNewState();
		switch (state) {
		case INGAME:
			a.setTicks(20);
			for (Player p : a.getPlayerList()) {
				p.getInventory().clear();
				ItemStack item = new ItemStack(Material.DIAMOND_SPADE);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName("§r" + p.getDisplayName() + "'s Spade");
				item.setItemMeta(meta);
				p.getInventory().addItem(item);

				IngameTimer ingameTimer = new IngameTimer(a);
				ingameTimer.runTaskTimer(Core.getOurInstance(), 0L, 20L);
			}
			break;
		case CLEANUP:
			CleanupTimer timer = new CleanupTimer(a);
			timer.runTaskTimer(Core.getOurInstance(), 0L, 20L);
			break;
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();

		b.getDrops().clear();

		if (p.isOp()) {
			e.getBlock().setType(Material.AIR);
		}
		if (!(this.arena.getCurrentState() == GameState.INGAME)) {
			if (!p.isOp()) {
				e.setCancelled(true);
			}
		} else if (this.arena.getCurrentState() == GameState.INGAME) {
			if (e.getBlock().getType() == Material.ICE) {

				e.setCancelled(true);
			}
			if (b.getType() != Material.ICE) {
				e.getBlock().setType(Material.AIR);
			}

			b.getDrops().remove(b);

		}
	}

	@EventHandler
	public void onBlockSet(BlockPlaceEvent e) {
		if (arena.getCurrentState().equals(GameState.INGAME)) {
			e.setCancelled(true);

			if (e.getBlock().getType() == Material.SNOW_BLOCK) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlock(BlockDamageEvent e) {
		Player p = e.getPlayer();
		Arena a = Core.getOurInstance().getArenaManager().getArena(p);
		if (a != null) {
			if (!p.getItemInHand().getType().equals(Material.DIAMOND_SPADE)
					|| !e.getBlock().getType().equals(Material.SNOW_BLOCK)
					|| !a.getCurrentState().equals(GameState.INGAME)) {
				e.setCancelled(true);
				return;
			}
			a.getBlocks().add(e.getBlock().getState());
			e.setInstaBreak(true);
			ConfigApi.setPath("stats." + e.getPlayer().getUniqueId().toString() + ".blocksBroken",
					ConfigApi.getPath("stats." + e.getPlayer().getUniqueId().toString() + ".blocksBroken") == null ? 1
							: ConfigApi.getIntPath("stats." + e.getPlayer().getUniqueId().toString() + ".blocksBroken")
									+ 1);
			ConfigApi.setPath("stats." + e.getPlayer().getUniqueId().toString() + ".player", e.getPlayer().getName());
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		Entity entity = e.getEntity();
		if (entity instanceof Player) {
			Player p = (Player) entity;
			if (e.getCause() != EntityDamageEvent.DamageCause.VOID) {
				e.setCancelled(Core.getOurInstance().getArenaManager().getArena(p) != null);
			}
		}
	}

	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		Entity entity = e.getEntity();
		if (entity instanceof Player) {
			Player p = (Player) entity;
			e.setCancelled(Core.getOurInstance().getArenaManager().getArena(p) != null);
		}
	}

	@EventHandler
	public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
		if (arena.getCurrentState().equals(GameState.INGAME)) {

			Player p = e.getPlayer();

			if (p.getGameMode() == GameMode.CREATIVE) {

				return;
			}
			e.setCancelled(true);
			p.setAllowFlight(false);
			p.setFlying(false);
			p.setVelocity(p.getLocation().getDirection().multiply(1.5).setY(1));
			p.playSound(p.getLocation(), Sound.NOTE_BASS_GUITAR, 2, 2);
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();

		if (Core.getOurInstance().getArenaManager().getArena(p) != null) {
			Arena a = Core.getOurInstance().getArenaManager().getArena(p);
			if (e.getTo().getBlock().getType().equals(Material.WATER)
					|| e.getTo().getBlock().getType().equals(Material.STATIONARY_WATER)) {
				a.broadcastMessage(
						MessageApi.prefix + p.getDisplayName() + " fell into the water. Better luck next time!");
				a.broadcastMessage(MessageApi.prefix + (a.getPlayerList().size() - 1) + " Players remaining.");

				Bukkit.getPluginManager().callEvent(new SpleefPlayerFallOutEvent(p, a));
				Core.getOurInstance().getArenaManager().quitArena(p);
				p.getInventory().clear();
				p.setHealth(20);
			}
			if (a.getCurrentState().equals(GameState.INGAME)) {
				if ((p.getGameMode() != GameMode.CREATIVE)
						&& (p.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR)
						&& (!p.isFlying())) {
					p.setAllowFlight(true);
				}
			}
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		ArenaManager am = Core.getOurInstance().getArenaManager();

		if (am.getArena(p) != null) {
			for (Player pl : Bukkit.getOnlinePlayers()) {
				if (am.getArena(pl) == null || am.getArena(pl) != am.getArena(p)) {
					if (e.getRecipients().contains(pl)) {
						e.getRecipients().remove(pl);
					}
				}
			}
		} else {
			for (Player pl : Bukkit.getOnlinePlayers()) {
				if (am.getArena(pl) != null) {
					if (e.getRecipients().contains(pl)) {
						e.getRecipients().remove(pl);
					}
				}
			}
		}
	}

	@EventHandler
	public void onFall(SpleefPlayerFallOutEvent e) {
		ConfigApi.setPath("stats." + e.getPlayer().getUniqueId().toString() + ".fallOuts",
				ConfigApi.getPath("stats." + e.getPlayer().getUniqueId().toString() + ".fallOuts") == null ? 1
						: ConfigApi.getIntPath("stats." + e.getPlayer().getUniqueId().toString() + ".fallOuts") + 1);
		ConfigApi.setPath("stats." + e.getPlayer().getUniqueId().toString() + ".player", e.getPlayer().getName());
	}

	@EventHandler
	public void onWin(SpleefPlayerWinEvent e) {
		ConfigApi.setPath("stats." + e.getPlayer().getUniqueId().toString() + ".wins",
				ConfigApi.getPath("stats." + e.getPlayer().getUniqueId().toString() + ".wins") == null ? 1
						: ConfigApi.getIntPath("stats." + e.getPlayer().getUniqueId().toString() + ".wins") + 1);
		ConfigApi.setPath("stats." + e.getPlayer().getUniqueId().toString() + ".player", e.getPlayer().getName());
	}

}
