package me.jaskowicz.oneinthechamber.Listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.jaskowicz.oneinthechamber.Inventories.LobbyInventory;
import me.jaskowicz.oneinthechamber.Settings.GameSettings;
import me.jaskowicz.oneinthechamber.Tasks.StartingGameTask;
import me.jaskowicz.oneinthechamber.Utils.Arena;
import me.jaskowicz.oneinthechamber.Utils.Game;
import me.jaskowicz.oneinthechamber.UtilsExtra.DatabaseUtils;
import me.jaskowicz.oneinthechamber.UtilsExtra.PlayerUtil;
import me.jaskowicz.oneinthechamber.UtilsExtra.ScoreboardAPI;
import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Utils.User;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.Random;

public class PlayerConnections implements Listener {

    private Plugin plugin = OneInTheChamber.getPlugin(OneInTheChamber.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        e.setJoinMessage("");

        Player pl = e.getPlayer();

        OneInTheChamber.USERS.put(pl.getUniqueId(), new User(pl));

        User u = OneInTheChamber.USERS.get(pl.getUniqueId());

        System.out.println(GameSettings.alterMode);

        if(!GameSettings.alterMode) {

            if (DatabaseUtils.tableContainsPlayerUUID(pl.getUniqueId().toString(), "STATS")) {
                try {
                    Statement statement = OneInTheChamber.connection.createStatement();
                    ResultSet result = statement.executeQuery("SELECT * FROM `STATS` WHERE `playerUUID`='" + pl.getUniqueId().toString() + "';");

                    if(result.next()) {

                        //System.out.println("test");

                        u.setKills(result.getInt("Kills"));
                        u.setDeaths(result.getInt("Deaths"));
                        u.setWins(result.getInt("Wins"));
                        u.setLoses(result.getInt("Loses"));
                        u.setDraws(result.getInt("Draws"));
                    }

                    result.close();
                    statement.close();

                } catch (SQLException exc) {
                    exc.printStackTrace();
                }
            }

            u.setCurrentScoreboardAPI(new ScoreboardAPI(pl));

            // For Bungeecord

            if (OneInTheChamber.ARENAS.size() == 0) {
                pl.kickPlayer(OneInTheChamber.prefix + ChatColor.RED + "No arenas have been created!");
                return;
            }

            if (OneInTheChamber.ARENASNOTINUSE.size() == 0) {
                pl.kickPlayer(OneInTheChamber.prefix + ChatColor.RED + "No arenas are available at this moment!");
                return;
            }

            if (u.getGameIn() != null) {
                pl.kickPlayer(OneInTheChamber.prefix + ChatColor.RED + "You are already in a game!");
                return;
            }

            if(GameSettings.forceStaffSpectator) {
                if (OneInTheChamber.GAMES.size() == 0) {
                    pl.kickPlayer(OneInTheChamber.prefix + ChatColor.RED + "There are no games for you to spectate!");
                    return;
                }

                for (Game randomgame : OneInTheChamber.GAMES.values()) {
                    if (randomgame.getUsersPlaying().size() != GameSettings.MAX_PLAYERS) {

                        if (randomgame.hasStarted()) {
                            continue;
                        }

                        Random generator = new Random();

                        randomgame.addUserSpectating(pl.getUniqueId(), u);
                        randomgame.addUser(pl.getUniqueId(), u);

                        u.setGameIn(randomgame);
                        u.getPlayer().setHealth(20);
                        u.getPlayer().setSaturation(20);
                        u.getPlayer().setFoodLevel(20);
                        PlayerUtil.storeAndClearItems(u);
                        //u.getPlayer().getInventory().setContents(LobbyInventory.getInventory(u.getPlayer()).getContents());
                        u.setLastGameMode(pl.getGameMode());
                        pl.setGameMode(GameMode.SPECTATOR);
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


                        for (User user : randomgame.getUsersPlaying().values()) {
                            user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has joined but is spectating " + ChatColor.AQUA + "(" + randomgame.getUsersPlaying().size() + "/" + GameSettings.MAX_PLAYERS + ")" + ChatColor.WHITE + "!");
                            user.getCurrentScoreboardAPI().changeLine("playersin", "" + ChatColor.AQUA + randomgame.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA + GameSettings.MAX_PLAYERS);
                        }

                        if (randomgame.getUsersPlaying().size() >= GameSettings.MIN_PLAYERS) {
                            if (randomgame.getStartingGameTask() == null) {
                                StartingGameTask startingGameTask = new StartingGameTask(randomgame, 600);
                                randomgame.setStartingGameTask(startingGameTask);
                                startingGameTask.runTaskTimer(plugin, 0, 20);
                            }
                        }

                        //u.getPlayer().getInventory().setContents(LobbyInventory.getInventory(u.getPlayer()).getContents());

                        return;
                    }
                }
            }

            if (OneInTheChamber.GAMES.size() != 0) {
                for (Game randomgame : OneInTheChamber.GAMES.values()) {
                    if (randomgame.getUsersPlaying().size() != GameSettings.MAX_PLAYERS) {

                        if (randomgame.hasStarted()) {
                            continue;
                        }

                        Random generator = new Random();

                        randomgame.addUserPlaying(pl.getUniqueId(), u);
                        randomgame.addUser(pl.getUniqueId(), u);

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


                        for (User user : randomgame.getUsersPlaying().values()) {
                            user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has joined " + ChatColor.AQUA + "(" + randomgame.getUsersPlaying().size() + "/" + GameSettings.MAX_PLAYERS + ")" + ChatColor.WHITE + "!");
                            user.getCurrentScoreboardAPI().changeLine("playersin", "" + ChatColor.AQUA + randomgame.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA + GameSettings.MAX_PLAYERS);
                        }

                        if (randomgame.getUsersPlaying().size() >= GameSettings.MIN_PLAYERS) {
                            if (randomgame.getStartingGameTask() == null) {
                                StartingGameTask startingGameTask = new StartingGameTask(randomgame, 600);
                                randomgame.setStartingGameTask(startingGameTask);
                                startingGameTask.runTaskTimer(plugin, 0, 20);
                            }
                        }

                        u.getPlayer().getInventory().setContents(LobbyInventory.getInventory(u.getPlayer()).getContents());

                        return;
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
                game.addUser(pl.getUniqueId(), u);

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
                u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.GREEN, "playersin", "" + ChatColor.AQUA + game.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA + GameSettings.MAX_PLAYERS, 7);
                u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.RED, "blankLine3", "" + ChatColor.STRIKETHROUGH, 6);

                u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.GRAY + "" + ChatColor.WHITE, "timelefttext", "" + ChatColor.WHITE + "Time until start:", 5);
                u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.GRAY + "" + ChatColor.GREEN, "timeleft", "" + ChatColor.AQUA + (GameSettings.MIN_PLAYERS - game.getUsersPlaying().size()) + " players needed to start!", 4);
                u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.RED, "blankLine2", "" + ChatColor.STRIKETHROUGH, 3);

