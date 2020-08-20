package me.jaskowicz.oneinthechamber.Listeners;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Utils.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class OnTeleport implements Listener {

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        User user = OneInTheChamber.USERS.get(player.getUniqueId());

        if(user != null) {
            if (user.getGameIn() != null) {
                if (event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE) {
                    event.setCancelled(true);
                    player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You can't teleport as a spectator!");
                }
            }
        }
    }
}
