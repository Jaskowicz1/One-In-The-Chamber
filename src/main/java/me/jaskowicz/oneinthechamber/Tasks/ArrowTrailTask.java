package me.jaskowicz.oneinthechamber.Tasks;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Utils.Game;
import me.jaskowicz.oneinthechamber.Utils.User;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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

        Arrow arrow1 = (Arrow) arrow;
        Player player = (Player) arrow1.getShooter();
        assert player != null;
        User user = OneInTheChamber.USERS.get(player.getUniqueId());

        if (user.hasFireTrailOn()) {
            Objects.requireNonNull(arrow.getLocation().getWorld()).spawnParticle(Particle.FLAME, arrow.getLocation(), 5, 0, 0, 0, 0.07);
        } else if (user.hasLavaTrailOn()) {
            Objects.requireNonNull(arrow.getLocation().getWorld()).spawnParticle(Particle.DRIP_LAVA, arrow.getLocation(), 5, 0, 0, 0, 0.07);
        } else if (user.hasWaterTrailOn()) {
            Objects.requireNonNull(arrow.getLocation().getWorld()).spawnParticle(Particle.DRIP_WATER, arrow.getLocation(), 5, 0, 0, 0, 0.07);
        } else if (user.hasHeartTrailOn()) {
            Objects.requireNonNull(arrow.getLocation().getWorld()).spawnParticle(Particle.HEART, arrow.getLocation(), 5, 0, 0, 0, 0.07);
        } else if (user.hasNoteTrailOn()) {
            Objects.requireNonNull(arrow.getLocation().getWorld()).spawnParticle(Particle.NOTE, arrow.getLocation(), 5, 0, 0, 0, 0.07);
        } else if (user.hasSnowTrailOn()) {
            Objects.requireNonNull(arrow.getLocation().getWorld()).spawnParticle(Particle.SNOW_SHOVEL, arrow.getLocation(), 5, 0, 0, 0, 0.07);
        } else if (user.hasBloodTrailOn()) {
            Objects.requireNonNull(arrow.getLocation().getWorld()).spawnParticle(Particle.BLOCK_CRACK, arrow.getLocation(), 5, 0, 0, 0, 0.07, Material.REDSTONE_BLOCK.getData());
        } else {
            Objects.requireNonNull(arrow.getLocation().getWorld()).spawnParticle(Particle.VILLAGER_HAPPY, arrow.getLocation(), 5, 0, 0, 0, 0.07);
        }

        arrow.getLocation().getWorld().playSound(arrow.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2F, 2F);
    }
}
