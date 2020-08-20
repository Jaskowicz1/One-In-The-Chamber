package me.jaskowicz.oneinthechamber.UtilsExtra;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Utils.User;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class RandomUtil {


    public static User getRandomUser() {
        Random generator = new Random();
        Object[] users = OneInTheChamber.USERS.values().toArray();
        return (User) users[generator.nextInt(users.length)];
    }

    public static User getRandomUserNotSelf(User u) {
        Random generator = new Random();
        Object[] users = OneInTheChamber.USERS.values().toArray();
        User user = (User) users[generator.nextInt(users.length)];

        while(user == u) {
            user = (User) users[generator.nextInt(users.length)];
        }

        return user;
    }

    public static User getRandomUser(HashMap<UUID, User> userHashMap) {
        Random generator = new Random();
        Object[] users = userHashMap.values().toArray();
        return (User) users[generator.nextInt(users.length)];
    }

    public static User getRandomUserNotSelf(HashMap<UUID, User> userHashMap, User u) {
        HashMap copy = (HashMap) userHashMap.clone();
        copy.remove(u.getPlayer().getUniqueId());

        Random generator = new Random();
        Object[] users = copy.values().toArray();

        return (User) users[generator.nextInt(users.length)];
    }

    public static Object getRandomObject(Object[] objects) {
        Random generator = new Random();
        return objects[generator.nextInt(objects.length)];
    }
}
