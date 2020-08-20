package me.jaskowicz.oneinthechamber.Tasks;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Utils.Game;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class ArrowTrailTask extends BukkitRunnable {

    private Plugin plugin = OneInTheChamber.getPlugin(OneInTheChamber.class);
    private Entity arrow;

    public ArrowTrailTask(Entity arrow) {
        this.arrow = arrow;
    }

    @Override
    public void run() {
        Objects.requireNonNull(arrow.getLocation().getWorld()).spawnParticle(Particle.VILLAGER_HAPPY, arrow.getLocation(), 5, 0, 0, 0, 0.07);
        //Objects.requireNonNull(arrow.getLocation().getWorld()).spawnParticle(Particle.VILLAGER_HAPPY, arrow.getLocation(), 5, 0.5, 1, 0.5, 0.07);
        arrow.getLocation().getWorld().playSound(arrow.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2F, 2F);
    }
}
