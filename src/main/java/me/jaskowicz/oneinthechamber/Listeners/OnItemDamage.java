package me.jaskowicz.oneinthechamber.Listeners;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Utils.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class OnItemDamage implements Listener {

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        User user = OneInTheChamber.USERS.get(player.getUniqueId());

        if(user.getGameIn() != null) {
            event.setCancelled(true);
            player.updateInventory();
        }
    }
}
