package fr.patatedouce.foxysg.commands.staff;


import fr.patatedouce.foxysg.enums.PlayerState;
import fr.patatedouce.foxysg.managers.GameManager;
import fr.patatedouce.foxysg.managers.PlayerDataManager;
import fr.patatedouce.foxysg.managers.PlayerManager;
import fr.patatedouce.foxysg.utils.Utils;
import fr.patatedouce.foxysg.utils.chat.Color;
import fr.patatedouce.foxysg.utils.command.BaseCommand;
import fr.patatedouce.foxysg.utils.command.Command;
import fr.patatedouce.foxysg.utils.command.CommandArgs;
import fr.patatedouce.foxysg.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SpectatorCommand extends BaseCommand {

    @Command(name = "spectator", permission = "foxysg.command.spectator.use")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2){
            player.sendMessage(Color.translate("&cUtilisation: &c/spectator add <joueur>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            player.sendMessage(GameManager.getInstance().getGamePrefix() + Color.translate("&cAucune ressource pour &c" + args[1] + "&c."));
            return;
        }

        PlayerData data = PlayerDataManager.getInstance().getByUUID(target.getUniqueId());

        if (data == null) {
            player.sendMessage(GameManager.getInstance().getGamePrefix() + Color.translate("&cProfile error: " + args[1] + "'"));
            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (PlayerManager.getInstance().isSpectator(target)) {
                player.sendMessage(GameManager.getInstance().getGamePrefix() + Color.translate("&a" + args[1] + "est déjà en mode spectateur"));
                return;
            }
            Utils.broadcastMessageToSpectators(GameManager.getInstance().getGamePrefix() + "&a&l" + target.getName() + " &aà été rajouté au mode spéctateur par &a&l" + player.getName() + "&a.");
            PlayerManager.getInstance().setSpectating(target);
        }
        if (args[0].equalsIgnoreCase("remove")) {
            if (!PlayerManager.getInstance().isSpectator(target)) {
                player.sendMessage(GameManager.getInstance().getGamePrefix() + Color.translate("&a" + args[1] + "n'est pas en mode spectateur"));
                return;
            }
            data.setState(PlayerState.LOBBY);
            Utils.broadcastMessageToSpectators(GameManager.getInstance().getGamePrefix() + "&a&l" + target.getName() + " &aà été retiré du spéctateur par &a&l" + player.getName() + "&a.");
            Utils.clearPlayer(player);
            player.teleport(GameManager.getInstance().getLobbyLocation());
            player.getInventory().clear();
            GameManager.getInstance().handleLobbyItems(player);
        }
    }
}
