package seti1;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KeepCopies {
    private final Map<UUID, InetAddress> copies = new HashMap<>();
    private final Map<UUID, Long> timeLastAnswer = new HashMap<>();

    public KeepCopies() {
        CheckAlive isAlive = new CheckAlive(copies, timeLastAnswer);
        isAlive.checkAliveCopies();
    }

    public void checkMap(InetAddress address, UUID uuid) {
        if (!copies.containsKey(uuid)) {
            copies.put(uuid, address);
            System.out.println("The number of copies has changed");
            for (InetAddress value : copies.values()) {
                System.out.println(value);
            }
        }

        timeLastAnswer.put(uuid, System.currentTimeMillis());
    }
}
