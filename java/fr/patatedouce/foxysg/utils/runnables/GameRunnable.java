package fr.patatedouce.foxysg.utils.runnables;

import lombok.Getter;
import lombok.Setter;
import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.enums.GameState;
import fr.patatedouce.foxysg.events.SGGameWinEvent;
import fr.patatedouce.foxysg.managers.GameManager;
import fr.patatedouce.foxysg.managers.PlayerManager;
import fr.patatedouce.foxysg.utils.Utils;
import fr.patatedouce.foxysg.utils.chat.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public class GameRunnable implements Runnable{
    private int seconds = 0;
    private boolean announced = false;

    public GameRunnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(FoxySG.getInstance(), this, 20L, 20L);
    }

    @Override
    public void run() {
        if (GameManager.getInstance().getGameState().equals(GameState.INGAME)) {
            seconds++;
            if (PlayerManager.getInstance().getGamePlayers().size() == 1) {
                if (!announced) {
                    Player winer = PlayerManager.getInstance().getGamePlayers().get(0);
                    setAnnounced(true);
                    Bukkit.getServer().getPluginManager().callEvent(new SGGameWinEvent(winer));
                }
            } else if (PlayerManager.getInstance().getGamePlayers().size() <= 0 || PlayerManager.getInstance().getGamePlayers().isEmpty()) {
                if (!announced) {
                    Bukkit.broadcastMessage(Color.translate("&cIl n'y a aucun joueur dans la partie. le serveur va redémarrer dans 10secondes"));
                    setAnnounced(true);
                    new BukkitRunnable() {
                        public void run() {
                            Bukkit.shutdown();
                        }
                    }.runTaskLaterAsynchronously(FoxySG.getInstance(), 200L);
                }
            }
            /*if (PlayerManager.getInstance().getGamePlayers().size() == 1) {
                if (!announced) {
                    setAnnounced(true);
                    Bukkit.getServer().getPluginManager().callEvent(new SGGameWinEvent());
                }
            }*/
        }
    }

    public String getTime() {
        return Utils.formatTime(seconds);
    }
}
