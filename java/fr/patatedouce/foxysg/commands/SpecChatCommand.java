package fr.patatedouce.foxysg.commands;


import fr.patatedouce.foxysg.managers.GameManager;
import fr.patatedouce.foxysg.managers.PlayerDataManager;
import fr.patatedouce.foxysg.player.PlayerData;
import fr.patatedouce.foxysg.utils.chat.Color;
import fr.patatedouce.foxysg.utils.command.BaseCommand;
import fr.patatedouce.foxysg.utils.command.Command;
import fr.patatedouce.foxysg.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class SpecChatCommand extends BaseCommand {
    @Command(name = "spectatorchat", permission = "foxysg.command.spectatorchat.use", aliases = {"specchat", "spc"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        PlayerData data = PlayerDataManager.getInstance().getByUUID(player.getUniqueId());
        data.setSpecChat(!data.isSpecChat());
        player.sendMessage(Color.translate(GameManager.getInstance().getGamePrefix() + "&aspec chat" + (data.isSpecChat() ? "&aactivé" : "&cdésactivé") + "&e."));
    }
}
