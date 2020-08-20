package me.jaskowicz.oneinthechamber.Tasks;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Settings.GameSettings;
import me.jaskowicz.oneinthechamber.Utils.Game;
import me.jaskowicz.oneinthechamber.Utils.User;
import me.jaskowicz.oneinthechamber.UtilsExtra.PlayerUtil;
import me.jaskowicz.oneinthechamber.UtilsExtra.TimeUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class StartingGameTask extends BukkitRunnable {

    private Plugin plugin = OneInTheChamber.getPlugin(OneInTheChamber.class);
    private Game game;
    private int time;
    public int timetilstart = 60;

    // time is in seconds
    public StartingGameTask(Game game, int time) {
        this.game = game;
        this.time = time;
    }

    /*
    for (User u : game.getUsersPlaying().values()) {
                    u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "It appears that the game has been set to force start!");
                }
                game.setStarted(true);
     */

    @Override
    public void run() {
        if (!game.hasStarted() && !game.isFinished()) {
            if (timetilstart == 60) {
                for (User u : game.getUsersPlaying().values()) {
                    u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game is starting in " + ChatColor.AQUA + timetilstart + ChatColor.WHITE + " second(s)!");
                    u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 0.5F);
                }
            } else if (timetilstart == 30) {
                for (User u : game.getUsersPlaying().values()) {
                    u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game is starting in " + ChatColor.AQUA + timetilstart + ChatColor.WHITE + " second(s)!");
                    u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 0.5F);
                }
            } else if (timetilstart <= 10 && timetilstart > 5) {
                for (User u : game.getUsersPlaying().values()) {
                    u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game is starting in " + ChatColor.AQUA + timetilstart + ChatColor.WHITE + " second(s)!");
                    u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 0.5F);
                }
            } else if (timetilstart <= 5 && timetilstart > 0) {

                if(timetilstart == 5) {
                    Random generator = new Random();

                    game.setBlockingMovement(true);

                    for (User u : game.getUsersPlaying().values()) {
                        int randomIndex = generator.nextInt(game.getArenaInUse().getSpawnpointsNotUsed().size());
                        Location randomSpawnpoint = (Location) game.getArenaInUse().getSpawnpointsNotUsed().values().toArray()[randomIndex];

                        /*
                        boolean playerFound;

                        for (User us : game.getUsersPlaying().values()) {
                            if(us.getPlayer().getLocation().getBlockX() == randomSpawnpoint.getBlockX() && us.getPlayer().getLocation().getBlockY() == randomSpawnpoint.getBlockY() && us.getPlayer().getLocation().getBlockZ() == randomSpawnpoint.getBlockZ()) {
                                playerFound = true;
                                break
                            }
                        }

                         */


                        u.getPlayer().teleport(randomSpawnpoint);
                        PlayerUtil.clearItems(u);

                        game.getArenaInUse().removeSpawnpointNotUsed(randomSpawnpoint);

                        u.getPlayer().getOpenInventory().close();
                    }
                }

                for (User u : game.getUsersPlaying().values()) {
                    u.getPlayer().sendTitle("" + ChatColor.GOLD + ChatColor.BOLD + "Starting in " + timetilstart + "!", ChatColor.YELLOW + "Get ready...", 0, 40, 0);
                    u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game is starting in " + ChatColor.AQUA + timetilstart + ChatColor.WHITE + " second(s)!");
                    u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 1F);
                }
            }

            for(User us : game.getUsersPlaying().values()) {
                us.getCurrentScoreboardAPI().changeLine("timeleft", "" + ChatColor.AQUA + TimeUtil.getDurationString(timetilstart));
            }


            if (timetilstart > 0) {
                timetilstart -= 1;
            } else {
                for (User u : game.getUsersPlaying().values()) {
                    u.getPlayer().sendTitle("" + ChatColor.GOLD + ChatColor.BOLD + "Starting in " + timetilstart + "!", ChatColor.YELLOW + "Get ready...", 0, 40, 0);
                }
                game.setStarted(true);
                game.setBlockingMovement(false);
                GameTask gameTask = new GameTask(game, time);
                game.setGameTask(gameTask);
                gameTask.runTaskTimer(plugin, 0, 20);
                this.cancel();
            }
        }
    }
}
