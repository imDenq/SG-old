package fr.patatedouce.foxysg.utils.chat;

import org.bukkit.ChatColor;
import java.util.List;
import java.util.stream.Collectors;

public class Color {

    public static String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> translate(List<String> list) {
        return list.stream().map(Color::translate).collect(Collectors.toList());
    }
}
