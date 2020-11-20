package fr.patatedouce.foxysg.listeners;

import fr.patatedouce.foxysg.utils.chat.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class PrestigeListener implements Listener{

    public void onInventoryClick(PlayerInteractEvent event, InventoryClickEvent event1){
        Player player = event.getPlayer();
        Inventory inventory = event1.getInventory();
        if (inventory.getName().equals(Color.translate("&cPrestiges"))){
            event.setCancelled(true);
            player.closeInventory();
            event1.setCancelled(true);
            return;
        }
    }
}
