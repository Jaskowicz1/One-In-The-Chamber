package me.jaskowicz.oneinthechamber.Listeners;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Utils.User;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class OnProjectileHit implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if(e.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getEntity();
            if(arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                User user = OneInTheChamber.USERS.get(player.getUniqueId());

                if(e.getHitEntity() != null) {
                    if (!(e.getHitEntity() instanceof Player)) {
                        if (user.getGameIn() != null) {

                            if(user.getGameIn().getARROWS().get(e.getEntity()) != null) {
                                user.getGameIn().getARROWS().get(e.getEntity()).cancel();
                                user.getGameIn().removeArrow(e.getEntity());
                            }

                            arrow.remove();
                        }
                    } else {
                        if(user.getGameIn() != null) {
                            if(user.getGameIn().getARROWS().get(e.getEntity()) != null) {
                                user.getGameIn().getARROWS().get(e.getEntity()).cancel();
                                user.getGameIn().removeArrow(e.getEntity());
                            }
                        }
                    }
                } else {
                    if (user.getGameIn() != null) {

                        if(user.getGameIn().getARROWS().get(e.getEntity()) != null) {
                            user.getGameIn().getARROWS().get(e.getEntity()).cancel();
                            user.getGameIn().removeArrow(e.getEntity());
                        }

                        arrow.remove();
                    }
                }
            }
        }
    }
}
