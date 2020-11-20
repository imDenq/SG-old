package fr.patatedouce.foxysg.managers;

import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.enums.GameState;
import fr.patatedouce.foxysg.utils.ItemBuilder;
import fr.patatedouce.foxysg.utils.Utils;
import fr.patatedouce.foxysg.utils.chat.Color;
import fr.patatedouce.foxysg.utils.countdowns.*;
import fr.patatedouce.foxysg.utils.runnables.GameRunnable;
import lombok.Data;
import lombok.Getter;
import fr.patatedouce.foxysg.player.PlayerData;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class GameManager {
    @Getter
    public static GameManager instance;
    private GameState gameState = GameState.LOBBY;
    private String gamePrefix = fr.patatedouce.foxysg.utils.chat.Color.translate(FoxySG.getInstance().getConfiguration("config").getString("PREFIXES.GAME"));
    private String borderPrefix = fr.patatedouce.foxysg.utils.chat.Color.translate(FoxySG.getInstance().getConfiguration("config").getString("PREFIXES.BORDER"));
    private String tsInfo = FoxySG.getInstance().getConfiguration("config").getString("INFORMATIONS.TS");
    private String webInfo = FoxySG.getInstance().getConfiguration("config").getString("INFORMATIONS.WEB");
    private String ipInfo = FoxySG.getInstance().getConfiguration("config").getString("INFORMATIONS.IP");
    private String storeInfo = FoxySG.getInstance().getConfiguration("config").getString("INFORMATIONS.STORE");
    private String serverName = FoxySG.getInstance().getConfiguration("config").getString("INFORMATIONS.SERVER");
    private int maxPlayers = FoxySG.getInstance().getConfiguration("config").getInt("MAXIMUM-PLAYERS-PER-GAME");
    private int minPlayers = FoxySG.getInstance().getConfiguration("config").getInt("MINIMUM-PLAYERS-TO-START-GAME");
    private int startCountdownValue = FoxySG.getInstance().getConfiguration("config").getInt("COUNTDOWNS.IN-SECONDS.START");
    private int prematchCountdownValue = FoxySG.getInstance().getConfiguration("config").getInt("COUNTDOWNS.IN-SECONDS.PRE-MATCH");
    private int pvpCountdownValue = FoxySG.getInstance().getConfiguration("config").getInt("COUNTDOWNS.IN-SECONDS.PVP-PROT");
    private int feastsCountdownValue = FoxySG.getInstance().getConfiguration("config").getInt("COUNTDOWNS.IN-MINUTES.FEASTS-SPAWN");
    private int deathMatchCountdownValue = FoxySG.getInstance().getConfiguration("config").getInt("COUNTDOWNS.IN-MINUTES.DEATH-MATCH");
    private int rebootCountdownValue = FoxySG.getInstance().getConfiguration("config").getInt("COUNTDOWNS.IN-SECONDS.REBOOT");
    private int pointsPerKill = FoxySG.getInstance().getConfiguration("config").getInt("POINTS.PER-KILL");
    private int pointsPerWin = FoxySG.getInstance().getConfiguration("config").getInt("POINTS.PER-WIN");
    private String rebootCommand = FoxySG.getInstance().getConfiguration("config").getString("REBOOT_COMMAND");
    private StartCountdown startCountdown = null;
    private PrematchCountdown prematchCountdown = null;
    private PvPCountdown pvpCountdown = null;
    private FeastCountdown feastCountdown = null;
    private GameRunnable gameRunnable = null;
    private DeathMatchCountdown deathMatchCountdown = null;
    private RebootCountdown rebootCountdown;
    private boolean forceStarted = false;
    private boolean toUseLobby = FoxySG.getInstance().getConfiguration("config").getBoolean("BOOLEANS.LOBBY-ENABLED");
    private boolean serverPremium = FoxySG.getInstance().getConfiguration("config").getBoolean("SERVER-PREMIUM");
    private String lobbyFallbackServer = FoxySG.getInstance().getConfiguration("config").getString("LOBBY-FALLBACK-SERVER");
    private String winner = "";
    private int winnerKills = 0;
    private int winnerTotalKills = 0;
    private boolean toCancelFirework = false;
    private static MetadataValue META_KEY = new FixedMetadataValue(FoxySG.getInstance(), true);
    private boolean deathMatchArenaSpawned = false;
    private Map<Location, BrewingStand> activeBrewingStands  = new HashMap<>();

    public GameManager() {
        instance = this;
    }

    public void handleLobbyItems(Player player) {
        ItemBuilder statistics = new ItemBuilder(Material.PAPER);
        statistics.setNameWithArrows(FoxySG.getInstance().getConfiguration("items").getString("join-inventory.leaderboard.name"));
        for (String lore : FoxySG.getInstance().getConfiguration("items").getStringList("join-inventory.leaderboard.lore")) {
            statistics.addLoreLine(lore);
        }

        ItemBuilder players = new ItemBuilder(Material.SIGN);
        players.setNameWithArrows(FoxySG.getInstance().getConfiguration("items").getString("join-inventory.player-stats.name"));
        for (String lore : FoxySG.getInstance().getConfiguration("items").getStringList("join-inventory.player-stats.lore")) {
            players.addLoreLine(lore);
        }

        ItemBuilder settings = new ItemBuilder(Material.CHEST);
        settings.setNameWithArrows(FoxySG.getInstance().getConfiguration("items").getString("join-inventory.settings.name"));
        for (String lore : FoxySG.getInstance().getConfiguration("items").getStringList("join-inventory.settings.lore")) {
            settings.addLoreLine(lore);
        }

        ItemBuilder stats = new ItemBuilder(Material.SKULL_ITEM).setDurability(3);
        stats.setNameWithArrows(FoxySG.getInstance().getConfiguration("items").getString("join-inventory.your-stats.name"));
        for (String lore : FoxySG.getInstance().getConfiguration("items").getStringList("join-inventory.your-stats.lore")) {
            stats.addLoreLine(lore);
        }

        player.getInventory().setItem(0, statistics.toItemStack());
        player.getInventory().setItem(1, players.toItemStack());
        //player.getInventory().setItem(5, stats.toItemStack());
        //player.getInventory().setItem(8, settings.toItemStack());
    }

    public Location getLobbyLocation() {
        Location lobbyLoc = WorldsManager.getInstance().getLobbyWorld().getSpawnLocation();
        lobbyLoc.setYaw(FoxySG.getInstance().getConfiguration("config").getInt("LOCATIONS.LOBBY.YAW"));
        lobbyLoc.setPitch(FoxySG.getInstance().getConfiguration("config").getInt("LOCATIONS.LOBBY.PITCH"));
        return lobbyLoc;
    }

    public Location getGameWorldCenterLocation() {
        if (!isGameCenterLocationValid()) {
            int x = 0;
            int z = 0;
            double y = WorldsManager.getInstance().getGameWorld().getHighestBlockYAt(x, z);
            return new Location(WorldsManager.getInstance().getGameWorld(), x, y, z);
        }
        String input = FoxySG.getInstance().getConfiguration("config").getString("GAME-CENTER-LOCATION");
        String[] coords = input.split(";");
        int x = Integer.valueOf(coords[0]);
        int y = Integer.valueOf(coords[1]);
        int z = Integer.valueOf(coords[2]);
        return new Location(WorldsManager.getInstance().getGameWorld(), x ,y, z);
    }

    private boolean isGameCenterLocationValid() {
        String input = FoxySG.getInstance().getConfiguration("config").getString("GAME-CENTER-LOCATION");
        String[] coords = input.split(";");

        for (String coord : coords) {
            if (!Utils.isInteger(coord)) {
                return false;
            }
        }
        if (coords.length < 3) {
            return false;
        }
        return true;
    }

    public void startGame() {
        setStartCountdown(new StartCountdown());
    }

    public int getRequiredPlayersToJoin() {
        return getMinPlayers() - PlayerManager.getInstance().getLobbyPlayers().size();
    }

    public ItemStack getChestItem() {
        ItemBuilder item = new ItemBuilder(Material.CHEST);
        item.setName("&cChest");
        item.addLoreLine("&citems du /game chest");
        item.addLoreLine("&cne rien mettre dedans");

        return item.toItemStack();
    }

    public List<PlayerData> getTop5GameKills() {
        return PlayerDataManager.getInstance().getPlayerDatas().values().stream().filter(playerData -> playerData.getGameKills().getAmount() > 0).limit(5).sorted().collect(Collectors.toList());
    }

    public void spawnFeast() {
        World world = WorldsManager.getInstance().getGameWorld();

        for (int x = -8; x < 8; ++x) {
            for (int z = -8; z < 8; ++z) {
                if (RandomUtils.nextInt(20) == 0) {
                    final Block block = world.getBlockAt(x + 8, world.getHighestBlockAt(x, z).getY(), z + 8);
                    if (block.getRelative(BlockFace.NORTH).getType() != Material.CHEST) {
                        if (block.getRelative(BlockFace.SOUTH).getType() != Material.CHEST) {
                            if (block.getRelative(BlockFace.EAST).getType() != Material.CHEST) {
                                if (block.getRelative(BlockFace.WEST).getType() != Material.CHEST) {
                                    FallingBlock fallingBlock = world.spawnFallingBlock(block.getLocation().add(0, 30, 0), Material.CHEST, (byte) 54);
                                    fallingBlock.setMetadata("fallingBlock", META_KEY);
                                }
                            }
                        }
                    }
                }
            }
        }
        world.getBlockAt(8, world.getHighestBlockAt(0, 0).getY(), 8).setType(Material.ENCHANTMENT_TABLE);
    }

    public void clearFlat(Location loc, int r) {
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        World w = loc.getWorld();
        for (int x = cx - r; x <= cx + r; x++) {
            for (int y = cy; y <= cy + (r / 2); y++) {
                for (int z = cz - r; z <= cz + r; z++) {
                    w.getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }
    }

    public void spawnDeathMatchArena() {
        List<Material> floorMaterials = new ArrayList<>();
        List<Material> WallbeetweenMaterials = new ArrayList<>();
        List<Material> WallupMaterials = new ArrayList<>();
        List<Material> WalldownMaterials = new ArrayList<>();

        try {
            FoxySG.getInstance().getConfiguration("config").getStringList("DEATH-MATCH.FLOOR-MATERIALS")
                    .forEach(mat -> floorMaterials.add(Material.valueOf(mat)));
            FoxySG.getInstance().getConfiguration("config").getStringList("DEATH-MATCH.WALL-MATERIALS.BEETWEEN")
                    .forEach(mat -> WallbeetweenMaterials.add(Material.valueOf(mat)));
            FoxySG.getInstance().getConfiguration("config").getStringList("DEATH-MATCH.WALL-MATERIALS.TOP")
                    .forEach(mat -> WallupMaterials.add(Material.valueOf(mat)));
            FoxySG.getInstance().getConfiguration("config").getStringList("DEATH-MATCH.WALL-MATERIALS.BOTTOM")
                    .forEach(mat -> WalldownMaterials.add(Material.valueOf(mat)));
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + e.getCause().getMessage());
            Bukkit.getConsoleSender().sendMessage(Color.translate("&cAn error ocured while spawning deathmatch arena. Please check your materials!"));
        }

        Location oldloc = WorldsManager.getInstance().getGameWorld().getBlockAt(0, WorldsManager.getInstance().getGameWorld().getHighestBlockYAt(0, 0) - (51 / (51 / 2)), 0).getLocation();
        clearFlat(oldloc, 16);

        int r = 15;
        int yTop = WorldsManager.getInstance().getGameWorld().getHighestBlockYAt(0, 0);
        Location loc = WorldsManager.getInstance().getGameWorld().getBlockAt(0, yTop, 0).getLocation();
        World w = loc.getWorld();

        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        for (int x = cx - r; x <= cx + r; x++) {
            for (int y = cy; y <= cy; y++) {
                for (int z = cz - r; z <= cz + r; z++) {
                    w.getBlockAt(x, y - 1, z).setType(Material.BEDROCK);
                    w.getBlockAt(x, y, z).setType(getRandomMaterial(floorMaterials));
                }
            }
        }


        int size = 15;
        Location centerLocation = new Location(w, 0, w.getHighestBlockYAt(0,0), 0);


        for (int i = 1; i < 1 + 1; i++) {
            for (int x = centerLocation.getBlockX() - size; x <= centerLocation.getBlockX() + size; ++x) {
                for (int y = 58; y <= 58; ++y) {
                    for (int z = centerLocation.getBlockZ() - size; z <= centerLocation.getBlockZ() + size; ++z) {
                        if (x == centerLocation.getBlockX() - size || x == centerLocation.getBlockX() + size || z == centerLocation.getBlockZ() - size
                                || z == loc.getBlockZ() + size) {
                            Location loc2 = new Location(w, x, y, z);
                            loc2.setY(w.getHighestBlockYAt(loc2));
                            loc2.getBlock().setType(getRandomMaterial(WalldownMaterials));
                        }
                    }
                }
            }
        }


        for (int i = 4; i < 4 + 4; i++) {
            for (int x = centerLocation.getBlockX() - size; x <= centerLocation.getBlockX() + size; ++x) {
                for (int y = 58; y <= 58; ++y) {
                    for (int z = centerLocation.getBlockZ() - size; z <= centerLocation.getBlockZ() + size; ++z) {
                        if (x == centerLocation.getBlockX() - size || x == centerLocation.getBlockX() + size || z == centerLocation.getBlockZ() - size
                                || z == loc.getBlockZ() + size) {
                            Location loc2 = new Location(w, x, y, z);
                            loc2.setY(w.getHighestBlockYAt(loc2));
                            loc2.getBlock().setType(getRandomMaterial(WallbeetweenMaterials));
                        }
                    }
                }
            }
        }


        for (int i = 1; i < 1 + 1; i++) {
            for (int x = centerLocation.getBlockX() - size; x <= centerLocation.getBlockX() + size; ++x) {
                for (int y = 58; y <= 58; ++y) {
                    for (int z = centerLocation.getBlockZ() - size; z <= centerLocation.getBlockZ() + size; ++z) {
                        if (x == centerLocation.getBlockX() - size || x == centerLocation.getBlockX() + size || z == centerLocation.getBlockZ() - size
                                || z == loc.getBlockZ() + size) {
                            Location loc2 = new Location(w, x, y, z);
                            loc2.setY(w.getHighestBlockYAt(loc2));
                            loc2.getBlock().setType(getRandomMaterial(WallupMaterials));
                        }
                    }
                }
            }
        }

        for (Entity entites : w.getEntities()) {
            if (!(entites instanceof Player)) {
                entites.remove();
            }
        }
    }

    public Material getRandomMaterial(List<Material> list) {
        final int r = RandomUtils.nextInt(list.size());
        return list.get(r);
    }
}
