package fr.patatedouce.foxysg;

import fr.patatedouce.foxysg.commands.*;
import fr.patatedouce.foxysg.commands.staff.*;
import fr.patatedouce.foxysg.layout.TablistLayout;
import fr.patatedouce.foxysg.listeners.*;
import fr.patatedouce.foxysg.managers.*;
import fr.patatedouce.foxysg.other.PlayerListener;
import fr.patatedouce.foxysg.tablist.TablistManager;
import fr.patatedouce.foxysg.tablist.TablistUpdateTask;
import fr.patatedouce.foxysg.tablist.tablist.TablistAdapter;
import fr.patatedouce.foxysg.utils.Utils;
import fr.patatedouce.foxysg.utils.board.BoardManager;
import fr.patatedouce.foxysg.utils.chat.Color;
import fr.patatedouce.foxysg.utils.command.CommandFramework;
import fr.patatedouce.foxysg.utils.tasks.BrewingTask;
import fr.patatedouce.foxysg.utils.tasks.DataSaveTask;
import fr.patatedouce.foxysg.utils.tasks.LobbyTask;
import fr.patatedouce.foxysg.utils.tasks.PlayerTask;
import lombok.Getter;
import lombok.Setter;
import fr.patatedouce.foxysg.border.BorderManager;
import fr.patatedouce.foxysg.layout.BoardLayout;
import fr.patatedouce.foxysg.utils.leaderboards.LeaderboardManager;
import fr.patatedouce.foxysg.utils.configurations.ConfigFile;
import fr.patatedouce.foxysg.utils.runnables.DataRunnable;
import me.allen.ziggurat.Ziggurat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class FoxySG extends JavaPlugin {
    @Getter
    public static FoxySG instance;
    private CommandFramework framework;
    private List<ConfigFile> files = new ArrayList<>();
    private BoardManager boardManager;
    private boolean pluginLoading;

    @Override
    public void onEnable() {
        instance = this;
        pluginLoading = true;
        registerConfigurations();
       //new Ziggurat(this, new TablistLayout());
        framework = new CommandFramework(this);
        setBoardManager(new BoardManager(this, new BoardLayout()));

        if (!isBorderShrinksStreamValid()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "border shrink stream error (config file fix)");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getPluginManager().registerEvents(new ButtonListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChunkListener(), this);
        Bukkit.getPluginManager().registerEvents(new GlassListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryHandler(), this);
        Bukkit.getPluginManager().registerEvents(new DeathMessagesListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new PrestigeListener(), this);

        new ChestsManager();
        new GameManager();
        new InventoryManager();
        new MongoManager();
        new PlayerDataManager();
        new PlayerManager();
        new WorldsManager();
        new DataCommand();
        new GameCommand();
        new ReloadConfigCommand();
        new RespawnCommand();
        new SpectatorCommand();
        new AnnounceCommand();
        new FoxySGCommand();
        new SettingsCommand();
        new PrestigeCommand(this);
        new SpecChatCommand();
        new StatsCommand();
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "Broadcast");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        new BorderManager();

        this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new TablistUpdateTask(), 20L, 20L);
        this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new LobbyTask(), 20L, 20L);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new PlayerTask(), 2L, 2L);
        this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new DataSaveTask(), 200L, 200L);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new BrewingTask(), 1L, 1L);
        Bukkit.getConsoleSender().sendMessage(Color.translate("&aServer enabled successfully"));

        new LeaderboardManager();
        new DataRunnable();
    }

    public ConfigFile getConfiguration(String name) {
        return files.stream().filter(config -> config.getName().equals(name)).findFirst().orElse(null);
    }

    public boolean isBorderShrinksStreamValid() {
        String shrinkStream = FoxySG.getInstance().getConfiguration("config").getString("BORDER.SHRINK-STREAM");
        String[] shrinksStream = shrinkStream.split(";");

        for (String shrink : shrinksStream) {
            if (!Utils.isInteger(shrink)) {
                return false;
            }
        }
        return true;
    }

    public void registerConfigurations() {
        files.addAll(Arrays.asList(
                new ConfigFile("config"),
                new ConfigFile("messages"),
                new ConfigFile("items"),
                new ConfigFile("inventory"),
                new ConfigFile("chests"),
                new ConfigFile("scoreboard")
        ));
    }

    public void setBoardManager(BoardManager boardManager) {
        this.boardManager = boardManager;
        long interval = this.boardManager.getAdapter().getInterval();
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, this.boardManager, interval, interval);
        this.getServer().getPluginManager().registerEvents(this.boardManager, this);
    }
}
