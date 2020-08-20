package me.jaskowicz.oneinthechamber.UtilsExtra;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Warning;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Objects;

public class ScoreboardAPI {

    // WOOOO FIRST SCOREBOARD API WHICH IS DEVELOPED BY ME AND NOT FROM SOMEONE ELSE IN 2013!!!!

    // Developed by Archie Jaskowicz
    // To be used by calling the actual method ScoreboardAPI();

    private Scoreboard scoreboard;
    private Objective objective;
    private Player player;

    private HashMap<String, String> teamEntries = new HashMap<>();

    private Plugin plugin = OneInTheChamber.getPlugin(OneInTheChamber.class);


    public ScoreboardAPI(Player player) {
        this.scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        //this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        this.player = player;

        if(scoreboard.getObjective("OneInTheChamber") != null) {
            scoreboard.getObjective("OneInTheChamber").unregister();
            objective = scoreboard.registerNewObjective("OneInTheChamber", "dummy", ChatColor.GREEN + "One In The Chamber");
        } else {
            objective = scoreboard.registerNewObjective("OneInTheChamber", "dummy", ChatColor.GREEN + "One In The Chamber");
        }

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        player.setScoreboard(scoreboard);
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Objective getObjective() {
        return this.objective;
    }

    public boolean teamExists(String teamName) {
        return scoreboard.getTeam(teamName) != null;
    }




    // All voids after this line.

    public void addLineAtScore(String entry, String teamName, String line, int score) {
        Team team = scoreboard.registerNewTeam(teamName);
        team.addEntry(entry);
        String colouredLine = ChatColor.translateAlternateColorCodes('&', line);
        team.setPrefix(colouredLine);

        /*
        This is code to fix some stuff where lines are more than 16 characters causing errors, however, it's not needed.

        // The text doesn't have more than 16 characters, so we are fine
        if (line.length() <= 16) {
            team.setPrefix(line);
        }

        // If the text actually goes above 32, cut it to 32 to prevent kicks and errors
        if (line.length() > 32) {
            line = line.substring(32);
        }

        if (line.length() > 16 && line.length() <= 32) {
            // Set the prefix to the first 16 characters
            team.setPrefix(line.substring(0, 16));

            // Now use the last 16 characters and put them into the suffix
            team.setSuffix(line.substring(16));
        }

        */

        while(teamEntries.get(entry) != null) {
            entry += "" + ChatColor.GRAY;
        }

        teamEntries.put(entry, teamName);
        objective.getScore(entry).setScore(score);
    }

    public void changeLine(String teamName, String line) {
        Objects.requireNonNull(scoreboard.getTeam(teamName)).setPrefix(line);
    }

    public void setScoreAtLine(String name, int score) {
        Score playerScore = objective.getScore(name);
        playerScore.setScore(score);
    }

    public void setScoreboardTitle(String name) {
        objective.setDisplayName(name);
    }

    public void addTeamWithColour(String teamName, ChatColor colour) {
        Team team = scoreboard.registerNewTeam(teamName);
        team.setColor(colour);
    }

    public void addPlayerToTeam(String teamName, Player player) {
        Objects.requireNonNull(scoreboard.getTeam(teamName)).addEntry(player.getName());
    }

    public void changeTeamPrefix(String teamName, String prefix) {
        Objects.requireNonNull(scoreboard.getTeam(teamName)).setPrefix(prefix);
    }

    public void removePlayerFromTeam(String teamName, Player player) {
        Objects.requireNonNull(scoreboard.getTeam(teamName)).removeEntry(player.getName());
    }

    public void removeTeam(String teamName) {
        Objects.requireNonNull(scoreboard.getTeam(teamName)).unregister();
    }

    public void clearLines() {
        for(String string : scoreboard.getEntries()) {
            scoreboard.resetScores(string);
        }

        for(String teamName : teamEntries.values()) {
            Objects.requireNonNull(scoreboard.getTeam(teamName)).unregister();
        }

        teamEntries.clear();
    }

    // line doesn't have to be exact value.
    public void clearLine(String line) {
        for(String string : teamEntries.keySet()) {
            if(string.contains(line)) {
                scoreboard.resetScores(string);
            }
        }
    }

    // NOT THE SAME AS 'clearLine'.
    public void resetScore(String line) {
        scoreboard.resetScores(line);
    }

    public void sendActionBar(String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    public void sendTitleSubtitle(String title, String subtitle) {
        player.sendTitle(title, subtitle, 20, 20, 20);
    }

    public void sendTitle(String title) {
        player.sendTitle(title, "", 20, 20, 20);
    }

    public void sendTitleSubtitleTime(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    public void sendTitleTime(String title, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, "", fadeIn, stay, fadeOut);
    }

    /**
     * @deprecated This doesn't animate anything and will do the EXACT same thing as adding a line but with no animations. It still needs to be figured out.
     */
    @Deprecated
    public void addLineAtScoreAnimated(String entry, String teamName, String line, ChatColor changeTo, int score) {
        Team team = scoreboard.registerNewTeam(teamName);
        team.addEntry(entry);
        String colouredLine = ChatColor.translateAlternateColorCodes('&', line);
        team.setPrefix(colouredLine);

        while(teamEntries.get(entry) != null) {
            entry += "" + ChatColor.GRAY;
        }

        teamEntries.put(entry, teamName);
        objective.getScore(entry).setScore(score);

        //AnimateScoreboard animateScoreboard = new AnimateScoreboard(this, teamName, line, changeTo);

        //animateScoreboard.runTaskTimer(plugin, 0L, 20L);
    }

    /**
     * @deprecated Should be avoided as it never should be used but in the event that it needs to be (which I am unaware of) then it can be used.
     */
    @Deprecated
    public void fullyUpdateScoreboard() {
        this.player.setScoreboard(scoreboard);
    }
}