package fr.patatedouce.foxysg.utils.inventories;

import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.utils.ItemBuilder;
import fr.patatedouce.foxysg.utils.chat.Color;
import fr.patatedouce.foxysg.utils.leaderboards.LeaderboardManager;
import fr.patatedouce.foxysg.utils.pagination.PaginatedMenu;
import fr.patatedouce.foxysg.utils.pagination.buttons.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class LobbyInventoryPlayers extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return Color.translate(FoxySG.getInstance().getConfiguration("inventory").getString("player-statistics-inventory.title"));
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();
        LeaderboardManager.getInstance().getAllStatistics().forEach(document -> buttons.put(buttons.size(), new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                ItemBuilder item = new ItemBuilder(Material.INK_SACK);
                item.setDurability(7);
                item.setName(FoxySG.getInstance().getConfiguration("inventory").getString("player-statistics-inventory.name")
                        .replaceAll("<player_name>", document.getString("name") == null ? "null" : document.getString("name"))
                );
                for (String string : FoxySG.getInstance().getConfiguration("inventory").getStringList("player-statistics-inventory.lore")) {
                    item.addLoreLine(string
                            .replaceAll("<player_kills>", String.valueOf(document.getInteger("kills")))
                            .replaceAll("<player_deaths>", String.valueOf(document.getInteger("deaths")))
                            .replaceAll("<player_killstreak>", String.valueOf(document.getInteger("killStreak")))
                            .replaceAll("<player_wins>", String.valueOf(document.getInteger("wins")))
                            .replaceAll("<player_points>", String.valueOf(document.getInteger("points")))
                            .replaceAll("<player_games_played>", String.valueOf(document.getInteger("gamesPlayed")))
                            .replaceAll("<player_golden_apples_eaten>", String.valueOf(document.getInteger("goldenApplesEaten")))
                            .replaceAll("<player_bow_shots>", String.valueOf(document.getInteger("bowShots")))
                            .replaceAll("<player_chests_opened>", String.valueOf(document.getInteger("chestBroke")))
                            .replaceAll("<player_potion_splashed>", String.valueOf(document.getInteger("potionSplashed")))
                            .replaceAll("<player_potion_drank>", String.valueOf(document.getInteger("potionDrank")))
                    );
                }
                return item.toItemStack();
            }
        }));
        return buttons;
    }
}
