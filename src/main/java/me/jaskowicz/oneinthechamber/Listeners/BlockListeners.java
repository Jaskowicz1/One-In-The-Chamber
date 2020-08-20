package me.jaskowicz.oneinthechamber.Listeners;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Utils.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListeners implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player pl = e.getPlayer();
        User user = OneInTheChamber.USERS.get(pl.getUniqueId());

        if(user.getGameIn() != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player pl = e.getPlayer();
        User user = OneInTheChamber.USERS.get(pl.getUniqueId());

        if(user.getGameIn() != null) {
            e.setCancelled(true);
        }
    }
}
