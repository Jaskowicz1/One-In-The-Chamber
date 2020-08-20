package me.jaskowicz.oneinthechamber.Utils;

import org.bukkit.Location;

import java.util.HashMap;

public class Arena {

    private String arenaName;
    private boolean enabled;
    private Game usedBy;
    private HashMap<Location, Location> spawnpoints = new HashMap<>();
    private HashMap<Location, Location> spawnpointsNotUsed = new HashMap<>();

    public Arena(String arenaName) {
        this.arenaName = arenaName;
    }

    public String getArenaName() {
        return arenaName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Game getUsedBy() {
        return usedBy;
    }

    public HashMap<Location, Location> getSpawnpoints() {
        return spawnpoints;
    }

    public HashMap<Location, Location> getSpawnpointsNotUsed() {
        return spawnpointsNotUsed;
    }


    // All voids after this line.


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setUsedBy(Game usedBy) {
        this.usedBy = usedBy;
    }

    public void addSpawnpoint(Location location) {
        this.spawnpoints.put(location, location);
    }

    public void addSpawnpointNotUsed(Location location) {
        this.spawnpointsNotUsed.put(location, location);
    }

    public void removeSpawnpoint(Location location) {
        this.spawnpoints.remove(location, location);
    }

    public void removeSpawnpointNotUsed(Location location) {
        this.spawnpointsNotUsed.remove(location);
    }
}
