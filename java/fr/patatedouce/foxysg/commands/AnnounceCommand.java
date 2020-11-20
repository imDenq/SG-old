package fr.patatedouce.foxysg.commands;

import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.enums.GameState;
import fr.patatedouce.foxysg.managers.GameManager;
import fr.patatedouce.foxysg.utils.Utils;
import fr.patatedouce.foxysg.utils.chat.Color;
import fr.patatedouce.foxysg.utils.command.BaseCommand;
import fr.patatedouce.foxysg.utils.command.Command;
import fr.patatedouce.foxysg.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class AnnounceCommand extends BaseCommand {
    @Command(name = "announce", permission = "foxysg.announce", aliases = {"ann", "sgannounce"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        if (!GameManager.getInstance().getGameState().equals(GameState.LOBBY)) {
            player.sendMessage(Color.translate("&cGame can't be announced since it is already started!"));
            return;
        }
        if (!FoxySG.getInstance().getConfiguration("config").getBoolean("ANNOUNCE.ENABLED")) {
            player.sendMessage(Color.translate("&cAnnonces désactivées !"));
            return;
        }
        String format = FoxySG.getInstance().getConfiguration("config").getString("ANNOUNCE.FORMAT");
        format = format.replace("<server_name>", GameManager.getInstance().getServerName());
        format = format.replace("<player_display_name>", player.getDisplayName());

        if (!FoxySG.getInstance().getConfiguration("config").getBoolean("ANNOUNCE.BUNGEE")) {
            Utils.broadcastMessage(format);
        } else {
            Utils.sendGlobalMessage(player, format);
        }
    }
}
