package net.pixelors.spleef.timer;

import net.pixelors.spleef.utils.Arena;
import net.pixelors.spleef.utils.GameState;
import net.pixelors.spleef.utils.MessageApi;

public class IngameTimer extends GameTimer {

	public IngameTimer(Arena arena) {
		super(arena);
	}

	@Override
	public void run() {
		int ticks = getArena().getTicks();
		if (ticks >= 0) {
			if ((ticks == 20 || ticks == 10)) {
				getArena().broadcastMessage(
						MessageApi.prefix + " The game ends in §l" + MessageApi.timerTrans(ticks) + "§r!");
						
			} if (ticks == 5 && ticks > 0){
				getArena().broadcastMessage(MessageApi.prefix + "The game ends in " + MessageApi.timerTrans(ticks) + "!");
			}
			if (ticks == 0 || getArena().getPlayerList().size() <= 1 && getArena().getCurrentState().equals(GameState.INGAME)) {

				getArena().broadcastMessage(MessageApi.prefix + "§l Cleanup has started!");
				getArena().setCurrentState(GameState.CLEANUP);
				getArena().setTicks(10);
				this.cancel();
				return;
			}
			getArena().setTicks(ticks--);
		} if(ticks >= 0){
			getArena().setTicks(10);
		}
	}
}