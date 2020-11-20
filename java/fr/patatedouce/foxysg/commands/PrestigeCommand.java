package fr.patatedouce.foxysg.commands;

import fr.patatedouce.foxysg.FoxySG;
import fr.patatedouce.foxysg.managers.PlayerDataManager;
import fr.patatedouce.foxysg.player.PlayerData;
import fr.patatedouce.foxysg.player.Stat;
import fr.patatedouce.foxysg.utils.Symbols;
import fr.patatedouce.foxysg.utils.chat.Color;
import fr.patatedouce.foxysg.utils.command.BaseCommand;
import fr.patatedouce.foxysg.utils.command.Command;
import fr.patatedouce.foxysg.utils.command.CommandArgs;
import fr.patatedouce.foxysg.listeners.PrestigeListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.ELF;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class PrestigeCommand extends BaseCommand {

    private FoxySG plugin;
    public static Inventory prestigeinv;
    public String Prestige;

    public PrestigeCommand(FoxySG plugin) {
        this.plugin = plugin;
        this.prestigeinv = Bukkit.createInventory((InventoryHolder) null, 36, Color.translate("&cPrestiges"));
    }

    @Command(name = "prestige")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        PlayerData data = PlayerDataManager.getInstance().getByUUID(player.getUniqueId());
        String[] args = command.getArgs();
        if (data.getPoints().getAmount() < 1000) {
            Prestige = "0";
        } else if (data.getPoints().getAmount() < 2000) {
            if (data.getPoints().getAmount() > 1000) {
                Prestige = "1";
            } else if (data.getPoints().getAmount() < 3000) {
                if (data.getPoints().getAmount() > 2000) {
                    Prestige = "2";
                } else if (data.getPoints().getAmount() < 5000) {
                    if (data.getPoints().getAmount() > 3000) {
                        Prestige = "3";
                    } else if (data.getPoints().getAmount() < 7500) {
                        if (data.getPoints().getAmount() > 5000) {
                            Prestige = "4";
                        } else if (data.getPoints().getAmount() < 10000) {
                            if (data.getPoints().getAmount() > 7500) {
                                Prestige = "5";
                            } else if (data.getPoints().getAmount() < 50000) {
                                if (data.getPoints().getAmount() > 10000) {
                                    Prestige = "6";
                                } else if (data.getPoints().getAmount() > 50000) {
                                    Prestige = "Maitre";
                                }
                            }
                        }
                    }
                }
            }
        }

        ItemStack head = new ItemStack(Material.SIGN);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName(Color.translate("&c" + player.getName()));
        ArrayList<String> headLore = new ArrayList();
        headLore.add(Color.translate("&7&m-------------------------"));
        headLore.add(Color.translate("&c&lPrestiges&7&l:"));
        headLore.add(Color.translate(" "));
        headLore.add(Color.translate("&7* &bVos points&7: &c" + data.getPoints()));
        headLore.add(Color.translate("&7&m-------------------------"));
        headMeta.setLore(headLore);
        head.setItemMeta(headMeta);

        ItemStack brick = new ItemStack(Material.BRICK);
        ItemMeta brickMeta = brick.getItemMeta();
        brickMeta.setDisplayName(Color.translate("&cBrick"));
        ArrayList<String> brickLore = new ArrayList();
        brickLore.add(Color.translate("&7&m------------------------------------"));
        brickLore.add(Color.translate("&cPrestige &7&l1"));
        brickLore.add(Color.translate(" "));
        brickLore.add(Color.translate("&cHabilitées&7:"));
        brickLore.add(Color.translate("&7* &bPrefix Chat &7(&c" + Symbols.p1 + "&7)"));
        brickLore.add(Color.translate("&7* &bAccess au slots réservés"));
        brickLore.add(Color.translate(" "));
        brickLore.add(Color.translate("&7* &bVos Points&7: &c" + data.getPoints().getAmount()));
        brickLore.add(Color.translate("&7* &bPoints requis&7: &c1000"));
        brickLore.add(Color.translate(" "));
        brickLore.add(Color.translate("&7&m------------------------------------"));
        brickMeta.setLore(brickLore);
        brick.setItemMeta(brickMeta);

        ItemStack coal = new ItemStack(Material.COAL);
        ItemMeta coalMeta = coal.getItemMeta();
        coalMeta.setDisplayName(Color.translate("&cCoal"));
        ArrayList<String> coalLore = new ArrayList();
        coalLore.add(Color.translate("&7&m------------------------------------"));
        coalLore.add(Color.translate("&cPrestige &a&l2"));
        coalLore.add(Color.translate(" "));
        coalLore.add(Color.translate("&cHabilitées&7:"));
        coalLore.add(Color.translate("&7* &bPrefix Chat &7(&c" + Symbols.p2 + "&7)"));
        coalLore.add(Color.translate("&7* &b30s de speed 2 au start"));
        coalLore.add(Color.translate("&7* &bAccess au slots réservés"));
        coalLore.add(Color.translate(" "));
        coalLore.add(Color.translate("&7* &bVos Points&7: &c" + data.getPoints().getAmount()));
        coalLore.add(Color.translate("&7* &bPoints requis&7: &c2000"));
        coalLore.add(Color.translate(" "));
        coalLore.add(Color.translate("&7&m------------------------------------"));
        coalMeta.setLore(coalLore);
        coal.setItemMeta(coalMeta);

        ItemStack iron = new ItemStack(Material.IRON_INGOT);
        ItemMeta ironMeta = iron.getItemMeta();
        ironMeta.setDisplayName(Color.translate("&cIron"));
        ArrayList<String> ironLore = new ArrayList();
        ironLore.add(Color.translate("&7&m------------------------------------"));
        ironLore.add(Color.translate("&cPrestige &e&l3"));
        ironLore.add(Color.translate(" "));
        ironLore.add(Color.translate("&cHabilitées&7:"));
        ironLore.add(Color.translate("&7* &bPrefix Chat &7(&c" + Symbols.p3 + "&7)"));
        ironLore.add(Color.translate("&7* &b60s de speed 2 au start"));
        ironLore.add(Color.translate("&7* &bAccess au slots réservés"));
        ironLore.add(Color.translate(" "));
        ironLore.add(Color.translate("&7* &bVos Points&7: &c" + data.getPoints().getAmount()));
        ironLore.add(Color.translate("&7* &bPoints requis&7: &c3000"));
        ironLore.add(Color.translate(" "));
        ironLore.add(Color.translate("&7&m------------------------------------"));
        ironMeta.setLore(ironLore);
        iron.setItemMeta(ironMeta);

        ItemStack gold = new ItemStack(Material.GOLD_INGOT);
        ItemMeta goldMeta = gold.getItemMeta();
        goldMeta.setDisplayName(Color.translate("&cGold"));
        ArrayList<String> goldLore = new ArrayList();
        goldLore.add(Color.translate("&7&m------------------------------------"));
        goldLore.add(Color.translate("&cPrestige &6&l4"));
        goldLore.add(Color.translate(" "));
        goldLore.add(Color.translate("&cHabilitées&7:"));
        goldLore.add(Color.translate("&7* &bPrefix Chat &7(&c" + Symbols.p4 + "&7)"));
        goldLore.add(Color.translate("&7* &b15s de speed 3 au start"));
        goldLore.add(Color.translate("&7* &bPosition exactes du tracker"));
        goldLore.add(Color.translate("&7* &bAccess au slots réservés"));
        goldLore.add(Color.translate(" "));
        goldLore.add(Color.translate("&7* &bVos Points&7: &c" + data.getPoints().getAmount()));
        goldLore.add(Color.translate("&7* &bPoints requis&7: &c5000"));
        goldLore.add(Color.translate(" "));
        goldLore.add(Color.translate("&7&m------------------------------------"));
        goldMeta.setLore(goldLore);
        gold.setItemMeta(goldMeta);

        ItemStack dmd = new ItemStack(Material.DIAMOND);
        ItemMeta dmdMeta = dmd.getItemMeta();
        dmdMeta.setDisplayName(Color.translate("&cDiamond"));
        ArrayList<String> dmdLore = new ArrayList();
        dmdLore.add(Color.translate("&7&m------------------------------------"));
        dmdLore.add(Color.translate("&cPrestige &c&l5"));
        dmdLore.add(Color.translate(" "));
        dmdLore.add(Color.translate("&cHabilitées&7:"));
        dmdLore.add(Color.translate("&7* &bPrefix Chat &7(&c" + Symbols.p5 + "&7)"));
        dmdLore.add(Color.translate("&7* &b30s de speed 3 au start"));
        dmdLore.add(Color.translate("&7* &bPosition exactes du tracker"));
        dmdLore.add(Color.translate("&7* &bAccess au slots réservés"));
        dmdLore.add(Color.translate("&7* &bAccess au /announce"));
        dmdLore.add(Color.translate(" "));
        dmdLore.add(Color.translate("&7* &bVos Points&7: &c" + data.getPoints().getAmount()));
        dmdLore.add(Color.translate("&7* &bPoints requis&7: &c7500"));
        dmdLore.add(Color.translate(" "));
        dmdLore.add(Color.translate("&7&m------------------------------------"));
        dmdMeta.setLore(dmdLore);
        dmd.setItemMeta(dmdMeta);

        ItemStack star = new ItemStack(Material.EMERALD);
        ItemMeta starMeta = star.getItemMeta();
        starMeta.setDisplayName(Color.translate("&cEmerald"));
        ArrayList<String> starLore = new ArrayList();
        starLore.add(Color.translate("&7&m------------------------------------"));
        starLore.add(Color.translate("&cPrestige &4&l6"));
        starLore.add(Color.translate(" "));
        starLore.add(Color.translate("&cHabilitées&7:"));
        starLore.add(Color.translate("&7* &bPrefix Chat &7(&c" + Symbols.p6 + "&7)"));
        starLore.add(Color.translate("&7* &b30s de speed 4 au start"));
        starLore.add(Color.translate("&7* &bPosition exactes du tracker"));
        starLore.add(Color.translate("&7* &bAccess au slots réservés"));
        starLore.add(Color.translate("&7* &bAccess au /announce"));
        starLore.add(Color.translate(" "));
        starLore.add(Color.translate("&7* &bVos Points&7: &c" + data.getPoints().getAmount()));
        starLore.add(Color.translate("&7* &bPoints requis&7: &c10000"));
        starLore.add(Color.translate(" "));
        starLore.add(Color.translate("&7&m------------------------------------"));
        starMeta.setLore(starLore);
        star.setItemMeta(starMeta);

        ItemStack master = new ItemStack(Material.NETHER_STAR);
        ItemMeta masterMeta = star.getItemMeta();
        masterMeta.setDisplayName(Color.translate("&cMaitre"));
        ArrayList<String> masterLore = new ArrayList();
        masterLore.add(Color.translate("&7&m------------------------------------"));
        masterLore.add(Color.translate("&cPrestige &d&lMaitre"));
        masterLore.add(Color.translate(" "));
        masterLore.add(Color.translate("&cHabilitées&7:"));
        masterLore.add(Color.translate("&7* &bPrefix Chat &7(&c" + Symbols.p7 + "&7)"));
        masterLore.add(Color.translate("&7* &b45s de speed 4 au start"));
        masterLore.add(Color.translate("&7* &bPosition exactes du tracker"));
        masterLore.add(Color.translate("&7* &bAccess au slots réservés"));
        masterLore.add(Color.translate("&7* &bAccess au /announce"));
        masterLore.add(Color.translate(" "));
        masterLore.add(Color.translate("&7* &bVos Points&7: &c" + data.getPoints().getAmount()));
        masterLore.add(Color.translate("&7* &bPoints requis&7: &c50000"));
        masterLore.add(Color.translate(" "));
        masterLore.add(Color.translate("&7&m------------------------------------"));
        masterMeta.setLore(masterLore);
        master.setItemMeta(masterMeta);

        prestigeinv.setItem(4, head);
        prestigeinv.setItem(19, brick);
        prestigeinv.setItem(20, coal);
        prestigeinv.setItem(21, iron);
        prestigeinv.setItem(22, gold);
        prestigeinv.setItem(23, dmd);
        prestigeinv.setItem(24, star);
        prestigeinv.setItem(25, master);

        if (args.length == 0) {
            player.sendMessage(Color.translate("&cUtilisation: /prestige <info/help>."));
            return;
        }

        if (args.length == 1 && args[0].equals("help")) {
            try {
                player.sendMessage(Color.translate("&7&m------------------------------------"));
                player.sendMessage(Color.translate("&cPrestige Help"));
                player.sendMessage(Color.translate(" "));
                player.sendMessage(Color.translate("&bJouez, faites des kills pour gagner"));
                player.sendMessage(Color.translate("&bdes points et augmenter en prestige."));
                player.sendMessage(Color.translate(" "));
                player.sendMessage(Color.translate("&6Les prestiges débloquent&7:"));
                player.sendMessage(Color.translate("&7* &aDes Habilitées exclusives"));
                player.sendMessage(Color.translate("&7* &aDes Prefix"));
                player.sendMessage(Color.translate("&7* &aEt bien plus !"));
                player.sendMessage(Color.translate(" "));
                player.sendMessage(Color.translate("&cVos Points&7: &b" + data.getPoints().getAmount()));
                player.sendMessage(Color.translate(" "));
                player.sendMessage(Color.translate("&7&m------------------------------------"));

            } catch (Exception e) {
                player.sendMessage(Color.translate("&cVotre data n'a pas été enregistrée veuillez vous reconnecter."));
            }
        }

        if (args.length == 1 && args[0].equals("info")) {
            player.openInventory(this.prestigeinv);
            player.updateInventory();
            return;
        }
    }
}