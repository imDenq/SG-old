package fr.patatedouce.foxysg.border;

import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.managers.GameManager;
import fr.patatedouce.foxysg.utils.Utils;
import fr.patatedouce.foxysg.utils.chat.Color;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

@Getter
@Setter
public class BorderManager {
    @Getter
    public static BorderManager instance;
    private Border border;
    private BorderTask borderTask;
    private int startBorder = FoxySG.getInstance().getConfiguration("config").getInt("BORDER.START");
    private int shrinkUntil = FoxySG.getInstance().getConfiguration("config").getInt("BORDER.SHRINK-UNTIL");
    private int shrinkEvery = FoxySG.getInstance().getConfiguration("config").getInt("BORDER.SHRINK-EVERY-SECONDS");

    public BorderManager() {
        instance = this;
    }

    public void startBorderShrink() {
        this.border = new Border();
        this.borderTask = new BorderTask();
        this.borderTask.runTaskTimer(FoxySG.getInstance(), 20L, 20L);
        Utils.broadcastMessage(GameManager.getInstance().getBorderPrefix() + FoxySG.getInstance().getConfiguration("messages").getString("start-border-strink")
                .replaceAll("<border_minutes>", String.valueOf(border.getSeconds() / 60))
                .replaceAll("<border_seconds>", String.valueOf(border.getSeconds()))
                .replaceAll("<last_border>", String.valueOf(border.getLastBorder()))
        );
    }

    public String getBorderInfo() {
        if (this.border == null) { return "";}

        if (this.border.getSize() == this.border.getLastBorder()) {
            return "";
        } else if (this.border.getSeconds() <= 60) {
            return Color.translate(FoxySG.getInstance().getConfiguration("scoreboard").getString("border-info.seconds")
                    .replaceAll("<seconds>", String.valueOf(this.border.getSeconds()))
            );
        } else {
            return Color.translate(FoxySG.getInstance().getConfiguration("scoreboard").getString("border-info.minutes")
                    .replaceAll("<minutes>", String.valueOf((this.border.getSeconds() / 60) + 1))
            );
        }
    }

    public void checkBorder(Player player) {
        Border border = getBorder();
        if (border == null) {
            return;
        }
        int size = border.getSize();
        World w = player.getWorld();

        if (!w.getName().equals(Bukkit.getWorld("world").getName())) {
            if (w.getEnvironment().equals(World.Environment.NETHER)) {
                return;
            }
            if (player.getLocation().getBlockX() > size) {
                player.teleport(new Location(w, (size - 2), player.getLocation().getBlockY(),
                        player.getLocation().getBlockZ()));
                player.setVelocity(player.getLocation().getDirection().multiply(1.0D));
                if (player.getLocation().getBlockY() < w.getHighestBlockYAt(player.getLocation().getBlockX(),
                        player.getLocation().getBlockZ())) {
                    player.teleport(new Location(w, player.getLocation().getBlockX(),
                            (w.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ())
                                    + 2),
                            player.getLocation().getBlockZ()));
                }
                player.sendMessage(GameManager.getInstance().getBorderPrefix() + Color.translate(FoxySG.getInstance().getConfiguration("messages").getString("stay-in-border")));
                player.playSound(player.getLocation(), Sound.EXPLODE, 1F, 1F);
                player.spigot().setCollidesWithEntities(true);
            }
            if (player.getLocation().getBlockZ() > size) {
                player.teleport(new Location(w, player.getLocation().getBlockX(), player.getLocation().getBlockY(),
                        (size - 2)));
                if (player.getLocation().getBlockY() < w.getHighestBlockYAt(player.getLocation().getBlockX(),
                        player.getLocation().getBlockZ())) {
                    player.teleport(new Location(w, player.getLocation().getBlockX(),
                            (w.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ())
                                    + 2),
                            player.getLocation().getBlockZ()));
                }
                player.sendMessage(GameManager.getInstance().getBorderPrefix() + Color.translate(FoxySG.getInstance().getConfiguration("messages").getString("stay-in-border")));
                player.playSound(player.getLocation(), Sound.EXPLODE, 1F, 1F);
                player.spigot().setCollidesWithEntities(true);
            }
            if (player.getLocation().getBlockX() < -size) {
                player.teleport(new Location(w, (-size + 2), player.getLocation().getBlockY(),
                        player.getLocation().getBlockZ()));
                if (player.getLocation().getBlockY() < w.getHighestBlockYAt(player.getLocation().getBlockX(),
                        player.getLocation().getBlockZ())) {
                    player.teleport(new Location(w, player.getLocation().getBlockX(),
                            (w.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ())
                                    + 2),
                            player.getLocation().getBlockZ()));
                }
                player.sendMessage(GameManager.getInstance().getBorderPrefix() + Color.translate(FoxySG.getInstance().getConfiguration("messages").getString("stay-in-border")));
                player.playSound(player.getLocation(), Sound.EXPLODE, 1F, 1F);
                player.spigot().setCollidesWithEntities(true);
            }
            if (player.getLocation().getBlockZ() < -size) {
                player.teleport(new Location(w, player.getLocation().getBlockX(), player.getLocation().getBlockY(),
                        (-size + 2)));
                if (player.getLocation().getBlockY() < w.getHighestBlockYAt(player.getLocation().getBlockX(),
                        player.getLocation().getBlockZ())) {
                    player.teleport(new Location(w, player.getLocation().getBlockX(),
                            (w.getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ())
                                    + 2),
                            player.getLocation().getBlockZ()));
                }
                player.sendMessage(GameManager.getInstance().getBorderPrefix() + Color.translate(FoxySG.getInstance().getConfiguration("messages").getString("stay-in-border")));
                player.playSound(player.getLocation(), Sound.EXPLODE, 1F, 1F);
                player.spigot().setCollidesWithEntities(true);
            }
        }
    }
}
