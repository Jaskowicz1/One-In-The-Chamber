package me.jaskowicz.oneinthechamber.Listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.jaskowicz.oneinthechamber.Inventories.GameInventory;
import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Settings.GameSettings;
import me.jaskowicz.oneinthechamber.Utils.Game;
import me.jaskowicz.oneinthechamber.Utils.User;
import me.jaskowicz.oneinthechamber.UtilsExtra.RandomUtil;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class OnEntityDamage implements Listener {

    private Plugin plugin = OneInTheChamber.getPlugin(OneInTheChamber.class);

    @EventHandler
    public void OnEntityDamager(EntityDamageEvent event) {

        if(event.getEntity() instanceof Player) {

            Player player = (Player) event.getEntity();
            User user = OneInTheChamber.USERS.get(player.getUniqueId());

            if(user.getGameIn() != null && user.getGameIn().hasStarted()) {

                Game game = user.getGameIn();

                if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {

                    if ((player.getHealth() - event.getFinalDamage()) <= 0 || player.isDead()) {

                        // This (should) prevent them from dying properly so they don't get "You died"
                        event.setCancelled(true);

                        player.getLocation().getWorld().strikeLightningEffect(player.getLocation());

                        user.setDeaths(user.getDeaths() + 1);

                        User lastHitBy = user.getLastHitBy();

                        if(lastHitBy == null) {
                            for (User us : game.getUsers().values()) {
                                us.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + player.getName() + ChatColor.WHITE + " has died!");
                            }
                        } else {
                            for (User us : game.getUsers().values()) {
                                us.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + player.getName() + ChatColor.WHITE + " was sentenced to death by " + ChatColor.AQUA + lastHitBy.getPlayer().getName() + ChatColor.WHITE + "!");
                            }

                            lastHitBy.setKills(lastHitBy.getKills() + 1);

                            if (lastHitBy.getPlayer().getInventory().getItem(8) != null) {
                                lastHitBy.getPlayer().getInventory().setItem(8, createDisplay(Material.ARROW, ChatColor.RESET + "" + ChatColor.RED + "An arrow", ChatColor.RESET + "" + ChatColor.GRAY + "For your bow.", lastHitBy.getPlayer().getInventory().getItem(8).getAmount() + 1));
                            } else {
                                lastHitBy.getPlayer().getInventory().setItem(8, createDisplay(Material.ARROW, ChatColor.RESET + "" + ChatColor.RED + "An arrow", ChatColor.RESET + "" + ChatColor.GRAY + "For your bow.", 1));
                            }
                        }

                        //player.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "You died!");

                        player.setHealth(20);
                        player.setFoodLevel(20);
                        player.setSaturation(20);
                        player.getInventory().clear();

                        player.setGameMode(GameMode.SPECTATOR);

                        game.removeUserAlive(player.getUniqueId());
                        game.addUserDead(player.getUniqueId(), user);

                        if(lastHitBy == null) {
                            if (game.getUsersAlive().size() > 0) {
                                AtomicReference<User> randomUser = new AtomicReference<>(RandomUtil.getRandomUser(game.getUsersAlive()));

                                player.setSpectatorTarget(randomUser.get().getPlayer());
                            }
                        } else {
                            player.setSpectatorTarget(lastHitBy.getPlayer());
                        }

                        user.setSecondsTilRespawn(3);

                        user.setLastHitBy(null);

                        if(user.hitTasks.size() > 0) {
                            for(BukkitTask bukkitTask : user.hitTasks.keySet()) {
                                bukkitTask.cancel();
                            }

                            user.hitTasks.clear();
                        }

                        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                            player.sendTitle("" + ChatColor.RED + ChatColor.BOLD + "You died!", ChatColor.YELLOW + "Respawning in " + user.getSecondsTilRespawn() + "...", 0, 40, 0);
                            user.setSecondsTilRespawn(user.getSecondsTilRespawn() - 1);
                            /*
                            if (game.getUsersDead().get(randomUser.get().getPlayer().getUniqueId()) == null) {
                                player.setSpectatorTarget(randomUser.get().getPlayer());
                            } else {
                                randomUser.set(RandomUtil.getRandomUser(game.getUsersAlive()));
                                player.setSpectatorTarget(randomUser.get().getPlayer());
                            }
                            */
                        }, 0, 20);

                        BukkitTask bukkitTask2 = Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            if (player.getGameMode() == GameMode.SPECTATOR) {
                                player.setSpectatorTarget(null);
                            }

                            player.setGameMode(GameMode.ADVENTURE);

                            player.setHealth(20);
                            player.setFoodLevel(20);
                            player.setSaturation(10);

                            user.getPlayer().getInventory().setContents(GameInventory.getInventory().getContents());

                            Random generator = new Random();

                            int randomIndex = generator.nextInt(game.getArenaInUse().getSpawnpoints().size());
                            Location randomspawnpoint = (Location) game.getArenaInUse().getSpawnpoints().values().toArray()[randomIndex];

                            player.teleport(randomspawnpoint);

                            game.removeUserDead(player.getUniqueId());
                            game.addUserAlive(player.getUniqueId(), user);

                            player.sendTitle("" + ChatColor.GREEN + ChatColor.BOLD + "You have respawned!", ChatColor.YELLOW + "Go get 'em!", 0, 20, 10);

                            bukkitTask.cancel();

                            game.respawnTasks.remove(bukkitTask);
                        }, 80);

                        game.respawnTasks.put(bukkitTask, user);
                        game.respawnTasks.put(bukkitTask2, user);

                        if (game.getUserScore().get(user) != null) {
                            if (game.getUserScore().get(user) > 0) {
                                game.addUserScore(user, game.getUserScore().get(user) - 1);

                                for (User user2 : game.getUsersPlaying().values()) {
                                    user2.getCurrentScoreboardAPI().setScoreAtLine(player.getName(), game.getUserScore().get(user));
                                }
                            }
                        }
                    }
                }
            } else if (user.getGameIn() != null && user.getGameIn().isFinished()) {
                event.setCancelled(true);
            } else if (user.getGameIn() != null && !user.getGameIn().hasStarted() && !user.getGameIn().isFinished()) {
                event.setDamage(0.0);
            }
        }
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player) {
            if(e.getDamager() instanceof Player) {
                Player damager = (Player) e.getDamager();
                Player damaged = (Player) e.getEntity();

                if(damager != damaged) {

                    User damagerUser = OneInTheChamber.USERS.get(damager.getUniqueId());
                    User damagedUser = OneInTheChamber.USERS.get(damaged.getUniqueId());

                    if (damagedUser.getGameIn() != null) {
                        if (damagerUser.getGameIn() != null) {
                            if (damagerUser.getGameIn() == damagedUser.getGameIn()) {

                                Game game = damagerUser.getGameIn();
                                if(game.hasStarted()) {

                                    Vector directionOfAttacker = e.getDamager().getLocation().getDirection();
                                    Vector directionOfDamaged = e.getEntity().getLocation().getDirection();

                                    // Backstabbed
                                    /*
                                    if(directionOfAttacker.dot(directionOfDamaged) > 0) {
                                        e.setDamage(e.getDamage() * 2);
                                        damager.sendMessage("" + ChatColor.GOLD + ChatColor.BOLD + "You backstabbed " + damaged.getName() + "! Double damage!");
                                        damaged.sendMessage("" + ChatColor.GOLD + ChatColor.BOLD + "You got backstabbed by " + damager.getName() + "!");
                                    }

                                     */

                                    damagedUser.setLastHitBy(damagerUser);

                                    if(damagedUser.hitTasks.size() > 0) {
                                        for(BukkitTask bukkitTask : damagedUser.hitTasks.keySet()) {
                                            bukkitTask.cancel();
                                        }

                                        damagedUser.hitTasks.clear();
                                    }

                                    BukkitTask hitTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                        damagedUser.setLastHitBy(null);
                                        for(BukkitTask bukkitTask : damagedUser.hitTasks.keySet()) {
                                            bukkitTask.cancel();
                                        }
                                        damagedUser.hitTasks.clear();
                                    }, 100);

                                    damagedUser.hitTasks.put(hitTask, damagedUser);

                                    if ((damaged.getHealth() - e.getFinalDamage()) <= 0 || damaged.isDead()) {

                                        damaged.getLocation().getWorld().strikeLightningEffect(damaged.getLocation());
                                        damagerUser.setKills(damagerUser.getKills() + 1);
                                        damagedUser.setDeaths(damagedUser.getDeaths() + 1);
                                        damager.playSound(damager.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5F, 2F);
                                        //damaged.playSound(damaged.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 5F, 2F);

                                        //damager.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "You killed " + ChatColor.AQUA + damaged.getName() + ChatColor.WHITE + "!");
                                        //damaged.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "You were killed by " + ChatColor.AQUA + damager.getName() + ChatColor.WHITE + "!");

                                        for(User us : game.getUsersPlaying().values()) {
                                            if (damager.getInventory().getItemInMainHand().getType().equals(Material.STONE_SWORD)) {
                                                us.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + damaged.getName() + ChatColor.WHITE + " was stabbed to death by " + ChatColor.AQUA + damager.getName() + ChatColor.WHITE + "!");
                                            } else {
                                                us.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + damaged.getName() + ChatColor.WHITE + " was murdered by " + ChatColor.AQUA + damager.getName() + ChatColor.WHITE + "!");
                                            }
                                        }

                                        damaged.setHealth(20);
                                        damaged.setFoodLevel(20);
                                        damaged.setSaturation(10);
                                        damaged.getInventory().clear();

                                        game.removeUserAlive(damaged.getUniqueId());
                                        game.addUserDead(damaged.getUniqueId(), damagedUser);

                                        damaged.setGameMode(GameMode.SPECTATOR);
                                        damaged.setSpectatorTarget(damager);
                                        damagedUser.setSecondsTilRespawn(3);

                                        damagedUser.setLastHitBy(null);

                                        if(damagedUser.hitTasks.size() > 0) {
                                            for(BukkitTask bukkitTask : damagedUser.hitTasks.keySet()) {
                                                bukkitTask.cancel();
                                            }

                                            damagedUser.hitTasks.clear();
                                        }

                                        if (damager.getInventory().getItem(8) != null) {
                                            damager.getInventory().setItem(8, createDisplay(Material.ARROW, ChatColor.RESET + "" + ChatColor.RED + "An arrow", ChatColor.RESET + "" + ChatColor.GRAY + "For your bow.", damager.getInventory().getItem(8).getAmount() + 1));
                                        } else {
                                            damager.getInventory().setItem(8, createDisplay(Material.ARROW, ChatColor.RESET + "" + ChatColor.RED + "An arrow", ChatColor.RESET + "" + ChatColor.GRAY + "For your bow.", 1));
                                        }

                                        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                                            damaged.sendTitle("" + ChatColor.RED + ChatColor.BOLD + "You were killed!", ChatColor.YELLOW + "Respawning in " + damagedUser.getSecondsTilRespawn() + "...", 0, 40, 0);
                                            damagedUser.setSecondsTilRespawn(damagedUser.getSecondsTilRespawn() - 1);
                                            damaged.setSpectatorTarget(damager);
                                        }, 0, 20);

                                        BukkitTask bukkitTask2 = Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                            if(damaged.getGameMode() == GameMode.SPECTATOR) {
                                                damaged.setSpectatorTarget(null);
                                            }

                                            damaged.setGameMode(GameMode.ADVENTURE);

                                            damaged.setHealth(20);
                                            damaged.setFoodLevel(20);
                                            damaged.setSaturation(10);

                                            damagedUser.getPlayer().getInventory().setContents(GameInventory.getInventory().getContents());

                                            Random generator = new Random();

                                            int randomIndex = generator.nextInt(game.getArenaInUse().getSpawnpoints().size());
                                            Location randomspawnpoint = (Location) game.getArenaInUse().getSpawnpoints().values().toArray()[randomIndex];

                                            damaged.teleport(randomspawnpoint);

                                            game.removeUserDead(damaged.getUniqueId());
                                            game.addUserAlive(damaged.getUniqueId(), damagedUser);

                                            damaged.sendTitle("" + ChatColor.GREEN + ChatColor.BOLD + "You have respawned!", ChatColor.YELLOW + "Go get 'em!", 0, 20, 10);

                                            bukkitTask.cancel();

                                            game.respawnTasks.remove(bukkitTask);
                                        }, 60);

                                        game.respawnTasks.put(bukkitTask, damagedUser);
                                        game.respawnTasks.put(bukkitTask2, damagedUser);

                                        if (game.getUserScore().get(damagedUser) != null) {
                                            if (game.getUserScore().get(damagedUser) > 0) {
                                                game.addUserScore(damagedUser, game.getUserScore().get(damagedUser) - 1);

                                                for (User user2 : game.getUsersPlaying().values()) {
                                                    user2.getCurrentScoreboardAPI().setScoreAtLine(damaged.getName(), game.getUserScore().get(damagedUser));
                                                }
                                            }
                                        }

                                        if (game.getUserScore().get(damagerUser) != null) {

                                            game.addUserScore(damagerUser, game.getUserScore().get(damagerUser) + 1);

                                            for (User user2 : game.getUsersPlaying().values()) {
                                                user2.getCurrentScoreboardAPI().setScoreAtLine(damager.getName(), game.getUserScore().get(damagerUser));
                                            }

                                            if (game.getUserScore().get(damagerUser) == GameSettings.maxValue) {

                                                // Here for me to actually understand.
                                                User winner = damagerUser;

                                                // Stop any respawn things.
                                                for(BukkitTask bukkitTask1 : game.respawnTasks.keySet()) {
                                                    bukkitTask1.cancel();
                                                }

                                                game.setFinished(true);
                                                game.setStarted(false);

                                                game.respawnTasks.clear();

                                                for (User u : game.getUsersPlaying().values()) {

                                                    if(u == winner) {
                                                        u.getPlayer().sendTitle("" + ChatColor.GOLD + ChatColor.BOLD + "You won!", ChatColor.YELLOW + "Congratulations!", 0, 100, 10);
                                                        u.setWins(u.getWins() + 1);
                                                    } else {
                                                        u.getPlayer().setGameMode(GameMode.SPECTATOR);
                                                        u.getPlayer().setSpectatorTarget(winner.getPlayer());
                                                        u.getPlayer().sendTitle("" + ChatColor.RED + ChatColor.BOLD + "You lost!", ChatColor.YELLOW + "Better luck next time..", 0, 100, 10);
                                                        u.setLoses(u.getLoses() + 1);
                                                    }
                                                }

                                                for(User u : game.getUsersPlaying().values()) {
                                                    u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game has ended! The winner is: " + ChatColor.AQUA + winner.getPlayer().getName());
                                                }

                                                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                    for (User u : game.getUsersPlaying().values()) {
                                                        u.getPlayer().sendMessage(ChatColor.GREEN + "Thank you for playing One In The Chamber!");

                                                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                                        out.writeUTF("Connect");
                                                        out.writeUTF("minigames");
                                                        u.getPlayer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                                                    }
                                                }, 100);

                                                game.getArenaInUse().setUsedBy(null);

                                                OneInTheChamber.ARENASNOTINUSE.put(game.getArenaInUse().getArenaName(), game.getArenaInUse());
                                                OneInTheChamber.ARENASINUSE.remove(game.getArenaInUse().getArenaName());

                                                game.getGameTask().cancel();
                                                OneInTheChamber.GAMES.remove(game.getArenaInUse());
                                            }
                                        } else {
                                            game.addUserScore(damagerUser, 1);

                                            for (User user : game.getUsersPlaying().values()) {
                                                user.getCurrentScoreboardAPI().setScoreAtLine(damager.getName(), game.getUserScore().get(damagerUser));
                                            }
                                        }
                                    }
                                } else {
                                    e.setDamage(0.0);
                                }
                            } else {
                                e.setCancelled(true);
                            }
                        } else {
                            e.setCancelled(true);
                        }
                    }
                } else {
                    e.setCancelled(true);
                }
            } else if(e.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) e.getDamager();
                Player damaged = (Player) e.getEntity();

                User damagedUser = OneInTheChamber.USERS.get(damaged.getUniqueId());

                if(damagedUser.getGameIn() != null) {

                    if(arrow.getShooter() instanceof Player) {
                        Player damager = (Player) arrow.getShooter();

                        if(damaged != damager) {

                            User damagerUser = OneInTheChamber.USERS.get(damager.getUniqueId());

                            if (damagerUser.getGameIn() != null) {
                                if (damagedUser.getGameIn() == damagerUser.getGameIn()) {

                                    Game game = damagerUser.getGameIn();

                                    damaged.getLocation().getWorld().strikeLightningEffect(damaged.getLocation());
                                    damagerUser.setKills(damagerUser.getKills() + 1);
                                    damagedUser.setDeaths(damagedUser.getDeaths() + 1);
                                    damager.playSound(damager.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5F, 2F);

                                    //damager.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "You killed " + ChatColor.AQUA + damaged.getName() + ChatColor.WHITE + "!");
                                    ///damaged.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "You were killed by " + ChatColor.AQUA + damager.getName() + ChatColor.WHITE + "!");

                                    for(User us : game.getUsersPlaying().values()) {
                                        us.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + damaged.getName() + ChatColor.WHITE + " was shot by " + ChatColor.AQUA + damager.getName() + ChatColor.WHITE + "!");
                                    }

                                    damaged.setHealth(20);
                                    damaged.setFoodLevel(20);
                                    damaged.setSaturation(10);
                                    damaged.getInventory().clear();

                                    game.removeUserAlive(damaged.getUniqueId());
                                    game.addUserDead(damaged.getUniqueId(), damagedUser);

                                    damagedUser.setLastHitBy(null);

                                    if(damagedUser.hitTasks.size() > 0) {
                                        for(BukkitTask bukkitTask : damagedUser.hitTasks.keySet()) {
                                            bukkitTask.cancel();
                                        }

                                        damagedUser.hitTasks.clear();
                                    }

                                    damaged.setGameMode(GameMode.SPECTATOR);
                                    damaged.setSpectatorTarget(damager);
                                    damagedUser.setSecondsTilRespawn(3);

                                    if (damager.getInventory().getItem(8) != null) {
                                        damager.getInventory().setItem(8, createDisplay(Material.ARROW, ChatColor.RESET + "" + ChatColor.RED + "An arrow", ChatColor.RESET + "" + ChatColor.GRAY + "For your bow.", damager.getInventory().getItem(8).getAmount() + 1));
                                    } else {
                                        damager.getInventory().setItem(8, createDisplay(Material.ARROW, ChatColor.RESET + "" + ChatColor.RED + "An arrow", ChatColor.RESET + "" + ChatColor.GRAY + "For your bow.", 1));
                                    }

                                    BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                                        damaged.sendTitle("" + ChatColor.RED + ChatColor.BOLD + "You were killed!", ChatColor.YELLOW + "Respawning in " + damagedUser.getSecondsTilRespawn() + "...", 0, 40, 0);
                                        damagedUser.setSecondsTilRespawn(damagedUser.getSecondsTilRespawn() - 1);
                                        damaged.setSpectatorTarget(damager);
                                    }, 0, 20);

                                    BukkitTask bukkitTask2 = Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                        if(damaged.getGameMode() == GameMode.SPECTATOR) {
                                            damaged.setSpectatorTarget(null);
                                        }

                                        damaged.setGameMode(GameMode.ADVENTURE);

                                        damaged.setHealth(20);
                                        damaged.setFoodLevel(20);
                                        damaged.setSaturation(10);

                                        damagedUser.getPlayer().getInventory().setContents(GameInventory.getInventory().getContents());

                                        Random generator = new Random();

                                        int randomIndex = generator.nextInt(game.getArenaInUse().getSpawnpoints().size());
                                        Location randomspawnpoint = (Location) game.getArenaInUse().getSpawnpoints().values().toArray()[randomIndex];

                                        damaged.teleport(randomspawnpoint);

                                        game.removeUserDead(damaged.getUniqueId());
                                        game.addUserAlive(damaged.getUniqueId(), damagedUser);

                                        damaged.sendTitle("" + ChatColor.GREEN + ChatColor.BOLD + "You have respawned!", ChatColor.YELLOW + "Go get 'em!", 0, 20, 10);

                                        bukkitTask.cancel();
                                        game.respawnTasks.remove(bukkitTask);
                                    }, 60);

                                    game.respawnTasks.put(bukkitTask, damagedUser);
                                    game.respawnTasks.put(bukkitTask2, damagedUser);

                                    if (game.getUserScore().get(damagerUser) != null) {
                                        game.addUserScore(damagerUser, game.getUserScore().get(damagerUser) + 1);

                                        for (User user : game.getUsersPlaying().values()) {
                                            user.getCurrentScoreboardAPI().setScoreAtLine(damager.getName(), game.getUserScore().get(damagerUser));
                                        }

                                        if (game.getUserScore().get(damagerUser) == GameSettings.maxValue) {

                                            // Here for me to actually understand.
                                            User winner = damagerUser;

                                            // Stop any respawn things.
                                            for(BukkitTask bukkitTask1 : game.respawnTasks.keySet()) {
                                                bukkitTask1.cancel();
                                            }

                                            game.setFinished(true);
                                            game.setStarted(false);

                                            game.respawnTasks.clear();

                                            for (User u : game.getUsersPlaying().values()) {

                                                if(u == winner) {
                                                    u.getPlayer().sendTitle("" + ChatColor.GOLD + ChatColor.BOLD + "You won!", ChatColor.YELLOW + "Congratulations!", 0, 100, 10);
                                                    u.setWins(u.getWins() + 1);
                                                } else {
                                                    u.getPlayer().setGameMode(GameMode.SPECTATOR);
                                                    u.getPlayer().setSpectatorTarget(winner.getPlayer());
                                                    u.getPlayer().sendTitle("" + ChatColor.RED + ChatColor.BOLD + "You lost!", ChatColor.YELLOW + "Better luck next time..", 0, 100, 10);
                                                    u.setLoses(u.getLoses() + 1);
                                                }
                                            }

                                            for(User u : game.getUsersPlaying().values()) {
                                                u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game has ended! The winner is: " + ChatColor.AQUA + winner.getPlayer().getName());
                                            }

                                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                for (User u : game.getUsersPlaying().values()) {
                                                    u.getPlayer().sendMessage(ChatColor.GREEN + "Thank you for playing One In The Chamber!");

                                                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                                    out.writeUTF("Connect");
                                                    out.writeUTF("minigames");
                                                    u.getPlayer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                                                }
                                            }, 100);

                                            game.getArenaInUse().setUsedBy(null);

                                            OneInTheChamber.ARENASNOTINUSE.put(game.getArenaInUse().getArenaName(), game.getArenaInUse());
                                            OneInTheChamber.ARENASINUSE.remove(game.getArenaInUse().getArenaName());

                                            game.getGameTask().cancel();
                                            OneInTheChamber.GAMES.remove(game.getArenaInUse());
                                        }
                                    } else {
                                        game.addUserScore(damagerUser, 1);

                                        for (User user : game.getUsersPlaying().values()) {
                                            user.getCurrentScoreboardAPI().setScoreAtLine(damager.getName(), game.getUserScore().get(damagerUser));
                                        }
                                    }
                                } else {
                                    e.setCancelled(true);
                                }
                            } else {
                                e.setCancelled(true);
                            }
                        } else {
                            e.setCancelled(true);
                        }
                    } else {
                        e.setCancelled(true);
                    }
                }
            }
        }

        if (e.getEntity() instanceof ItemFrame) {
            if (e.getDamager() instanceof Player) {
                Player pl = (Player) e.getDamager();
                User user = OneInTheChamber.USERS.get(pl.getUniqueId());
                if(user.getGameIn() != null) {
                    if (!(pl.getGameMode() == GameMode.CREATIVE)) {
                        e.setCancelled(true);
                    }
                }
            }

            if (e.getDamager() instanceof Projectile) {
                if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
                    Projectile p = (Projectile) e.getDamager();
                    Player pl = (Player) p.getShooter();
                    User user = OneInTheChamber.USERS.get(pl.getUniqueId());
                    if(user.getGameIn() != null) {
                        if (!(pl.getGameMode() == GameMode.CREATIVE)) {
                            e.getDamager().remove();
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }

        if (e.getEntity() instanceof ArmorStand) {
            if (e.getDamager() instanceof Player) {
                Player pl = (Player) e.getDamager();
                User user = OneInTheChamber.USERS.get(pl.getUniqueId());
                if(user.getGameIn() != null) {
                    if (!(pl.getGameMode() == GameMode.CREATIVE)) {
                        e.setCancelled(true);
                    }
                }
            }

            if (e.getDamager() instanceof Projectile) {
                if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
                    Projectile p = (Projectile) e.getDamager();
                    Player pl = (Player) p.getShooter();
                    User user = OneInTheChamber.USERS.get(pl.getUniqueId());
                    if(user.getGameIn() != null) {
                        if (!(pl.getGameMode() == GameMode.CREATIVE)) {
                            e.getDamager().remove();
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    private static ItemStack createDisplay(Material material, String name, String lore, int Amount) {
        ItemStack item = new ItemStack(material, Amount);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add(lore);
        meta.setLore(Lore);
        item.setItemMeta(meta);
        return item;
    }
}
