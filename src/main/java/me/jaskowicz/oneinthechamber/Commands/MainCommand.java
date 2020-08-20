package me.jaskowicz.oneinthechamber.Commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.jaskowicz.oneinthechamber.Inventories.GameInventory;
import me.jaskowicz.oneinthechamber.Inventories.LobbyInventory;
import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Settings.GameSettings;
import me.jaskowicz.oneinthechamber.Tasks.GameTask;
import me.jaskowicz.oneinthechamber.Tasks.StartingGameTask;
import me.jaskowicz.oneinthechamber.Utils.Arena;
import me.jaskowicz.oneinthechamber.Utils.Game;
import me.jaskowicz.oneinthechamber.Utils.User;
import me.jaskowicz.oneinthechamber.UtilsExtra.PlayerUtil;
import me.jaskowicz.oneinthechamber.UtilsExtra.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainCommand implements CommandExecutor {

    private Plugin plugin = OneInTheChamber.getPlugin(OneInTheChamber.class);

    private String getHelpMenu() {
        return ChatColor.WHITE + "/oitc help " + ChatColor.GRAY + "|" + ChatColor.WHITE + "Shows this menu." + "\n"
                + ChatColor.WHITE + "/oitc join " + ChatColor.GRAY + "|" + ChatColor.WHITE + " Joins a random game." + "\n"
                + ChatColor.WHITE + "/oitc leave " + ChatColor.GRAY + "|" + ChatColor.WHITE + " Leave the game you are currently in." + "\n"
                + ChatColor.WHITE + "/oitc create <arenaname> " + ChatColor.GRAY + "|" + ChatColor.WHITE + " Create an arena." + "\n"
                + ChatColor.WHITE + "/oitc delete <arenaname> " + ChatColor.GRAY + "|" + ChatColor.WHITE + " Delete an arena." + "\n"
                + ChatColor.WHITE + "/oitc enable <arenaname> " + ChatColor.GRAY + "|" + ChatColor.WHITE + " Enable an arena." + "\n"
                + ChatColor.WHITE + "/oitc disable <arenaname> " + ChatColor.GRAY + "|" + ChatColor.WHITE + " Disable an arena." + "\n"
                + ChatColor.WHITE + "/oitc setspawnpos <arenaname> " + ChatColor.GRAY + "|" + ChatColor.WHITE + " Set a spawn position for an arena." + "\n"
                + ChatColor.WHITE + "/oitc arenas " + ChatColor.GRAY + "|" + ChatColor.WHITE + " Shows a list of arenas." + "\n"
                + ChatColor.WHITE + "/oitc games " + ChatColor.GRAY + "|" + ChatColor.WHITE + " Shows a list of current games." + "\n"
                + ChatColor.WHITE + "/oitc forcestart " + ChatColor.GRAY + "|" + ChatColor.WHITE + " Force the game you are in to start." + "\n"
                + ChatColor.WHITE + "/oitc forceend " + ChatColor.GRAY + "|" + ChatColor.WHITE + " Force the game you are in to stop." + "\n"
                + ChatColor.WHITE + "/oitc forceallend " + ChatColor.GRAY + "|" + ChatColor.WHITE + " Force all games to stop." + "\n";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player pl;
        User u;

        if (!(sender instanceof Player)) {
            sender.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You can not do these commands as console.");
            return true;
        } else {
            pl = (Player) sender;
            u = OneInTheChamber.USERS.get(pl.getUniqueId());
        }

        if (command.getName().equalsIgnoreCase("OneInTheChamber")) {
            if (pl.hasPermission("oneinthechamber.manage")) {
                if (args.length == 0) {
                    pl.sendMessage(getHelpMenu());
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("help")) {
                        pl.sendMessage(getHelpMenu());
                    } else if (args[0].equalsIgnoreCase("arenas")) {
                        if (OneInTheChamber.ARENAS.size() != 0) {
                            StringBuilder message = new StringBuilder(OneInTheChamber.prefix + ChatColor.WHITE + "Arenas: ");
                            for (int i = 0; i < OneInTheChamber.ARENAS.values().size(); i++) {
                                String enabledtxt = ChatColor.RED + " (Disabled)";
                                Arena arena = (Arena) OneInTheChamber.ARENAS.values().toArray()[i];

                                if (arena.isEnabled()) {
                                    enabledtxt = ChatColor.GREEN + " (Enabled)";
                                }

                                if (i != (OneInTheChamber.ARENAS.values().size() - 1)) {
                                    message.append(ChatColor.AQUA).append(arena.getArenaName()).append(enabledtxt).append(ChatColor.WHITE).append(", ");
                                } else {
                                    message.append(ChatColor.AQUA).append(arena.getArenaName()).append(enabledtxt).append(ChatColor.WHITE).append(".");
                                }
                            }

                            pl.sendMessage(message.toString());
                        } else {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "No arenas were found!");
                        }
                    } else if (args[0].equalsIgnoreCase("games")) {
                        if (OneInTheChamber.GAMES.size() != 0) {
                            StringBuilder message = new StringBuilder(OneInTheChamber.prefix + ChatColor.WHITE + "Games: ");
                            for (int i = 0; i < OneInTheChamber.GAMES.values().size(); i++) {
                                Game game = (Game) OneInTheChamber.GAMES.values().toArray()[i];
                                String gameinfo = ChatColor.YELLOW + " (Starting)";

                                if(game.hasStarted()) {
                                    gameinfo = ChatColor.GREEN + " (Started)";
                                } else if(game.isFinished()) {
                                    gameinfo = ChatColor.RED + " (Finished)";
                                }

                                if (i != (OneInTheChamber.GAMES.values().size() - 1)) {
                                    message.append(ChatColor.AQUA).append(game).append(gameinfo).append(ChatColor.WHITE).append(", ");
                                } else {
                                    message.append(ChatColor.AQUA).append(game).append(gameinfo).append(ChatColor.WHITE).append(".");
                                }
                            }

                            pl.sendMessage(message.toString());
                        } else {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "No games were found!");
                        }
                    } else if (args[0].equalsIgnoreCase("forcestart")) {

                        if(u.getGameIn() != null) {
                            Game game = u.getGameIn();

                            if (!game.hasStarted() && !game.isFinished()) {

                                if(game.getUsersPlaying().size() >= GameSettings.MIN_PLAYERS) {
                                    pl.sendMessage(OneInTheChamber.prefix + ChatColor.GREEN + "Force starting game!");

                                    for (User user : game.getUsersPlaying().values()) {
                                        user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "It appears that the game has been set to force start!");
                                    }

                                    if(game.getStartingGameTask() != null) {

                                        StartingGameTask startingGameTask = game.getStartingGameTask();
                                        startingGameTask.timetilstart = 5;
                                    } else {
                                        for (User user : game.getUsersPlaying().values()) {
                                            user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "That's odd, this game can't be force started!");
                                        }
                                    }
                                } else {
                                    pl.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "There are not enough players to start the game!");
                                }
                            } else {
                                pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "You can not force start this game!");
                            }
                        } else {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You are not in a game!");
                        }
                    } else if (args[0].equalsIgnoreCase("forceend")) {

                        if(u.getGameIn() != null) {
                            Game game = u.getGameIn();

                            if (!game.isFinished() && game.hasStarted()) {
                                pl.sendMessage(OneInTheChamber.prefix + ChatColor.GREEN + "Forcing game to stop!");

                                for (User user : game.getUsersPlaying().values()) {
                                    user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "It appears that the game has been set to force end!");
                                }

                                // Stop any respawn things.
                                for (BukkitTask bukkitTask1 : game.respawnTasks.keySet()) {
                                    bukkitTask1.cancel();
                                }

                                game.respawnTasks.clear();


                                game.setFinished(true);
                                game.setStarted(false);

                                for (User user : game.getUsersPlaying().values()) {
                                    user.getPlayer().sendTitle("" + ChatColor.GRAY + ChatColor.BOLD + "Draw!", ChatColor.YELLOW + "The game was force ended..", 0, 100, 10);
                                    user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game has ended! Nobody won as the game was force ended.");
                                    user.setDraws(user.getDraws() + 1);
                                }

                                Bukkit.getScheduler().runTaskLater(plugin, () -> {

                                    for (User user : game.getUsersPlaying().values()) {
                                        user.getPlayer().sendMessage(ChatColor.GREEN + "Thank you for playing One In The Chamber!");

                                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                        out.writeUTF("Connect");
                                        out.writeUTF("minigames");
                                        u.getPlayer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                                    }

                                    game.getArenaInUse().setUsedBy(null);

                                    OneInTheChamber.ARENASNOTINUSE.put(game.getArenaInUse().getArenaName(), game.getArenaInUse());
                                    OneInTheChamber.ARENASINUSE.remove(game.getArenaInUse().getArenaName());

                                    game.getGameTask().cancel();
                                    OneInTheChamber.GAMES.remove(game.getArenaInUse());
                                }, 100);

                            } else {
                                pl.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You can not force stop this game!");
                            }
                        } else {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You are not in a game!");
                        }
                    } else if (args[0].equalsIgnoreCase("forceallend")) {

                        pl.sendMessage(OneInTheChamber.prefix + ChatColor.GREEN + "Forcing all games to stop!");

                        for (Game game : OneInTheChamber.GAMES.values()) {
                            if (!game.isFinished()) {
                                for (User user : game.getUsersPlaying().values()) {
                                    user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "It appears that your game has been set to force stop!");
                                }

                                // Stop any respawn things.
                                for(BukkitTask bukkitTask1 : game.respawnTasks.keySet()) {
                                    bukkitTask1.cancel();
                                }

                                game.respawnTasks.clear();


                                game.setFinished(true);
                                game.setStarted(false);

                                for (User user : game.getUsersPlaying().values()) {
                                    user.getPlayer().sendTitle("" + ChatColor.GRAY + ChatColor.BOLD + "Draw!", ChatColor.YELLOW + "The game was force ended..", 0, 100, 10);
                                    user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game has ended! Nobody won as the game was force ended.");
                                    u.setDraws(u.getDraws() + 1);
                                }

                                Bukkit.getScheduler().runTaskLater(plugin, () -> {

                                    for (User user : game.getUsersPlaying().values()) {
                                        user.getPlayer().sendMessage(ChatColor.GREEN + "Thank you for playing One In The Chamber!");

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

                                game.getGameTask().cancel();
                            } else {
                                for (User user : game.getUsersPlaying().values()) {
                                    user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.GREEN + "Someone attempted to stop your game however it seems like the game is already meant to stop.");
                                }
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("join")) {

                        if(OneInTheChamber.ARENAS.size() == 0) {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "No arenas have been created!");
                            return true;
                        }

                        if(OneInTheChamber.ARENASNOTINUSE.size() == 0) {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "No arenas are available at this moment!");
                            return true;
                        }

                        if(u.getGameIn() != null) {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You are already in a game!");
                            return true;
                        }

                        if(OneInTheChamber.GAMES.size() != 0) {
                            for(Game randomgame : OneInTheChamber.GAMES.values()) {
                                if (randomgame.getUsersPlaying().size() != GameSettings.MAX_PLAYERS) {

                                    if(randomgame.hasStarted()) {
                                        continue;
                                    }

                                    Random generator = new Random();

                                    randomgame.addUserPlaying(pl.getUniqueId(), u);

                                    u.setGameIn(randomgame);
                                    u.getPlayer().setHealth(20);
                                    u.getPlayer().setSaturation(20);
                                    u.getPlayer().setFoodLevel(20);
                                    PlayerUtil.storeAndClearItems(u);
                                    u.getPlayer().getInventory().setContents(LobbyInventory.getInventory(u.getPlayer()).getContents());
                                    u.setLastGameMode(pl.getGameMode());
                                    pl.setGameMode(GameMode.ADVENTURE);
                                    u.setLocationBeforeJoin(u.getPlayer().getLocation());

                                    int randomIndex = generator.nextInt(randomgame.getArenaInUse().getSpawnpoints().size());
                                    Location randomspawnpoint = (Location) randomgame.getArenaInUse().getSpawnpoints().values().toArray()[randomIndex];

                                    u.getPlayer().teleport(randomspawnpoint);

                                    u.getCurrentScoreboardAPI().clearLines();
                                    u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.WHITE, "playersintext", "" + ChatColor.WHITE + "Players:", 8);
                                    u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.GREEN, "playersin", "" + ChatColor.AQUA + randomgame.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA + GameSettings.MAX_PLAYERS, 7);
                                    u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.RED, "blankLine3", "" + ChatColor.STRIKETHROUGH, 6);

                                    u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.GRAY + "" + ChatColor.WHITE, "timelefttext", "" + ChatColor.WHITE + "Time until start:", 5);
                                    u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.GRAY + "" + ChatColor.GREEN, "timeleft", "" + ChatColor.AQUA + (GameSettings.MIN_PLAYERS - randomgame.getUsersPlaying().size()) + " players needed to start!", 4);
                                    u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.RED, "blankLine2", "" + ChatColor.STRIKETHROUGH, 3);

                                    u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.AQUA, "mapusedtext", "" + ChatColor.WHITE + "Map:", 2);
                                    u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.WHITE, "mapused", "" + ChatColor.AQUA + randomgame.getArenaInUse().getArenaName(), 1);


                                    for(User user : randomgame.getUsersPlaying().values()) {
                                        user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has joined " + ChatColor.AQUA + "(" + randomgame.getUsersPlaying().size() + "/" + GameSettings.MAX_PLAYERS + ")" + ChatColor.WHITE + "!");
                                        user.getCurrentScoreboardAPI().changeLine("playersin", "" + ChatColor.AQUA + randomgame.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA + GameSettings.MAX_PLAYERS);
                                    }

                                    if(randomgame.getUsersPlaying().size() >= GameSettings.MIN_PLAYERS) {
                                        if(randomgame.getStartingGameTask() == null) {
                                            StartingGameTask startingGameTask = new StartingGameTask(randomgame, 600);
                                            randomgame.setStartingGameTask(startingGameTask);
                                            startingGameTask.runTaskTimer(plugin, 0, 20);
                                        }
                                    }

                                    return true;
                                }
                            }

                            // This code here will only execute if the for loop ended, meaning no games are available, thus creating a new game.

                            Arena arena = null;
                            Random generator = new Random();

                            while (arena == null) {
                                int randomIndex2 = generator.nextInt(OneInTheChamber.ARENASNOTINUSE.size());
                                Arena randomarena = (Arena) OneInTheChamber.ARENASNOTINUSE.values().toArray()[randomIndex2];

                                if (randomarena.getUsedBy() == null) {
                                    arena = randomarena;
                                }
                            }

                            Game game = new Game(arena);
                            arena.setUsedBy(game);

                            OneInTheChamber.ARENASINUSE.put(arena.getArenaName(), arena);
                            OneInTheChamber.ARENASNOTINUSE.remove(arena.getArenaName());

                            game.addUserPlaying(pl.getUniqueId(), u);

                            u.setGameIn(game);
                            u.getPlayer().setHealth(20);
                            u.getPlayer().setSaturation(20);
                            u.getPlayer().setFoodLevel(20);
                            PlayerUtil.storeAndClearItems(u);
                            u.getPlayer().getInventory().setContents(LobbyInventory.getInventory(u.getPlayer()).getContents());
                            u.setLastGameMode(pl.getGameMode());
                            pl.setGameMode(GameMode.ADVENTURE);
                            u.setLocationBeforeJoin(u.getPlayer().getLocation());

                            int randomIndex = generator.nextInt(arena.getSpawnpoints().size());
                            Location randomspawnpoint = (Location) arena.getSpawnpoints().values().toArray()[randomIndex];

                            u.getPlayer().teleport(randomspawnpoint);
                            pl.getInventory().clear();

                            OneInTheChamber.GAMES.put(arena, game);

                            u.getCurrentScoreboardAPI().clearLines();
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.WHITE, "playersintext", "" + ChatColor.WHITE + "Players:", 8);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.GREEN, "playersin", "" + ChatColor.AQUA + game.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA +  GameSettings.MAX_PLAYERS, 7);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.RED, "blankLine3", "" + ChatColor.STRIKETHROUGH, 6);

                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.GRAY + "" + ChatColor.WHITE, "timelefttext", "" + ChatColor.WHITE + "Time until start:", 5);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.GRAY + "" + ChatColor.GREEN, "timeleft", "" + ChatColor.AQUA + (GameSettings.MIN_PLAYERS - game.getUsersPlaying().size()) + " players needed to start!", 4);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.RED, "blankLine2", "" + ChatColor.STRIKETHROUGH, 3);

                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.AQUA, "mapusedtext", "" + ChatColor.WHITE + "Map:", 2);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.WHITE, "mapused", "" + ChatColor.AQUA + game.getArenaInUse().getArenaName(), 1);

                            for(User user : game.getUsersPlaying().values()) {
                                user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has joined " + ChatColor.AQUA + "(" + game.getUsersPlaying().size() + "/" + GameSettings.MAX_PLAYERS + ")" + ChatColor.WHITE + "!");
                            }

                            return true;
                        } else {
                            Random generator = new Random();

                            Arena arena = null;

                            while(arena == null) {
                                int randomIndex = generator.nextInt(OneInTheChamber.ARENASNOTINUSE.size());
                                Arena randomarena = (Arena) OneInTheChamber.ARENASNOTINUSE.values().toArray()[randomIndex];

                                if(randomarena.getUsedBy() == null) {
                                    arena = randomarena;
                                }
                            }

                            Game game = new Game(arena);
                            arena.setUsedBy(game);

                            OneInTheChamber.ARENASINUSE.put(arena.getArenaName(), arena);
                            OneInTheChamber.ARENASNOTINUSE.remove(arena.getArenaName());

                            game.addUserPlaying(pl.getUniqueId(), u);

                            u.setGameIn(game);
                            u.getPlayer().setHealth(20);
                            u.getPlayer().setSaturation(20);
                            u.getPlayer().setFoodLevel(20);
                            PlayerUtil.storeAndClearItems(u);
                            u.getPlayer().getInventory().setContents(LobbyInventory.getInventory(u.getPlayer()).getContents());
                            u.setLastGameMode(pl.getGameMode());
                            pl.setGameMode(GameMode.ADVENTURE);
                            u.setLocationBeforeJoin(u.getPlayer().getLocation());

                            int randomIndex = generator.nextInt(arena.getSpawnpoints().size());

                            Location randomspawnpoint = (Location) arena.getSpawnpoints().values().toArray()[randomIndex];

                            u.getPlayer().teleport(randomspawnpoint);

                            OneInTheChamber.GAMES.put(arena, game);

                            u.getCurrentScoreboardAPI().clearLines();
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.WHITE, "playersintext", "" + ChatColor.WHITE + "Players:", 8);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.GREEN, "playersin", "" + ChatColor.AQUA + game.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA +  GameSettings.MAX_PLAYERS, 7);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.RED, "blankLine3", "" + ChatColor.STRIKETHROUGH, 6);

                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.GRAY + "" + ChatColor.WHITE, "timelefttext", "" + ChatColor.WHITE + "Time until start:", 5);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.GRAY + "" + ChatColor.GREEN, "timeleft", "" + ChatColor.AQUA + (GameSettings.MIN_PLAYERS - game.getUsersPlaying().size()) + " players needed to start!", 4);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.RED, "blankLine2", "" + ChatColor.STRIKETHROUGH, 3);

                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.AQUA, "mapusedtext", "" + ChatColor.WHITE + "Map:", 2);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.WHITE, "mapused", "" + ChatColor.AQUA + game.getArenaInUse().getArenaName(), 1);


                            for(User user : game.getUsersPlaying().values()) {
                                user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has joined " + ChatColor.AQUA + "(" + game.getUsersPlaying().size() + "/" + GameSettings.MAX_PLAYERS + ")" + ChatColor.WHITE + "!");
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("leave")) {
                        if(u.getGameIn() != null) {
                            if(!u.getGameIn().hasStarted()) {
                                u.getGameIn().removeUserPlaying(pl.getUniqueId());

                                Game game = u.getGameIn();

                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("Connect");
                                out.writeUTF("minigames");
                                u.getPlayer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

                                if(game.getUsersPlaying().size() != 0) {
                                    for (User user : game.getUsersPlaying().values()) {
                                        user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has left " + ChatColor.AQUA + "(" + game.getUsersPlaying().size() + "/" + GameSettings.MAX_PLAYERS + ")" + ChatColor.WHITE + "!");
                                        user.getCurrentScoreboardAPI().changeLine("playersin", "" + ChatColor.AQUA + game.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA + GameSettings.MAX_PLAYERS);
                                    }

                                    if (game.getUsersPlaying().size() < GameSettings.MIN_PLAYERS) {
                                        if(game.getStartingGameTask() != null) {
                                            game.getStartingGameTask().cancel();
                                            game.setStartingGameTask(null);

                                            for(User user : game.getUsersPlaying().values()) {
                                                user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.RED + "There are not enough players to start the game!");
                                                user.getCurrentScoreboardAPI().changeLine("timeleft", "" + ChatColor.AQUA + (GameSettings.MIN_PLAYERS - game.getUsersPlaying().size()) + " players needed to start!");
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
                                pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game does not support you leaving mid-match yet!");
                            }
                        } else {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You're not currently in a game!");
                        }
                    } else {
                        pl.sendMessage(getHelpMenu());
                    }
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("create")) {
                        String arenaName = args[1];

                        File arenaFile = new File(plugin.getDataFolder() + File.separator + "arenas", arenaName + ".yml");

                        if (arenaFile.exists()) {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "There is already an arena with this name!");
                        } else {
                            FileConfiguration arenaData = YamlConfiguration.loadConfiguration(arenaFile);

                            arenaData.set("arena.name", arenaName);
                            arenaData.set("arena.enabled", false);

                            Arena a = new Arena(arenaName);

                            OneInTheChamber.ARENAS.put(arenaName, a);

                            try {
                                arenaData.save(arenaFile);

                                pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "Successfully created the arena: " + ChatColor.AQUA + arenaName);
                            } catch (IOException e1) {
                                plugin.getLogger().severe("Could not save the arena: " + arenaName + "'s data file!");
                                e1.printStackTrace();
                                pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "Un-Successfully enabled the arena: " + ChatColor.AQUA + arenaName);
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("delete")) {
                        String arenaName = args[1];

                        File arenaFile = new File(plugin.getDataFolder() + File.separator + "arenas", arenaName + ".yml");

                        if (arenaFile.exists()) {

                            OneInTheChamber.ARENAS.remove(arenaName);

                            arenaFile.delete();

                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "Successfully deleted the arena: " + ChatColor.AQUA + arenaName);
                        } else {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "There was no arena found with this name!");
                        }
                    } else if (args[0].equalsIgnoreCase("enable")) {
                        String arenaName = args[1];

                        File arenaFile = new File(plugin.getDataFolder() + File.separator + "arenas", arenaName + ".yml");

                        if (arenaFile.exists()) {
                            FileConfiguration arenaData = YamlConfiguration.loadConfiguration(arenaFile);

                            arenaData.set("arena.enabled", true);

                            Arena arena = OneInTheChamber.ARENAS.get(arenaName);

                            OneInTheChamber.ARENASNOTINUSE.put(arena.getArenaName(), arena);

                            arena.setEnabled(true);

                            try {
                                arenaData.save(arenaFile);

                                pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "Successfully enabled the arena: " + ChatColor.AQUA + arenaName);
                            } catch (IOException e1) {
                                plugin.getLogger().severe("Could not save the arena: " + arenaName + "'s data file!");
                                e1.printStackTrace();

                                pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "Un-Successfully enabled the arena: " + ChatColor.AQUA + arenaName);
                            }
                        } else {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "This arena doesn't exist!");
                        }
                    } else if (args[0].equalsIgnoreCase("disable")) {
                        String arenaName = args[1];

                        File arenaFile = new File(plugin.getDataFolder() + File.separator + "arenas", arenaName + ".yml");

                        if (arenaFile.exists()) {
                            FileConfiguration arenaData = YamlConfiguration.loadConfiguration(arenaFile);

                            arenaData.set("arena.enabled", false);

                            Arena arena = OneInTheChamber.ARENAS.get(arenaName);

                            OneInTheChamber.ARENASNOTINUSE.remove(arena.getArenaName());
                            OneInTheChamber.ARENASINUSE.remove(arena.getArenaName());

                            arena.setEnabled(false);

                            try {
                                arenaData.save(arenaFile);

                                pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "Successfully disabled the arena: " + ChatColor.AQUA + arenaName);
                            } catch (IOException e1) {
                                plugin.getLogger().severe("Could not save the arena: " + arenaName + "'s data file!");
                                e1.printStackTrace();

                                pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "Un-Successfully disabled the arena: " + ChatColor.AQUA + arenaName);
                            }
                        } else {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "This arena doesn't exist!");
                        }
                    } else if (args[0].equalsIgnoreCase("setspawnpos")) {
                        String arenaName = args[1];

                        File arenaFile = new File(plugin.getDataFolder() + File.separator + "arenas", arenaName + ".yml");

                        if (arenaFile.exists()) {
                            FileConfiguration arenaData = YamlConfiguration.loadConfiguration(arenaFile);

                            if(arenaData.getList("arena.spawnLocs") != null) {

                                List list = arenaData.getList("arena.spawnLocs");
                                list.add(pl.getLocation());
                                arenaData.set("arena.spawnLocs", list);
                            } else {
                                List list = new ArrayList();
                                list.add(pl.getLocation());
                                arenaData.set("arena.spawnLocs", list);
                            }

                            Arena arena = OneInTheChamber.ARENAS.get(arenaName);

                            arena.addSpawnpoint(pl.getLocation());

                            try {
                                arenaData.save(arenaFile);

                                pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "Successfully set a spawnpoint for the arena: " + ChatColor.AQUA + arenaName);
                            } catch (IOException e1) {
                                plugin.getLogger().severe("Could not save the arena: " + arenaName + "'s data file!");
                                e1.printStackTrace();

                                pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "Un-Successfully set a spawnpoint for the arena: " + ChatColor.AQUA + arenaName);
                            }
                        } else {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "This arena doesn't exist!");
                        }
                    } else {
                        pl.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "Invalid syntax.");
                    }
                } else {
                    pl.sendMessage(getHelpMenu());
                }
            } else {
                if(args.length == 0) {
                    pl.sendMessage(getHelpMenu());
                } else if(args.length == 1) {
                    if (args[0].equalsIgnoreCase("join")) {

                        if(OneInTheChamber.ARENAS.size() == 0) {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "No arenas have been created!");
                            return true;
                        }

                        if(OneInTheChamber.ARENASNOTINUSE.size() == 0) {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "No arenas are available at this moment!");
                            return true;
                        }

                        if(u.getGameIn() != null) {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You are already in a game!");
                            return true;
                        }

                        if(OneInTheChamber.GAMES.size() != 0) {
                            for(Game randomgame : OneInTheChamber.GAMES.values()) {
                                if (randomgame.getUsersPlaying().size() != GameSettings.MAX_PLAYERS) {

                                    if(randomgame.hasStarted()) {
                                        continue;
                                    }

                                    Random generator = new Random();

                                    randomgame.addUserPlaying(pl.getUniqueId(), u);

                                    u.setGameIn(randomgame);
                                    u.getPlayer().setHealth(20);
                                    u.getPlayer().setSaturation(20);
                                    u.getPlayer().setFoodLevel(20);
                                    PlayerUtil.storeAndClearItems(u);
                                    u.getPlayer().getInventory().setContents(LobbyInventory.getInventory(u.getPlayer()).getContents());
                                    u.setLastGameMode(pl.getGameMode());
                                    pl.setGameMode(GameMode.ADVENTURE);
                                    u.setLocationBeforeJoin(u.getPlayer().getLocation());

                                    int randomIndex = generator.nextInt(randomgame.getArenaInUse().getSpawnpoints().size());
                                    Location randomspawnpoint = (Location) randomgame.getArenaInUse().getSpawnpoints().values().toArray()[randomIndex];

                                    u.getPlayer().teleport(randomspawnpoint);

                                    u.getCurrentScoreboardAPI().clearLines();
                                    u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.WHITE, "playersintext", "" + ChatColor.WHITE + "Players:", 8);
                                    u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.GREEN, "playersin", "" + ChatColor.AQUA + randomgame.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA + GameSettings.MAX_PLAYERS, 7);
                                    u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.RED, "blankLine3", "" + ChatColor.STRIKETHROUGH, 6);

                                    u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.GRAY + "" + ChatColor.WHITE, "timelefttext", "" + ChatColor.WHITE + "Time until start:", 5);
                                    u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.GRAY + "" + ChatColor.GREEN, "timeleft", "" + ChatColor.AQUA + (GameSettings.MIN_PLAYERS - randomgame.getUsersPlaying().size()) + " players needed to start!", 4);
                                    u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.RED, "blankLine2", "" + ChatColor.STRIKETHROUGH, 3);

                                    u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.AQUA, "mapusedtext", "" + ChatColor.WHITE + "Map:", 2);
                                    u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.WHITE, "mapused", "" + ChatColor.AQUA + randomgame.getArenaInUse().getArenaName(), 1);


                                    for(User user : randomgame.getUsersPlaying().values()) {
                                        user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has joined " + ChatColor.AQUA + "(" + randomgame.getUsersPlaying().size() + "/" + GameSettings.MAX_PLAYERS + ")" + ChatColor.WHITE + "!");
                                        user.getCurrentScoreboardAPI().changeLine("playersin", "" + ChatColor.AQUA + randomgame.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA + GameSettings.MAX_PLAYERS);
                                    }

                                    if(randomgame.getUsersPlaying().size() >= GameSettings.MIN_PLAYERS) {
                                        if(randomgame.getStartingGameTask() == null) {
                                            StartingGameTask startingGameTask = new StartingGameTask(randomgame, 600);
                                            randomgame.setStartingGameTask(startingGameTask);
                                            startingGameTask.runTaskTimer(plugin, 0, 20);
                                        }
                                    }

                                    return true;
                                }
                            }

                            // This code here will only execute if the for loop ended, meaning no games are available, thus creating a new game.

                            Arena arena = null;
                            Random generator = new Random();

                            while (arena == null) {
                                int randomIndex2 = generator.nextInt(OneInTheChamber.ARENASNOTINUSE.size());
                                Arena randomarena = (Arena) OneInTheChamber.ARENASNOTINUSE.values().toArray()[randomIndex2];

                                if (randomarena.getUsedBy() == null) {
                                    arena = randomarena;
                                }
                            }

                            Game game = new Game(arena);
                            arena.setUsedBy(game);

                            OneInTheChamber.ARENASINUSE.put(arena.getArenaName(), arena);
                            OneInTheChamber.ARENASNOTINUSE.remove(arena.getArenaName());

                            game.addUserPlaying(pl.getUniqueId(), u);

                            u.setGameIn(game);
                            u.getPlayer().setHealth(20);
                            u.getPlayer().setSaturation(20);
                            u.getPlayer().setFoodLevel(20);
                            PlayerUtil.storeAndClearItems(u);
                            u.getPlayer().getInventory().setContents(LobbyInventory.getInventory(u.getPlayer()).getContents());
                            u.setLastGameMode(pl.getGameMode());
                            pl.setGameMode(GameMode.ADVENTURE);
                            u.setLocationBeforeJoin(u.getPlayer().getLocation());

                            int randomIndex = generator.nextInt(arena.getSpawnpoints().size());
                            Location randomspawnpoint = (Location) arena.getSpawnpoints().values().toArray()[randomIndex];

                            u.getPlayer().teleport(randomspawnpoint);

                            OneInTheChamber.GAMES.put(arena, game);

                            u.getCurrentScoreboardAPI().clearLines();
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.WHITE, "playersintext", "" + ChatColor.WHITE + "Players:", 8);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.GREEN, "playersin", "" + ChatColor.AQUA + game.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA +  GameSettings.MAX_PLAYERS, 7);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.RED, "blankLine3", "" + ChatColor.STRIKETHROUGH, 6);

                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.GRAY + "" + ChatColor.WHITE, "timelefttext", "" + ChatColor.WHITE + "Time until start:", 5);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.GRAY + "" + ChatColor.GREEN, "timeleft", "" + ChatColor.AQUA + (GameSettings.MIN_PLAYERS - game.getUsersPlaying().size()) + " players needed to start!", 4);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.RED, "blankLine2", "" + ChatColor.STRIKETHROUGH, 3);

                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.AQUA, "mapusedtext", "" + ChatColor.WHITE + "Map:", 2);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.WHITE, "mapused", "" + ChatColor.AQUA + game.getArenaInUse().getArenaName(), 1);

                            for(User user : game.getUsersPlaying().values()) {
                                user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has joined " + ChatColor.AQUA + "(" + game.getUsersPlaying().size() + "/" + GameSettings.MAX_PLAYERS + ")" + ChatColor.WHITE + "!");
                            }

                            return true;
                        } else {
                            Random generator = new Random();

                            Arena arena = null;

                            while(arena == null) {
                                int randomIndex = generator.nextInt(OneInTheChamber.ARENASNOTINUSE.size());
                                Arena randomarena = (Arena) OneInTheChamber.ARENASNOTINUSE.values().toArray()[randomIndex];

                                if(randomarena.getUsedBy() == null) {
                                    arena = randomarena;
                                }
                            }

                            Game game = new Game(arena);
                            arena.setUsedBy(game);

                            OneInTheChamber.ARENASINUSE.put(arena.getArenaName(), arena);
                            OneInTheChamber.ARENASNOTINUSE.remove(arena.getArenaName());

                            game.addUserPlaying(pl.getUniqueId(), u);

                            u.setGameIn(game);
                            u.getPlayer().setHealth(20);
                            u.getPlayer().setSaturation(20);
                            u.getPlayer().setFoodLevel(20);
                            PlayerUtil.storeAndClearItems(u);
                            u.getPlayer().getInventory().setContents(LobbyInventory.getInventory(u.getPlayer()).getContents());
                            u.setLastGameMode(pl.getGameMode());
                            pl.setGameMode(GameMode.ADVENTURE);
                            u.setLocationBeforeJoin(u.getPlayer().getLocation());

                            int randomIndex = generator.nextInt(arena.getSpawnpoints().size());
                            Location randomspawnpoint = (Location) arena.getSpawnpoints().values().toArray()[randomIndex];

                            u.getPlayer().teleport(randomspawnpoint);

                            OneInTheChamber.GAMES.put(arena, game);

                            u.getCurrentScoreboardAPI().clearLines();
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.WHITE, "playersintext", "" + ChatColor.WHITE + "Players:", 8);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.GREEN, "playersin", "" + ChatColor.AQUA + game.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA + GameSettings.MAX_PLAYERS, 7);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.RED, "blankLine3", "" + ChatColor.STRIKETHROUGH, 6);

                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.GRAY + "" + ChatColor.WHITE, "timelefttext", "" + ChatColor.WHITE + "Time until start:", 5);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.GRAY + "" + ChatColor.GREEN, "timeleft", "" + ChatColor.AQUA + (GameSettings.MIN_PLAYERS - game.getUsersPlaying().size()) + " players needed to start!", 4);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.RED, "blankLine2", "" + ChatColor.STRIKETHROUGH, 3);

                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.AQUA, "mapusedtext", "" + ChatColor.WHITE + "Map:", 2);
                            u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.WHITE, "mapused", "" + ChatColor.AQUA + game.getArenaInUse().getArenaName(), 1);

                            for(User user : game.getUsersPlaying().values()) {
                                user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has joined " + ChatColor.AQUA + "(" + game.getUsersPlaying().size() + "/" + GameSettings.MAX_PLAYERS + ")" + ChatColor.WHITE + "!");
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("leave"))  {
                        if(u.getGameIn() != null) {
                            if(!u.getGameIn().hasStarted()) {
                                u.getGameIn().removeUserPlaying(pl.getUniqueId());

                                Game game = u.getGameIn();

                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("Connect");
                                out.writeUTF("minigames");
                                u.getPlayer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

                                if(game.getUsersPlaying().size() != 0) {
                                    for (User user : game.getUsersPlaying().values()) {
                                        user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has left " + ChatColor.AQUA + "(" + game.getUsersPlaying().size() + "/" + GameSettings.MAX_PLAYERS + ")" + ChatColor.WHITE + "!");
                                        user.getCurrentScoreboardAPI().changeLine("playersin", "" + ChatColor.AQUA + game.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA + GameSettings.MAX_PLAYERS);
                                    }

                                    if (game.getUsersPlaying().size() < GameSettings.MIN_PLAYERS) {
                                        if(game.getStartingGameTask() != null) {
                                            game.getStartingGameTask().cancel();
                                            game.setStartingGameTask(null);

                                            for(User user : game.getUsersPlaying().values()) {
                                                user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.RED + "There are not enough players to start the game!");
                                                user.getCurrentScoreboardAPI().changeLine("timeleft", "" + ChatColor.AQUA + (GameSettings.MIN_PLAYERS - game.getUsersPlaying().size()) + " players needed to start!");
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
                                pl.sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game does not support you leaving mid-match yet!");
                            }
                        } else {
                            pl.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You're not currently in a game!");
                        }
                    } else {
                        pl.sendMessage(getHelpMenu());
                    }
                } else {
                    pl.sendMessage(getHelpMenu());
                }
            }
        }

        return true;
    }
}
