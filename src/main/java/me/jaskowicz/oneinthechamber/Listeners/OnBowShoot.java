package me.jaskowicz.oneinthechamber.Listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Tasks.ArrowTrailTask;
import me.jaskowicz.oneinthechamber.Utils.Game;
import me.jaskowicz.oneinthechamber.Utils.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.Plugin;

public class OnBowShoot implements Listener {

    private Plugin plugin = OneInTheChamber.getPlugin(OneInTheChamber.class);

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            User user = OneInTheChamber.USERS.get(player.getUniqueId());
            Entity arrow = event.getProjectile();

            if(user.getGameIn() != null) {
                if(user.getGameIn().hasStarted()) {
                    Game game = user.getGameIn();

                    ArrowTrailTask arrowTrailTask = new ArrowTrailTask(arrow);
                    game.addArrow(arrow, arrowTrailTask);
                    arrowTrailTask.runTaskTimer(plugin, 0, 2);

                    // Just to make sure arrows don't stay
                    // 15 seconds counter.
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        event.getProjectile().remove();
                        event.setCancelled(true);
                    }, 400);

                } else {
                    event.getProjectile().remove();
                    event.setCancelled(true);
                }
            }
        }
    }
}
