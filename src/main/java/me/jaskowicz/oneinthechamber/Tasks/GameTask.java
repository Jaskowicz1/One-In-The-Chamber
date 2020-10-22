package me.jaskowicz.oneinthechamber.Tasks;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.jaskowicz.oneinthechamber.Inventories.GameInventory;
import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Settings.GameSettings;
import me.jaskowicz.oneinthechamber.Utils.Game;
import me.jaskowicz.oneinthechamber.Utils.User;
import me.jaskowicz.oneinthechamber.UtilsExtra.PlayerUtil;
import me.jaskowicz.oneinthechamber.UtilsExtra.TimeUtil;
import org.bukkit.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class GameTask extends BukkitRunnable {

    private Plugin plugin = OneInTheChamber.getPlugin(OneInTheChamber.class);
    private Game game;
    private int time;

    private int maxtime;

    // time is in seconds
    public GameTask(Game game, int time) {
        this.game = game;
        this.time = time;
        this.maxtime = time;
    }

    /* TODO:
        *  Redo this entire class.
     */

    public void run() {

        if(game.hasStarted()) {
            // Starting area
            if (time == maxtime) {

                Random generator = new Random();

                for (User us : game.getUsersPlaying().values()) {

                    // Title here.
                    us.getPlayer().sendTitle("" + ChatColor.GREEN + ChatColor.BOLD + "Begin!", ChatColor.YELLOW + "First to " + GameSettings.maxValue + " points wins!", 0, 40, 0);
                    us.getPlayer().playSound(us.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 2F);
                    us.getPlayer().sendMessage(ChatColor.GREEN + "The game has started.");

                    game.addUserAlive(us.getPlayer().getUniqueId(), us);

                    us.getPlayer().getInventory().clear();

                    us.getPlayer().getInventory().setContents(GameInventory.getInventory().getContents());

                    us.getPlayer().setHealth(20);
                    us.getPlayer().setFoodLevel(20);
                    us.getPlayer().setSaturation(10);
                    us.getPlayer().setGameMode(GameMode.ADVENTURE);

                    us.getCurrentScoreboardAPI().clearLines();
                    us.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.WHITE, "timelefttext", "" + ChatColor.WHITE + "Time left:", 26);
                    us.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.GREEN, "timeleft", "" + ChatColor.AQUA + TimeUtil.getDurationString(time), 25);
                    us.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.RED, "blankLine1", "" + ChatColor.STRIKETHROUGH, 24);

                    us.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.AQUA, "firsttotext", "" + ChatColor.WHITE + "First to:", 23);
                    us.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.WHITE, "firstto", "" + ChatColor.AQUA + GameSettings.maxValue + " points", 22);
                    us.getCurrentScoreboardAPI().addLineAtScore(ChatColor.YELLOW + "" + ChatColor.RED, "blankLine2", "" + ChatColor.STRIKETHROUGH, 21);


                    for (User user : game.getUsersPlaying().values()) {
                        us.getCurrentScoreboardAPI().setScoreAtLine(user.getPlayer().getName(), 0);
                    }

                    // Just to make sure it goes back to normal.
                    for(Location spawnLoc : game.getArenaInUse().getSpawnpoints().values()) {
                        game.getArenaInUse().addSpawnpointNotUsed(spawnLoc);
                    }

                    //us.getCurrentScoreboardAPI().addLineAtScore(ChatColor.YELLOW + "" + ChatColor.WHITE, "blankLine1", "" + ChatColor.STRIKETHROUGH, -1);
                    //us.getCurrentScoreboardAPI().addLineAtScore(ChatColor.AQUA + "" + ChatColor.WHITE, "websiteLink", "" + ChatColor.GRAY + ChatColor.ITALIC + MainSettings.m_Website, -2);
                }

                time -= 1;
            } else {

                if (time > 0) {
                    if (time == 60) {
                        for (User u : game.getUsersPlaying().values()) {
                            u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game will end in " + ChatColor.AQUA + time + ChatColor.WHITE + " second(s) left!");
                            u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 0.5F);
                        }
                    } else if (time == 30) {
                        for (User u : game.getUsersPlaying().values()) {
                            u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game will end in " + ChatColor.AQUA + time + ChatColor.WHITE + " second(s) left!");
                            u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 0.5F);
                        }
                    } else if (time <= 10 && time > 5) {
                        for (User u : game.getUsersPlaying().values()) {
                            u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game will end in " + ChatColor.AQUA + time + ChatColor.WHITE + " second(s) left!");
                            u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 0.5F);
                        }
                    } else if (time <= 5) {
                        for (User u : game.getUsersPlaying().values()) {
                            u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game will end in " + ChatColor.AQUA + time + ChatColor.WHITE + " second(s) left!");
                            u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 1F);
                        }
                    }

                    for (User us : game.getUsersPlaying().values()) {
                        us.getCurrentScoreboardAPI().changeLine("timeleft", "" + ChatColor.GREEN + TimeUtil.getDurationString(time));
                    }

                    time -= 1;
                } else {

                    // Stop any respawn things.
                    for (BukkitTask bukkitTask1 : game.respawnTasks.keySet()) {
                        bukkitTask1.cancel();
                    }

                    game.respawnTasks.clear();


                    game.setFinished(true);
                    game.setStarted(false);

                    for (User u : game.getUsersPlaying().values()) {
                        u.getPlayer().sendTitle("" + ChatColor.GRAY + ChatColor.BOLD + "Draw!", ChatColor.YELLOW + "Maybe don't be too kind to each other..", 0, 100, 10);
                        u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game has ended! Nobody won as the time ran out!");
                        u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 5f, 0.5F);
                        if(game.getUsersPlaying().get(u.getPlayer().getUniqueId()) != null) {
                            u.setDraws(u.getDraws() + 1);
                        }
                    }

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {

                        for (User u : game.getUsersPlaying().values()) {
                            u.getPlayer().sendMessage(ChatColor.GREEN + "Thank you for playing One In The Chamber!");

                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("Connect");
                            out.writeUTF("minigames");
                            u.getPlayer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                        }

                        game.getArenaInUse().setUsedBy(null);

                        OneInTheChamber.ARENASNOTINUSE.put(game.getArenaInUse().getArenaName(), game.getArenaInUse());
                        OneInTheChamber.ARENASINUSE.remove(game.getArenaInUse().getArenaName());

                        OneInTheChamber.GAMES.remove(game.getArenaInUse());
                    }, 100);

                    this.cancel();
                }
            }
        }
    }
}
