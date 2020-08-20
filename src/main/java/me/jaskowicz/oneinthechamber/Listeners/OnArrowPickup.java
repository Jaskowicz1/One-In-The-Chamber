package me.jaskowicz.oneinthechamber.Listeners;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Utils.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupArrowEvent;

public class OnArrowPickup implements Listener {

    @EventHandler
    public void onArrowPickup(PlayerPickupArrowEvent e) {
        Player player = e.getPlayer();
        User user = OneInTheChamber.USERS.get(player.getUniqueId());

        if(user.getGameIn() != null) {
            e.setCancelled(true);
        }
    }
}
