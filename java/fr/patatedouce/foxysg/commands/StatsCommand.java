package fr.patatedouce.foxysg.commands;

import fr.patatedouce.foxysg.managers.GameManager;
import fr.patatedouce.foxysg.managers.InventoryManager;
import fr.patatedouce.foxysg.managers.PlayerDataManager;
import fr.patatedouce.foxysg.utils.chat.Color;
import fr.patatedouce.foxysg.utils.command.BaseCommand;
import fr.patatedouce.foxysg.utils.command.Command;
import fr.patatedouce.foxysg.utils.command.CommandArgs;
import fr.patatedouce.foxysg.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class StatsCommand extends BaseCommand {

    @Command(name = "stats")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(Color.translate("&cUtilisation: /stats <joueur>."));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(Color.translate(GameManager.getInstance().getGamePrefix() + "&cJoueur hors ligne"));
            return;
        }

        PlayerData targetData = PlayerDataManager.getInstance().getByUUID(target.getUniqueId());
        if (targetData == null) {
            player.sendMessage(Color.translate(GameManager.getInstance().getGamePrefix() + "&cdata problem veuillez contacter patate."));
            return;
        }

        /*player.sendMessage(Color.translate("&7&m-----------------------"));
        player.sendMessage(Color.translate("&e&l" + target.getName() + "'s Statistics"));
        player.sendMessage(Color.translate("&7* &eKills&7: &6" + targetData.getKills().getAmount()));
        player.sendMessage(Color.translate("&7* &eDeaths&7: &6" + targetData.getDeaths().getAmount()));
        player.sendMessage(Color.translate("&7* &eWins&7: &6" + targetData.getWins().getAmount()));
        player.sendMessage(Color.translate("&7* &ePoints&7: &6" + targetData.getPoints().getAmount()));
        player.sendMessage(Color.translate("&7* &eGames Played&7: &6" + targetData.getGamesPlayed().getAmount()));
        player.sendMessage(Color.translate("&7* &eHighest KillStreak&7: &6" + targetData.getKillStreak().getAmount()));
        player.sendMessage(Color.translate("&7* &eKDR&7: &6" + targetData.getKdr()));
        player.sendMessage(Color.translate("&7&m-----------------------")); */
        player.openInventory(InventoryManager.getInstance().getStatsInventory(targetData));

    }
}
