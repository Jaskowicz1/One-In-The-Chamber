package me.jaskowicz.oneinthechamber.Inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class LobbyInventory {

    public static Inventory getInventory(Player player) {

        if(player.hasPermission("oneinthechamber.manage")) {
            Inventory PlayerHotbar = Bukkit.createInventory(null, 9, "Hotbar");
            PlayerHotbar.setItem(0, createDisplay(Material.CHEST, ChatColor.RESET + "" + ChatColor.GOLD + ChatColor.BOLD + "Cosmetics", ChatColor.RESET + "" + ChatColor.GRAY + "Cosmetics for your arrows!", 1));
            PlayerHotbar.setItem(4, createDisplay(Material.NETHER_STAR, ChatColor.RESET + "" + ChatColor.GOLD + ChatColor.BOLD + "Statistics", ChatColor.RESET + "" + ChatColor.GRAY + "Check your stats!", 1));
            PlayerHotbar.setItem(7, createDisplay(Material.LIME_TERRACOTTA, ChatColor.RESET + "" + ChatColor.GREEN + ChatColor.BOLD + "Force Start", ChatColor.RESET + "" + ChatColor.GRAY + "Force start the game.", 1));
            PlayerHotbar.setItem(8, createDisplay(Material.BARRIER, ChatColor.RESET + "" + ChatColor.RED + ChatColor.BOLD + "Leave Game", ChatColor.RESET + "" + ChatColor.GRAY + "Leave the game you're in.", 1));

            return PlayerHotbar;
        } else {
            Inventory PlayerHotbar = Bukkit.createInventory(null, 9, "Hotbar");
            PlayerHotbar.setItem(0, createDisplay(Material.CHEST, ChatColor.RESET + "" + ChatColor.GOLD + "Cosmetics", ChatColor.RESET + "" + ChatColor.GRAY + "Cosmetics for your arrows!", 1));
            PlayerHotbar.setItem(4, createDisplay(Material.NETHER_STAR, ChatColor.RESET + "" + ChatColor.GOLD + ChatColor.BOLD + "Statistics", ChatColor.RESET + "" + ChatColor.GRAY + "Check your stats!", 1));
            PlayerHotbar.setItem(8, createDisplay(Material.BARRIER, ChatColor.RESET + "" + ChatColor.RED + ChatColor.BOLD + "Leave Game", ChatColor.RESET + "" + ChatColor.GRAY + "Leave the game you're in.", 1));

            return PlayerHotbar;
        }
    }

    private static ItemStack createDisplay(Material material, String name, String lore, int Amount) {
        ItemStack item = new ItemStack(material, Amount);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add(lore);
        meta.setLore(Lore);
        item.setItemMeta(meta);
        return item;
    }
}
