package me.jaskowicz.oneinthechamber.Utils;

import me.jaskowicz.oneinthechamber.Tasks.GameTask;
import me.jaskowicz.oneinthechamber.UtilsExtra.ScoreboardAPI;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class User {

    private final Player player;
    private Game gameIn;
    private Location locationBeforeJoin;
    private HashMap<Integer, ItemStack> lastItems = new HashMap<>();
    private ScoreboardAPI currentScoreboardAPI;
    private int secondsTilRespawn;
    private GameMode lastGameMode;

    private User lastHitBy;

    private int kills;
    private int deaths;
    private int wins;
    private int loses;
    private int draws;

    public HashMap<BukkitTask, User> hitTasks = new HashMap<>();

    public User(Player player) {
        this.player = player;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    public Game getGameIn() {
        return gameIn;
    }

    public Location getLocationBeforeJoin() {
        return locationBeforeJoin;
    }

    public HashMap<Integer, ItemStack> getLastItems() {
        return lastItems;
    }

    public ScoreboardAPI getCurrentScoreboardAPI() {
        return currentScoreboardAPI;
    }

    public int getSecondsTilRespawn() {
        return secondsTilRespawn;
    }

    public GameMode getLastGameMode() {
        return lastGameMode;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    public int getDraws() {
        return draws;
    }

    public User getLastHitBy() {
        return lastHitBy;
    }


    // All voids after this line.


    public void setGameIn(Game gameIn) {
        this.gameIn = gameIn;
    }

    public void addLastItem(int slot, ItemStack itemStack) {
        this.lastItems.put(slot, itemStack);
    }

    public void removeLastItem(int slot) {
        this.lastItems.remove(slot);
    }

    public void clearLastItems() {
        this.lastItems.clear();
    }

    public void setLocationBeforeJoin(Location locationBeforeJoin) {
        this.locationBeforeJoin = locationBeforeJoin;
    }

    public void setCurrentScoreboardAPI(ScoreboardAPI currentScoreboardAPI) {
        this.currentScoreboardAPI = currentScoreboardAPI;
    }

    public void setSecondsTilRespawn(int secondsTilRespawn) {
        this.secondsTilRespawn = secondsTilRespawn;
    }

    public void setLastGameMode(GameMode lastGameMode) {
        this.lastGameMode = lastGameMode;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public void setLastHitBy(User lastHitBy) {
        this.lastHitBy = lastHitBy;
    }
}
