package fr.patatedouce.foxysg.tablist.tablist;

import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.tablist.TablistEntrySupplier;
import net.minecraft.util.com.google.common.collect.HashBasedTable;
import net.minecraft.util.com.google.common.collect.Table;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class TablistAdapter implements TablistEntrySupplier {

    private final FoxySG plugin;

    public TablistAdapter(FoxySG plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Table<Integer, Integer, String> getEntries(Player player) {
        Table<Integer, Integer, String> tab = HashBasedTable.create();
        tab.put(0,19, ChatColor.GRAY + "play.foxymc.eu");
        tab.put(2, 11, ChatColor.GRAY + getCardinalDirection(player) + " (" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockZ() + ")");
        return tab;
    }


    public static String getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() + 180F) % 360.0F;
        if (rotation < 0.0D) {
            rotation += 360.0D;
        }
        if ((0.0D <= rotation) && (rotation < 22.5D)) {
            return "N";
        }
        if ((22.5D <= rotation) && (rotation < 67.5D)) {
            return "NE";
        }
        if ((67.5D <= rotation) && (rotation < 112.5D)) {
            return "E";
        }
        if ((112.5D <= rotation) && (rotation < 157.5D)) {
            return "SE";
        }
        if ((157.5D <= rotation) && (rotation < 202.5D)) {
            return "S";
        }
        if ((202.5D <= rotation) && (rotation < 247.5D)) {
            return "SW";
        }
        if ((247.5D <= rotation) && (rotation < 292.5D)) {
            return "W";
        }
        if ((292.5D <= rotation) && (rotation < 337.5D)) {
            return "NW";
        }
        if ((337.5D <= rotation) && (rotation < 360.0D)) {
            return "N";
        }
        return null;
    }

    @Override
    public String getHeader(Player player) {
        return "§7Tu joues sur §bFoxy§6Faction!";
    }

    @Override
    public String getFooter(Player player) {
        return "§7Tu joues jouez sur §bFoxy§6Faction!";
    }


}	

