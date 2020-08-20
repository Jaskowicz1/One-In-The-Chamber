package me.jaskowicz.oneinthechamber.Inventories;

import me.jaskowicz.oneinthechamber.UtilsExtra.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class CosmeticInventory {

    private static Inventory inventory = null;

    private static ItemStack head1 = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM4ZmI2MzdkNmUxYTdiYThmYTk3ZWU5ZDI5MTVlODQzZThlYzc5MGQ4YjdiZjYwNDhiZTYyMWVlNGQ1OWZiYSJ9fX0=");

    public static Inventory getInventory() {
        //Title
        inventory = Bukkit.createInventory(null, 54, "" + ChatColor.RESET + ChatColor.GOLD + "Arrow Trails");

        //Row 1
        inventory.setItem(0, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(1, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(2, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(3, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(4, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(5, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(6, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(7, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(8, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));

        //Row 2
        inventory.setItem(9, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(10, createDisplay(Material.BLAZE_POWDER, "" + ChatColor.RESET + ChatColor.RED + "Fire Trail", "" + ChatColor.RESET + ChatColor.GRAY + "Have a trail of Fire on your arrows!", 1));
        inventory.setItem(11, createDisplay(Material.LAVA_BUCKET, "" + ChatColor.RESET + ChatColor.RED + "Lava Trail", "" + ChatColor.RESET + ChatColor.GRAY + "Have a trail of Lava on your arrows!", 1));
        inventory.setItem(12, createDisplay(Material.WATER_BUCKET, "" + ChatColor.RESET + ChatColor.RED + "Water Trail", "" + ChatColor.RESET + ChatColor.GRAY + "Have a trail of Water on your arrows!", 1));
        inventory.setItem(13, createHead(head1, "" + ChatColor.RESET + ChatColor.RED + "Heart Trail", "" + ChatColor.RESET + ChatColor.GRAY + "Have a trail of Hearts on your arrows!"));
        inventory.setItem(14, createDisplay(Material.NOTE_BLOCK, "" + ChatColor.RESET + ChatColor.RED + "Note Trail", "" + ChatColor.RESET + ChatColor.GRAY + "Have a trail of Notes on your arrows!", 1));
        inventory.setItem(15, createDisplay(Material.SNOWBALL, "" + ChatColor.RESET + ChatColor.RED + "Snow Trail", "" + ChatColor.RESET + ChatColor.GRAY + "Have a trail of Snow on your arrows!", 1));
        inventory.setItem(16, createDisplay(Material.REDSTONE, "" + ChatColor.RESET + ChatColor.RED + "Blood Trail", "" + ChatColor.RESET + ChatColor.GRAY + "Have a trail of Blood on your arrows!", 1));
        inventory.setItem(17, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));

        //Row 3
        inventory.setItem(18, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(19, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(20, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(21, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(22, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        //inventory.setItem(19, createDisplay(Material.REDSTONE, "" + ChatColor.RESET + ChatColor.RED + "Angry Trail", "" + ChatColor.RESET + ChatColor.GRAY + "Have a trail of Angry particles!", 1));
        //inventory.setItem(20, createDisplay(Material.BOOK, "" + ChatColor.RESET + ChatColor.RED + "Enchant Trail", "" + ChatColor.RESET + ChatColor.GRAY + "Have a trail of Enchantment particles!", 1));
        //inventory.setItem(21, createDisplay(Material.INK_SAC, "" + ChatColor.RESET + ChatColor.RED + "Purple Trail", "" + ChatColor.RESET + ChatColor.GRAY + "Have a trail of Purple particles!", 1));
        //inventory.setItem(22, createDisplay(Material.REDSTONE, "" + ChatColor.RESET + ChatColor.RED + "Blood Trail", "" + ChatColor.RESET + ChatColor.GRAY + "Have a trail of Blood particles!", 1));
        inventory.setItem(23, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(24, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(25, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(26, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));


        //Row 4
        inventory.setItem(27, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(28, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(29, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(30, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(31, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(32, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(33, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(34, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(35, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));

        //Row 5
        inventory.setItem(36, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(37, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(38, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(39, createDisplay(Material.RED_CONCRETE, "" + ChatColor.RESET + ChatColor.RED + "Clear arrow trails", "" + ChatColor.RESET + ChatColor.GRAY + "Clear all arrow trails.", 1));
        inventory.setItem(40, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(41, createDisplay(Material.REDSTONE_BLOCK, "" + ChatColor.RESET + ChatColor.RED + "Exit", "" + ChatColor.RESET + ChatColor.GRAY + "Close the menu.", 1));
        inventory.setItem(42, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(43, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(44, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));

        //Row 6
        inventory.setItem(45, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(46, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(47, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(48, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(49, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(50, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(51, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(52, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));
        inventory.setItem(53, createDisplay(Material.BLACK_STAINED_GLASS_PANE, "*-*", null, 1));

        return inventory;
    }


    private static ItemStack createDisplay(Material material, String name, String lore, int Amount) {
        ItemStack item = new ItemStack(material, Amount);
        ItemMeta meta = item.getItemMeta();
        if (name != null) {
            meta.setDisplayName(name);
        }
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

    private static ItemStack createHead(ItemStack item, String displayName, String lore) {
        SkullMeta headMeta = (SkullMeta) item.getItemMeta();

        if (displayName != null) {
            assert headMeta != null;
            headMeta.setDisplayName(displayName);
        }
        if (lore != null) {
            ArrayList<String> Lore = new ArrayList<String>();
            String[] lore2 = lore.split("\n");

            for (String lore3 : lore2) {
                lore3 = lore3.replace("\n", "");
                Lore.add(lore3);
            }

            assert headMeta != null;
            headMeta.setLore(Lore);
        }

        item.setItemMeta(headMeta);

        return item;
    }
}
