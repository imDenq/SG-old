package fr.patatedouce.foxysg.commands;

import fr.patatedouce.foxysg.managers.PlayerDataManager;
import fr.patatedouce.foxysg.utils.chat.Color;
import fr.patatedouce.foxysg.utils.command.BaseCommand;
import fr.patatedouce.foxysg.utils.command.Command;
import fr.patatedouce.foxysg.utils.command.CommandArgs;
import fr.patatedouce.foxysg.player.PlayerData;
import org.bukkit.entity.Player;


public class SettingsCommand extends BaseCommand {

    @Command(name = "settings")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        PlayerData data = PlayerDataManager.getInstance().getByUUID(player.getUniqueId());
        try {
            player.openInventory(data.getSettingsInventory());
        } catch (Exception e) {
            player.sendMessage(Color.translate("&cVotre data n'a pas été enregistrée veuillez vous reconnecter."));
        }
    }
}
