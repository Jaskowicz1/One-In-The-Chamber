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

    private boolean fireTrailCosmeticOn = false;
    private boolean lavaTrailCosmeticOn = false;
    private boolean waterTrailCosmeticOn = false;
    private boolean angryTrailCosmeticOn = false;
    private boolean emeraldTrailCosmeticOn = false;
    private boolean snowTrailCosmeticOn = false;
    private boolean noteTrailCosmeticOn = false;
    private boolean heartTrailCosmeticOn = false;
    private boolean enchantTrailCosmeticOn = false;
    private boolean purpleTrailCosmeticOn = false;
    private boolean bloodTrailCosmeticOn = false;
    private boolean trailOn = false;

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

    // ALL TRAIL STUFF (It's a fucking mess)

    public boolean hasFireTrailOn() {
        return this.fireTrailCosmeticOn;
    }

    public void setHasFireTrailOn(boolean fireTrailCosmeticOn) {
        this.fireTrailCosmeticOn = fireTrailCosmeticOn;
    }

    public boolean hasWaterTrailOn() {
        return this.waterTrailCosmeticOn;
    }

    public void setHasWaterTrailOn(boolean waterTrailCosmeticOn) {
        this.waterTrailCosmeticOn = waterTrailCosmeticOn;
    }

    public boolean hasLavaTrailOn() {
        return this.lavaTrailCosmeticOn;
    }

    public void setHasLavaTrailOn(boolean lavaTrailCosmeticOn) {
        this.lavaTrailCosmeticOn = lavaTrailCosmeticOn;
    }

    public boolean hasNoteTrailOn() {
        return this.noteTrailCosmeticOn;
    }

    public void setHasNoteTrailOn(boolean noteTrailCosmeticOn) {
        this.noteTrailCosmeticOn = noteTrailCosmeticOn;
    }

    public boolean hasHeartTrailOn() {
        return this.heartTrailCosmeticOn;
    }

    public void setHasHeartTrailOn(boolean heartTrailCosmeticOn) {
        this.heartTrailCosmeticOn = heartTrailCosmeticOn;
    }

    public boolean hasSnowTrailOn() {
        return this.snowTrailCosmeticOn;
    }

    public void setHasSnowTrailOn(boolean snowTrailCosmeticOn) {
        this.snowTrailCosmeticOn = snowTrailCosmeticOn;
    }

    public boolean hasEmeraldTrailOn() {
        return this.emeraldTrailCosmeticOn;
    }

    public void setHasEmeraldTrailOn(boolean emeraldTrailCosmeticOn) {
        this.emeraldTrailCosmeticOn = emeraldTrailCosmeticOn;
    }

    public boolean hasAngryTrailOn() {
        return this.angryTrailCosmeticOn;
    }

    public void setHasAngryTrailOn(boolean angryTrailCosmeticOn) {
        this.angryTrailCosmeticOn = angryTrailCosmeticOn;
    }

    public boolean hasEnchantTrailOn() {
        return this.enchantTrailCosmeticOn;
    }

    public void setHasEnchantTrailOn(boolean enchantTrailCosmeticOn) {
        this.enchantTrailCosmeticOn = enchantTrailCosmeticOn;
    }

    public boolean hasPurpleTrailOn() {
        return this.purpleTrailCosmeticOn;
    }

    public void setHasPurpleTrailOn(boolean purpleTrailCosmeticOn) {
        this.purpleTrailCosmeticOn = purpleTrailCosmeticOn;
    }

    public boolean hasBloodTrailOn() {
        return this.bloodTrailCosmeticOn;
    }

    public void setBloodTrailCosmeticOn(boolean bloodTrailCosmeticOn) {
        this.bloodTrailCosmeticOn = bloodTrailCosmeticOn;
    }

    public boolean hasTrailOn() {
        return this.trailOn;
    }

    public void setHasTrailOn(boolean trailOn) {
        this.trailOn = trailOn;
    }
}
