package fr.patatedouce.foxysg.api;

import fr.patatedouce.foxysg.enums.GameState;
import lombok.Getter;
import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.managers.GameManager;
import fr.patatedouce.foxysg.managers.PlayerDataManager;
import fr.patatedouce.foxysg.managers.PlayerManager;
import fr.patatedouce.foxysg.player.PlayerData;
import org.bukkit.entity.Player;

import java.util.List;

public class FoxyAPI {

    @Getter
    private static FoxyAPI api = new FoxyAPI();

    public PlayerData getPlayerData(Player player) {
        return PlayerDataManager.getInstance().getByUUID(player.getUniqueId());
    }

    public GameState getGameState() {
        return GameManager.getInstance().getGameState();
    }

    public List<Player> getGamePlayers() {
        return PlayerManager.getInstance().getGamePlayers();
    }

    public List<Player> getLobbyPlayers() {
        return PlayerManager.getInstance().getLobbyPlayers();
    }

    public FoxySG getGame() {
        return FoxySG.getInstance();
    }
}
