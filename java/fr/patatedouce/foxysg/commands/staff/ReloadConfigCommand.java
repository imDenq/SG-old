package fr.patatedouce.foxysg.commands.staff;

import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.utils.chat.Color;
import fr.patatedouce.foxysg.utils.command.BaseCommand;
import fr.patatedouce.foxysg.utils.command.Command;
import fr.patatedouce.foxysg.utils.command.CommandArgs;
import fr.patatedouce.foxysg.utils.configurations.ConfigFile;

import java.util.ArrayList;
import java.util.List;

public class ReloadConfigCommand extends BaseCommand {
    @Command(name = "reloadfiles", isAdminOnly = true)

    public void onCommand(CommandArgs command) {
        FoxySG.getInstance().getFiles().forEach(ConfigFile::save);

        List<String> names = new ArrayList<>();
        FoxySG.getInstance().getFiles().forEach(configFile -> names.add(configFile.getName() + ".yml"));
        command.getPlayer().sendMessage(Color.translate("&aConfig reloaded in (> 10ms). &7[&c"
        + names.toString().replace("[", "")
                .replace("]", "")
                .replace(",", "&7,&c") + "&7]&a."));
    }
}
