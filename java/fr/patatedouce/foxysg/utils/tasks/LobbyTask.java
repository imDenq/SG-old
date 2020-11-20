package fr.patatedouce.foxysg.utils.tasks;

import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.enums.GameState;
import fr.patatedouce.foxysg.managers.GameManager;
import fr.patatedouce.foxysg.managers.PlayerManager;
import fr.patatedouce.foxysg.utils.Utils;
import org.bukkit.Sound;

public class LobbyTask implements Runnable {

    @Override
    public void run() {
        if (!GameManager.getInstance().getGameState().equals(GameState.LOBBY)) {
            return;
        }
        if (PlayerManager.getInstance().getLobbyPlayers().size() < GameManager.getInstance().getMinPlayers()) {
            if (GameManager.getInstance().getStartCountdown() != null && !GameManager.getInstance().getStartCountdown().hasExpired() && !GameManager.getInstance().isForceStarted()) {
                GameManager.getInstance().getStartCountdown().cancelCountdown();
                Utils.broadcastMessage(FoxySG.getInstance().getConfiguration("messages").getString("start-stopped-not-enough-players"), true);
                Utils.broadcastMessage(FoxySG.getInstance().getConfiguration("messages").getString("require-players-amount")
                        .replaceAll("<player_amount>", String.valueOf(GameManager.getInstance().getRequiredPlayersToJoin()))
                        , true);
                Utils.playSound(Sound.FIREWORK_BLAST);
            }
        }
        if (PlayerManager.getInstance().getLobbyPlayers().size() >= GameManager.getInstance().getMinPlayers()) {
            if (GameManager.getInstance().getStartCountdown() == null || (GameManager.getInstance().getStartCountdown() != null && GameManager.getInstance().getStartCountdown().hasExpired())) {
                if (GameManager.getInstance().getGameState().equals(GameState.LOBBY)) {
                    GameManager.getInstance().startGame();
                }
            }
        }
    }
}