                u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.RED + "" + ChatColor.AQUA, "mapusedtext", "" + ChatColor.WHITE + "Map:", 2);
                u.getCurrentScoreboardAPI().addLineAtScore(ChatColor.BLACK + "" + ChatColor.WHITE, "mapused", "" + ChatColor.AQUA + game.getArenaInUse().getArenaName(), 1);

                for (User user : game.getUsersPlaying().values()) {
                    user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has joined " + ChatColor.AQUA + "(" + game.getUsersPlaying().size() + "/" + GameSettings.MAX_PLAYERS + ")" + ChatColor.WHITE + "!");
                    user.getCurrentScoreboardAPI().changeLine("playersin", "" + ChatColor.AQUA + game.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA + GameSettings.MAX_PLAYERS);
                }

                u.getPlayer().getInventory().setContents(LobbyInventory.getInventory(u.getPlayer()).getContents());

            } else {
                Random generator = new Random();

                Arena arena = null;

                while (arena == null) {
                    int randomIndex = generator.nextInt(OneInTheChamber.ARENASNOTINUSE.size());
                    Arena randomarena = (Arena) OneInTheChamber.ARENASNOTINUSE.values().toArray()[randomIndex];

                    if (randomarena.getUsedBy() == null) {
                        arena = randomarena;
                    }
                }

                Game game = new Game(arena);
                arena.setUsedBy(game);

                OneInTheChamber.ARENASINUSE.put(arena.getArenaName(), arena);
                OneInTheChamber.ARENASNOTINUSE.remove(arena.getArenaName());

                game.addUserPlaying(pl.getUniqueId(), u);
                game.addUser(pl.getUniqueId(), u);

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


                for (User user : game.getUsersPlaying().values()) {
                    user.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has joined " + ChatColor.AQUA + "(" + game.getUsersPlaying().size() + "/" + GameSettings.MAX_PLAYERS + ")" + ChatColor.WHITE + "!");
                    user.getCurrentScoreboardAPI().changeLine("playersin", "" + ChatColor.AQUA + game.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA + GameSettings.MAX_PLAYERS);
                }

                u.getPlayer().getInventory().setContents(LobbyInventory.getInventory(u.getPlayer()).getContents());
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {

        e.setQuitMessage("");

        Player pl = e.getPlayer();
        User user = OneInTheChamber.USERS.get(pl.getUniqueId());

        if(user.getGameIn() != null) {

            Game game = user.getGameIn();

            if(user.getGameIn().hasStarted()) {

                if(user.getGameIn().getUsersPlaying().get(user.getPlayer().getUniqueId()) != null) {
                    user.setLoses(user.getLoses() + 1);
                    for(User user2 : game.getUsers().values()) {
                        user2.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has left the match!");
                        user2.getCurrentScoreboardAPI().resetScore(pl.getName());
                    }
                } else {
                    for(User user2 : game.getUsers().values()) {
                        user2.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has stopped spectating!");
                    }
                }

                pl.teleport(user.getLocationBeforeJoin());
                PlayerUtil.restoreItemsAndClearSaved(user);
                pl.setGameMode(user.getLastGameMode());
                user.getGameIn().removeUserPlaying(pl.getUniqueId());
                user.getGameIn().removeUserAlive(pl.getUniqueId());
                user.getGameIn().removeUserDead(pl.getUniqueId());
                user.getGameIn().removeUserSpectating(pl.getUniqueId());

                user.setGameIn(null);

                pl.updateInventory();


                if(game.getUsersPlaying().size() == 1) {

                    // Stop any respawn things.
                    for (BukkitTask bukkitTask1 : game.respawnTasks.keySet()) {
                        bukkitTask1.cancel();
                    }

                    game.respawnTasks.clear();


                    game.setFinished(true);
                    game.setStarted(false);

                    for (User u : game.getUsersPlaying().values()) {
                        u.getPlayer().sendTitle("" + ChatColor.GOLD + ChatColor.BOLD + "You win?", ChatColor.YELLOW + "Everyone fled the scene.", 0, 100, 10);
                        u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game has ended and... you won?");
                        u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 5f, 0.5F);
                        u.setWins(u.getWins() + 1);

                    }

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {

                        for (User u : game.getUsers().values()) {
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

                    game.getStartingGameTask().cancel();
                } else if (game.getUsersPlaying().size() == 0) {
                    OneInTheChamber.GAMES.remove(game.getArenaInUse());

                    OneInTheChamber.ARENASNOTINUSE.put(game.getArenaInUse().getArenaName(), game.getArenaInUse());
                    OneInTheChamber.ARENASINUSE.remove(game.getArenaInUse().getArenaName());

                    game.getArenaInUse().setUsedBy(null);
                }
            } else if (user.getGameIn().isFinished()){
                pl.teleport(user.getLocationBeforeJoin());
                PlayerUtil.restoreItemsAndClearSaved(user);
                pl.setGameMode(user.getLastGameMode());
                user.getGameIn().removeUserPlaying(pl.getUniqueId());
                user.getGameIn().removeUserAlive(pl.getUniqueId());
                user.getGameIn().removeUserDead(pl.getUniqueId());
                user.getGameIn().removeUserSpectating(pl.getUniqueId());

                user.setGameIn(null);

                pl.updateInventory();
            } else {
                pl.teleport(user.getLocationBeforeJoin());
                PlayerUtil.restoreItemsAndClearSaved(user);
                pl.setGameMode(user.getLastGameMode());
                user.getGameIn().removeUserPlaying(pl.getUniqueId());
                user.getGameIn().removeUserSpectating(pl.getUniqueId());

                user.setGameIn(null);

                pl.updateInventory();

                if(game.getUsersPlaying().size() != 0) {
                    for (User user1 : game.getUsers().values()) {
                        user1.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has left " + ChatColor.AQUA + "(" + game.getUsersPlaying().size() + "/" + GameSettings.MAX_PLAYERS + ")" + ChatColor.WHITE + "!");
                        user1.getCurrentScoreboardAPI().changeLine("playersin", "" + ChatColor.AQUA + game.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA + GameSettings.MAX_PLAYERS);
                    }

                    if (game.getUsersPlaying().size() < GameSettings.MIN_PLAYERS) {
                        if(game.getStartingGameTask() != null) {
                            game.getStartingGameTask().cancel();
                            game.setStartingGameTask(null);

                            for(User user1 : game.getUsers().values()) {
                                user1.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.RED + "There are not enough players to start the game!");
                                user1.getCurrentScoreboardAPI().changeLine("timeleft", "" + ChatColor.AQUA + (GameSettings.MIN_PLAYERS - game.getUsersPlaying().size()) + " players needed to start!");
                            }
                        }
                    }
                } else {
                    OneInTheChamber.GAMES.remove(game.getArenaInUse());

                    OneInTheChamber.ARENASNOTINUSE.put(game.getArenaInUse().getArenaName(), game.getArenaInUse());
                    OneInTheChamber.ARENASINUSE.remove(game.getArenaInUse().getArenaName());

                    game.getArenaInUse().setUsedBy(null);
                }
            }
        }

        if(!GameSettings.alterMode) {

            if (DatabaseUtils.tableContainsPlayerUUID(pl.getUniqueId().toString(), "STATS")) {
                try {
                    String databaseQuery2 = "UPDATE `STATS` SET Kills=?, Deaths=?, Wins=?, Loses=?, Draws=? WHERE `playerUUID`='" + pl.getUniqueId().toString() + "';";

                    PreparedStatement statement2 = OneInTheChamber.connection.prepareStatement(databaseQuery2);

                    statement2.setInt(1, user.getKills());
                    statement2.setInt(2, user.getDeaths());
                    statement2.setInt(3, user.getWins());
                    statement2.setInt(4, user.getLoses());
                    statement2.setInt(5, user.getDraws());

                    statement2.execute();
                    statement2.close();

                } catch (SQLException exc) {
                    exc.printStackTrace();
                }
            } else {
                try {
                    String databaseQuery2 = "INSERT INTO `STATS` (`playerName`, `playerUUID`, `Kills`, `Deaths`, " +
                            "`Wins`, `Loses`, `Draws`) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?);";

                    PreparedStatement statement2 = OneInTheChamber.connection.prepareStatement(databaseQuery2);

                    statement2.setString(1, pl.getName());
                    statement2.setString(2, pl.getUniqueId().toString());
                    statement2.setInt(3, user.getKills());
                    statement2.setInt(4, user.getDeaths());
                    statement2.setInt(5, user.getWins());
                    statement2.setInt(6, user.getLoses());
                    statement2.setInt(7, user.getDraws());

                    statement2.execute();
                    statement2.close();

                } catch (SQLException exc) {
                    exc.printStackTrace();
                }
            }
        }

        OneInTheChamber.USERS.remove(pl.getUniqueId());
    }

    @EventHandler
    public void onPlayerKicked(PlayerKickEvent e) {

        e.setLeaveMessage("");

        Player pl = e.getPlayer();
        User user = OneInTheChamber.USERS.get(pl.getUniqueId());

        // Win/Lose stats not effected by kick.

        if(!GameSettings.alterMode) {

            if (DatabaseUtils.tableContainsPlayerUUID(pl.getUniqueId().toString(), "STATS")) {
                try {
                    String databaseQuery2 = "UPDATE `STATS` SET Kills=?, Deaths=?, Wins=?, Loses=?, Draws=? WHERE `playerUUID`='" + pl.getUniqueId().toString() + "';";

                    PreparedStatement statement2 = OneInTheChamber.connection.prepareStatement(databaseQuery2);

                    statement2.setInt(1, user.getKills());
                    statement2.setInt(2, user.getDeaths());
                    statement2.setInt(3, user.getWins());
                    statement2.setInt(4, user.getLoses());
                    statement2.setInt(5, user.getDraws());

                    statement2.execute();
                    statement2.close();

                } catch (SQLException exc) {
                    exc.printStackTrace();
                }
            } else {
                try {
                    String databaseQuery2 = "INSERT INTO `STATS` (`playerName`, `playerUUID`, `Kills`, `Deaths`, " +
                            "`Wins`, `Loses`, `Draws`) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?);";

                    PreparedStatement statement2 = OneInTheChamber.connection.prepareStatement(databaseQuery2);

                    statement2.setString(1, pl.getName());
                    statement2.setString(2, pl.getUniqueId().toString());
                    statement2.setInt(3, user.getKills());
                    statement2.setInt(4, user.getDeaths());
                    statement2.setInt(5, user.getWins());
                    statement2.setInt(6, user.getLoses());
                    statement2.setInt(7, user.getDraws());

                    statement2.execute();
                    statement2.close();

                } catch (SQLException exc) {
                    exc.printStackTrace();
                }
            }
        }

        if(user.getGameIn() != null) {

            Game game = user.getGameIn();

            if(user.getGameIn().hasStarted()) {

                if(user.getGameIn().getUsersPlaying().get(user.getPlayer().getUniqueId()) != null) {
                    user.setLoses(user.getLoses() + 1);
                    for(User user2 : game.getUsers().values()) {
                        user2.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has left the match!");
                        user2.getCurrentScoreboardAPI().resetScore(pl.getName());
                    }
                } else {
                    for(User user2 : game.getUsers().values()) {
                        user2.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has stopped spectating!");
                    }
                }

                pl.teleport(user.getLocationBeforeJoin());
                PlayerUtil.restoreItemsAndClearSaved(user);
                pl.setGameMode(user.getLastGameMode());
                user.getGameIn().removeUserPlaying(pl.getUniqueId());
                user.getGameIn().removeUserAlive(pl.getUniqueId());
                user.getGameIn().removeUserDead(pl.getUniqueId());
                user.getGameIn().removeUserSpectating(pl.getUniqueId());

                user.setGameIn(null);

                pl.updateInventory();

                if(game.getUsersPlaying().size() == 1) {

                    // Stop any respawn things.
                    for (BukkitTask bukkitTask1 : game.respawnTasks.keySet()) {
                        bukkitTask1.cancel();
                    }

                    game.respawnTasks.clear();


                    game.setFinished(true);
                    game.setStarted(false);

                    for (User u : game.getUsersPlaying().values()) {
                        u.getPlayer().sendTitle("" + ChatColor.GOLD + ChatColor.BOLD + "You win?", ChatColor.YELLOW + "Everyone fled the scene.", 0, 100, 10);
                        u.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.WHITE + "The game has ended and... you won?");
                        u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 5f, 0.5F);
                        u.setWins(u.getWins() + 1);
                    }

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {

                        for (User u : game.getUsers().values()) {
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

                    game.getStartingGameTask().cancel();
                } else if (game.getUsersPlaying().size() == 0) {

                    for (User u : game.getUsersSpectating().values()) {
                        u.getPlayer().sendMessage(ChatColor.GREEN + "Thank you for playing One In The Chamber!");

                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF("minigames");
                        u.getPlayer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                    }

                    OneInTheChamber.GAMES.remove(game.getArenaInUse());

                    OneInTheChamber.ARENASNOTINUSE.put(game.getArenaInUse().getArenaName(), game.getArenaInUse());
                    OneInTheChamber.ARENASINUSE.remove(game.getArenaInUse().getArenaName());

                    game.getArenaInUse().setUsedBy(null);
                }
            } else if (user.getGameIn().isFinished()){
                pl.teleport(user.getLocationBeforeJoin());
                PlayerUtil.restoreItemsAndClearSaved(user);
                pl.setGameMode(user.getLastGameMode());
                user.getGameIn().removeUserPlaying(pl.getUniqueId());
                user.getGameIn().removeUserAlive(pl.getUniqueId());
                user.getGameIn().removeUserDead(pl.getUniqueId());
                user.getGameIn().removeUserSpectating(pl.getUniqueId());

                user.setGameIn(null);

                pl.updateInventory();
            } else {
                pl.teleport(user.getLocationBeforeJoin());
                PlayerUtil.restoreItemsAndClearSaved(user);
                pl.setGameMode(user.getLastGameMode());
                user.getGameIn().removeUserPlaying(pl.getUniqueId());
                user.getGameIn().removeUserSpectating(pl.getUniqueId());

                user.setGameIn(null);

                pl.updateInventory();

                if(game.getUsers().size() != 0) {
                    for (User user1 : game.getUsersPlaying().values()) {
                        user1.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.AQUA + pl.getName() + ChatColor.WHITE + " has left " + ChatColor.AQUA + "(" + game.getUsersPlaying().size() + "/" + GameSettings.MAX_PLAYERS + ")" + ChatColor.WHITE + "!");
                        user1.getCurrentScoreboardAPI().changeLine("playersin", "" + ChatColor.AQUA + game.getUsersPlaying().size() + ChatColor.GRAY + "/" + ChatColor.AQUA + GameSettings.MAX_PLAYERS);
                    }

                    if (game.getUsersPlaying().size() < GameSettings.MIN_PLAYERS) {
                        if(game.getStartingGameTask() != null) {
                            game.getStartingGameTask().cancel();
                            game.setStartingGameTask(null);

                            for(User user1 : game.getUsers().values()) {
                                user1.getPlayer().sendMessage(OneInTheChamber.prefix + ChatColor.RED + "There are not enough players to start the game!");
                                user1.getCurrentScoreboardAPI().changeLine("timeleft", "" + ChatColor.AQUA + (GameSettings.MIN_PLAYERS - game.getUsersPlaying().size()) + " players needed to start!");
                            }
                        }
                    }
                } else {

                    for (User u : game.getUsersSpectating().values()) {
                        u.getPlayer().sendMessage(ChatColor.GREEN + "Thank you for playing One In The Chamber!");

                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF("minigames");
                        u.getPlayer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                    }

                    OneInTheChamber.GAMES.remove(game.getArenaInUse());

                    OneInTheChamber.ARENASNOTINUSE.put(game.getArenaInUse().getArenaName(), game.getArenaInUse());
                    OneInTheChamber.ARENASINUSE.remove(game.getArenaInUse().getArenaName());

                    game.getArenaInUse().setUsedBy(null);
                }
            }
        }

        OneInTheChamber.USERS.remove(pl.getUniqueId());
    }
}