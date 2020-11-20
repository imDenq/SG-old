package fr.patatedouce.foxysg.commands;

import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.utils.chat.Color;
import fr.patatedouce.foxysg.utils.command.BaseCommand;
import fr.patatedouce.foxysg.utils.command.Command;
import fr.patatedouce.foxysg.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class FoxySGCommand extends BaseCommand {
    @Command(name = "foxysg", aliases = {"sg", "info"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        if (player.isOp()) {
            player.sendMessage(Color.translate("&7&m----------------------------------"));
            player.sendMessage(Color.translate("&c&lFoxySG administration pannel"));
            player.sendMessage(Color.translate("&7* &b/game &8- &cToutes les commandes de partie"));
            player.sendMessage(Color.translate("&7* &b/data &8- &cToutes les commandes de data"));
            player.sendMessage(Color.translate("&7* &b/stats &8- &cChecker vos stats"));
            player.sendMessage(Color.translate("&7* &b/respawn &8- &cRespawn un joueur"));
            player.sendMessage(Color.translate("&7* &b/announce &8- &cAnnoncer le match sur le network"));
            player.sendMessage(Color.translate("&7* &b/settings &8- &cChanger vos settings"));
            player.sendMessage(Color.translate("&7* &b/reloadfiles &8- &cReload la config"));
            player.sendMessage(Color.translate("&7* &b/spectatorchat &8- &cRejoindre le spec chat"));
            player.sendMessage(Color.translate("&7* &b/spectator <add:remove> <player>"));
            player.sendMessage(Color.translate("&7&m----------------------------------"));
        } else {
            player.sendMessage(Color.translate("&7&m----------------------------------"));
            player.sendMessage(Color.translate("&c&lFoxyMC survival games"));
            player.sendMessage(Color.translate("&7* &b/stats &8- &cChecker vos stats"));
            player.sendMessage(Color.translate("&7* &b/settings &8- &cChanger vos settings"));
            player.sendMessage(Color.translate("&7* &b/prestige &8- &cSysteme de prestige &7&o(soon)"));
            player.sendMessage(Color.translate("&7&m----------------------------------"));
        }
    }
}
