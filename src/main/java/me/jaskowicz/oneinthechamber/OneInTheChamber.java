package me.jaskowicz.oneinthechamber;

import me.jaskowicz.oneinthechamber.Commands.MainCommand;
import me.jaskowicz.oneinthechamber.Listeners.*;
import me.jaskowicz.oneinthechamber.Tasks.DatabaseAliveTask;
import me.jaskowicz.oneinthechamber.Utils.Arena;
import me.jaskowicz.oneinthechamber.Utils.Game;
import me.jaskowicz.oneinthechamber.Utils.User;
import me.jaskowicz.oneinthechamber.UtilsExtra.DatabaseUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public final class OneInTheChamber extends JavaPlugin {

    public static HashMap<UUID, User> USERS = new HashMap<>();
    public static HashMap<String, Arena> ARENAS = new HashMap<>();
    public static HashMap<String, Arena> ARENASINUSE = new HashMap<>();
    public static HashMap<String, Arena> ARENASNOTINUSE = new HashMap<>();
    public static HashMap<Arena, Game> GAMES = new HashMap<>();
    //public static HashMap<Game, GameTask> GAMETASKS = new HashMap<>();

    public static String prefix = ChatColor.GREEN + "[OneInTheChamber]" + ChatColor.GRAY + " » " + ChatColor.RESET;
    public static String noPerms = ChatColor.RED + "You lack the permissions to run this command.";

    public static Connection connection;

    @Override
    public void onEnable() {
        if (!this.getDataFolder().exists()) {
            getLogger().info("Error: No config.yml found! Creating...");
            this.getDataFolder().mkdirs();
            this.saveDefaultConfig();
            this.saveConfig();
            getLogger().info("config.yml was created successfully!");
        }

        File userfolder = new File(getDataFolder(), File.separator + "players");
        if (!userfolder.exists()) {
            getLogger().info("Error: No players folder found! Creating...");
            userfolder.mkdirs();
            getLogger().info("The players folder was successfully created!");
        }

        File arenafolder = new File(getDataFolder(), File.separator + "arenas");
        if (!arenafolder.exists()) {
            getLogger().info("Error: No arenas folder found! Creating...");
            arenafolder.mkdirs();
            getLogger().info("The arenas folder was successfully created!");
        }

        for (File files : Objects.requireNonNull(arenafolder.listFiles())) {
            File filetoload = new File(getDataFolder() + File.separator + "arenas", files.getName());

            FileConfiguration arenaData = YamlConfiguration.loadConfiguration(filetoload);

            String ArenaName = arenaData.getString("arena.name");

            Arena a = new Arena(ArenaName);

            if (arenaData.getList("arena.spawnLocs") != null) {
                List<Location> spawnLocations = (List<Location>) arenaData.getList("arena.spawnLocs");
                assert spawnLocations != null;
                for(Location spawnLoc : spawnLocations) {
                    a.addSpawnpoint(spawnLoc);
                    a.addSpawnpointNotUsed(spawnLoc);
                }
            }

            if (arenaData.getBoolean("arena.enabled")) {
                a.setEnabled(true);
            }

            ARENAS.put(ArenaName, a);
            ARENASNOTINUSE.put(ArenaName, a);
        }

        getLogger().info(ARENAS.size() + " Arenas were found and added.");


        DatabaseUtils databaseUtils = new DatabaseUtils();

        try {
            getLogger().info("Loading SQL...");
            databaseUtils.openConnection();
            getLogger().info("SQL has loaded successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().log(Level.SEVERE, "SQL failed to load.");
        }

        Bukkit.getServer().getPluginManager().registerEvents(new BlockListeners(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnArrowPickup(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnBowShoot(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnEntityDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnEntityInteract(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnEntityPickup(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnFoodChange(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnGameModeChange(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnInventoryClick(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnItemDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnItemDrop(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnPlayerBlockInteract(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnPlayerInteract(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnPlayerMove(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnProjectileHit(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnTeleport(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerConnections(), this);

        this.getCommand("oneinthechamber").setExecutor(new MainCommand());

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        DatabaseAliveTask databaseAliveTask = new DatabaseAliveTask();

        databaseAliveTask.runTaskTimer(this, 0, 20 * 15);

        getLogger().info("OneInTheChamber has been enabled!");
        getLogger().info("Made & Developed by: Archie Jaskowicz.");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        for(Player player : Bukkit.getOnlinePlayers()) {
            User user = USERS.get(player.getUniqueId());

            if(DatabaseUtils.tableContainsPlayerUUID(player.getUniqueId().toString(), "STATS")) {
                try {
                    String databaseQuery2 = "UPDATE `STATS` SET Kills=?, Deaths=?, Wins=?, Loses=? WHERE playerUUID=`" + player.getUniqueId().toString() + "`;";

                    PreparedStatement statement2 = OneInTheChamber.connection.prepareStatement(databaseQuery2);

                    statement2.setInt(1, user.getKills());
                    statement2.setInt(2, user.getDeaths());
                    statement2.setInt(3, user.getWins());
                    statement2.setInt(4, user.getLoses());

                    statement2.execute();
                    statement2.close();

                } catch(SQLException exc) {
                    exc.printStackTrace();
                }
            } else {
                try {
                    String databaseQuery2 = "INSERT INTO `STATS` (`playerName`, `playerUUID`, `Kills`, `Deaths`, " +
                            "`Wins`, `Loses`) " +
                            "VALUES (?, ?, ?, ?, ?, ?);";

                    PreparedStatement statement2 = OneInTheChamber.connection.prepareStatement(databaseQuery2);

                    statement2.setString(1, player.getName());
                    statement2.setString(2, player.getUniqueId().toString());
                    statement2.setInt(3, user.getKills());
                    statement2.setInt(4, user.getDeaths());
                    statement2.setInt(5, user.getWins());
                    statement2.setInt(6, user.getLoses());

                    statement2.execute();
                    statement2.close();

                } catch(SQLException exc) {
                    exc.printStackTrace();
                }
            }
        }

        getLogger().info("OneInTheChamber has been Disabled. Have a good day!");
        getLogger().info("Made by: Archie Jaskowicz.");
        saveConfig();
    }
}
