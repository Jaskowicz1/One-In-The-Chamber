package me.jaskowicz.oneinthechamber.Listeners;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Utils.User;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class OnEntityInteract implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        Player player = event.getPlayer();
        User user = OneInTheChamber.USERS.get(player.getUniqueId());

        if (event.getRightClicked().getType().equals(EntityType.ITEM_FRAME)) {
            if(user.getGameIn() != null) {
                if (!(event.getPlayer().getGameMode() == GameMode.CREATIVE)) {
                    event.setCancelled(true);
                }
            }
        }

        if (event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
            if(user.getGameIn() != null) {
                if (!(event.getPlayer().getGameMode() == GameMode.CREATIVE)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
