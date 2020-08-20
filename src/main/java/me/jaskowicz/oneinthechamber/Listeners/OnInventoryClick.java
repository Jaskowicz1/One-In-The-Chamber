package me.jaskowicz.oneinthechamber.Listeners;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Utils.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class OnInventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        User user = OneInTheChamber.USERS.get(player.getUniqueId());

        ItemStack clicked = event.getCurrentItem();
        Inventory inv = event.getInventory();
        InventoryView invview = event.getView();
        String invname = event.getView().getTitle();

        if(clicked == null) {
            return;
        }

        if(clicked.getItemMeta() == null) {
            return;
        }

        if(user.getGameIn() != null) {
            if(invname.contains("Your Statistics")) {
                event.setCancelled(true);
            } else {
                if(!user.getGameIn().hasStarted() && !user.getGameIn().isFinished()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
