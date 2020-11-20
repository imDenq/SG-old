package fr.patatedouce.foxysg.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListener implements Listener {

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (event.getWorld().getName().equalsIgnoreCase("Coc-541")) {
            event.setCancelled(true);
        }
    }

}
