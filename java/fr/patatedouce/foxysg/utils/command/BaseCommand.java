package fr.patatedouce.foxysg.utils.command;

import fr.patatedouce.foxysg.FoxySG;

public abstract class BaseCommand {

	public FoxySG main = FoxySG.getInstance();

	public BaseCommand() {
		main.getFramework().registerCommands(this);
	}

	public abstract void onCommand(CommandArgs command);

}
