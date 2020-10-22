package me.jaskowicz.oneinthechamber.Tasks;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseAliveTask extends BukkitRunnable {

    @Override
    public void run() {
        try {
            Statement statement = OneInTheChamber.connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM `STATS` ORDER BY `Draws` DESC LIMIT 1;");

            result.close();
            statement.close();

            return;
        } catch(SQLException e) {
            e.printStackTrace();
            return;
        }
    }
}
