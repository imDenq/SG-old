package fr.patatedouce.foxysg.commands.staff;

import fr.patatedouce.foxysg.managers.GameManager;
import fr.patatedouce.foxysg.managers.MongoManager;
import fr.patatedouce.foxysg.managers.PlayerDataManager;
import fr.patatedouce.foxysg.utils.Utils;
import fr.patatedouce.foxysg.utils.chat.Color;
import fr.patatedouce.foxysg.utils.command.BaseCommand;
import fr.patatedouce.foxysg.utils.command.Command;
import fr.patatedouce.foxysg.utils.command.CommandArgs;
import fr.patatedouce.foxysg.player.PlayerData;
import fr.patatedouce.foxysg.utils.leaderboards.LeaderboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DataCommand extends BaseCommand {
    @Command(name = "data", isAdminOnly = true)

    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 1 && args[0].equals("update-leaderboards")) {
            player.sendMessage(Color.translate(GameManager.getInstance().getGamePrefix() + "&aUpdating leaderboards..."));
            LeaderboardManager.getInstance().updateAllLeaderboards();
            player.sendMessage(Color.translate(GameManager.getInstance().getGamePrefix() + "&aAll leaderboards have been updated."));
            return;
        }
        if (args.length == 1 && args[0].equals("dropAll")) {
            /*MongoManager.getInstance().getStatsCollection().drop();
            LeaderboardManager.getInstance().updateAllLeaderboards();
            PlayerDataManager.getInstance().getPlayerDatas().clear();
            Utils.getOnlinePlayers().forEach(online -> PlayerDataManager.getInstance().handeCreateData(online.getUniqueId()));
            player.sendMessage(Color.translate(GameManager.getInstance().getGamePrefix() + "&eYou have dropped all data collections."));*/


            player.sendMessage(Color.translate("&7&m-------------------------------------------"));
            player.sendMessage(Color.translate("&cConfirmer via &c&o/data dropConfirm"));
            player.sendMessage(Color.translate("&7&m-------------------------------------------"));
            return;
        }
        if (args.length == 1 && args[0].equals("dropConfirm")) {
            player.sendMessage(Color.translate(GameManager.getInstance().getGamePrefix() + "&csa drop toutes les infos du mongodb."));
            MongoManager.getInstance().getStatsCollection().drop();
            LeaderboardManager.getInstance().updateAllLeaderboards();
            PlayerDataManager.getInstance().getPlayerDatas().clear();
            Utils.getOnlinePlayers().forEach(online -> PlayerDataManager.getInstance().handleCreateData(online.getUniqueId()));
            player.sendMessage(Color.translate(GameManager.getInstance().getGamePrefix() + "&aVous avez drop toutes les datas des joueurs avec success"));
            return;
        }
        if (args.length < 4) {
            player.sendMessage(Color.translate("&7&m-------------------------------------"));
            player.sendMessage(Color.translate("&c&lCommandes&7:"));
            player.sendMessage(Color.translate("&7* &b/data update-leaderboards &7- &cupdate le leaderboard"));
            player.sendMessage(Color.translate("&7* &b/data dropAll &7- &cdrop tout les stats du mongo"));
            player.sendMessage(Color.translate("&8"));
            LeaderboardManager.getInstance().getLeaderboardList().forEach(leaderboard -> {
                player.sendMessage(Color.translate("&7* &b/data <player> set " + leaderboard.getMongoValue() + " &b<number> &7- &cchanger " + leaderboard.getMongoValue() + "&c."));

            });
            player.sendMessage(Color.translate("&7&m-------------------------------------"));
        }
        if (args.length == 4) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(Color.translate("&cJoueur hors ligne."));
                return;
            }
            PlayerData targetData = PlayerDataManager.getInstance().getByUUID(target.getUniqueId());
            if (targetData == null) {
                player.sendMessage(Color.translate("&cerreur pour " + args[0] + " data."));
                return;
            }
            if (!args[1].equalsIgnoreCase("set")) {
                player.performCommand("data");
                return;
            }
            if (!isMongoValueAvailable(args[2])) {
                player.sendMessage(Color.translate("&cerreur pour " + args[2] + " data."));
                return;
            }
            if (!targetData.hasData()) {
                player.sendMessage(Color.translate("&cData pour " + args[0] + " non crée."));
                return;
            }
            if (!Utils.isInteger(args[3])) {
                player.sendMessage(Color.translate("&centrez un nombre valide"));
                return;
            }
            PlayerDataManager.getInstance().saveData(targetData, (GameManager.getInstance().isServerPremium() ? targetData.getUuid().toString() : targetData.getName()), args[2], Integer.parseInt(args[3]));
            player.sendMessage(Color.translate(GameManager.getInstance().getGamePrefix() + "&aVous avez set &c" + args[2] + " &ade&c " + target.getName() + " &aà &c" + args[3] + ""));
        }
    }

    private boolean isMongoValueAvailable(String value) {
        return LeaderboardManager.getInstance().getLeaderboardList().stream().anyMatch(leaderboard -> leaderboard.getMongoValue().equals(value));
    }
}
