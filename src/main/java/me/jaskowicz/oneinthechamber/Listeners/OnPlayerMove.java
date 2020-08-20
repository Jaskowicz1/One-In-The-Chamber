package me.jaskowicz.oneinthechamber.Listeners;

import me.jaskowicz.oneinthechamber.Inventories.GameInventory;
import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Utils.Game;
import me.jaskowicz.oneinthechamber.Utils.User;
import me.jaskowicz.oneinthechamber.UtilsExtra.RandomUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class OnPlayerMove implements Listener {

    private Plugin plugin = OneInTheChamber.getPlugin(OneInTheChamber.class);

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        User user = OneInTheChamber.USERS.get(player.getUniqueId());

        if(user.getGameIn() != null) {
            Game game = user.getGameIn();

            if(game.isBlockingMovement() && game.getUsersPlaying().get(player.getUniqueId()) != null) {
                if(event.getTo() != null) {
                    if (event.getTo().getX() != event.getFrom().getX() || event.getTo().getY() != event.getFrom().getY() || event.getTo().getZ() != event.getFrom().getZ()) {
                        player.teleport(event.getFrom());
                        event.setCancelled(true);

                        player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You can not move yet!");
                    }
                }
            } else {
                if(game.getArenaInUse().getArenaName().equals("Osgiliath") || game.getArenaInUse().getArenaName().equals("PirateShip")) {
                    if(game.hasStarted()) {
                        if (event.getTo().getBlock().getType().equals(Material.WATER) && player.getGameMode().equals(GameMode.ADVENTURE)) {
                            player.getLocation().getWorld().strikeLightningEffect(player.getLocation());

                            user.setDeaths(user.getDeaths() + 1);

                            User lastHitBy = user.getLastHitBy();

                            if(lastHitBy == null) {
                                for (User us : game.getUsers().values()) {
                                    us.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + player.getName() + ChatColor.WHITE + " has died!");
                                }
                            } else {
                                if(game.getArenaInUse().getArenaName().equals("PirateShip")) {
                                    for (User us : game.getUsers().values()) {
                                        us.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + player.getName() + ChatColor.WHITE + " was thrown over-board by " + ChatColor.AQUA + lastHitBy.getPlayer().getName() + ChatColor.WHITE + "!");
                                    }
                                } else {
                                    for (User us : game.getUsers().values()) {
                                        us.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + player.getName() + ChatColor.WHITE + " was sentenced to death by " + ChatColor.AQUA + lastHitBy.getPlayer().getName() + ChatColor.WHITE + "!");
                                    }
                                }

                                lastHitBy.setKills(lastHitBy.getKills() + 1);

                                if (lastHitBy.getPlayer().getInventory().getItem(8) != null) {
                                    lastHitBy.getPlayer().getInventory().setItem(8, createDisplay(Material.ARROW, ChatColor.RESET + "" + ChatColor.RED + "An arrow", ChatColor.RESET + "" + ChatColor.GRAY + "For your bow.", lastHitBy.getPlayer().getInventory().getItem(8).getAmount() + 1));
                                } else {
                                    lastHitBy.getPlayer().getInventory().setItem(8, createDisplay(Material.ARROW, ChatColor.RESET + "" + ChatColor.RED + "An arrow", ChatColor.RESET + "" + ChatColor.GRAY + "For your bow.", 1));
                                }
                            }

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

                                    for (User user2 : game.getUsers().values()) {
                                        user2.getCurrentScoreboardAPI().setScoreAtLine(player.getName(), game.getUserScore().get(user));
                                    }
                                }
                            }
                        }
                    } else {
                        if (event.getTo().getBlock().getType().equals(Material.WATER) && player.getGameMode().equals(GameMode.ADVENTURE)) {
                            player.getLocation().getWorld().strikeLightningEffect(player.getLocation());

                            Random generator = new Random();

                            int randomIndex = generator.nextInt(game.getArenaInUse().getSpawnpoints().size());
                            Location randomspawnpoint = (Location) game.getArenaInUse().getSpawnpoints().values().toArray()[randomIndex];

                            player.teleport(randomspawnpoint);
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
