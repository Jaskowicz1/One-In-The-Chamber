package me.jaskowicz.oneinthechamber.Inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GameInventory {

    public static Inventory getInventory() {
        Inventory PlayerHotbar = Bukkit.createInventory(null, 9, "Hotbar");
        PlayerHotbar.setItem(0, createDisplay(Material.IRON_SWORD, ChatColor.RESET + "" + ChatColor.RED + "Knife", ChatColor.RESET + "" + ChatColor.GRAY + "Stab your foe to death.", 1));
        PlayerHotbar.setItem(1, createDisplay(Material.BOW, ChatColor.RESET + "" + ChatColor.RED + "Hunter's Bow", ChatColor.RESET + "" + ChatColor.GRAY + "A bow that can only be handled by one, worthy to seek blood.", 1));
        PlayerHotbar.setItem(8, createDisplay(Material.ARROW, ChatColor.RESET + "" + ChatColor.RED + "Arrow of Death", ChatColor.RESET + "" + ChatColor.GRAY + "A sacred arrow known to instantly wipe out anyone who is hit by such arrow.", 1));

        return PlayerHotbar;
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
