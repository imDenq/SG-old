package fr.patatedouce.foxysg.events;

import fr.patatedouce.foxysg.managers.PlayerDataManager;
import fr.patatedouce.foxysg.player.PlayerData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

@Getter
@Setter
public class SGChestOpenEvent extends Event {
    public Player player;
    public PlayerData playerData = null;
    public Location chestLocation;
    public Inventory inventory;
    public static HandlerList handlers = new HandlerList();


    public SGChestOpenEvent(Player player, Inventory inventory, Location location) {
        this.player = player;
        this.chestLocation = location;
        this.inventory = inventory;
        if (PlayerDataManager.getInstance().getByUUID(player.getUniqueId()) != null) {
            this.playerData = PlayerDataManager.getInstance().getByUUID(player.getUniqueId());
        }
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
