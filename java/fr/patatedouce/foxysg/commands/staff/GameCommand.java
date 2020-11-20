package fr.patatedouce.foxysg.commands.staff;

import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.enums.GameState;
import fr.patatedouce.foxysg.managers.ChestsManager;
import fr.patatedouce.foxysg.managers.GameManager;
import fr.patatedouce.foxysg.managers.WorldsManager;
import fr.patatedouce.foxysg.utils.Clickable;
import fr.patatedouce.foxysg.utils.Utils;
import fr.patatedouce.foxysg.utils.chat.Color;
import fr.patatedouce.foxysg.utils.command.BaseCommand;
import fr.patatedouce.foxysg.utils.command.Command;
import fr.patatedouce.foxysg.utils.command.CommandArgs;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GameCommand extends BaseCommand {
    @Command(name = "game", permission = "foxysg.command.game.use")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(Color.translate("&7&m-----------------------------"));
            player.sendMessage(Color.translate("&cCommandes&7:"));
            player.sendMessage(Color.translate("&7* &b/game start &7- &cfore la game a se start"));
            player.sendMessage(Color.translate("&7* &b/game map &7- &cteleport a la map (partie)"));
            player.sendMessage(Color.translate("&7* &b/game savemap &7- &csave la map"));
            player.sendMessage(Color.translate("&7* &b/game lobby &7- &cteleport sur le lobby"));
            player.sendMessage(Color.translate("&7* &b/game chests &7- &cexecute le systeme de chest"));
            player.sendMessage(Color.translate("&7* &b/game spawn darena &7- &cfaire un deathmatch avant la partie"));
            player.sendMessage(Color.translate("&7* &b/game setgamespawn &7- &cSet le spawn de la game)"));
            player.sendMessage(Color.translate("&7&m-----------------------------"));
            return;
        }
        if (args.length == 1) {
            switch (args[0]) {
                case "start": {
                    if (GameManager.getInstance().getStartCountdown() != null && !GameManager.getInstance().getStartCountdown().hasExpired()) {
                        player.sendMessage(Color.translate("&cLe start de la partie est déjà en route!"));
                        return;
                    }
                    if (!GameManager.getInstance().getGameState().equals(GameState.LOBBY)) {
                        player.sendMessage(Color.translate("&cUniquement sur le lobby."));
                        return;
                    }
                    Utils.broadcastMessage(GameManager.getInstance().getGamePrefix() + Color.translate("&aThe pre-match has been forced to start!"));
                    GameManager.getInstance().setForceStarted(true);
                    GameManager.getInstance().startGame();
                    break;
                }
                case "chests":
                case "chest": {
                    player.openInventory(ChestsManager.getInstance().chestsInventory());
                    break;
                }
                /*case "getchest": {
                    player.getInventory().addItem(GameManager.getInstance().getChestItem());
                    player.sendMessage(Color.translate("&eYou have received a new &f'Custom Chest'. Read lore of chest for instructions!"));
                    break;
                }*/
                case "map": {
                    Location loc = new Location(WorldsManager.getInstance().getGameWorld(), 0, 90, 0);
                    player.teleport(loc);
                    player.sendMessage(Color.translate("&atéléporté a la map de game tapez /game savemap pour la sauvegarder"));
                    break;
                }
                case "lobby": {
                    player.teleport(GameManager.getInstance().getLobbyLocation());
                    player.sendMessage(Color.translate("&aTéléporté au lobby"));
                    break;
                }
                case "savemap": {
                    if (!player.getWorld().getName().equalsIgnoreCase(WorldsManager.getInstance().getGameWorld().getName())) {
                        player.sendMessage(Color.translate("&a/game map est requis."));
                        return;
                    }
                    if (!GameManager.getInstance().getGameState().equals(GameState.LOBBY)) {
                        player.sendMessage(Color.translate("&cCommande executable au lobby uniquement"));
                        return;
                    }
                    WorldsManager.getInstance().getGameWorld().getPlayers().forEach(online -> {
                        player.teleport(GameManager.getInstance().getLobbyLocation());
                        player.sendMessage(Color.translate("&atéléporté de la map game."));
                    });
                    WorldsManager.getInstance().saveCurrentWorld();
                    player.sendMessage(Color.translate("&amap save success"));
                    break;
                }
                case "setgamespawn" : {
                    if (!player.getWorld().getName().equalsIgnoreCase(WorldsManager.getInstance().getGameWorld().getName())) {
                        Clickable clickable = new Clickable("&cUtilise /game map ou clique sur ce message..",
                                "&cClique pour te tp a la game map", "/game map");
                        clickable.sendToPlayer(player);
                        return;
                    }
                    int x = player.getLocation().getBlockX();
                    int y = player.getLocation().getBlockY();
                    int z = player.getLocation().getBlockZ();
                    String toSave = x + ";" + y + ";" + z;
                    FoxySG.getInstance().getConfiguration("config").getConfiguration().set("GAME-CENTER-LOCATION", toSave);
                    FoxySG.getInstance().getConfiguration("config").save();
                    player.sendMessage(Color.translate("&aspawn set: &c(" + toSave + ")"));
                    break;
                }
                default: {
                    player.performCommand("game");
                    break;
                }
            }
        }
        if (args.length == 2) {
            switch (args[0]) {
                case "spawn" : {
                    if (args[1].equalsIgnoreCase("darena")) {
                        player.sendMessage(Color.translate(GameManager.getInstance().getGamePrefix() + "&cDeathMatch arena set."));
                        GameManager.getInstance().spawnDeathMatchArena();
                        GameManager.getInstance().setDeathMatchArenaSpawned(true);
                        break;
                    }
                }
                default: {
                    player.performCommand("game");
                    break;
                }
            }
        }
    }
}
