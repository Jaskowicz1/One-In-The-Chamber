package me.jaskowicz.oneinthechamber.Listeners;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Utils.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class OnFoodChange implements Listener {

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        if(e.getEntity() instanceof Player) {
            Player pl = (Player) e.getEntity();
            User user = OneInTheChamber.USERS.get(pl.getUniqueId());

            if(user.getGameIn() != null) {
                e.setCancelled(true);
            }
        }
    }
}
