package me.jaskowicz.oneinthechamber.Listeners;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Utils.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class OnEntityPickup implements Listener {

    @EventHandler
    public void onEntityPickup(EntityPickupItemEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            User user = OneInTheChamber.USERS.get(player.getUniqueId());

            if(user.getGameIn() != null) {
                event.setCancelled(true);
            }
        }
    }
}
