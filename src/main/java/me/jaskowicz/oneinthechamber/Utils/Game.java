package me.jaskowicz.oneinthechamber.Utils;

import me.jaskowicz.oneinthechamber.Tasks.ArrowTrailTask;
import me.jaskowicz.oneinthechamber.Tasks.GameTask;
import me.jaskowicz.oneinthechamber.Tasks.StartingGameTask;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class Game {

    private Arena arenaInUse;
    private HashMap<UUID, User> usersPlaying = new HashMap<>();
    private HashMap<UUID, User> usersAlive = new HashMap<>();
    private HashMap<UUID, User> usersDead = new HashMap<>();
    private HashMap<UUID, User> usersSpectating = new HashMap<>();
    private HashMap<User, Integer> userScore = new HashMap<>();
    private HashMap<UUID, User> users = new HashMap<>();
    private HashMap<Entity, ArrowTrailTask> ARROWS = new HashMap<>();
    private boolean started;
    private boolean finished;
    private boolean blockingMovement;
    private GameTask gameTask;
    private StartingGameTask startingGameTask;

    public HashMap<BukkitTask, User> respawnTasks = new HashMap<>();

    public Game(Arena arenaInUse) {
        this.arenaInUse = arenaInUse;
    }

    public Arena getArenaInUse() {
        return arenaInUse;
    }

    public HashMap<UUID, User> getUsers() {
        return users;
    }

    public HashMap<UUID, User> getUsersPlaying() {
        return usersPlaying;
    }

    public HashMap<UUID, User> getUsersAlive() {
        return usersAlive;
    }

    public HashMap<UUID, User> getUsersDead() {
        return usersDead;
    }

    public HashMap<UUID, User> getUsersSpectating() {
        return usersSpectating;
    }

    public boolean hasStarted() {
        return this.started;
    }

    public boolean isFinished() {
        return finished;
    }

    public GameTask getGameTask() {
        return gameTask;
    }

    public StartingGameTask getStartingGameTask() {
        return startingGameTask;
    }

    public HashMap<User, Integer> getUserScore() {
        return userScore;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public HashMap<Entity, ArrowTrailTask> getARROWS() {
        return ARROWS;
    }

    public boolean isBlockingMovement() {
        return blockingMovement;
    }


    // All voids after this line.


    public void addUser(UUID uuid, User user) {
        this.users.put(uuid, user);
    }

    public void removeUser(UUID uuid) {
        this.users.remove(uuid);
    }

    public void addUserPlaying(UUID uuid, User user) {
        this.usersPlaying.put(uuid, user);
    }

    public void removeUserPlaying(UUID uuid) {
        this.usersPlaying.remove(uuid);
    }

    public void addUserAlive(UUID uuid, User user) {
        this.usersAlive.put(uuid, user);
    }

    public void removeUserAlive(UUID uuid) {
        this.usersAlive.remove(uuid);
    }

    public void addUserDead(UUID uuid, User user) {
        this.usersDead.put(uuid, user);
    }

    public void removeUserDead(UUID uuid) {
        this.usersDead.remove(uuid);
    }

    public void addUserSpectating(UUID uuid, User user) {
        this.usersSpectating.put(uuid, user);
    }

    public void removeUserSpectating(UUID uuid) {
        this.usersSpectating.remove(uuid);
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void setGameTask(GameTask gameTask) {
        this.gameTask = gameTask;
    }

    public void setStartingGameTask(StartingGameTask startingGameTask) {
        this.startingGameTask = startingGameTask;
    }

    public void addUserScore(User user, int score) {
        this.userScore.put(user, score);
    }

    public void removeUserScore(User user) {
        this.userScore.remove(user);
    }

    public void addArrow(Entity entity, ArrowTrailTask trailTask) {
        this.ARROWS.put(entity, trailTask);
    }

    public void removeArrow(Entity entity) {
        this.ARROWS.remove(entity);
    }

    public void setBlockingMovement(boolean blockingMovement) {
        this.blockingMovement = blockingMovement;
    }
}
