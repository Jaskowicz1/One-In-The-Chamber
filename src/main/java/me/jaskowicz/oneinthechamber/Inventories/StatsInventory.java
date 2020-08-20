package me.jaskowicz.oneinthechamber.Inventories;

import me.jaskowicz.oneinthechamber.Utils.User;
import me.jaskowicz.oneinthechamber.UtilsExtra.MathsUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class StatsInventory {

    public static Inventory getInventory(User user) {

        String kd = "";

        if(user.getKills() == 0) {
            kd = ChatColor.AQUA + "0";
        } else {
            if(user.getDeaths() == 0) {
                kd = ChatColor.AQUA + "1";
            } else {
                kd = "" + ChatColor.AQUA + MathsUtil.round(((double) user.getKills()) / ((double) user.getDeaths()), 2);
            }
        }

        Inventory inventory = Bukkit.createInventory(null, 27, "Your Statistics");
        for(int i=0; i < 9; i++) {
            inventory.setItem(i, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "-=-", ""));
        }

        inventory.setItem(9, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "-=-", ""));
        inventory.setItem(10, createDisplay(Material.LIME_CONCRETE, ChatColor.RESET + "" + ChatColor.WHITE + ChatColor.BOLD + "Kills: " + ChatColor.AQUA + user.getKills(), ChatColor.RESET + "" + ChatColor.GRAY + "This is how many kills you have." + "\n" + ChatColor.RESET + "" + ChatColor.GRAY + "Your K/D: " + kd));
        inventory.setItem(11, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "-=-", ""));
        inventory.setItem(12, createDisplay(Material.RED_CONCRETE, ChatColor.RESET + "" + ChatColor.WHITE + ChatColor.BOLD + "Deaths: " + ChatColor.AQUA + user.getDeaths(), ChatColor.RESET + "" + ChatColor.GRAY + "This is how many deaths you have." + "\n" + ChatColor.RESET + "" + ChatColor.GRAY + "Your K/D: " + kd));
        inventory.setItem(13, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "-=-", ""));
        inventory.setItem(14, createDisplay(Material.YELLOW_CONCRETE, ChatColor.RESET + "" + ChatColor.WHITE + ChatColor.BOLD + "Wins: " + ChatColor.AQUA + user.getWins(), ChatColor.RESET + "" + ChatColor.GRAY + "This is how many wins you have." + "\n" + ChatColor.RESET + "" + ChatColor.GRAY + "You have also drawn " + ChatColor.AQUA + user.getDraws() + ChatColor.GRAY + " time(s)."));
        inventory.setItem(15, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "-=-", ""));
        inventory.setItem(16, createDisplay(Material.BLACK_CONCRETE, ChatColor.RESET + "" + ChatColor.WHITE + ChatColor.BOLD + "Loses: " + ChatColor.AQUA + user.getLoses(), ChatColor.RESET + "" + ChatColor.GRAY + "This is how many loses you have." + "\n" + ChatColor.RESET + "" + ChatColor.GRAY + "You have also drawn " + ChatColor.AQUA + user.getDraws() + ChatColor.GRAY + " time(s)."));
        inventory.setItem(17, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "-=-", ""));

        for(int i=18; i < 27; i++) {
            inventory.setItem(i, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "-=-", ""));
        }

        return inventory;
    }

    private static ItemStack createDisplay(Material material, String name, String lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        if (lore != null) {
            ArrayList<String> Lore = new ArrayList<String>();
            String[] lore2 = lore.split("\n");
            for (String lore3 : lore2) {
                lore3 = lore3.replace("\n", "");
                Lore.add(lore3);
            }
            meta.setLore(Lore);
        }
        item.setItemMeta(meta);
        return item;
    }
}
