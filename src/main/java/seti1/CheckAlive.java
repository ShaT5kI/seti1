package seti1;

import java.net.InetAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CheckAlive {
    private final Map<UUID, InetAddress> copies;
    private final Map<UUID, Long> timeLastAnswer;

    public CheckAlive(Map<UUID, InetAddress> copies, Map<UUID, Long> time) {
        this.copies = copies;
        this.timeLastAnswer = time;
    }

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void checkAliveCopies() {
        final Runnable checker = () -> {
            for (Map.Entry<UUID, Long> entry : timeLastAnswer.entrySet()) {
                if (System.currentTimeMillis() - entry.getValue() > 1000) {
                    if (copies.containsKey(entry.getKey())) {
                        copies.remove(entry.getKey());
                        System.out.println("The number of copies has changed");
                        for (InetAddress value : copies.values()) {
                            System.out.println(value);
                        }
                    }
                    timeLastAnswer.remove(entry.getKey());
                }
            }
        };

        scheduler.scheduleAtFixedRate(checker, 0, 1, TimeUnit.SECONDS);
    }
}
