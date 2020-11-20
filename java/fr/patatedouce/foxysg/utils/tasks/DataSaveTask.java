package fr.patatedouce.foxysg.utils.tasks;

import fr.patatedouce.foxysg.managers.PlayerDataManager;
import fr.patatedouce.foxysg.player.PlayerData;

public class DataSaveTask implements Runnable {

    @Override
    public void run() {
        PlayerDataManager.getInstance().getPlayerDatas().values().forEach(PlayerData::save);
    }
}
