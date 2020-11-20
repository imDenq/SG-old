package fr.patatedouce.foxysg.utils.leaderboards;


import com.mongodb.BasicDBObject;
import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.managers.MongoManager;
import fr.patatedouce.foxysg.managers.PlayerDataManager;
import fr.patatedouce.foxysg.player.PlayerData;
import fr.patatedouce.foxysg.utils.ItemBuilder;
import fr.patatedouce.foxysg.utils.chat.Color;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class LeaderboardManager {
    @Getter public static LeaderboardManager instance;
    private List<Leaderboard> leaderboardList = new ArrayList<>();
    private List<Document> top5Wins = MongoManager.getInstance().getStatsCollection().find().limit(5).sort(new BasicDBObject("wins", -1)).into(new ArrayList<>());
    private List<Document> allStatistics = MongoManager.getInstance().getStatsCollection().find().into(new ArrayList<>());
    public LeaderboardManager() {
        instance = this;
        leaderboardList.addAll(Arrays.asList(
                new Leaderboard(Material.DIAMOND_SWORD,"Kills", "kills", FoxySG.getInstance().getConfiguration("inventory").getBoolean("leaderboard.kills.enabled")),
                new Leaderboard(Material.SKULL_ITEM,"Deaths", "deaths", FoxySG.getInstance().getConfiguration("inventory").getBoolean("leaderboard.deaths.enabled")),
                new Leaderboard(Material.NETHER_STAR,"Wins", "wins", FoxySG.getInstance().getConfiguration("inventory").getBoolean("leaderboard.wins.enabled")),
                new Leaderboard(Material.GOLD_NUGGET,"Points", "points", FoxySG.getInstance().getConfiguration("inventory").getBoolean("leaderboard.points.enabled")),
                new Leaderboard(Material.BOOK,"Games Played", "gamesPlayed", FoxySG.getInstance().getConfiguration("inventory").getBoolean("leaderboard.games-played.enabled")),
                new Leaderboard(Material.PAINTING,"KillStreak", "killStreak", FoxySG.getInstance().getConfiguration("inventory").getBoolean("leaderboard.kill-streak.enabled")),
                new Leaderboard(Material.GOLDEN_APPLE, "Golden Apple Eaten", "goldenApplesEaten", FoxySG.getInstance().getConfiguration("inventory").getBoolean("leaderboard.golden-apple-eaten.enabled")),
                new Leaderboard(Material.BOW, "Bow Shots", "bowShots", FoxySG.getInstance().getConfiguration("inventory").getBoolean("leaderboard.bow-shots.enabled")),
                new Leaderboard(Material.CHEST, "Chest Broke", "chestBroke", FoxySG.getInstance().getConfiguration("inventory").getBoolean("leaderboard.chest-broke.enabled")),
                new Leaderboard(Material.POTION, "Potion Splashed", "potionSplashed", FoxySG.getInstance().getConfiguration("inventory").getBoolean("leaderboard.potion-splashed.enabled")),
                new Leaderboard(Material.POTION, "Potion Drank", "potionDrank", FoxySG.getInstance().getConfiguration("inventory").getBoolean("leaderboard.potion-drank.enabled"))
                ));
    }

    public void updateAllLeaderboards() {

        new BukkitRunnable() {
            public void run() {
                for (Leaderboard leaderboard : getLeaderboardList()) {
                    leaderboard.load();
                }
                allStatistics = MongoManager.getInstance().getStatsCollection().find().into(new ArrayList<>());
                top5Wins = MongoManager.getInstance().getStatsCollection().find().limit(5).sort(new BasicDBObject("wins", -1)).into(new ArrayList<>());
            }
        }.runTaskAsynchronously(FoxySG.getInstance());
    }

    public Inventory getInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, Color.translate(FoxySG.getInstance().getConfiguration("inventory").getString("leaderboard-inventory.title")));
        PlayerData data = PlayerDataManager.getInstance().getByUUID(player.getUniqueId());

        ItemBuilder stats = new ItemBuilder(Material.SKULL_ITEM);
        stats.setDurability(3);
        stats.setName("&cVos Stats");
        stats.addLoreLine("&7&m-----------------------");
        stats.addLoreLine("&7* &bKills&7: &c" + data.getKills().getAmount());
        stats.addLoreLine("&7* &bDeaths&7: &c" + data.getDeaths().getAmount());
        stats.addLoreLine("&7* &bWins&7: &c" + data.getWins().getAmount());
        stats.addLoreLine("&7* &bPoints&7: &c" + data.getPoints().getAmount());
        stats.addLoreLine("&7* &bGames Played&7: &c" + data.getGamesPlayed().getAmount());
        stats.addLoreLine("&7* &bKDR&7: &c" + data.getKdr());
        stats.addLoreLine("&7&m-----------------------");

        inv.setItem(4, stats.toItemStack());

        ItemBuilder item;
        int i = 18;
        for (Leaderboard leaderboards : getLeaderboardList()) {
            if (leaderboards.isEnabled()) {
                if (leaderboards.getName().equals("Potion Splashed")) {
                    item = new ItemBuilder(Material.POTION).setDurability(16421);
                } else {
                    item = new ItemBuilder(leaderboards.getMaterial());
                }

                item.setName(FoxySG.getInstance().getConfiguration("inventory").getString("leaderboard-inventory.name").replaceAll("<name>", leaderboards.getName()));

                if (leaderboards.getFormats().isEmpty()) {
                    for (String string : FoxySG.getInstance().getConfiguration("inventory").getStringList("leaderboard-inventory.empty-leaderboard")) {
                        item.addLoreLine(string);
                    }
                } else {
                    item.addLoreLine("&7&m-----------------------");
                    for (String format : leaderboards.getFormats()) {
                        item.addLoreLine(format);
                    }
                    item.addLoreLine("&7&m-----------------------");
                }

                inv.setItem(i, item.toItemStack());
                i = i + 2;

                if (i == 36) {
                    i = i + 3;
                }
            }
        }
        return inv;
    }
}
