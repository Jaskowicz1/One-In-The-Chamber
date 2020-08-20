package me.jaskowicz.oneinthechamber.UtilsExtra;

import me.jaskowicz.oneinthechamber.Utils.User;
import org.bukkit.inventory.ItemStack;

public class PlayerUtil {

    public static void storeAndClearItems(User user) {
        for(int i = 0; i < user.getPlayer().getInventory().getSize(); i++) {
            ItemStack itemStack = user.getPlayer().getInventory().getItem(i);
            user.addLastItem(i, itemStack);
        }

        user.getPlayer().getInventory().clear();
    }

    public static void clearItems(User user) {
        user.getPlayer().getInventory().clear();
    }

    public static void restoreItemsAndClearSaved(User user) {
        user.getPlayer().getInventory().clear();

        for(int i : user.getLastItems().keySet()) {
            ItemStack itemStack = user.getLastItems().get(i);
            user.getPlayer().getInventory().setItem(i, itemStack);
        }

        user.getPlayer().updateInventory();
        user.clearLastItems();
    }
}
