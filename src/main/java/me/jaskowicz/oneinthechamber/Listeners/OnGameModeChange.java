package me.jaskowicz.oneinthechamber.Listeners;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Utils.User;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class OnGameModeChange implements Listener {

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        User user = OneInTheChamber.USERS.get(player.getUniqueId());

        if(user.getGameIn() != null && user.getGameIn().hasStarted()) {
            if(event.getNewGameMode() != GameMode.SPECTATOR && event.getNewGameMode() != GameMode.ADVENTURE) {
                event.setCancelled(true);
                player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You're not allowed to change gamemodes during a game!");
            }
        }
    }
}
