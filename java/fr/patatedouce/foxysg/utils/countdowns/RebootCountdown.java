package fr.patatedouce.foxysg.utils.countdowns;

import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.managers.GameManager;
import fr.patatedouce.foxysg.managers.PlayerDataManager;
import fr.patatedouce.foxysg.player.PlayerData;
import fr.patatedouce.foxysg.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;


public class RebootCountdown extends BukkitRunnable {

    public int seconds;
    private long expire;

    public RebootCountdown() {
        seconds = GameManager.getInstance().getRebootCountdownValue();
        long duration = 1000 * seconds;
        expire = System.currentTimeMillis() + duration;
        this.runTaskTimer(FoxySG.getInstance(), 0L, 20L);
    }

    public void execute() {
        PlayerDataManager.getInstance().getPlayerDatas().values().forEach(PlayerData::save);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), GameManager.getInstance().getRebootCommand());
    }

    public void everySecond() {

    }

    public List<Integer> seconds() {
        return Arrays.asList(60 * 5, 60 * 4, 60 * 3, 60 * 2, 60, 30, 20, 10, 5, 4, 3, 2, 1);
    }

    public void everySeconds() {
        if (seconds > 60) {
            Utils.broadcastMessage(FoxySG.getInstance().getConfiguration("messages").getString("reboot-countdown-minutes")
                            .replaceAll("<minutes>", String.valueOf(seconds/60))
                    , true);
        } else {
            Utils.broadcastMessage(FoxySG.getInstance().getConfiguration("messages").getString("reboot-countdown-seconds")
                            .replaceAll("<seconds>", String.valueOf(seconds))
                    , true);
        }
    }

    public Sound playSound() {
        return Sound.NOTE_PLING;
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() - this.expire > 1;
    }

    public long getRemaining() {
        return this.expire - System.currentTimeMillis();
    }

    public int getSecondsLeft() {
        return (int) getRemaining() / 1000;
    }

    public String getTimeLeft() {
        return Utils.formatTime(getSecondsLeft());
    }

    public void cancelCountdown() {
        this.cancel();
        this.expire = 0;
    }

    @Override
    public void run() {
        --seconds;
        if (seconds().contains(seconds)) {
            everySeconds();
        }
        if (seconds == 0) {
            execute();
            cancel();
        }
    }
}
