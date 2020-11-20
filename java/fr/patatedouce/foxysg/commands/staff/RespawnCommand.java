package fr.patatedouce.foxysg.commands.staff;

import fr.patatedouce.foxysg.enums.PlayerState;
import fr.patatedouce.foxysg.managers.GameManager;
import fr.patatedouce.foxysg.managers.PlayerDataManager;
import fr.patatedouce.foxysg.utils.Utils;
import fr.patatedouce.foxysg.utils.chat.Color;
import fr.patatedouce.foxysg.utils.command.BaseCommand;
import fr.patatedouce.foxysg.utils.command.Command;
import fr.patatedouce.foxysg.utils.command.CommandArgs;
import fr.patatedouce.foxysg.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RespawnCommand extends BaseCommand {

    @Command(name = "respawn", permission = "foxysg.command.respawn")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(Color.translate("&cUtilisation: /respawn <joueur>"));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(Color.translate("&cJoueur hors ligne"));
            return;
        }
        PlayerData targetData = PlayerDataManager.getInstance().getByUUID(target.getUniqueId());
        if (targetData == null) {
            player.sendMessage(Color.translate("&cAucune info pour ce joueur"));
            return;
        }
        if (targetData.getRespawnInfo() == null) {
            player.sendMessage(Color.translate("&cAucune info pour" + args[0] + "'."));
            return;
        }
        Utils.clearPlayer(target);
        target.teleport(targetData.getRespawnInfo().getLocation());
        target.getInventory().setContents(targetData.getRespawnInfo().getInventory());
        target.getInventory().setArmorContents(targetData.getRespawnInfo().getArmor());
        target.updateInventory();
        targetData.setRespawnInfo(null);
        target.setFlying(false);
        targetData.setState(PlayerState.INGAME);

        player.sendMessage(Color.translate(GameManager.getInstance().getGamePrefix() + "&aVous avez respawn &c" + target.getName() + "&a."));
        target.sendMessage(Color.translate(GameManager.getInstance().getGamePrefix() + "&aVous avez été respawn par &c" + player.getName() + "&a."));
    }
}
