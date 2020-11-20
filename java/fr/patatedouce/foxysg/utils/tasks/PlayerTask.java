package fr.patatedouce.foxysg.utils.tasks;

import fr.patatedouce.foxysg.managers.PlayerManager;
import fr.patatedouce.foxysg.utils.Utils;
import org.bukkit.entity.Player;

public class PlayerTask implements Runnable {

    @Override
    public void run() {
        for (Player online : Utils.getOnlinePlayers()) {
            PlayerManager.getInstance().getSpectatorPlayers().forEach(online::hidePlayer);
            //PlayerManager.getInstance().getPrematchPlayers().forEach(online::hidePlayer);
        }
        for (Player online : Utils.getOnlinePlayers()) {
            PlayerManager.getInstance().getGamePlayers().forEach(online::showPlayer);
        }
    }
}
