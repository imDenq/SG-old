package fr.patatedouce.foxysg.tablist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TablistUpdateTask implements Runnable
{
	@SuppressWarnings("deprecation")
	@Override
	public void run()
	{
		TablistManager manager = TablistManager.INSTANCE;
		if (manager == null)
			return;
		for(Player online : Bukkit.getServer().getOnlinePlayers()) {
			Tablist tablist = manager.getTablist(online);
			if(tablist != null) {
				tablist.hideRealPlayers().update();
			} else if(tablist == null) {
				tablist.update();
			}
		}
	}
}
