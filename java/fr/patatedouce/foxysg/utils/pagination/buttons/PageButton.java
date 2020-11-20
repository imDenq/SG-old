package fr.patatedouce.foxysg.utils.pagination.buttons;

import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.utils.chat.Color;
import lombok.AllArgsConstructor;
import fr.patatedouce.foxysg.utils.pagination.PaginatedMenu;
import fr.patatedouce.foxysg.utils.pagination.ViewAllPagesMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class PageButton extends Button {

    private int mod;
    private PaginatedMenu menu;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (this.hasNext(player)) {
            itemMeta.setDisplayName(Color.translate(this.mod > 0 ? FoxySG.getInstance().getConfiguration("inventory").getString("player-statistics-inventory.page-button.next-page-name") : FoxySG.getInstance().getConfiguration("inventory").getString("player-statistics-inventory.page-button.previous-page-name")));
        } else {
            itemMeta.setDisplayName(Color.translate(this.mod > 0 ? FoxySG.getInstance().getConfiguration("inventory").getString("player-statistics-inventory.page-button.last-page-name") : FoxySG.getInstance().getConfiguration("inventory").getString("player-statistics-inventory.page-button.first-page-name")));
        }

        List<String> lore = new ArrayList<>();
        for (String string : FoxySG.getInstance().getConfiguration("inventory").getStringList("player-statistics-inventory.page-button.lore")) {
            lore.add(string);
        }

        itemMeta.setLore(Color.translate(lore));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb) {
        if (clickType == ClickType.RIGHT) {
            new ViewAllPagesMenu(this.menu).openMenu(player);
            playNeutral(player);
        } else {
            if (hasNext(player)) {
                this.menu.modPage(player, this.mod);
                playNeutral(player);
            } else {
                playFail(player);
            }
        }
    }

    private boolean hasNext(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return pg > 0 && this.menu.getPages(player) >= pg;
    }

}
