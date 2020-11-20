package fr.patatedouce.foxysg.utils.inventories;

import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.managers.PlayerManager;
import fr.patatedouce.foxysg.utils.ItemBuilder;
import fr.patatedouce.foxysg.utils.chat.Color;
import fr.patatedouce.foxysg.utils.pagination.buttons.Button;
import fr.patatedouce.foxysg.utils.pagination.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class AlivePlayersInventory extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return Color.translate(FoxySG.getInstance().getConfiguration("inventory").getString("player-alive-inventory.title"));
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();
        PlayerManager.getInstance().getGamePlayers().forEach(online -> {
            buttons.put(buttons.size(), new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    ItemBuilder item = new ItemBuilder(Material.SKULL_ITEM);
                    item.setDurability(3);
                    item.setName(FoxySG.getInstance().getConfiguration("inventory").getString("player-alive-inventory.name").replaceAll("<player_name>", online.getName()));
                    for (String lore : FoxySG.getInstance().getConfiguration("inventory").getStringList("player-alive-inventory.lore")) {
                        item.addLoreLine(lore
                                .replaceAll("<player_name>", online.getName())
                        );
                    }
                    return item.toItemStack();
                }
            });
        });
        return buttons;
    }
}
