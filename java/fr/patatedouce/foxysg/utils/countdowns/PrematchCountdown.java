package fr.patatedouce.foxysg.utils.countdowns;

import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.border.BorderManager;
import fr.patatedouce.foxysg.enums.GameState;
import fr.patatedouce.foxysg.enums.PlayerState;
import fr.patatedouce.foxysg.managers.GameManager;
import fr.patatedouce.foxysg.managers.PlayerDataManager;
import fr.patatedouce.foxysg.managers.PlayerManager;
import fr.patatedouce.foxysg.managers.WorldsManager;
import fr.patatedouce.foxysg.player.PlayerData;
import fr.patatedouce.foxysg.utils.ItemBuilder;
import fr.patatedouce.foxysg.utils.Utils;
import fr.patatedouce.foxysg.utils.runnables.GameRunnable;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;


public class PrematchCountdown extends BukkitRunnable {

    public int seconds;
    private long expire;

    public PrematchCountdown() {
        seconds = GameManager.getInstance().getPrematchCountdownValue();
        long duration = 1000 * seconds;
        expire = System.currentTimeMillis() + duration;
        this.runTaskTimer(FoxySG.getInstance(), 0L, 20L);
    }

    public void execute() {
        Utils.broadcastMessage(FoxySG.getInstance().getConfiguration("messages").getString("game-started"), true);
        PlayerManager.getInstance().getPrematchPlayers().forEach(player -> {
            PlayerData data = PlayerDataManager.getInstance().getByUUID(player.getUniqueId());

            Utils.clearPlayer(player);
            player.teleport(GameManager.getInstance().getGameWorldCenterLocation());
            data.setState(PlayerState.INGAME);
            data.getGamesPlayed().increaseAmount(1);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setGameMode(GameMode.SURVIVAL);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 49, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 15, 14));
            player.getInventory().addItem(new ItemBuilder(Material.COMPASS).setNameWithArrows(FoxySG.getInstance().getConfiguration("items").getString("player-tracker.name")).toItemStack());
        });
        WorldsManager.getInstance().getGameWorld().setPVP(false);
        GameManager.getInstance().setGameState(GameState.INGAME);
        BorderManager.getInstance().startBorderShrink();
        GameManager.getInstance().setGameRunnable(new GameRunnable());
        Bukkit.getScheduler().scheduleSyncDelayedTask(FoxySG.getInstance(), () -> {
            GameManager.getInstance().setPvpCountdown(new PvPCountdown());
            GameManager.getInstance().setFeastCountdown(new FeastCountdown());
            GameManager.getInstance().setDeathMatchCountdown(new DeathMatchCountdown());
        }, 20L);
    }

    public List<Integer> seconds() {
        return Arrays.asList(60 * 5, 60 * 4, 60 * 3, 60 * 2, 60, 30, 20, 10, 5, 4, 3, 2, 1);
    }

    public void everySeconds() {
        if (seconds > 60) {
            Utils.broadcastMessage(FoxySG.getInstance().getConfiguration("messages").getString("game-begin-countdown-minutes")
                            .replaceAll("<minutes>", String.valueOf(seconds/60))
                    , true);
        } else {
            Utils.broadcastMessage(FoxySG.getInstance().getConfiguration("messages").getString("game-begin-countdown-seconds")
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
