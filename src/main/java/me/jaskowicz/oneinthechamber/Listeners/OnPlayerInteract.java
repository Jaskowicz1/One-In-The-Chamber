package me.jaskowicz.oneinthechamber.Listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.jaskowicz.oneinthechamber.Inventories.StatsInventory;
import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Settings.GameSettings;
import me.jaskowicz.oneinthechamber.Tasks.StartingGameTask;
import me.jaskowicz.oneinthechamber.Utils.Game;
import me.jaskowicz.oneinthechamber.Utils.User;
import me.jaskowicz.oneinthechamber.UtilsExtra.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class OnPlayerInteract implements Listener {

    private Plugin plugin = OneInTheChamber.getPlugin(OneInTheChamber.class);

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        User user = OneInTheChamber.USERS.get(player.getUniqueId());

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if(itemStack.getItemMeta() == null) {
            return;
        }

        if(event.getHand() == null) {
            return;
        }

        if(!event.getHand().equals(EquipmentSlot.HAND)) {
            if(user.getGameIn() != null) {
                event.setCancelled(true);
            }
            return;
        }

        if(user.getGameIn() != null) {
            if(event.getAction() == Action.RIGHT_CLICK_AIR && itemStack.getItemMeta().getDisplayName().contains("Force Start") || event.getAction() == Action.RIGHT_CLICK_BLOCK && itemStack.getItemMeta().getDisplayName().contains("Force Start")) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);

                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 2F);

                    Game game = user.getGameIn();

                    if (!game.hasStarted() && !game.isFinished()) {

                        if(game.getUsersPlaying().size() >= GameSettings.MIN_PLAYERS) {
                            player.sendMessage(OneInTheChamber.prefix + ChatColor.GREEN + "Force starting game!");

                            for (User u : game.getUsersPlaying().values()) {
                                u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "It appears that the game has been set to force start!");
                            }

                            if(game.getStartingGameTask() != null) {

                                StartingGameTask startingGameTask = game.getStartingGameTask();
                                startingGameTask.timetilstart = 5;
                            } else {
                                for (User u : game.getUsersPlaying().values()) {
                                    u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "That's odd, this game can't be force started!");
                                }
                            }
                        } else {
                            player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "There are not enough players to start the game!");
                        }
                    } else {
                        player.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "You can not force start this game!");
                    }
                } else {
                    player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You are not in a game!");
                }
            } else if(event.getAction() == Action.RIGHT_CLICK_AIR && itemStack.getItemMeta().getDisplayName().contains("Leave Game") || event.getAction() == Action.RIGHT_CLICK_BLOCK && itemStack.getItemMeta().getDisplayName().contains("Leave Game")) {
                if(user.getGameIn() != null) {

                    event.setCancelled(true);

                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 5.0F, 0.6F);

                    if(!user.getGameIn().hasStarted()) {
                        user.getGameIn().removeUserPlaying(player.getUniqueId());

                        Game game = user.getGameIn();

                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF("minigames");
                        user.getPlayer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

                        if(game.getUsersPlaying().size() != 0) {
                            for (User u : game.getUsersPlaying().values()) {
                                u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + player.getName() + ChatColor.WHITE + " has left " + ChatColor.AQUA + "(" + game.getUsersPlaying().size() + "/" + GameSettings.MAX_PLAYERS + ")" + ChatColor.WHITE + "!");
                                u.getCurrentScoreboardAPI().changeLine("playersin", "" + ChatColor.AQUA + game.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA + GameSettings.MAX_PLAYERS);
                            }

                            if (game.getUsersPlaying().size() < GameSettings.MIN_PLAYERS) {
                                if(game.getStartingGameTask() != null) {
                                    game.getStartingGameTask().cancel();
                                    game.setStartingGameTask(null);

                                    for(User u : game.getUsersPlaying().values()) {
                                        u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.RED + "There are not enough players to start the game!");
                                        u.getCurrentScoreboardAPI().changeLine("timeleft", "" + ChatColor.AQUA + (GameSettings.MIN_PLAYERS - game.getUsersPlaying().size()) + " players needed to start!");
                                    }
                                }
                            }
                        } else {
                            OneInTheChamber.GAMES.remove(game.getArenaInUse());

                            OneInTheChamber.ARENASNOTINUSE.put(game.getArenaInUse().getArenaName(), game.getArenaInUse());
                            OneInTheChamber.ARENASINUSE.remove(game.getArenaInUse().getArenaName());

                            game.getArenaInUse().setUsedBy(null);
                        }
                    } else {
                        player.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game does not support you leaving mid-match yet!");
                    }
                } else {
                    player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You're not currently in a game!");
                }
            } else if(event.getAction() == Action.RIGHT_CLICK_AIR && itemStack.getItemMeta().getDisplayName().contains("Cosmetics") || event.getAction() == Action.RIGHT_CLICK_BLOCK && itemStack.getItemMeta().getDisplayName().contains("Cosmetics")) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);

                    player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 5.0F, 1F);

                    if(!user.getGameIn().hasStarted()) {
                        player.sendMessage(OneInTheChamber.prefix + ChatColor.YELLOW + "Coming soon...");
                    } else {
                        player.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "This is not allowed!");
                    }
                } else {
                    player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You're not currently in a game!");
                }
            } else if(event.getAction() == Action.RIGHT_CLICK_AIR && itemStack.getItemMeta().getDisplayName().contains("Statistics") || event.getAction() == Action.RIGHT_CLICK_BLOCK && itemStack.getItemMeta().getDisplayName().contains("Statistics")) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);

                    player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 5.0F, 1F);

                    if(!user.getGameIn().hasStarted()) {
                        player.openInventory(StatsInventory.getInventory(user));
                    } else {
                        player.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "This is not allowed!");
                    }
                } else {
                    player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You're not currently in a game!");
                }
            }
        }
    }
}
