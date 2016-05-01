package net.pixelors.spleef.timer;

import net.pixelors.spleef.Core;
import net.pixelors.spleef.utils.Arena;
import net.pixelors.spleef.utils.GameState;
import net.pixelors.spleef.utils.MessageApi;

public class WaitingTimer extends GameTimer {

	public WaitingTimer(Arena arena) {
		super(arena);
	}

	@Override
	public void run() {
		int ticks = getArena().getTicks();
		if (ticks >= 0) {
			if ((ticks % 10 == 0 || ticks <= 3) && ticks > 0) {
				getArena().broadcastMessage(
						MessageApi.prefix + " The game starts in §l" + MessageApi.timerTrans(ticks) + "§r!");
			}
			if (getArena().getPlayerList().size() < Core.getOurInstance().MIN_PLAYERS) {
				getArena().broadcastMessage(MessageApi.prefix + "§l Not enough players in arena, timer stopped!");
				this.cancel();
				return;
			}
			if (ticks == 0) {
				getArena().broadcastMessage(MessageApi.prefix + "§l The game has started!");
				getArena().setCurrentState(GameState.INGAME);
				this.cancel();
				return;
			} 
			ticks -= 1;
			getArena().setTicks(ticks);
		}
	}
}
