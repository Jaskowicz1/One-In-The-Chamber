package me.jaskowicz.oneinthechamber.UtilsExtra;


import me.jaskowicz.oneinthechamber.OneInTheChamber;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.HashMap;

public class DatabaseUtils {

    private Plugin plugin = OneInTheChamber.getPlugin(OneInTheChamber.class);

    public DatabaseUtils() {

    }

    public void openConnection() throws SQLException {
        if (OneInTheChamber.connection != null && !OneInTheChamber.connection.isClosed()) {
            return;
        }

        try {
            if (OneInTheChamber.connection != null && !OneInTheChamber.connection.isClosed()) {
                return;
            }

            FileConfiguration configData = plugin.getConfig();

            OneInTheChamber.connection = DriverManager.getConnection("jdbc:mysql://" + configData.getString("database.host") + "/" + configData.getString("database.database") + "?autoReconnect=true&tcpKeepAlive=true&useUnicode=yes&useSSL=false", "" + configData.getString("database.username"), "" + configData.getString("database.password"));
            Statement cre = OneInTheChamber.connection.createStatement();
            cre.executeUpdate("CREATE TABLE IF NOT EXISTS `STATS`(`UserID` int NOT NULL AUTO_INCREMENT, `playerName` varchar (256), `playerUUID` varchar(256), `Kills` int, `Deaths` int, `Wins` int, `Loses` int, `Draws` int, PRIMARY KEY (`UserID`));");
            cre.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            OneInTheChamber.connection.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean tableContainsPlayerUUID(String playerUUID, String table) {
        try {
            PreparedStatement sql = OneInTheChamber.connection.prepareStatement("SELECT * FROM `" + table + "` WHERE playerUUID=?;");
            sql.setString(1, playerUUID);

            try {
                ResultSet resultset = sql.executeQuery();

                boolean containsGuild = resultset.next();

                sql.close();
                resultset.close();

                return containsGuild;
            } catch(Exception e) {
                e.printStackTrace();
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public static boolean tableContainsPlayerName(String playerName, String table) {
        try {
            PreparedStatement sql = OneInTheChamber.connection.prepareStatement("SELECT * FROM `" + table + "` WHERE playerName=?;");
            sql.setString(1, playerName);

            try {
                ResultSet resultset = sql.executeQuery();

                boolean containsGuild = resultset.next();

                sql.close();
                resultset.close();

                return containsGuild;
            } catch(Exception e) {
                e.printStackTrace();
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public static HashMap<String, Integer> getTopFiveKills() {

        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();

        try {
            Statement statement = OneInTheChamber.connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM `STATS` ORDER BY `Kills` DESC LIMIT 5;");

            while (result.next()) {

                stringIntegerHashMap.put(result.getString("playerName"), result.getInt("Kills"));
            }

            result.close();
            statement.close();

            return stringIntegerHashMap;
        } catch(SQLException e) {
            e.printStackTrace();
            return stringIntegerHashMap;
        }
    }

    public static HashMap<String, Integer> getTopFiveDeaths() {

        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();

        try {
            Statement statement = OneInTheChamber.connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM `STATS` ORDER BY `Deaths` DESC LIMIT 5;");

            while (result.next()) {

                stringIntegerHashMap.put(result.getString("playerName"), result.getInt("Deaths"));
            }

            result.close();
            statement.close();

            return stringIntegerHashMap;
        } catch(SQLException e) {
            e.printStackTrace();
            return stringIntegerHashMap;
        }
    }

    public static HashMap<String, Integer> getTopFiveWins() {

        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();

        try {
            Statement statement = OneInTheChamber.connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM `STATS` ORDER BY `Wins` DESC LIMIT 5;");

            while (result.next()) {

                stringIntegerHashMap.put(result.getString("playerName"), result.getInt("Wins"));
            }

            result.close();
            statement.close();

            return stringIntegerHashMap;
        } catch(SQLException e) {
            e.printStackTrace();
            return stringIntegerHashMap;
        }
    }

    public static HashMap<String, Integer> getTopFiveLoses() {

        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();

        try {
            Statement statement = OneInTheChamber.connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM `STATS` ORDER BY `Loses` DESC LIMIT 5;");

            while (result.next()) {

                stringIntegerHashMap.put(result.getString("playerName"), result.getInt("Loses"));
            }

            result.close();
            statement.close();

            return stringIntegerHashMap;
        } catch(SQLException e) {
            e.printStackTrace();
            return stringIntegerHashMap;
        }
    }

    public static HashMap<String, Integer> getTopFiveDraws() {

        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();

        try {
            Statement statement = OneInTheChamber.connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM `STATS` ORDER BY `Draws` DESC LIMIT 5;");

            while (result.next()) {

                stringIntegerHashMap.put(result.getString("playerName"), result.getInt("Draws"));
            }

            result.close();
            statement.close();

            return stringIntegerHashMap;
        } catch(SQLException e) {
            e.printStackTrace();
            return stringIntegerHashMap;
        }
    }
}
